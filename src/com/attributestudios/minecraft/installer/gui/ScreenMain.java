package com.attributestudios.minecraft.installer.gui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.attributestudios.minecraft.installer.Main;
import com.attributestudios.minecraft.installer.enums.ModImage;
import com.attributestudios.minecraft.installer.gui.swing.JImagePane;
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class ScreenMain extends JFrame implements Runnable
{
	private static final long	serialVersionUID	= 8465552478290576935L;
	private JPanel	contentPane;
	
	private static ArrayList<BufferedImage> imagesList;
	
	private static boolean imageListInitialized = false;
	private JImagePane imageLabel;
	
	private int currentIconIndex;
	
	/**
	 * Locking object for thread synchronization.
	 */
	private final Object lock = new Object();
	
	private int imageSwitchTimer;
	
	/**
	 * Create the main screen.
	 */
	/**
	 * 
	 */
	public ScreenMain()
	{
		if(!imageListInitialized)
		{
			throw new RuntimeException("Attempted to load main window without first initializing the image list.");
		}
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 825, 500);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(this.contentPane);
		this.setTitle("Forgefox Modpack Installer");
		this.setAutoRequestFocus(true);
		
		JPanel advancedOptionsPanel = new JPanel();
		advancedOptionsPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), Main.english.localize("ui.border.extrasettings"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JTabbedPane modTabs = new JTabbedPane(JTabbedPane.TOP);
		modTabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		GroupLayout gl_contentPane = new GroupLayout(this.contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(modTabs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(advancedOptionsPanel, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(modTabs, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
						.addComponent(advancedOptionsPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE))
					.addContainerGap())
		);
		
		JPanel tabPanel0 = new JPanel();
		modTabs.addTab(Main.english.localize("ui.tabs.fullinstall"), null, tabPanel0, Main.english.localize("ui.tabs.fullinstall.tip"));
		
		JPanel imagePanel = new JPanel();
		imagePanel.setBorder(UIManager.getBorder("TitledBorder.border"));
		imagePanel.setLayout(new BorderLayout(0, 0));
		
		// Define "previous image" buttons
		JButton iterateImageLeft = new JButton("◄");
		iterateImageLeft.setToolTipText("Previous Image");
		iterateImageLeft.addMouseListener(new MouseAdapter() {
			/* (non-Javadoc)
			 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
			 * Handles a click of the "Last Image" button.
			 * Iterates the displayed image icon in the image label by one index to the left (decrements).
			 * Wraps as necessary.
			 */
			@Override
			public void mouseReleased(MouseEvent arg0) 
			{				
				if(--ScreenMain.this.currentIconIndex < 0)
				{
					ScreenMain.this.currentIconIndex = imagesList.size() - 1;
				}
				
				ScreenMain.this.setCurrentImagePaneIndex(ScreenMain.this.currentIconIndex);
			}
		});
		imagePanel.add(iterateImageLeft, BorderLayout.WEST);
		
		
		// Define "next image" button.
		JButton iterateImageRight = new JButton("►");
		iterateImageRight.setToolTipText("Next Image");
		iterateImageRight.addMouseListener(new MouseAdapter() {
			/* (non-Javadoc)
			 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
			 * Handles a click of the "Next Image" button.
			 * Iterates the displayed image icon in the image label by one index to the right (increments).
			 * Wraps as necessary.
			 */
			@Override
			public void mouseReleased(MouseEvent e) 
			{
				if(++ScreenMain.this.currentIconIndex >= imagesList.size())
				{
					ScreenMain.this.currentIconIndex = 0;
				}
				
				ScreenMain.this.setCurrentImagePaneIndex(ScreenMain.this.currentIconIndex);
			}
		});
		imagePanel.add(iterateImageRight, BorderLayout.EAST);
		
		
		this.imageLabel = new JImagePane(null);
		imagePanel.add(this.imageLabel, BorderLayout.CENTER);
		
		
		JPanel installOptionsPanel0 = new JPanel();
		installOptionsPanel0.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), Main.english.localize("ui.border.installsettings"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_tabPanel0 = new GroupLayout(tabPanel0);
		gl_tabPanel0.setHorizontalGroup(
			gl_tabPanel0.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tabPanel0.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_tabPanel0.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(installOptionsPanel0, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(imagePanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_tabPanel0.setVerticalGroup(
			gl_tabPanel0.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tabPanel0.createSequentialGroup()
					.addContainerGap()
					.addComponent(imagePanel, GroupLayout.PREFERRED_SIZE, 255, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(installOptionsPanel0, GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
					.addContainerGap())
		);
		tabPanel0.setLayout(gl_tabPanel0);
		this.contentPane.setLayout(gl_contentPane);
	}

	/**
	 * Initializes and loads the image list.
	 * @param imagesList
	 */
	public static void intializeImageList()
	{
		try
		{
			imagesList = new ArrayList<BufferedImage>(ModImage.values().length);
			
			for(ModImage modImage : ModImage.values())
			{
				String imageLoc = modImage.getResourceLoc();
				
				Main.log("Loading image " + imageLoc + "...", Level.FINER);
				
				BufferedImage bufferedImage = ImageIO.read(Main.class.getClassLoader().getResourceAsStream(imageLoc));
				
				imagesList.add(bufferedImage);
			}
			
			
			imageListInitialized = true;
		}
		catch(IOException e)
		{
			imageListInitialized = false;
		}
	}
	
	/**
	 * Sets the current icon of the 
	 * @param index
	 */
	public void setCurrentImagePaneIndex(int index) 
	{
		this.imageLabel.setImage(imagesList.get(index));
		this.imageLabel.setToolTipText(Main.english.localize("img.tooltip." + ModImage.values()[index].toString().toLowerCase()));
		
		synchronized(this.lock)
		{
			this.currentIconIndex = index;
			this.imageSwitchTimer = 0;
		}
	}

	public static List<BufferedImage> getImageList()
	{
		return imagesList;
	}

	@Override
	public void run()
	{
		while(this.isVisible())
		{
			synchronized(this.lock)
			{
				if(++this.imageSwitchTimer > 10)
				{
					if(++this.currentIconIndex >= imagesList.size())
					{
						this.currentIconIndex = 0;
					}
					
					this.setCurrentImagePaneIndex(this.currentIconIndex);
				}
			}
			
			try
			{
				Thread.sleep(1000);
			} catch(InterruptedException e)
			{
				// continue unconcerned
			}
		}
	}
}
