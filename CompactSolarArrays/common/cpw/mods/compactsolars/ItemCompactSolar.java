package cpw.mods.compactsolars;

import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class ItemCompactSolar extends ItemBlock {
	public ItemCompactSolar(int id) {
		super(id);
        setMaxDamage(0);
        setHasSubtypes(true);
	}
	
	public int getMetadata(int i) {
		if (i<CompactSolarType.values().length) {
			return i;
		} else {
			return 0;
		}
	}
	@Override
	public String getItemNameIS(ItemStack itemstack) {
		return CompactSolarType.values()[itemstack.getItemDamage()].name();
	}

}
