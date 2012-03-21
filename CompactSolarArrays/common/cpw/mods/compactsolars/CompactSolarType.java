package cpw.mods.compactsolars;

import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_CompactSolars;
import net.minecraft.src.forge.Configuration;
import net.minecraft.src.ic2.api.Items;

public enum CompactSolarType {
	LV(8, "Low Voltage Solar Array", "guiLVSolarArray", "lvTransformer", TileEntityCompactSolar.class),
	MV(64, "Medium Voltage Solar Array", "guiMVSolarArray", "mvTransformer", TileEntityCompactSolarMV.class),
	HV(512, "High Voltage Solar Array", "guiHVSolarArray", "hvTransformer", TileEntityCompactSolarHV.class);

	private int output;
	public Class<? extends TileEntityCompactSolar> clazz;
	public String friendlyName;
	public String transformerName;
	public int guiId;
	public String guiName;

	private CompactSolarType(int output, String friendlyName, String guiName, String transformerName, Class<? extends TileEntityCompactSolar> clazz) {
		this.output=output;
		this.friendlyName=friendlyName;
		this.guiName=guiName;
		this.transformerName=transformerName;
		this.clazz=clazz;
	}
	
	public static void initGUIs(Configuration cfg) {
		int defGUI = 75;
		for (CompactSolarType typ : values()) {
			if (typ.guiName != null) {
				typ.guiId = Integer.parseInt(cfg.getOrCreateIntProperty(typ.guiName, Configuration.GENERAL_PROPERTY, defGUI++).value);
				mod_CompactSolars.proxy.registerGUI(typ.guiId);
			} else {
				typ.guiId = -1;
			}
		}
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
		ModLoader.AddRecipe(target, args);
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
