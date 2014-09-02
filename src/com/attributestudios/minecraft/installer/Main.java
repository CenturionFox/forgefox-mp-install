/**
 * Main package for the ForgeFox Installer
 */
package com.attributestudios.minecraft.installer;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.attributestudios.api.util.Localizer;
import com.attributestudios.api.util.logging.LoggingUtil;
import com.attributestudios.api.util.logging.SimpleLogFormatter;
import com.attributestudios.minecraft.installer.tasks.InitializationTask;
import com.attributestudios.minecraft.installer.updates.Update;

/**
 * The second version of the downloader / installer
 * 	for the ForgeFox server modpack.
 * 
 * @author  Bridger Maskrey (maskreybe@live.com)
 * @version 2.0.0
 * @date.   2014-08-12
 */
public class Main 
{
	/**
	 * The current version of the installer.
	 */
	public static final String VERSION = "2.0.0 Beta";
	
	/**
	 * The main debug logger for the program.
	 */
	public static Logger debugLogger = LoggingUtil.constructLogger("ForgeFox Installer", new SimpleLogFormatter());
	
	/**
	 * The localizer class for the English language.
	 */
	public static Localizer english;
	
	public static List<Update> updatesList = new ArrayList<Update>();
	
	/**
	 * The main method of the installer.  Bootstraps the splash
	 * 	screen, parses all command line switches, and sets up the
	 * 	localizer.
	 * @param args  The command line arguments passed to the program.
	 * @since 2.0.0
	 */
	public static void main(String[] args) 
	{
		// Set up the English language localization
		InputStream language = Main.class.getClassLoader().getResourceAsStream("lang/en-US.lang");
		english = new Localizer(language, "en-US");
		english.initialize();
		
		Settings.loadConfig();
		Settings.parseCommands(args);
		
		// Signal start
		log("Starting ForgeFox Modpack Installer...");
		
		if(!Settings.isDropbox())
		{
			// Delegate current to the new thread, wait until that
			// 	   thread's execution is complete before continuing.
			new InitializationTask().run();
			
		}
		else
		{
			//TODO: Dropbox setup.
		}
		
		File temp = new File(Settings.config.getValue("tempfile", "./temp"));
		Main.log("Scheduling " + temp + " for deletion on exit.");
		temp.deleteOnExit();
		
		temp = new File(Settings.config.getValue("md5.downloaded", "./temp/updates.md5"));
		Main.log("Scheduling " + temp + " for deletion on exit.");
		temp.deleteOnExit();
		
		temp = new File(Settings.config.getValue("update.downloaded", "./temp/temp_updates.zip"));
		Main.log("Scheduling " + temp + " for deletion on exit.");
		temp.deleteOnExit();
		
		System.exit(-1);
	}
	
	/**
	 * Outputs the specified object to the debug logger on all
	 * 	specified levels.  If no level is specified it automatically
	 * 	outputs as INFO.
	 * @param obj    The object to output to the logger.
	 * @param levels The levels at which to output.
	 * @since 2.0.0
	 */
	public static <T> void log(T obj, Level...levels)
	{
		if(levels.length > 0)
		{
			for(Level level : levels)
			{
				debugLogger.log(level, obj.toString());
			}
		}
		else
		{
			debugLogger.log(Level.INFO, obj.toString());
		}
	}
}
