package com.ricardothecoder.minimoos.addons.tconstruct;

import java.util.ArrayList;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class AlloyManager
{
	private static ArrayList<String> alloys = new ArrayList<String>();
	
	public static void registerAlloy(Fluid resultFluid, FluidStack... ingredient)
	{
		alloys.add(resultFluid.getUnlocalizedName());
		if (ingredient.length == 2)
			new BreedRecipe(ingredient[0].getFluid(), ingredient[1].getFluid(), resultFluid);
	}
	
	public static boolean isAlloy(Fluid fluid)
	{
		return alloys.contains(fluid.getUnlocalizedName());
	}
}
