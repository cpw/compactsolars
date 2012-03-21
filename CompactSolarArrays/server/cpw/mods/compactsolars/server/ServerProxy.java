package cpw.mods.compactsolars.server;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;

import net.minecraft.src.Container;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICrafting;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
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
		return false;
	}

	@Override
	public void showGUI(TileEntityCompactSolar te, EntityPlayer player) {
		ModLoader.OpenGUI(player, te.getType().guiId, te, new ContainerCompactSolar(player.inventory, te, te.getType()));
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
