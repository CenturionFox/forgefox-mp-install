package com.attributestudios.minecraft.installer.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.attributestudios.api.util.logging.LoggingUtil;
import com.attributestudios.minecraft.installer.Settings;
import com.attributestudios.minecraft.installer.Main;


/**
 * The splash screen GUI for the ForgeFox Modpack installer.
 * Outputs progress information for update downloads and
 * other startup operations.
 * 
 * @author Bridger Maskrey
 * @version 2.0.0
 * @date. 2014-08-14
 */
public class ScreenSplash extends JFrame implements Runnable
{
	/**
	 * The serial version ID for the ForgeFox splash screen.
	 */
	private static final long	serialVersionUID	= -1952407875475519753L;

	/**
	 * The main content pane of this frame.
	 */
	private JPanel				contentPane;
	private JLabel				progressOutput;
	private JProgressBar		userComfort;

	/**
	 * Create the frame.
	 * This sets the frame layout to
	 */
	public ScreenSplash()
	{
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		this.setType(Type.UTILITY);
		this.setResizable(false);
		this.setUndecorated(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 550, 275);
		this.setAlwaysOnTop(true);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(8, 8, 8, 8));
		this.setContentPane(this.contentPane);

		BufferedImage splashBG = null;
		String modImage = Settings.getSplashImage().getResourceLoc();

		try
		{
			Main.log("Reading in image " + modImage + "...");
			splashBG = ImageIO.read(Main.class.getClassLoader().getResourceAsStream(modImage));

		}
		catch (IOException e)
		{
			LoggingUtil.writeStackTraceToLogger(Main.debugLogger,
												e, 
												"Unable to read mod image " + modImage + ": ",
												Level.SEVERE);
		}

		this.contentPane.setLayout(null);

		this.progressOutput = new JLabel();
		this.progressOutput.setFont(new Font("Tahoma", Font.PLAIN, 10));
		this.progressOutput.setVerticalAlignment(SwingConstants.BOTTOM);
		this.progressOutput.setText("<html>");
		this.progressOutput.setBackground(Color.LIGHT_GRAY);
		this.progressOutput.setForeground(Color.WHITE);
		this.progressOutput.setBounds(10, 11, 530, 201);
		this.progressOutput.setOpaque(false);
		this.contentPane.add(this.progressOutput);

		this.userComfort = new JProgressBar();
		this.userComfort.setForeground(UIManager.getColor("ProgressBar.foreground"));
		this.userComfort.setIndeterminate(true);
		this.userComfort.setBounds(10, 259, 530, 5);
		this.contentPane.add(this.userComfort);

		JLabel forgeFoxLabel = new JLabel("ForgeFox Installer " + Main.VERSION);
		forgeFoxLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		forgeFoxLabel.setForeground(Color.WHITE);
		forgeFoxLabel.setBounds(10, 223, 377, 29);
		this.contentPane.add(forgeFoxLabel);

		JLabel imgLabel = new JLabel(new ImageIcon(splashBG));
		imgLabel.setBounds(0, 0, 550, 275);
		this.contentPane.add(imgLabel);

		this.setLocationRelativeTo(null);

		this.printLocalizedText("splash.debug.initialize");
	}

	/**
	 * Adds the localized value of the passed string to the progress
	 * label of the splash screen.
	 * 
	 * @param textUnlocalized
	 */
	public void printLocalizedText(String textUnlocalized)
	{
		String localized = Main.english.localize(textUnlocalized);
		this.progressOutput.setText(this.progressOutput.getText() + "<br>" + localized);
	}

	/**
	 * "Finishes" the progress bar by setting it to 100% and disabling the indeterminate
	 * 	animation.
	 */
	public void finishProgressBar()
	{
		this.userComfort.setIndeterminate(false);
		this.userComfort.setValue(100);
	}

	/**
	 * Keeps the splash screen on top of all other windows.
	 */
	@Override
	public void run()
	{
		while (this.isVisible())
		{
			this.setAlwaysOnTop(true);
		}
		
		Main.log("Hiding splash screen...", Level.FINE);
	}
}
