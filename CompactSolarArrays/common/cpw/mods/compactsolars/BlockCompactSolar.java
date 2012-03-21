package cpw.mods.compactsolars;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.mod_CompactSolars;
import net.minecraft.src.forge.ITextureProvider;

public class BlockCompactSolar extends BlockContainer implements ITextureProvider {
	private Random random;

	public BlockCompactSolar(int blockId) {
		super(blockId,Material.iron);
		setBlockName("CompactSolar");
		setHardness(3.0F);
		random=new Random();
		setRequiresSelfNotify();
	}
	@Override
	public TileEntity getBlockEntity() {
		return null;
	}

	@Override
	public TileEntity getBlockEntity(int md) {
		return CompactSolarType.makeEntity(md);
	}
	
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
	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer player) {
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
			player.openGui(mod_CompactSolars.instance, tecs.getType().ordinal(), world, i, j, k);
		}
		return true;
	}
	
	protected int damageDropped(int i) {
		return i;
	}
	public void onBlockRemoval(World world, int i, int j, int k)
	{
	    TileEntityCompactSolar tileSolar = (TileEntityCompactSolar)world.getBlockTileEntity(i, j, k);
	    if (tileSolar != null)
	    {
	    	dropContent(0, tileSolar, world);
	    }
	    super.onBlockRemoval(world, i, j, k);
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
                	mod_CompactSolars.proxy.applyExtraDataToDrops(entityitem, (NBTTagCompound)itemstack.getTagCompound().copy());
                }
                world.spawnEntityInWorld(entityitem);
            }
        }
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addCreativeItems(ArrayList itemList) {
		for (CompactSolarType type : CompactSolarType.values()) {
			itemList.add(new ItemStack(this,1,type.ordinal()));
		}
		
	}
}
