/**
 * Contains all GUI information and classes.
 */
package com.attributestudios.minecraft.installer.enums;

/**
 * An enumeration of all mod image resource locations
 * 	that can be applied to the image label of the main
 * 	installer gui.
 * 
 * @author 	Bridger Maskrey
 * @version 1.0.0
 * @date.   2014-08-13
 */
public enum ModImage
{
	// Define enumeration
	BASEMENT("img/basement.png"),
	MOUNTAIN("img/mountain.png"),
	SURVIVAL("img/survival.png"),
	MACHINE ("img/machine.png"),
	REDWOOD ("img/redwood.png"),
	BONES   ("img/bones.png"),
	FOX     ("img/fox.png"),
	SKY		("img/sky.png");
	
	/**
	 * The resource location of the mod image.
	 */
	private String resourceLocation;
	
	/**
	 * Creates a new ModImage with the specified
	 *  resource location.
	 * @param newResourceLoc The ModImage's resource location.
	 */
	ModImage(String newResourceLoc)
	{
		this.resourceLocation = newResourceLoc;
	}
	
	/**
	 * Obtains the resource location of the ModImage.
	 * @return The resource location of the ModImage.
	 */
	public String getResourceLoc()
	{
		return this.resourceLocation;
	}
}
