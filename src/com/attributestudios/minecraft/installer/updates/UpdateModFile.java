package com.attributestudios.minecraft.installer.updates;

/**
 * Models a file that should be updated.
 * 
 * @author  Bridger Maskrey (maskreybe@live.com)
 * @version 1.0.0
 * @date.   2014-08-19
 */
public class UpdateModFile extends UpdateFile
{	
	/**
	 * Version UID.
	 */
	private static final long	serialVersionUID	= 1873215480038399069L;
	
	private String modName 	 = "Unknown";
	private String modAuthor = "Unknown";
	
	private String modURL = null;	
	
	public String getModName()
	{
		return this.modName;
	}

	public void setModName(String modName)
	{
		this.modName = modName;
	}

	public String getModAuthor()
	{
		return this.modAuthor;
	}

	public void setModAuthor(String modAuthor)
	{
		this.modAuthor = modAuthor;
	}

	public String getModURL()
	{
		return this.modURL;
	}

	public void setModURL(String modURL)
	{
		this.modURL = modURL;
	}
}
