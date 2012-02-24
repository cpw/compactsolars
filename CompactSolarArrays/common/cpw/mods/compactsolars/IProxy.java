package cpw.mods.compactsolars;

import java.io.File;

import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NBTTagCompound;

public interface IProxy {

	File getMinecraftDir();

	void registerTranslations();

	void registerTileEntities();

	boolean isRemote();

	void showGUI(TileEntityCompactSolar te, EntityPlayer player);

	void registerRenderInformation();

	void applyExtraDataToDrops(EntityItem entityitem, NBTTagCompound copy);

	void registerGUI(int guiId);
}
