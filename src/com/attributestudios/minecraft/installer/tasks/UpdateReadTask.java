package com.attributestudios.minecraft.installer.tasks;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import com.attributestudios.api.util.logging.LoggingUtil;
import com.attributestudios.minecraft.installer.Main;
import com.attributestudios.minecraft.installer.Settings;
import com.attributestudios.minecraft.installer.updates.Update;

/**
 * Reads all updates and constructs a list of them.
 * 
 * @author  Bridger Maskrey
 * @version 1.0.0
 * @date.   2014-08-19
 */
public class UpdateReadTask implements Runnable
{
	/**
	 * A list containing all read-in update files from the update directory.
	 */
	public static List<Update> updateList = new ArrayList<Update>();
	
	@Override
	public void run()
	{
		List<File> validUpdateFiles;
		File updateDirectory = new File(Settings.config.getValue("update.folder", "./updates"));
		
		// Create the update directory
		if(!updateDirectory.exists()) updateDirectory.mkdirs();
		
		Main.log("Reading updates from " + updateDirectory.getAbsolutePath() + "...");
		
		if(!updateDirectory.isDirectory())
		{
			throw new RuntimeException(
					new FileSystemException("File " + Settings.config.getValue("update.folder", "./updates") + " is not a directory!"));
		}
		
		validUpdateFiles = Arrays.asList(updateDirectory.listFiles(new FileFilter()
		{
			/**
			 * Tests if the given file should be included in the valid file list.
			 * A file is valid if it is not a directory and its extension is ".xml".
			 * 
			 * @param file The file to check.
			 */
			public boolean accept(File file)
			{
				return file.getName().matches("update_\\S+.xml") && !file.isDirectory();
			}
		}));

		int filesRead = 0;
		
		for(File file : validUpdateFiles)
		{
			Main.log("Reading update file " + file.getAbsolutePath(), Level.FINE);
			try 
			{	
				updateList.add(Update.readFromXML(file));
				filesRead++;
			} catch (Exception e)
			{
				LoggingUtil.writeStackTraceToLogger(Main.debugLogger,
						e, "Unable to read suspected update file " + file.getAbsolutePath() + ": " + e.getMessage(),
						Level.WARNING);
			}
		}
		
		Main.log(filesRead > 0 ? "Read in " + filesRead + " update" + (filesRead == 1 ? "" : "s") + " successfully!" :
			"No update files could be found.");
	}

}
