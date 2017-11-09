package com.ricardothecoder.minimoos.addons.avaritia;

import com.ricardothecoder.minimoos.Config;
import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.minimoos.addons.tconstruct.TConstructPlugin;
import com.ricardothecoder.minimoos.feed.FeedRecipe;
import com.ricardothecoder.minimoos.fluids.Fluids;
import com.ricardothecoder.yac.util.ModLogger;

import morph.avaritia.init.ModItems;
import morph.avaritia.init.ModBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

public class AvaritiaPlugin
{
	public static void init()
	{
		if (!Config.allowAvaritiaExpansion)
			return;
		
		ModLogger.info(References.NAME, "Found Avaritia Installed. Registering addon.");
		
		new FeedRecipe(ModItems.neutron_pile, Fluids.liquifiedNeutronium, 1, 2000);
		
		if (Loader.isModLoaded("tconstruct"))
		{
			TConstructPlugin.registerFluidRecipe(Fluids.liquifiedNeutronium, new ItemStack(ModBlocks.resource, 1, 0), 6000);
		}
	}
}
