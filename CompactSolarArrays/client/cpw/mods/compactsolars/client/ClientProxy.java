package cpw.mods.compactsolars.client;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.src.BaseModMp;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ModLoader;
import net.minecraft.src.ModLoaderMp;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.forge.MinecraftForgeClient;
import cpw.mods.compactsolars.CompactSolarType;
import cpw.mods.compactsolars.IProxy;
import cpw.mods.compactsolars.TileEntityCompactSolar;

public class ClientProxy extends BaseModMp implements IProxy {
	@Override
	public File getMinecraftDir() {
		return Minecraft.getMinecraftDir();
	}

	@Override
	public void registerTranslations() {
		for (CompactSolarType typ : CompactSolarType.values()) {
			ModLoader.AddLocalization(typ.name() + ".name", typ.friendlyName);
		}
	}

	@Override
	public void registerTileEntities() {
		for (CompactSolarType typ : CompactSolarType.values()) {
			ModLoader.RegisterTileEntity(typ.clazz, typ.name());
		}
	}

	@Override
	public boolean isRemote() {
		return ModLoader.getMinecraftInstance().theWorld.isRemote;
	}

	@Override
	public GuiScreen HandleGUI(int i) {
		for (CompactSolarType type: CompactSolarType.values()) {
			if (type.guiId==i) {
				return GUISolar.GUI.buildGUI(type,ModLoader.getMinecraftInstance().thePlayer.inventory,CompactSolarType.makeEntity(type.ordinal()));
			}
		}
		return null;
	}

	@Override
	public String getVersion() {
		// NOOP we don't get called like that
		return "";
	}

	@Override
	public void load() {
		// NOOP we don't get called like that
		
	}

	@Override
	public void showGUI(TileEntityCompactSolar te, EntityPlayer player) {
		GUISolar.GUI.showGUI(te,player);
	}

	@Override
	public void registerRenderInformation() {
		MinecraftForgeClient.preloadTexture("/cpw/mods/compactsolars/sprites/block_textures.png");
	}

	@Override
	public void applyExtraDataToDrops(EntityItem entityitem, NBTTagCompound copy) {
        entityitem.item.setTagCompound(copy);
	}

	@Override
	public void registerGUI(int guiId) {
		ModLoaderMp.RegisterGUI(this, guiId);
	}

}
