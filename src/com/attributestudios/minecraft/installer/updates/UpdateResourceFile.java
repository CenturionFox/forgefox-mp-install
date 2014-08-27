package com.attributestudios.minecraft.installer.updates;

import com.attributestudios.minecraft.installer.enums.ResourceType;

/**
 * Represents an update resource file.
 * @author  Bridger Maskrey (maskreybe@live.com)
 * @version 1.0.0
 * @date.	2014-08-19
 */
public class UpdateResourceFile extends UpdateFile
{
	/**
	 * Version UID
	 */
	private static final long	serialVersionUID	= -2011399729145283422L;
	
	private ResourceType typeOfResource = ResourceType.OTHER;

	public ResourceType getTypeOfResource()
	{
		return this.typeOfResource;
	}

	public void setTypeOfResource(ResourceType typeOfResource)
	{
		this.typeOfResource = typeOfResource;
	}
}
