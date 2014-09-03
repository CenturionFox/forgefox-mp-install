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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Models an update class that can be read from
 * 	an XML file.
 * 
 * @author  Bridger Maskrey (maskreybe@live.com)
 * @version 1.0.0
 * @date.	2014-08-19
 */
public class Update implements Serializable, Collection<UpdateFile>
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
	 * A description of the update (why is the update being done, what changed)
	 */
	private String updateDescription;
	
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
		
		// Set the update description to "No description available."
		this.updateDescription = "No description available.";
		
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

	public String getUpdateDescription()
	{
		return this.updateDescription;
	}
	
	public void setUpdateDescription(String newDescription)
	{
		this.updateDescription = newDescription;
	}
	
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
	public void writeToXML(File updateDirectory) throws IOException
	{
		File updateXML = new File(updateDirectory.getPath() + "/update_" + this.updateDate + ".xml");
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

	/**
	 * Adds an {@link UpdateFile} to the file list.
	 * @param file The UpdateFile to add.
	 * @return
	 */
	@Override
	public boolean add(UpdateFile file)
	{
		return this.updatedFiles.add(file);
	}
	
	@Override
	public boolean addAll(Collection<? extends UpdateFile> c) 
	{
		return this.updatedFiles.addAll(c);
	}

	@Override
	public void clear() 
	{
		this.updatedFiles.clear();
	}

	@Override
	public boolean contains(Object o) 
	{
		return this.updatedFiles.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) 
	{
		return this.updatedFiles.contains(c);
	}

	@Override
	public boolean isEmpty() 
	{
		return this.updatedFiles.isEmpty();
	}

	@Override
	public Iterator<UpdateFile> iterator() 
	{
		return this.updatedFiles.iterator();
	}

	@Override
	public boolean remove(Object o) 
	{
		return this.updatedFiles.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) 
	{
		return this.updatedFiles.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		return this.updatedFiles.retainAll(c);
	}

	@Override
	public int size() 
	{
		return this.updatedFiles.size();
	}

	@Override
	public Object[] toArray() 
	{
		return this.updatedFiles.toArray();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) 
	{
		return (T[]) this.toArray();
	}
}
