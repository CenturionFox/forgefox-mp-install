package com.attributestudios.minecraft.installer.gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.attributestudios.minecraft.installer.Main;
import com.attributestudios.minecraft.installer.enums.ModImage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ScreenMain extends JFrame
{
	private static final long	serialVersionUID	= 8465552478290576935L;
	private JPanel	contentPane;
	
	private static ArrayList<ImageIcon> imagesList;
	
	private static boolean imageListInitialized = false;
	private JLabel imageLabel;
	
	private int currentIconIndex;
	
	/**
	 * Create the frame.
	 */
	public ScreenMain()
	{
		if(!imageListInitialized)
		{
			throw new RuntimeException("Attempted to load main window without first initializing the image list.");
		}
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 790, 470);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(this.contentPane);
		this.setTitle("Forgefox Modpack Installer");
		
		JPanel imagePanel = new JPanel();
		imagePanel.setBorder(UIManager.getBorder("ToolTip.border"));
		
		JPanel advancedOptionsPanel = new JPanel();
		advancedOptionsPanel.setBorder(UIManager.getBorder("ToolTip.border"));
		GroupLayout gl_contentPane = new GroupLayout(this.contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(imagePanel, GroupLayout.PREFERRED_SIZE, 564, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(advancedOptionsPanel, GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(advancedOptionsPanel, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
						.addComponent(imagePanel, GroupLayout.PREFERRED_SIZE, 256, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		imagePanel.setLayout(new BorderLayout(0, 0));
		
		JButton iterateImageLeft = new JButton("<");
		
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
				
				ScreenMain.this.setImageIcon(ScreenMain.this.currentIconIndex);
			}
		});
		
		imagePanel.add(iterateImageLeft, BorderLayout.WEST);
		
		JButton iterateImageRight = new JButton(">");
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
				
				ScreenMain.this.setImageIcon(ScreenMain.this.currentIconIndex);
			}
		});
		imagePanel.add(iterateImageRight, BorderLayout.EAST);
		
		this.imageLabel = new JLabel("");
		imagePanel.add(this.imageLabel, BorderLayout.CENTER);
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
			imagesList = new ArrayList<ImageIcon>(ModImage.values().length);
			
			for(ModImage modImage : ModImage.values())
			{
				String imageLoc = modImage.getResourceLoc();
				
				Main.log("Loading image " + imageLoc + "...", Level.FINER);
				
				BufferedImage bufferedImage = ImageIO.read(Main.class.getClassLoader().getResourceAsStream(imageLoc));
				
				// Resize the current image.
				Graphics resizeGraphics = bufferedImage.createGraphics();
				resizeGraphics.drawImage(bufferedImage, 0, 0, (int)(bufferedImage.getWidth() / 1.15F), (int)(bufferedImage.getHeight() / 1.15F), null);
				
				imagesList.add(new ImageIcon(bufferedImage));
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
	public void setImageIcon(int index) 
	{
		this.imageLabel.setIcon(imagesList.get(index));
		this.currentIconIndex = index;
	}

	public static List<ImageIcon> getImageList()
	{
		return imagesList;
	}
}
