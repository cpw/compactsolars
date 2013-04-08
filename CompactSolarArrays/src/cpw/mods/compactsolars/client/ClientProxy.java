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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.compactsolars.CommonProxy;
import cpw.mods.compactsolars.TileEntityCompactSolar;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerTileEntityRenderers() {
		// NOOP for now
	}

	@Override
	public void registerRenderInformation() {
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int X, int Y, int Z) {
		TileEntity te=world.getBlockTileEntity(X, Y, Z);
		if (te!=null && te instanceof TileEntityCompactSolar) {
			TileEntityCompactSolar tecs=(TileEntityCompactSolar) te;
			return GUISolar.GUI.buildGUI(tecs.getType(), player.inventory, tecs);
		} else {
			return null;
		}
	}

}
