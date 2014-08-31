import java.io.File;
import java.io.IOException;

import com.attributestudios.minecraft.installer.enums.FileAction;
import com.attributestudios.minecraft.installer.enums.ResourceType;
import com.attributestudios.minecraft.installer.enums.Side;
import com.attributestudios.minecraft.installer.updates.Update;
import com.attributestudios.minecraft.installer.updates.UpdateConfigFile;
import com.attributestudios.minecraft.installer.updates.UpdateModFile;
import com.attributestudios.minecraft.installer.updates.UpdateResourceFile;


public class TestXML {

	public static void main(String[] args)
	{
		Update update = new Update();
		
		update.setUpdateDate("2014-09-01");
		
		UpdateModFile modFile = new UpdateModFile();
		modFile.setAction(FileAction.REPLACE);
		modFile.setDescription("Mod Description.");
		modFile.setModAuthor("Mod Author");
		modFile.setModName("Mod Name!");
		modFile.setModURL("www.themobwebsite.com");
		modFile.setNewFile("/mods/the new mod file.zip");
		modFile.setOldFile("/mods/the old mod file.zip");
		modFile.setUpdateSide(Side.CLIENT);
		
		UpdateConfigFile configFile = new UpdateConfigFile();
		configFile.setAction(FileAction.REPLACE);
		configFile.setConfigModName("Mod Name!");
		configFile.setDescription("Config Description.");
		configFile.setNewFile("/config/the new config file.cfg");
		configFile.setOldFile("/config/the old config file.cfg");
		configFile.setUpdateSide(Side.SERVER);
		
		UpdateResourceFile resourceFile = new UpdateResourceFile();
		resourceFile.setAction(FileAction.REMOVE);
		resourceFile.setDescription("Resource Description.");
		resourceFile.setNewFile("/resources/the new resource file.ogg");
		resourceFile.setOldFile("/resources/the old resource file.ogg");
		resourceFile.setTypeOfResource(ResourceType.MUSIC);
		resourceFile.setUpdateSide(Side.CLIENT);
		
		update.add(modFile);
		update.add(configFile);
		update.add(resourceFile);
		
		try 
		{
			update.writeToXML(new File("./updates"));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
}
