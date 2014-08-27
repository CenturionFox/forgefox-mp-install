package com.attributestudios.minecraft.installer.updates;

import java.io.Serializable;

import com.attributestudios.minecraft.installer.enums.FileAction;
import com.attributestudios.minecraft.installer.enums.Side;

/**
 * Abstract class that represents the base of any
 * 	type of updatable file related to a update. Includes
 * 	strings used to identify a new and old file,
 * 	a description string, an enum representing the
 *  action to be taken when installing this update,
 *  and the "side" to which this update should be installed.
 *  
 * @author  Bridger Maskrey (maskreybe@live.com)
 * @version 1.0.0
 * @date.	2014-08-19
 */
public abstract class UpdateFile implements Serializable
{
	/**
	 * The current version of the UpdateFile class.
	 */
	private static final long	serialVersionUID	= 3579312777503202876L;

	/**
	 * String representation of the path of the new file to install.
	 */
	protected String newFile = null;
	
	/**
	 * String representation of the path of the new file to replace or uninstall.
	 */
	protected String oldFile = null;

	/**
	 * A description of the updated file (why is it being installed,
	 * 	what changed, etc.)
	 */
	protected String description = null;
	
	/**
	 * The type of install to execute.
	 * @see FileAction The FileAction enum for more information on install types.
	 */
	private FileAction action = FileAction.INSTALL;
	
	/**
	 * The "side" on which to install the updated file.
	 * @see Side The Side enum for more information on installation sides.
	 */
	private Side updateSide = Side.COMMON; 
	
	/**
	 * Gets the file path to the new, updated file.
	 * @return The new updated file, or null if there is no new file specified.
	 */
	public String getNewFile()
	{
		return this.newFile;
	}

	/**
	 * Sets the value of the new file path.
	 * @param newFile The new new file path.
	 */
	public void setNewFile(String newFile)
	{
		this.newFile = newFile;
	}

	/**
	 * Get the file path to the old file path, if extant.
	 * @return The old file, or null if there is no old file specified.
	 */
	public String getOldFile()
	{
		return this.oldFile;
	}

	/**
	 * Sets the value of the old file path.
	 * @param oldFile The new old file path.
	 */
	public void setOldFile(String oldFile)
	{
		this.oldFile = oldFile;
	}
	
	/**
	 * Gets the action to apply when updating.
	 * @return The FileAction to apply as an update.
	 * @see FileAction The FileAction enum for more information on update file actions.
	 */
	public FileAction getAction()
	{
		return this.action;
	}

	/**
	 * Sets the action to apply when updating.
	 * @param action The new action to apply when updating.
	 * @see FileAction The FileAction enum for more information on update file actions.
	 */
	public void setAction(FileAction action)
	{
		this.action = action;
	}

	/**
	 * Gets the installation side on which to update the update file.
	 * @return The installation side on which to update the update.
	 * @see Side The Side enum for more information on installation sides.
	 */
	public Side getUpdateSide()
	{
		return this.updateSide;
	}

	/**
	 * Sets the installation side on which to update the update file.
	 * @param updateSide The installation site on which to update the update file.
	 * @see Side The Side enum for more information on installation sides.
	 */
	public void setUpdateSide(Side updateSide)
	{
		this.updateSide = updateSide;
	}
	
	/**
	 * Gets the descriptive text that accompanies this update.
	 * @return The description of this update file.
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * Sets the accompanying descriptive text of this update file.
	 * @param description The descriptive text of this update file.
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
}
