package net.minecraft.src;

import java.io.File;

import net.minecraft.src.forge.Configuration;
import net.minecraft.src.forge.MinecraftForge;
import cpw.mods.compactsolars.BlockCompactSolar;
import cpw.mods.compactsolars.CompactSolarType;
import cpw.mods.compactsolars.IProxy;
import cpw.mods.compactsolars.ItemCompactSolar;
import cpw.mods.compactsolars.ServerClientProxy;
import cpw.mods.compactsolars.TileEntityCompactSolar;


public class mod_CompactSolars extends BaseModMp {

	public static IProxy proxy;
	public static BlockCompactSolar compactSolarBlock;
	public static float productionRate=1.0F;
	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public void load() {
		MinecraftForge.versionDetect("CompactSolars", 1, 3, 3);
		proxy=ServerClientProxy.getProxy();
		File cfgFile = new File(proxy.getMinecraftDir(), "config/IC2CompactSolars.cfg");
		Configuration cfg = new Configuration(cfgFile);
		try {
			cfg.load();
			compactSolarBlock = new BlockCompactSolar(Integer.parseInt(cfg.getOrCreateBlockIdProperty("compactSolar", 183).value));
			CompactSolarType.initGUIs(cfg);
		} catch (Exception e) {
			ModLoader.getLogger().severe("CompactSolars was unable to load it's configuration successfully");
			e.printStackTrace(System.err);
			throw new RuntimeException(e);
		} finally {
			cfg.save();
		}
		ModLoader.RegisterBlock(compactSolarBlock, ItemCompactSolar.class);
		proxy.registerTranslations();
		proxy.registerTileEntities();
		proxy.registerRenderInformation();
	}

	@Override
	public void ModsLoaded() {
		CompactSolarType.generateRecipes(compactSolarBlock);
	}

	public static void openGUI(EntityPlayer player, TileEntityCompactSolar te) {
		proxy.showGUI(te,player);
	}
}
