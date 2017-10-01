package com.ricardothecoder.minimoos.items;

import com.ricardothecoder.minimoos.References;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class GoldenEssence extends Item
{
	public GoldenEssence()
	{
		setCreativeTab(CreativeTabs.FOOD);
		setRegistryName(new ResourceLocation(References.MODID, "goldenessence"));
		setUnlocalizedName("goldenessence");
		setMaxStackSize(64);
	}
}
