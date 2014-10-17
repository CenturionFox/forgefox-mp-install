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
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.attributestudios.api.swing.JImagePane;
import com.attributestudios.minecraft.installer.Main;
import com.attributestudios.minecraft.installer.enums.ModImage;

public class ScreenMain extends JFrame implements Runnable
{
	private static final long	serialVersionUID	= 8465552478290576935L;
	private JPanel	contentPane;
	
	private static ArrayList<BufferedImage> imagesList;
	
	private static boolean imageListInitialized = false;
	private JImagePane imagePane;
	
	private int currentIconIndex;
	
	/**
	 * Locking object for thread synchronization.
	 */
	private final Object lock = new Object();
	
	private int imageSwitchTimer;
	
	/**
	 * Create the main screen.
	 */
	public ScreenMain()
	{
		if(!imageListInitialized)
		{
			throw new RuntimeException("Attempted to load main window without first initializing the image list.");
		}
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 865, 500);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(this.contentPane);
		this.setTitle("Forgefox Modpack Installer");
		this.setAutoRequestFocus(true);
		
		JPanel advancedOptionsPanel = new JPanel();
		advancedOptionsPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), Main.english.localize("ui.border.extrasettings"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JTabbedPane modTabs = new JTabbedPane(JTabbedPane.TOP);
		modTabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		GroupLayout contentPaneGroupLayout = new GroupLayout(this.contentPane);
		contentPaneGroupLayout.setHorizontalGroup(
			contentPaneGroupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(contentPaneGroupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(modTabs)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(advancedOptionsPanel, GroupLayout.PREFERRED_SIZE, 186, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		contentPaneGroupLayout.setVerticalGroup(
			contentPaneGroupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(contentPaneGroupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(contentPaneGroupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(advancedOptionsPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
						.addComponent(modTabs, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE))
					.addContainerGap())
		);
		
		JPanel fullInstallTabPanel = new JPanel();
		modTabs.addTab(Main.english.localize("ui.tabs.fullinstall"), null, fullInstallTabPanel, Main.english.localize("ui.tabs.fullinstall.tip"));
		
		JPanel imageControlPanel = new JPanel();
		imageControlPanel.setBorder(UIManager.getBorder("TitledBorder.border"));
		imageControlPanel.setLayout(new BorderLayout(0, 0));
		
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
		imageControlPanel.add(iterateImageLeft, BorderLayout.WEST);
		
		
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
		imageControlPanel.add(iterateImageRight, BorderLayout.EAST);
		
		
		this.imagePane = new JImagePane(null);
		imageControlPanel.add(this.imagePane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), Main.english.localize("ui.border.installsettings"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout fullInstallTabLayout = new GroupLayout(fullInstallTabPanel);
		fullInstallTabLayout.setHorizontalGroup(
			fullInstallTabLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, fullInstallTabLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(fullInstallTabLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, fullInstallTabLayout.createSequentialGroup()
							.addGap(2)
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 592, GroupLayout.PREFERRED_SIZE))
						.addComponent(imageControlPanel, GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE))
					.addContainerGap())
		);
		fullInstallTabLayout.setVerticalGroup(
			fullInstallTabLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(fullInstallTabLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(imageControlPanel, GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		fullInstallTabPanel.setLayout(fullInstallTabLayout);
		
		
		// FOR LOOP GOES HERE
		
		JPanel temp_modpanel = new JPanel();
		modTabs.addTab("New tab", null, temp_modpanel, null);
		GroupLayout gl_temp_modpanel = new GroupLayout(temp_modpanel);
		gl_temp_modpanel.setHorizontalGroup(
			gl_temp_modpanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 618, Short.MAX_VALUE)
		);
		gl_temp_modpanel.setVerticalGroup(
			gl_temp_modpanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 400, Short.MAX_VALUE)
		);
		temp_modpanel.setLayout(gl_temp_modpanel);
		this.contentPane.setLayout(contentPaneGroupLayout);
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
		this.imagePane.setImage(imagesList.get(index));
		this.imagePane.setToolTipText(Main.english.localize("img.tooltip." + ModImage.values()[index].toString().toLowerCase()));
		
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
				Main.log("Sleep interrupted in main screen image iterator; ignoring!", Level.FINER);
			}
		}
	}
}
