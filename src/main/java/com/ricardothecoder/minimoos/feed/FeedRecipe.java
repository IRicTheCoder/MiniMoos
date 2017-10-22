package com.ricardothecoder.minimoos.feed;

import java.util.HashMap;

import com.ricardothecoder.minimoos.Config;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public class FeedRecipe
{
	public static HashMap<Item, FeedRecipe> recipes = new HashMap<Item, FeedRecipe>();
	
	public Item itemToFeed;
	public int meta;
	public Fluid resultFluid;
	public int chance;
	public int total;
	
	public FeedRecipe(ItemStack itemToFeed, Fluid resultFluid, int chance, int total)
	{
		this.itemToFeed = itemToFeed.getItem();
		this.meta = itemToFeed.getMetadata();
		this.resultFluid = resultFluid;
		this.chance = chance;
		this.total = total;
		
		recipes.put(itemToFeed.getItem(), this);
		Config.excludedFluids.add(resultFluid);
	}
	
	public ItemStack getStack()
	{
		return new ItemStack(itemToFeed, 1, meta);
	}
	
	public static Item getItemFromFluid(Fluid fluid)
	{
		for (FeedRecipe recipe : recipes.values())
		{
			if (recipe.resultFluid.equals(fluid))
			{
				return recipe.itemToFeed;
			}
		}
		
		return null;
	}
	
	public static ItemStack getItemStackFromFluid(Fluid fluid)
	{
		for (FeedRecipe recipe : recipes.values())
		{
			if (recipe.resultFluid.equals(fluid))
			{
				return new ItemStack(recipe.itemToFeed, 1, recipe.meta);
			}
		}
		
		return null;
	}
}
