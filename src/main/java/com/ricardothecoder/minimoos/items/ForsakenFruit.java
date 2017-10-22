package com.ricardothecoder.minimoos.items;

import java.util.List;

import com.ricardothecoder.minimoos.References;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class ForsakenFruit extends ItemFood
{
	public ForsakenFruit()
	{
		super(14, 4.0F, false);

		setCreativeTab(CreativeTabs.FOOD);
		setRegistryName(new ResourceLocation(References.MODID, "forsakenfruit"));
		setUnlocalizedName("forsakenfruit");
		setMaxStackSize(64);
	}

	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		tooltip.add("" + TextFormatting.ITALIC + TextFormatting.YELLOW + "Feed it to a Sacred or Demon moo to make them reproduce");
		tooltip.add("" + TextFormatting.ITALIC + TextFormatting.GRAY + "You only need to feed it to one");
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
}
