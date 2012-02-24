package cpw.mods.compactsolars.client;

import org.lwjgl.opengl.GL11;

import cpw.mods.compactsolars.CompactSolarType;
import cpw.mods.compactsolars.ContainerCompactSolar;
import cpw.mods.compactsolars.TileEntityCompactSolar;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ModLoader;

public class GUISolar extends GuiContainer {
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
		public static void showGUI(TileEntityCompactSolar te, EntityPlayer player) {
			GUISolar gui=buildGUI(te.getType(),player.inventory,te);
			if (gui!=null) {
				ModLoader.OpenGUI(player, gui);
			} else {
				player.displayGUIChest(te);
			}
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
	protected void drawGuiContainerForegroundLayer() {
        fontRenderer.drawString(type.mainType.friendlyName, 8, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        int k = mc.renderEngine.getTexture("/ic2/sprites/GUISolarGenerator.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(k);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
        if (container.tile.theSunIsVisible)
        {
            drawTexturedModalRect(l + 80, i1 + 45, 176, 0, 14, 14);
        }
	}

}
