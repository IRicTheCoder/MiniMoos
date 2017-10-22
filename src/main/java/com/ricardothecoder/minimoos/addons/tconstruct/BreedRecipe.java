package com.ricardothecoder.minimoos.addons.tconstruct;

import java.util.ArrayList;

import com.ricardothecoder.minimoos.Config;
import com.ricardothecoder.minimoos.entities.EntityFluidMoo;

import net.minecraftforge.fluids.Fluid;

public class BreedRecipe
{
	private static ArrayList<BreedRecipe> recipes = new ArrayList<BreedRecipe>();
	
	private Fluid fluidOne;
	private Fluid fluidTwo;
	
	public Fluid resultFluid;
	
	public BreedRecipe(Fluid one, Fluid two, Fluid result)
	{
		fluidOne = one;
		fluidTwo = two;
		
		resultFluid = result;
		
		recipes.add(this);
	}
	
	public boolean containsFluid(Fluid fluid)
	{
		return fluidOne.getUnlocalizedName().equals(fluid.getUnlocalizedName()) || fluidTwo.getUnlocalizedName().equals(fluid.getUnlocalizedName());
	}
	
	public static BreedRecipe canMateMoos(EntityFluidMoo male, EntityFluidMoo female)
	{
		if (!Config.allowAlloyBreeding)
			return null;
		
		for (BreedRecipe recipe : recipes)
		{
			if (recipe.containsFluid(male.getFluid()) && recipe.containsFluid(female.getFluid()) && !male.getFluid().getUnlocalizedName().equals(female.getFluid().getUnlocalizedName()))
			{
				return recipe;
			}
		}
		
		return null;
	}
}
