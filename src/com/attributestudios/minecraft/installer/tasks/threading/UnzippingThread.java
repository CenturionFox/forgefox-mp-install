package com.attributestudios.minecraft.installer.tasks.threading;

import java.io.File;
import java.io.IOException;

import com.attributestudios.api.util.io.ZippingUtils;
import com.attributestudios.minecraft.installer.Settings;

/**
 * Unzips a specific file using an instance of ZippingUtils.
 * 
 * @author  Bridger Maskrey (maskreybe@live.com)
 * @version 1.0.0
 * @date.	2014-08-18
 */
public class UnzippingThread extends Thread
{
	private String unzipLocation;
	private String zippedFileLoc;
	
	private static final ZippingUtils expander = new ZippingUtils();
	
	public UnzippingThread(String threadName, String zippedFileLoc, String unzipLocation)
	{
		super(threadName);
		this.unzipLocation = unzipLocation;
		this.zippedFileLoc = zippedFileLoc;
	}
	
	public void run()
	{
		if(this.unzipLocation != null && this.zippedFileLoc != null)
		{
			try
			{
				expander.extract(
					new File(Settings.config.getProperty(this.zippedFileLoc, "null")),
					new File(Settings.config.getProperty(this.unzipLocation, "null")));
			}
			catch (IOException e)
			{
				RuntimeException exception = new RuntimeException(e);
				
				throw exception;
			}
		}
	}
}
