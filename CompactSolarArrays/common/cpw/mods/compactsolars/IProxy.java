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

import java.io.File;

import net.minecraft.src.EntityItem;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.forge.IGuiHandler;

public interface IProxy extends IGuiHandler {
	File getMinecraftDir();

	void registerTranslations();

	void registerTileEntities();

	boolean isRemote();

	void registerRenderInformation();

	void applyExtraDataToDrops(EntityItem entityitem, NBTTagCompound copy);
}
