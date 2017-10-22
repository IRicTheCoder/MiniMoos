package com.ricardothecoder.minimoos.items;

import java.util.List;

import com.ricardothecoder.minimoos.References;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class GoldenEssence extends Item
{
	public GoldenEssence()
	{
		setCreativeTab(CreativeTabs.FOOD);
		setRegistryName(new ResourceLocation(References.MODID, "goldenessence"));
		setUnlocalizedName("goldenessence");
		setMaxStackSize(64);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		tooltip.add("" + TextFormatting.ITALIC + TextFormatting.YELLOW + "Feed it to a Fluid Moo to improve quantity");
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
}
