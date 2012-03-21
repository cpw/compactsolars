package cpw.mods.compactsolars;

import java.io.File;

import net.minecraft.src.EntityItem;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.forge.IGuiHandler;

public interface IProxy extends IGuiHandler {
	File getMinecraftDir();

	void registerTranslations();

	void registerTileEntities();

	boolean isRemote();

	void registerRenderInformation();

	void applyExtraDataToDrops(EntityItem entityitem, NBTTagCompound copy);
}
