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

import ic2.api.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Configuration;

import com.google.common.base.Throwables;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public enum CompactSolarType {
	LV(8, "Low Voltage Solar Array", "lvTransformer", TileEntityCompactSolar.class, "lvHat"),
	MV(64, "Medium Voltage Solar Array", "mvTransformer", TileEntityCompactSolarMV.class, "mvHat"),
	HV(512, "High Voltage Solar Array", "hvTransformer", TileEntityCompactSolarHV.class, "hvHat");

	private int output;
	public Class<? extends TileEntityCompactSolar> clazz;
	public String friendlyName;
	public String transformerName;
	public final ResourceLocation hatTexture;
    public final String hatName;
    private ItemSolarHat item;
    public final ResourceLocation hatItemTexture;

	private CompactSolarType(int output, String friendlyName, String transformerName, Class<? extends TileEntityCompactSolar> clazz, String hatTexture) {
		this.output=output;
		this.friendlyName=friendlyName;
		this.transformerName=transformerName;
		this.clazz=clazz;
		this.hatName = "solarHat"+name();
		this.hatTexture = new ResourceLocation("compactsolars","textures/armor/"+hatTexture+".png");
		this.hatItemTexture = new ResourceLocation("compactsolars",hatTexture);
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

	public ItemSolarHat buildHat(Configuration cfg, int id)
	{
        int itemId = cfg.getItem(hatName, id).getInt(id);
        item = new ItemSolarHat(itemId, this);
        GameRegistry.registerItem(item, hatName);
        return item;
	}

	public static void buildHats(Configuration cfg, int defaultId)
	{
	    for (CompactSolarType typ : values())
	    {
	        typ.buildHat(cfg, defaultId++);
	    }
	}

    public static void generateHatRecipes(BlockCompactSolar block)
    {
        Item ironHat = Item.helmetIron;
        for (CompactSolarType typ : values())
        {
            ItemStack solarBlock = new ItemStack(block, 0, typ.ordinal());
            GameRegistry.addShapelessRecipe(new ItemStack(typ.item), solarBlock, ironHat);
        }
    }
}
