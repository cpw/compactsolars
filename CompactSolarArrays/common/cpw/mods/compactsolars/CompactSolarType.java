package cpw.mods.compactsolars;

import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
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
		ModLoader.addRecipe(target, args);
	}
	public int getOutput() {
		return output;
	}
	
	public static TileEntityCompactSolar makeEntity(int metadata) {
		int solartype = metadata;
		try {
			TileEntityCompactSolar te = values()[solartype].clazz.newInstance();
			return te;
		} catch (InstantiationException e) {
			// unpossible
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// unpossible
			e.printStackTrace();
		}
		return null;
	}

	public int getTextureRow() {
		return ordinal();
	}

	public String[] tileEntityNames() {
		return new String[] { "CompactSolarType."+name(), name(), name()+" Solar Array" };
	}

}
