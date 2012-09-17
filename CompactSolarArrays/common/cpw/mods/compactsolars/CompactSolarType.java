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

import com.google.common.base.Throwables;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.src.ItemStack;
import ic2.api.Items;

public enum CompactSolarType {
	LV(8, "Low Voltage Solar Array", "lvTransformer", TileEntityCompactSolar.class),
	MV(64, "Medium Voltage Solar Array", "mvTransformer", TileEntityCompactSolarMV.class),
	HV(512, "High Voltage Solar Array", "hvTransformer", TileEntityCompactSolarHV.class);

	private int output;
	public Class<? extends TileEntityCompactSolar> clazz;
	public String friendlyName;
	public String transformerName;

	private CompactSolarType(int output, String friendlyName, String transformerName, Class<? extends TileEntityCompactSolar> clazz) {
		this.output=output;
		this.friendlyName=friendlyName;
		this.transformerName=transformerName;
		this.clazz=clazz;
	}

	public static void generateRecipes(BlockCompactSolar block) {
		ItemStack solar=Items.getItem("solarPanel");
		ItemStack parent=solar;
		for (CompactSolarType typ : values()) {
			ItemStack targ=new ItemStack(block,1,typ.ordinal());
			ItemStack transformer=Items.getItem(typ.transformerName);
			addRecipe(targ,"SSS","SXS","SSS",'S',parent,'X',transformer);
			parent=targ;
		}
	}

	private static void addRecipe(ItemStack target, Object... args) {
		GameRegistry.addRecipe(target, args);
	}
	public int getOutput() {
		return output;
	}

	public static TileEntityCompactSolar makeEntity(int metadata) {
		int solartype = metadata;
		try {
			TileEntityCompactSolar te = values()[solartype].clazz.newInstance();
			return te;
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}

	public int getTextureRow() {
		return ordinal();
	}

	public String tileEntityName() {
		return "CompactSolarType."+name();
	}

}
