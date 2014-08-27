package com.attributestudios.minecraft.installer.updates;

/**
 * Models an update setting for a configuration file.
 * 
 * @author  Bridger Maskrey (maskreybe@live.com)
 * @version 1.0.0
 * @date.   2014-08-19
 */
public class UpdateConfigFile extends UpdateFile
{
	/**
	 * The current version UID.
	 */
	private static final long	serialVersionUID	= 4427562199242975248L;
	
	/**
	 * The name of the mod that this config belongs to.
	 * Used simply for identification purposes.
	 */
	private String configModName = "Unknown";

	/**
	 * Gets the name of the mod that this config belongs to.
	 * @return The name of the mod that this config belongs to.
	 */
	public String getConfigModName()
	{
		return this.configModName;
	}

	/**
	 * Sets the name of the mod that this config belongs to.
	 * @param configModName The name of the mod that this config belongs to.
	 */
	public void setConfigModName(String configModName)
	{
		this.configModName = configModName;
	}
	
}
