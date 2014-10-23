package com.attributestudios.minecraft.installer.tasks.threading;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.attributestudios.api.util.io.ResourceDownloader;
import com.attributestudios.minecraft.installer.Settings;

/**
 * Downloads a specified file using a new ObjectDownloader.
 * 
 * @author  Bridger Maskrey (maskreybe@live.com)
 * @version 1.0.0
 * @date.	2014-08-18
 */
public class DownloaderThread extends Thread 
{
	private String settingsURL;
	private String outputFile;
	private static final ResourceDownloader downloader = new ResourceDownloader();
	
	public DownloaderThread(String threadName, String settingsURL, String outputFile)
	{
		super(threadName);
		this.settingsURL = settingsURL;
		this.outputFile = outputFile;
	}
	
	public void run()
	{
		if(this.settingsURL != null && this.outputFile != null)
		{
			try
			{
				downloader.downloadFile(
					new URL(Settings.config.getValue(this.settingsURL, "null")), 
					new File(Settings.config.getValue(this.outputFile, "null")));
			}
			catch (IOException e)
			{
				RuntimeException exception = new RuntimeException(e);
				
				throw exception;
			}
		}
	}
}
