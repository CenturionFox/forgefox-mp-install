package com.attributestudios.minecraft.installer;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.attributestudios.api.util.ConfigurationLoader;
import com.attributestudios.api.util.logging.LoggingUtil;
import com.attributestudios.minecraft.installer.enums.ModImage;

/**
 * Class that stores all settings enabled by the command line switches.
 * Also has a command line help option.
 * 
 * @author  Bridger Maskrey (maskreybe@live.com)
 * @version 1.0.0
 * @date.   2014-08-12
 */
public class Settings
{
	/**
	 * Loads the default settings as defined in the configuration file.
	 */
	public static ConfigurationLoader config;
	
	/**
	 * Boolean determining whether or not the program should attempt to zip all
	 *  updates that do not yet have an associated zip file.
	 * Automatically set on whether or not the current active directory is located somewhere
	 *  relative to 
	 */
	private static boolean updateDropbox = System.getProperty("user.dir").contains("Dropbox" + File.pathSeparator + "SharedFileTransfer" + File.pathSeparator + "BASE_MODPACK");
	
	/**
	 * The image to display on the splash screen
	 */
	private static ModImage splashScreenImage = ModImage.FOX;
	
	/**
	 * Parses the command line arguments passed to the program.
	 * @param programArgs
	 */
	public static void parseCommands(String[] programArgs)
	{
		for(String s : programArgs)
		{
			// Switch to output help for switches
			if("-?".equals(s) || "--help".equals(s))
			{
				printCommandHelp();
				System.exit(0);
			}
			// Switch to force dropbox-esque code processing
			else if("-D".equals(s))
			{
				updateDropbox = true;
			}
			// Switch to enable verbose logging in the specified level
			else if(s.matches("-d:\\S+") || s.matches("--debugLoggingLevel:\\S+"))
			{
				String levelName = s.split(":")[1].toUpperCase();
				System.out.println("[ForgeFox Installer Args Parser] [INFO] Setting logger levels to " + levelName + "...");
				setupLoggers(levelName);
			}
			else if(s.matches("-l:\\S+") || s.matches("--lookAndFeel:\\S+"))
			{
				String lookAndFeelName = s.split(":")[1];
				System.out.println("[Forgefox Installer Args Parser] [INFO] Attempt to set look and feel to " + lookAndFeelName);
				loadLookAndFeel(lookAndFeelName);
			}
		}
	}
	
	/**
	 * Are we in a "Dropbox" environment?
	 * @return True if we are in a "Dropbox" environment.
	 */
	public static boolean isDropbox()
	{
		return updateDropbox;
	}

	public static ModImage getSplashImage()
	{
		return splashScreenImage;
	}
	
	/**
	 * Loads the configuration files and loads the values from it.
	 */
	public static void loadConfig()
	{
		// Load static configuration file
		config = new ConfigurationLoader(Main.class.getClassLoader().getResourceAsStream("config/main.properties"));
		config.initialize();
		
		setupLoggers(config.getValue("loglevel", "info").toUpperCase());
		
		splashScreenImage = ModImage.valueOf(config.getValue("splashimg", "fox").toUpperCase());
	}

	/**
	 * Sets the loggers to the log level represented by the specified name.
	 * @param levelName The name of the level to set the logger to.
	 */
	private static void setupLoggers(String levelName)
	{
		// Set level to default value (INFO)
		Level level = Level.INFO;
		
		Set<Logger> loggerSet = new HashSet<Logger>();
		
		try
		{
			level = Level.parse(levelName);
			// Load in all these utility classes to initialize their static loggers.
			Class.forName("com.attributestudios.api.util.Localizer");
			Class.forName("com.attributestudios.api.util.ConfigurationLoader");
			Class.forName("com.attributestudios.api.util.io.ResourceDownloader");
			Class.forName("com.attributestudios.api.util.io.ZippingUtils");
		}
		catch(IllegalArgumentException | ClassNotFoundException exception)
		{
			LoggingUtil.writeStackTraceToLogger(Main.debugLogger,
					exception,
					"Unable to set logging level: ", 
					Level.WARNING);
		}
		
		loggerSet.add(Logger.getLogger("Localization"));
		loggerSet.add(Logger.getLogger("Configuration"));
		loggerSet.add(Logger.getLogger("Downloader"));
		loggerSet.add(Logger.getLogger("Zip / Unzip"));
		loggerSet.add(Main.debugLogger);
		
		for(Logger log : loggerSet)
		{
			for(Handler handler : log.getHandlers())
			{
				handler.setLevel(level);
			}
			log.setLevel(level);
		}
		
		Main.log("Set logger level to " + level, Level.FINE);
	}
	
	/**
	 * Outputs localized command-line switch help information.
	 */
	public static void printCommandHelp()
	{
		if(Main.english != null)
		{
			System.out.println(Main.english.localize("system.help.intro"));
			System.out.println(Main.english.localize("system.help.warn"));
			System.out.println(Main.english.localize("system.help.warn1"));
			System.out.println();
			System.out.print(Main.english.localize("system.help.switch"));
			System.out.println(Main.english.localize("system.help.switch.def"));
			System.out.println();
			System.out.print(Main.english.localize("system.help.switch.help"));
			System.out.println(Main.english.localize("system.help.switch.help.def"));
			System.out.println(Main.english.localize("system.help.switch.help.verbose"));
			System.out.print(Main.english.localize("system.help.switch.dropbox"));
			System.out.println(Main.english.localize("system.help.switch.dropbox.def"));
			System.out.print(Main.english.localize("system.help.switch.debuglog"));
			System.out.println(Main.english.localize("system.help.switch.debuglog.def"));
			System.out.println(Main.english.localize("system.help.switch.debuglog.verbose"));
			System.out.print(Main.english.localize("system.help.switch.lookandfeel"));
			System.out.println(Main.english.localize("system.help.switch.lookandfeel.def"));
			System.out.println(Main.english.localize("system.help.switch.lookandfeel.verbose"));

		}
		else
		{
			Main.log("Unable to load the localizer. Help cannot be displayed.", Level.SEVERE);
			System.exit(1);
		}
	}

	public static void loadLookAndFeel(String lookAndFeelClassName)
	{
		try
		{
			UIManager.setLookAndFeel(lookAndFeelClassName);
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1)
		{
			e1.printStackTrace();
		}
	}
}
