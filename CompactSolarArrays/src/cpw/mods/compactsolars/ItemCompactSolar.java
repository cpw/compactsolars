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
package cpw.mods.compactsolars;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class ItemCompactSolar extends ItemBlockWithMetadata {
	public ItemCompactSolar(int id) {
		super(id, CompactSolars.compactSolarBlock);
	}

	public int getMetadata(int i) {
		if (i<CompactSolarType.values().length) {
			return i;
		} else {
			return 0;
		}
	}
	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		return "tile.compactsolars:"+CompactSolarType.values()[itemstack.getItemDamage()].name()+"_block";
	}
}
