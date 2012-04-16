package net.minecraft.src;

import java.io.File;

import net.minecraft.src.forge.Configuration;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.forge.NetworkMod;
import net.minecraft.src.forge.Property;
import cpw.mods.compactsolars.BlockCompactSolar;
import cpw.mods.compactsolars.CompactSolarType;
import cpw.mods.compactsolars.IProxy;
import cpw.mods.compactsolars.ItemCompactSolar;
import cpw.mods.compactsolars.ServerClientProxy;
import static cpw.mods.compactsolars.Version.*;

public class mod_CompactSolars extends NetworkMod {

	public static IProxy proxy;
	public static BlockCompactSolar compactSolarBlock;
	public static int productionRate=1;
	public static mod_CompactSolars instance;
	@Override
	public String getVersion() {
		return version();
	}

	@Override
	public void load() {
		MinecraftForge.versionDetect("CompactSolars", 3, 0, 1);
		proxy=ServerClientProxy.getProxy();
		if (mod_CompactSolars.instance==null) {
			mod_CompactSolars.instance=this;
		}
		MinecraftForge.setGuiHandler(mod_CompactSolars.instance, proxy);
		File cfgFile = new File(proxy.getMinecraftDir(), "config/IC2CompactSolars.cfg");
		Configuration cfg = new Configuration(cfgFile);
		try {
			cfg.load();
			Property block = cfg.getOrCreateBlockIdProperty("compactSolar", 183);
			block.comment="The block id for the compact solar arrays.";
			compactSolarBlock = new BlockCompactSolar(block.getInt(183));
			Property scale = cfg.getOrCreateIntProperty("scaleFactor", Configuration.CATEGORY_GENERAL, 1);
			scale.comment="The EU generation scaling factor. " +
					"The average number of ticks needed to generate one EU packet." +
					"1 is every tick, 2 is every other tick etc. " +
					"Each Solar will still generate a whole packet (8, 64, 512 EU).";
			productionRate = scale.getInt(1);
		} catch (Exception e) {
			ModLoader.getLogger().severe("CompactSolars was unable to load it's configuration successfully");
			e.printStackTrace(System.err);
			throw new RuntimeException(e);
		} finally {
			cfg.save();
		}
		ModLoader.registerBlock(compactSolarBlock, ItemCompactSolar.class);
		proxy.registerTranslations();
		proxy.registerTileEntities();
		proxy.registerRenderInformation();
	}

	@Override
	public void modsLoaded() {
		CompactSolarType.generateRecipes(compactSolarBlock);
	}

	@Override
	public boolean clientSideRequired() {
		return true;
	}

	@Override
	public boolean serverSideRequired() {
		return false;
	}
}
