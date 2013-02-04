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

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCompactSolar extends BlockContainer {
	private Random random;

	public BlockCompactSolar(int blockId) {
		super(blockId,Material.iron);
		setBlockName("CompactSolar");
		setHardness(3.0F);
		random=new Random();
		setRequiresSelfNotify();
		setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return CompactSolarType.makeEntity(metadata);
	}

	@Override
	public int getBlockTexture(IBlockAccess worldAccess, int i, int j, int k, int l) {
		int meta=worldAccess.getBlockMetadata(i, j, k);
		CompactSolarType type=CompactSolarType.values()[meta];
		if (l==1) {
			return type.getTextureRow()*16+1;		// Top
		} else if (l==0) {
			return type.getTextureRow()*16+2;		// Bottom
		} else {
			return type.getTextureRow()*16;			// Sides
		}
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		CompactSolarType typ=CompactSolarType.values()[j];
		switch (i) {
		case 1: // Top
			return typ.getTextureRow()*16+1;
		case 0: // Bottom
			return typ.getTextureRow()*16+2;
		default: // Sides
			return typ.getTextureRow()*16;
		}
	}

	public String getTextureFile() {
		return "/cpw/mods/compactsolars/sprites/block_textures.png";
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int s, float f1, float f2, float f3) {
        if (player.isSneaking())
        {
            return false;
        }

        if (world.isRemote) {
        	return true;
        }

		TileEntity te = world.getBlockTileEntity(i, j, k);
		if (te!=null && te instanceof TileEntityCompactSolar) {
			TileEntityCompactSolar tecs = (TileEntityCompactSolar) te;
			player.openGui(CompactSolars.instance, tecs.getType().ordinal(), world, i, j, k);
		}
		return true;
	}

	@Override
	public int damageDropped(int i) {
		return i;
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, int par5, int par6) {
	    TileEntityCompactSolar tileSolar = (TileEntityCompactSolar)world.getBlockTileEntity(i, j, k);
	    if (tileSolar != null)
	    {
	    	dropContent(0, tileSolar, world);
	    }
	    super.breakBlock(world, i, j, k, par5, par6);
	}

	public void dropContent(int newSize, TileEntityCompactSolar tileSolar, World world) {
        for (int l = newSize; l < tileSolar.getSizeInventory(); l++)
        {
            ItemStack itemstack = tileSolar.getStackInSlot(l);
            if (itemstack == null)
            {
                continue;
            }
            float f = random.nextFloat() * 0.8F + 0.1F;
            float f1 = random.nextFloat() * 0.8F + 0.1F;
            float f2 = random.nextFloat() * 0.8F + 0.1F;
            while (itemstack.stackSize > 0)
            {
                int i1 = random.nextInt(21) + 10;
                if (i1 > itemstack.stackSize)
                {
                    i1 = itemstack.stackSize;
                }
                itemstack.stackSize -= i1;
                EntityItem entityitem = new EntityItem(world, (float)tileSolar.xCoord + f, (float)tileSolar.yCoord + (newSize>0 ? 1 : 0) + f1, (float)tileSolar.zCoord + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
                float f3 = 0.05F;
                entityitem.motionX = (float)random.nextGaussian() * f3;
                entityitem.motionY = (float)random.nextGaussian() * f3 + 0.2F;
                entityitem.motionZ = (float)random.nextGaussian() * f3;
                if (itemstack.hasTagCompound())
                {
                	entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                }
                world.spawnEntityInWorld(entityitem);
            }
        }
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List itemList) {
		for (CompactSolarType type : CompactSolarType.values()) {
			itemList.add(new ItemStack(this,1,type.ordinal()));
		}
	}
}
