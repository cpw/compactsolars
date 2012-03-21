package cpw.mods.compactsolars.client;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.src.BaseModMp;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ModLoader;
import net.minecraft.src.ModLoaderMp;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
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

		try {
			Field idToNameMap = TileEntity.class.getDeclaredField("a");
			idToNameMap.setAccessible(true);
			@SuppressWarnings("unchecked")
			Map<String, Class<?>> map = (Map<String, Class<?>>) idToNameMap.get(null);
			for (CompactSolarType typ : CompactSolarType.values()) {
				String[] tileNames = typ.tileEntityNames();
				ModLoader.RegisterTileEntity(typ.clazz, tileNames[0]);
				for (int i = 1; i < tileNames.length; i++) {
					map.put(tileNames[i], typ.clazz);
				}

			}
		} catch (Exception e) {
			// UNPOSSIBLE? hope so
			ModLoader.getLogger().severe("A fatal error occured initializing CompactSolars!");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isRemote() {
		return ModLoader.getMinecraftInstance().theWorld.isRemote;
	}

	@Override
	public GuiScreen HandleGUI(int i) {
		for (CompactSolarType type : CompactSolarType.values()) {
			if (type.guiId == i) {
				return GUISolar.GUI.buildGUI(type, ModLoader.getMinecraftInstance().thePlayer.inventory, CompactSolarType.makeEntity(type.ordinal()));
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
		GUISolar.GUI.showGUI(te, player);
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
