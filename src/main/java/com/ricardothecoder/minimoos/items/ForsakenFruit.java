package com.ricardothecoder.minimoos.items;

import com.ricardothecoder.minimoos.References;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.util.ResourceLocation;

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
}
