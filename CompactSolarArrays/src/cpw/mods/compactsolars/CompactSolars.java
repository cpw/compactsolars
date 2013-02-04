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

import java.util.logging.Level;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid="CompactSolars", name="Compact Solar Arrays", dependencies="required-after:IC2@[1.112,);required-after:Forge@[6.6.1,)")
@NetworkMod(clientSideRequired=false,serverSideRequired=true,versionBounds="[4.0,)")
public class CompactSolars {
  @SidedProxy(clientSide="cpw.mods.compactsolars.client.ClientProxy", serverSide="cpw.mods.compactsolars.CommonProxy")
	public static CommonProxy proxy;
	public static BlockCompactSolar compactSolarBlock;
	public static int productionRate=1;
	@Instance("CompactSolars")
	public static CompactSolars instance;

	@PreInit
	public void preInit(FMLPreInitializationEvent preinit) {
		Version.init(preinit.getVersionProperties());
        preinit.getModMetadata().version = Version.version();
		Configuration cfg = new Configuration(preinit.getSuggestedConfigurationFile());
		try {
			cfg.load();
			Property block = cfg.getBlock("compactSolar", 650);
			block.comment="The block id for the compact solar arrays.";
			compactSolarBlock = new BlockCompactSolar(block.getInt(650));
			CompactSolarType.buildHats(cfg, 19551);
			Property scale = cfg.get(Configuration.CATEGORY_GENERAL, "scaleFactor", 1);
			scale.comment="The EU generation scaling factor. " +
					"The average number of ticks needed to generate one EU packet." +
					"1 is every tick, 2 is every other tick etc. " +
					"Each Solar will still generate a whole packet (8, 64, 512 EU).";
			productionRate = scale.getInt(1);
		} catch (Exception e) {
			FMLLog.log(Level.SEVERE, e, "CompactSolars was unable to load it's configuration successfully");
			throw new RuntimeException(e);
		} finally {
			cfg.save();
		}
	}
	@Init
	public void load(FMLInitializationEvent init) {
		GameRegistry.registerBlock(compactSolarBlock, ItemCompactSolar.class, "CompactSolarBlock");
		for (CompactSolarType typ : CompactSolarType.values()) {
			LanguageRegistry.instance().addStringLocalization(typ.name() + ".name", typ.friendlyName);
			GameRegistry.registerTileEntity(typ.clazz, typ.tileEntityName());
		}
		proxy.registerTileEntityRenderers();
		proxy.registerRenderInformation();
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
	}

	@PostInit
	public void modsLoaded(FMLPostInitializationEvent postinit) {
		CompactSolarType.generateRecipes(compactSolarBlock);
		CompactSolarType.generateHatRecipes(compactSolarBlock);
	}

	@ServerStopping
	public void resetMap(FMLServerStoppingEvent evt)
	{
	    ItemSolarHat.clearRaining();
	}
}
