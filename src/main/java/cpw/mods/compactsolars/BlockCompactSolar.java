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

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCompactSolar extends BlockContainer {
    private Random random;
    @SideOnly(Side.CLIENT)
    private IIcon[][] textures;

    public BlockCompactSolar() {
        super(Material.iron);
        setBlockName("CompactSolar");
        setHardness(3.0F);
        random = new Random();
        setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int i) {
        return null;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return CompactSolarType.makeEntity(metadata);
    }

    @Override
    public IIcon getIcon(int i, int j) {
        if (j >= CompactSolarType.values().length) {
            return null;
        } else {
            return textures[j][i > 2 ? 2 : i];
        }
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int s, float f1, float f2, float f3) {
        if (player.isSneaking()) {
            return false;
        }

        if (world.isRemote) {
            return true;
        }

        TileEntity te = world.getTileEntity(i, j, k);
        if (te != null && te instanceof TileEntityCompactSolar) {
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
    public void breakBlock(World world, int i, int j, int k, Block par5, int par6) {
        TileEntityCompactSolar tileSolar = (TileEntityCompactSolar) world.getTileEntity(i, j, k);
        if (tileSolar != null) {
            dropContent(0, tileSolar, world);
        }
        super.breakBlock(world, i, j, k, par5, par6);
    }

    public void dropContent(int newSize, TileEntityCompactSolar tileSolar, World world) {
        for (int l = newSize; l < tileSolar.getSizeInventory(); l++) {
            ItemStack itemstack = tileSolar.getStackInSlot(l);
            if (itemstack == null) {
                continue;
            }
            float f = random.nextFloat() * 0.8F + 0.1F;
            float f1 = random.nextFloat() * 0.8F + 0.1F;
            float f2 = random.nextFloat() * 0.8F + 0.1F;
            while (itemstack.stackSize > 0) {
                int i1 = random.nextInt(21) + 10;
                if (i1 > itemstack.stackSize) {
                    i1 = itemstack.stackSize;
                }
                itemstack.stackSize -= i1;
                EntityItem entityitem = new EntityItem(world, (float) tileSolar.xCoord + f, (float) tileSolar.yCoord + (newSize > 0 ? 1 : 0) + f1, (float) tileSolar.zCoord + f2, new ItemStack(
                        itemstack.getItem(), i1, itemstack.getItemDamage()));
                float f3 = 0.05F;
                entityitem.motionX = (float) random.nextGaussian() * f3;
                entityitem.motionY = (float) random.nextGaussian() * f3 + 0.2F;
                entityitem.motionZ = (float) random.nextGaussian() * f3;
                if (itemstack.hasTagCompound()) {
                    entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                }
                world.spawnEntityInWorld(entityitem);
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List itemList) {
        for (CompactSolarType type : CompactSolarType.values()) {
            itemList.add(new ItemStack(this, 1, type.ordinal()));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        textures = new IIcon[CompactSolarType.values().length][3];
        for (CompactSolarType typ : CompactSolarType.values()) {
            for (int i = 0; i < 3; i++) {
                String side = i == 0 ? "Bottom" : i == 1 ? "Top" : "Side";
                String texName = String.format("compactsolars:%s%s", typ.name().toLowerCase(), side);
                textures[typ.ordinal()][i] = par1IconRegister.registerIcon(texName);
            }
        }
    }

}
