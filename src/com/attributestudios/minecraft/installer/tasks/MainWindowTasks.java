package com.attributestudios.minecraft.installer.tasks;

import com.attributestudios.minecraft.installer.gui.ScreenMain;

public class MainWindowTasks implements Runnable 
{

	private ScreenMain mainScreen;
	
	@Override
	public void run() 
	{
		// Initialize main screen and set image label to 0.
		this.mainScreen = new ScreenMain();
		this.mainScreen.setImageIcon(0);
		
		this.mainScreen.setVisible(true);
		
		while(this.mainScreen.isVisible())
		{
		 ;
		}
	}

}
