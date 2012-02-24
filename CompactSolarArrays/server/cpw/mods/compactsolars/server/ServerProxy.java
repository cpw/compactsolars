package cpw.mods.compactsolars.server;

import java.io.File;

import net.minecraft.src.Container;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICrafting;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NBTTagCompound;
import cpw.mods.compactsolars.CompactSolarType;
import cpw.mods.compactsolars.ContainerCompactSolar;
import cpw.mods.compactsolars.IProxy;
import cpw.mods.compactsolars.TileEntityCompactSolar;
import cpw.mods.ironchest.ContainerIronChestBase;

public class ServerProxy implements IProxy {

	@Override
	public File getMinecraftDir() {
		return new File(".");
	}

	@Override
	public void registerTranslations() {
		// NOOP on server
	}

	@Override
	public void registerTileEntities() {
		for (CompactSolarType typ : CompactSolarType.values()) {
			ModLoader.RegisterTileEntity(typ.clazz, typ.name());
		}
	}

	@Override
	public boolean isRemote() {
		return false;
	}

	@Override
	public void showGUI(TileEntityCompactSolar te, EntityPlayer player) {
		ModLoader.OpenGUI(player, te.getType().guiId, te, new ContainerCompactSolar(player.inventory,te, te.getType()));
	}

	@Override
	public void registerRenderInformation() {
		// NOOP on server
	}

	@Override
	public void applyExtraDataToDrops(EntityItem entityitem, NBTTagCompound copy) {
        entityitem.item.setTagCompound(copy);
	}

	@Override
	public void registerGUI(int guiId) {
		// NOOP on server
	}
}
