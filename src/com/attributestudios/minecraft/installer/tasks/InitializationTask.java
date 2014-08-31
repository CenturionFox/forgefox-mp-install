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
import com.attributestudios.minecraft.installer.gui.SplashScreen;
import com.attributestudios.minecraft.installer.tasks.threading.DownloaderThread;
import com.attributestudios.minecraft.installer.tasks.threading.UnzippingThread;

public class InitializationTask implements Runnable
{
	private SplashScreen splash;
	
	@Override
	public void run()
	{
		this.splash = new SplashScreen();
		
		this.splash.setVisible(true);
		
		new Thread(this.splash, "Splash Screen On-Top").start();
		
		this.splash.printLocalizedText("splash.debug.download.md5");
		try
		{
			new DownloaderThread("MD5 Downloader", "download.digest", "md5.downloaded").run();
		
			this.splash.printLocalizedText("splash.debug.compare");
			if(!this.compareMD5s())
			{
				this.splash.printLocalizedText("splash.debug.compare.mismatch");
				Main.log("MD5 mismatch! Downloading new update files...");
				
				new DownloaderThread("Updates Downloader", "download.updates", "update.downloaded").run();
				
				this.splash.printLocalizedText("splash.debug.extract");
				new UnzippingThread("Updates Unzipper", "update.downloaded", "update.folder").run();
				
				this.splash.printLocalizedText("splash.debug.copy.md5");
				
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
		
		//TODO: Set up update lists!
		
		try
		{
			this.splash.printLocalizedText("splash.debug.completion");
			this.splash.finishProgressBar();
			// Sleep for 2 seconds so users may peruse the output
			Thread.sleep(2000L);
		}
		catch (InterruptedException e)
		{
			// Interruped? No problem, just keep going!
		}
		
		this.splash.setVisible(false);
	}

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
