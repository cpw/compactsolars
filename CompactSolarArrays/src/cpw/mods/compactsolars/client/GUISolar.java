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
package cpw.mods.compactsolars.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.compactsolars.CompactSolarType;
import cpw.mods.compactsolars.ContainerCompactSolar;
import cpw.mods.compactsolars.TileEntityCompactSolar;

public class GUISolar extends GuiContainer {
    public static final ResourceLocation gui = new ResourceLocation("ic2","textures/gui/GUISolarGenerator.png");
	public enum GUI {
		LV(CompactSolarType.LV),
		MV(CompactSolarType.MV),
		HV(CompactSolarType.HV);

		private CompactSolarType mainType;

		private GUI(CompactSolarType mainType) {
			this.mainType=mainType;
		}

		protected Container makeContainer(IInventory player, TileEntityCompactSolar solarTile) {
			return new ContainerCompactSolar(player, solarTile, mainType);
		}

		public static GUISolar buildGUI(CompactSolarType type, IInventory playerInventory, TileEntityCompactSolar solarTile) {
			for (GUI gui : values()) {
				if (solarTile.getType()==gui.mainType) {
					return new GUISolar(gui,playerInventory,solarTile);
				}
			}
			return null;
		}
	}

	private GUI type;
	private ContainerCompactSolar container;
	private GUISolar(GUI type, IInventory player, TileEntityCompactSolar chest) {
		super(type.makeContainer(player,chest));
		this.container=(ContainerCompactSolar)inventorySlots;
		this.type=type;
		this.allowUserInput=false;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p1, int p2) {
        fontRenderer.drawString(type.mainType.friendlyName, 8, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.func_110577_a(gui);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
        if (container.tile.theSunIsVisible)
        {
            drawTexturedModalRect(l + 80, i1 + 45, 176, 0, 14, 14);
        }
	}

}
