package com.ricardothecoder.minimoos.items;

import com.ricardothecoder.minimoos.References;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class MiniWheat extends Item
{
	public MiniWheat()
	{
		setCreativeTab(CreativeTabs.FOOD);
		setRegistryName(new ResourceLocation(References.MODID, "miniwheat"));
		setUnlocalizedName("miniwheat");
		setMaxStackSize(64);
	}
}
