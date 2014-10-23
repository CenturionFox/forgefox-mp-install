package com.attributestudios.minecraft.installer.tasks;

import java.util.concurrent.Callable;

import com.attributestudios.minecraft.installer.gui.ScreenMain;

public class MainWindowTasks implements Callable<Void> 
{

	private ScreenMain mainScreen;
	
	@Override
	public Void call() 
	{
		// Initialize main screen and set image label to 0.
		this.mainScreen = new ScreenMain();
		this.mainScreen.setCurrentImagePaneIndex(0);
		
		this.mainScreen.setVisible(true);
		this.mainScreen.requestFocus();
		
		new Thread(this.mainScreen, "MainScreen Image Iteration Thread").start();
		
		while(this.mainScreen.isVisible())
		{
			;
		}
		
		return null;
	}

}
