package com.attributestudios.minecraft.installer.enums;

/**
 * Type of action to apply to the new mod or configuration file.
 * 
 * FileAction consists of three different action types:<ul>
 * <li>REPLACE: replaces the specified "old file" with the specified "new file"</li>
 * <li>REMOVE:  removes the specified "old file", does nothing with the "new file"</li>
 * <li>INSTALL: installs the specified "new file", does nothing with the "old file"</li>
 * </ul>
 * Note that the default FileAction is INSTALL.
 * 
 * @author  Bridger Maskrey (maskreybe@live.com)
 * @version 1.0.0
 * @date.   2014-08-19
 */
public enum FileAction
{
	/**
	 * Replaces the specified "old file" with the specified "new file".
	 */
	REPLACE,
	/**
	 * Deletes the specified "old file" and ignored the specified "new file".
	 */
	REMOVE,
	/**
	 * Installs the specified "new file" and ignores the specified "old file".
	 */
	INSTALL;
}