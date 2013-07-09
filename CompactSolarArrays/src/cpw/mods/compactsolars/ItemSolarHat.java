package cpw.mods.compactsolars;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;

import java.util.Map;
import java.util.Random;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.ISpecialArmor;

import com.google.common.collect.MapMaker;
import com.google.common.math.IntMath;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSolarHat extends ItemArmor implements ISpecialArmor {
    private class PlayerState {
        boolean canRain;
        public long buildUp;
        public long lastTick;
    }
    private static Random random = new Random();
    private static Map<EntityPlayer,PlayerState> playerState = new MapMaker().weakKeys().makeMap();
    private CompactSolarType type;

    public ItemSolarHat(int par1, CompactSolarType type)
    {
        super(par1, EnumHelper.addArmorMaterial("COMPACTSOLARHAT", 1, new int[] { 1, 1, 1, 1}, 1), 0, 0);
        this.type = type;
        setUnlocalizedName("compactsolars:"+type.hatName);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer)
    {
        return type.hatTexture.toString();
    }

    @Override
    public void onArmorTickUpdate(World worldObj, EntityPlayer player, ItemStack itemStack)
    {
        // client side or no sky: no charge
        if (worldObj.isRemote || worldObj.provider.hasNoSky)
        {
            return;
        }
        // productionrate is set, and the tick is not zero : no charge
        if (CompactSolars.productionRate!=1 && random.nextInt(CompactSolars.productionRate)!=0)
        {
            return;
        }
        int xCoord = MathHelper.floor_double(player.posX);
        int zCoord = MathHelper.floor_double(player.posZ);

        boolean isRaining = false;
        if (!this.playerState.containsKey(player))
        {
            this.playerState.put(player, new PlayerState());
        }
        PlayerState state = playerState.get(player);
        if (worldObj.getTotalWorldTime() % 20 == 0)
        {
            boolean canRain = worldObj.getWorldChunkManager().getBiomeGenAt(xCoord, zCoord).getIntRainfall() > 0;
            state.canRain = canRain;
        }
        isRaining = state.canRain && (worldObj.isRaining() || worldObj.isThundering());
        boolean theSunIsVisible=worldObj.isDaytime() && !isRaining && worldObj.canBlockSeeTheSky(xCoord, MathHelper.floor_double(player.posY) + 1, zCoord);

        if (!theSunIsVisible)
        {
            return;
        }

        int available = type.getOutput();
        for (ItemStack is : player.inventory.armorInventory)
        {
            if (is == itemStack)
            {
                continue;
            }
            if (is != null)
            {
                if (is.getItem() instanceof IElectricItem)
                {
                    IElectricItem electricItem = (IElectricItem) is.getItem();
                    available -= ElectricItem.manager.charge(is, available, type.ordinal()+1, false, false);
                }

            }
        }
        if (available <= 0)
        {
            state.buildUp+=IntMath.pow(2,type.ordinal());
        }
        else
        {
            state.buildUp=Math.max(state.buildUp-(worldObj.getTotalWorldTime() -state.lastTick),0);
        }
        state.lastTick = worldObj.getTotalWorldTime();
        int dose =IntMath.pow(10, type.ordinal()) * 5;
        if (state.buildUp > dose)
        {
            player.addPotionEffect(new PotionEffect(Potion.confusion.id, dose >> 2, 0));
            state.buildUp -= dose;
        }
    }

    public static void clearRaining()
    {
        ItemSolarHat.playerState.clear();
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot)
    {
        return new ArmorProperties(0, 0, 0);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot)
    {
        return 0;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot)
    {
        return;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon(type.hatItemTexture.toString());
    }
}
