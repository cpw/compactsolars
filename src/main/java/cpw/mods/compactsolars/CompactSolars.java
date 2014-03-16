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

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "CompactSolars", name = "Compact Solar Arrays", dependencies = "required-after:Forge@[10.12,);required-after:IC2@[2.1,)")
public class CompactSolars
{
    @SidedProxy(clientSide = "cpw.mods.compactsolars.client.ClientProxy", serverSide = "cpw.mods.compactsolars.CommonProxy")
    public static CommonProxy proxy;
    public static BlockCompactSolar compactSolarBlock;
    public static int productionRate = 1;
    @Instance("CompactSolars")
    public static CompactSolars instance;

    @EventHandler
    public void preInit (FMLPreInitializationEvent preinit)
    {
        Version.init(preinit.getVersionProperties());
        preinit.getModMetadata().version = Version.version();
        Configuration cfg = new Configuration(preinit.getSuggestedConfigurationFile());
        try
        {
            cfg.load();
            compactSolarBlock = new BlockCompactSolar();
            CompactSolarType.buildHats();
            Property scale = cfg.get(Configuration.CATEGORY_GENERAL, "scaleFactor", 1);
            scale.comment = "The EU generation scaling factor. " + "The average number of ticks needed to generate one EU packet." + "1 is every tick, 2 is every other tick etc. "
                    + "Each Solar will still generate a whole packet (8, 64, 512 EU).";
            productionRate = scale.getInt(1);
        }
        catch (Exception e)
        {
            FMLLog.log(Level.ERROR, e, "CompactSolars was unable to load it's configuration successfully");
            throw new RuntimeException(e);
        }
        finally
        {
            cfg.save();
        }
    }

    @EventHandler
    public void load (FMLInitializationEvent init)
    {
        GameRegistry.registerBlock(compactSolarBlock, ItemCompactSolar.class, "CompactSolarBlock");
        for (CompactSolarType typ : CompactSolarType.values())
        {
            GameRegistry.registerTileEntity(typ.clazz, typ.tileEntityName());
        }
        proxy.registerTileEntityRenderers();
        proxy.registerRenderInformation();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
    }

    @EventHandler
    public void modsLoaded (FMLPostInitializationEvent postinit)
    {
        CompactSolarType.generateRecipes(compactSolarBlock);
        CompactSolarType.generateHatRecipes(compactSolarBlock);
    }

    @EventHandler
    public void resetMap (FMLServerStoppingEvent evt)
    {
        ItemSolarHat.clearRaining();
    }
}
