package com.ricardothecoder.minimoos.addons.tconstruct;

import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.yac.util.ModLogger;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;

public class TConstructPlugin
{
	public static void initTConstruct()
	{
		ModLogger.info(References.NAME, "Found Tinkers' Construct Installed. Registering addon.");
		
		for (AlloyRecipe recipe : TinkerRegistry.getAlloys())
		{
			if (recipe.getFluids().size() <= 2)
				AlloyManager.registerAlloy(recipe.getResult().getFluid(), recipe.getFluids().get(0).getFluid(), recipe.getFluids().get(1).getFluid());
		}
	}
	
	public static void registerFluidRecipe(Fluid fluid, ItemStack result, int amount)
	{
		TinkerRegistry.registerBasinCasting(result, null, fluid, amount);
	}
}
