package com.ricardothecoder.minimoos.addons.tconstruct;

import java.util.ArrayList;

import net.minecraftforge.fluids.Fluid;

public class AlloyManager
{
	private static ArrayList<String> alloys = new ArrayList<String>();
	
	public static void registerAlloy(Fluid resultFluid, Fluid ingredientOne, Fluid ingredientTwo)
	{
		alloys.add(resultFluid.getUnlocalizedName());
		new BreedRecipe(ingredientOne, ingredientTwo, resultFluid);
	}
	
	public static boolean isAlloy(Fluid fluid)
	{
		return alloys.contains(fluid.getUnlocalizedName());
	}
}
