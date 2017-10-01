package com.ricardothecoder.minimoos.items;

import com.ricardothecoder.minimoos.References;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class DemonSoul extends Item
{
	public DemonSoul()
	{
		setCreativeTab(CreativeTabs.FOOD);
		setRegistryName(new ResourceLocation(References.MODID, "demonsoul"));
		setUnlocalizedName("demonsoul");
		setMaxStackSize(64);
	}
}
