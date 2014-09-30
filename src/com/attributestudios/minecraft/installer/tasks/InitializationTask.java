package com.attributestudios.minecraft.installer.tasks;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;

import org.apache.commons.io.IOUtils;

import com.attributestudios.api.util.logging.LoggingUtil;
import com.attributestudios.minecraft.installer.Main;
import com.attributestudios.minecraft.installer.Settings;
import com.attributestudios.minecraft.installer.gui.ScreenSplash;
import com.attributestudios.minecraft.installer.tasks.threading.DownloaderThread;
import com.attributestudios.minecraft.installer.tasks.threading.UnzippingThread;

/**
 * 
 * 
 * @author  Bridger Maskrey
 * @version 1.0.0
 * @date.	
 */
public class InitializationTask implements Runnable
{
	/**
	 * An instance of splash screen to display while the program is loading.
	 * This screen also contains a status window that outputs the current progress
	 * 	of the initialization tasks.
	 */
	private ScreenSplash splash;
	
	/**
	 * Shows a splash screen, displays output to the user to convey what is currently
	 * 	happening during initialization, and downloads a compare file and, if necessary,
	 *  any mod update files.
	 */
	@Override
	public void run()
	{
		this.splash = new ScreenSplash();
		
		this.splash.setVisible(true);
		
		// Create and start a new, low priority thread that keeps the splash screen on top of
		//	other windows.
		Thread setScreenOnTop = new Thread(this.splash, "Splash Screen On-Top");
		setScreenOnTop.setPriority(Thread.MIN_PRIORITY);
		setScreenOnTop.start();
		
		this.splash.printLocalizedText("splash.debug.download.md5");
		
		try
		{
			// Download the MD5 files from the server.
			new DownloaderThread("MD5 Downloader", "download.digest", "md5.downloaded").run();
		
			// Compare the extant and downloaded MD5 hash.
			this.splash.printLocalizedText("splash.debug.compare");
			if(!this.compareMD5s())
			{
				this.splash.printLocalizedText("splash.debug.compare.mismatch");
				Main.log("MD5 mismatch! Downloading new update files...");
				
				// MD5 mismatch; redownload the mod update folder.
				new DownloaderThread("Updates Downloader", "download.updates", "update.downloaded").run();
				
				this.splash.printLocalizedText("splash.debug.extract");
				new UnzippingThread("Updates Unzipper", "update.downloaded", "update.folder").run();
				
				this.splash.printLocalizedText("splash.debug.copy.md5");
				
				// Overwrite extant MD5 file to prevent unnecessary downloading.
				this.copyMD5s();
			}
			else
			{
				this.splash.printLocalizedText("splash.debug.compare.match");
				Main.log("MD5 match! No further action needed. Continuing.");
			}
		}
		catch(RuntimeException exception)
		{
			this.splash.printLocalizedText("splash.debug.failure");
			LoggingUtil.writeStackTraceToLogger(Main.debugLogger,
				exception, 
				"Failed to download files: ", 
				Level.SEVERE);
		}
		
		//Set up update lists
		this.splash.printLocalizedText("splash.debug.update.populate");
		try
		{
			new UpdateReadTask().run();
			this.splash.printLocalizedText("splash.debug.update.complete");
		}
		catch(RuntimeException e)
		{
			LoggingUtil.writeStackTraceToLogger(Main.debugLogger,
				e,
				"Update read task did not complete successfully: ",
				Level.SEVERE);
		}
		
		try
		{
			this.splash.printLocalizedText("splash.debug.completion");
			this.splash.finishProgressBar();
			
			// Sleep for 2 seconds so users may peruse the output
			Thread.sleep(2000L);
		}
		catch (InterruptedException e)
		{
			// Whoops... Let's just output a warning and continue.
			LoggingUtil.writeStackTraceToLogger(Main.debugLogger,
				e,
				"The thread's sleep was interrupted unexpectedly. ",
				Level.WARNING);
		}
		
		// Hide the splash screen.
		// This also halts the Splash On-Top thread.
		this.splash.setVisible(false);
	}

	/**
	 * Copies the downloaded MD5 hash over the existing MD5 hash.
	 * If the existing MD5 does not exist, this continues as intended;
	 * 	if it does exist, it is deleted before the copy process starts.
	 */
	private void copyMD5s()
	{
		Main.log("Replacing old MD5 file.");
		
		File existingMD5 = new File(Settings.config.getValue("md5.file", "./updates.md5"));
		File downloadedMD5 = new File(Settings.config.getValue("md5.downloaded", "./temp/updates.md5"));
		
		if(existingMD5.exists())
		{
			existingMD5.delete();
		}
		
		try(FileInputStream oldMD5 = new FileInputStream(downloadedMD5);
			BufferedOutputStream newMD5 = new BufferedOutputStream(new FileOutputStream(existingMD5)))
		{
			IOUtils.copy(oldMD5, newMD5);
		}
		catch (IOException e)
		{
			LoggingUtil.writeStackTraceToLogger(Main.debugLogger,
				e,
				"Unable to copy MD5 information: ",
				Level.SEVERE);
		}
	}

	/**
	 * Compares the existing and downloaded MD5 files.
	 * Should either file not exist, the updater assumes that a redownload of
	 * 	update files is necessary.
	 * @return True if the files match, false if a redownload is needed.
	 */
	private boolean compareMD5s()
	{
		Main.log("Comparing data from md5 files...");
		
		File existingMD5 = new File(Settings.config.getValue("md5.file", "./updates.md5"));
		File downloadedMD5 = new File(Settings.config.getValue("md5.downloaded", "./temp/updates.md5"));
		
		if(!existingMD5.exists() || !downloadedMD5.exists())
		{
			Main.log("Unable to find MD5 hash file. Assuming download necessary.");
			return false;
		}
		
		try
		{
			String existingMD5Content = IOUtils.toString(existingMD5.toURI(), Charset.defaultCharset());
			String downloadedMD5Content = IOUtils.toString(downloadedMD5.toURI(), Charset.defaultCharset());
			
			return existingMD5Content.equals(downloadedMD5Content);
		}
		catch(IOException e)
		{
			LoggingUtil.writeStackTraceToLogger(Main.debugLogger,
				e,
				"Unable to read MD5 files. ",
				Level.SEVERE);
			Main.log("Assuming download necessary...", Level.WARNING);
			return false;
		}
	}
}
