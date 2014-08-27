package com.attributestudios.minecraft.installer.updates;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Models an update class that can be read from
 * 	an XML file.
 * 
 * @author  Bridger Maskrey (maskreybe@live.com)
 * @version 1.0.0
 * @date.	2014-08-19
 */
public class Update implements Serializable
{	
	/**
	 * The version UID for the Update object.
	 */
	private static final long	serialVersionUID	= 20295096748394L;
	
	/**
	 * DateFormat object to be used to build a date from the updated date.
	 */
	public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * A string representation of the update date, formatted
	 * 	in the style of "yyyy-MM-dd".
	 */
	private String updateDate;
	
	/**
	 * A list of all files that should be updated.
	 */
	private List<UpdateFile> updatedFiles;
	
	/**
	 * Creates a new Update object and initializes the update date and
	 * 	the updated file list.
	 */
	public Update()
	{
		// Set the update date to the java standard epoch (1970-01-01)
		this.updateDate = "1970-01-01";
		
		// Initialize the updated file list.
		this.updatedFiles = new ArrayList<UpdateFile>();
	}	
	
	/**
	 * Gets the date that this update was published on.
	 * @return The update date of the update.
	 */
	public String getUpdateDate()
	{
		return this.updateDate;
	}
	
	/**
	 * Sets the update date to a new update date.
	 * @param updateDate The new date-formatted String to set as the update date.
	 */
	public void setUpdateDate(String updateDate)
	{
		this.updateDate = updateDate;
	}
	
	/**
	 * Gets a reference to the list of files to include in the update.
	 * @return A reference to the file update list, which can be edited
	 */
	public List<UpdateFile> getUpdatedFiles()
	{
		return this.updatedFiles;
	}

	/**
	 * Sets the list of updated files to a new list of updated files.
	 * @param updatedFiles The new list of updated files to set as the list of updated files.
	 */
	public void setUpdatedFiles(List<UpdateFile> updatedFiles)
	{
		this.updatedFiles = updatedFiles;
	}
	
	/**
	 * Writes the update to a new XML file with a name equivalent to "update_" + the update date.
	 * @throws IOException Thrown when the XML file fails to be written to the disk.
	 */
	public void writeToXML() throws IOException
	{
		File updateXML = new File("./updates/update_" + this.updateDate + ".xml");
		try(BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(updateXML));
			XMLEncoder encoder = new XMLEncoder(output))
		{
			encoder.writeObject(this);
		}
	}
	
	/**
	 * Reads a new Update form an extant XML file.
	 * @param xmlFile The file from which to read the new Update.
	 * @return A new Update file as read from the XML file.
	 * @throws IOException Thrown when the XML file fails to be read correctly.
	 */
	public static Update readFromXML(File xmlFile) throws IOException
	{
		try(BufferedInputStream input = new BufferedInputStream(new FileInputStream(xmlFile));
			XMLDecoder decoder = new XMLDecoder(input))
		{
			return (Update)decoder.readObject();
		}
	}
}
