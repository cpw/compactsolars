/*******************************************************************************
 * Copyright (c) 2012 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.compactsolars.client;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.forge.IGuiHandler;
import net.minecraft.src.forge.MinecraftForgeClient;
import cpw.mods.compactsolars.CompactSolarType;
import cpw.mods.compactsolars.IProxy;
import cpw.mods.compactsolars.TileEntityCompactSolar;

public class ClientProxy implements IProxy, IGuiHandler {
	@Override
	public File getMinecraftDir() {
		return Minecraft.getMinecraftDir();
	}

	@Override
	public void registerTranslations() {
		for (CompactSolarType typ : CompactSolarType.values()) {
			ModLoader.addLocalization(typ.name() + ".name", typ.friendlyName);
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
				ModLoader.registerTileEntity(typ.clazz, tileNames[0]);
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
	public void registerRenderInformation() {
		MinecraftForgeClient.preloadTexture("/cpw/mods/compactsolars/sprites/block_textures.png");
	}

	@Override
	public void applyExtraDataToDrops(EntityItem entityitem, NBTTagCompound copy) {
		entityitem.item.setTagCompound(copy);
	}

	@Override
	public Object getGuiElement(int ID, EntityPlayer player, World world, int X, int Y, int Z) {
		TileEntity te=world.getBlockTileEntity(X, Y, Z);
		if (te!=null && te instanceof TileEntityCompactSolar) {
			TileEntityCompactSolar tecs=(TileEntityCompactSolar) te;
			return GUISolar.GUI.buildGUI(tecs.getType(), player.inventory, tecs);
		} else {
			return null;
		}
	}

}
