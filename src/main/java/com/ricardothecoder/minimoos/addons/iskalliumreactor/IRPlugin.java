package com.ricardothecoder.minimoos.addons.iskalliumreactor;

import com.ricardothecoder.minimoos.Config;
import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.minimoos.addons.tconstruct.TConstructPlugin;
import com.ricardothecoder.minimoos.feed.FeedRecipe;
import com.ricardothecoder.minimoos.fluids.Fluids;
import com.ricardothecoder.yac.util.ModLogger;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import zairus.iskalliumreactors.block.IRBlocks;
import zairus.iskalliumreactors.item.IRItems;

public class IRPlugin
{
	public static void init()
	{
		if (!Config.allowIRExpansion)
			return;
		
		ModLogger.info(References.NAME, "Found Iskallium Reactors Installed. Registering addon.");
		
		new FeedRecipe(new ItemStack(IRItems.ISKALLIUM_ESSENCE), Fluids.liquidIskallium, 1, 10);
		
		if (Loader.isModLoaded("tconstruct"))
		{
			TConstructPlugin.registerFluidRecipe(Fluids.liquidIskallium, new ItemStack(IRBlocks.ISKALLIUM_ORE, 1, 0), 2252);
		}
	}
}
