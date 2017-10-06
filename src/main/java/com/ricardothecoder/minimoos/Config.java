package com.ricardothecoder.minimoos;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.ricardothecoder.minimoos.addons.tconstruct.AlloyManager;
import com.ricardothecoder.yac.fluids.FluidManager;

import net.minecraft.item.EnumRarity;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;

public class Config
{
	// Instance
	public static Configuration config;

	// Fluid Moos
	public static boolean spawnReady;
	public static int maxUseDelay;
	public static int maxQuantity;
	public static int maxEfficiency;
	public static boolean foolMoo;
	public static int foolRate;
	public static int sacredRate;
	public static int demonRate;

	// Fluid Control
	private static ArrayList<Fluid> spawnableFluidsOver = new ArrayList<Fluid>();
	private static ArrayList<Fluid> spawnableFluidsNether = new ArrayList<Fluid>();
	private static ArrayList<Fluid> spawnableFluidsEnd = new ArrayList<Fluid>();
	
	// Tinkers' Contruct Addon
	public static boolean allowBreeding;
	public static boolean noAlloySpawn;
	
	// MineAddons Addon
	public static boolean allowEntityConversion;
	
	// Mine Factory Reloaded Addon
	public static boolean harvestPenalty;
	public static boolean areMoosRanchable;
	public static boolean areExactCopiesAllowed;

	public static void load(File configFile)
	{
		if (config == null)
			config = new Configuration(configFile);

		// Global Options
		config.setCategoryComment("Fluid Moos", "Base Mini Moos configuration");
		spawnReady = config.getBoolean("SpawnReady", "Fluid Moos", true, "Should the fluid mini moos spawn ready to be used?");
		maxUseDelay = config.getInt("MaxUseDelay", "Fluid Moos", 120, 40, 3600, "What is the max time you should wait before using a cow? (In Seconds)") * 20;
		maxQuantity = config.getInt("MaxQuantity", "Fluid Moos", 4, 0, 10, "Cow's give buckets based on the quantity they have, what should be the maximum? (0 is Unlimited)");
		maxEfficiency = config.getInt("MaxEfficiency", "Fluid Moos", 4, 0, 10, "The max use delay is divided by the cow's efficiency, what should be the maximum?  (0 is Unlimited)");
		foolMoo = config.getBoolean("FoolMoo", "Fluid Moos", true, "Should the Fool Moo be allowed to spawn?");
		foolRate = config.getInt("FoolRate", "Fluid Moos", 15, 5, 25, "Percentage of Fool Moos being spawned?");
		sacredRate = config.getInt("SacredWeight", "Fluid Moos", 1, 1, 8, "The weight rate a Sacred Moo should spawn?");
		demonRate = config.getInt("DemonWeight", "Fluid Moos", 1, 1, 8, "The weight rate a Demon Moo should spawn?");

		allowBreeding = false;
		noAlloySpawn = false;
		
		allowEntityConversion = false;
		
		// Register Config for addons
		registerAddonConfig();
		
		if (config.hasChanged())
			config.save();
	}
	
	public static void lateLoad()
	{
		// Fluid Control
		config.setCategoryComment("Fluid Spawns", "Control which fluids can be used when spawning Mini Moos and theirs rate (0 will disable them)");
		config.setCategoryComment("Fluid Temperatures", "Control the temperature of each fluid (below 300 spawns in the end, above or equal to 1300 spawns in the nether)");
		for (Fluid fluid : FluidManager.getContainableFluids())
		{
			int weight = config.getInt(fluid.getLocalizedName(new FluidStack(fluid, 0)), "Fluid Spawns", 2 * getWeightByRarity(fluid.getRarity()), 0, Integer.MAX_VALUE, "");
			int temperature = config.getInt(fluid.getLocalizedName(new FluidStack(fluid, 0)), "Fluid Temperatures", fluid.getTemperature(), Integer.MIN_VALUE, Integer.MAX_VALUE, "");
			
			if (Loader.isModLoaded("tconstruct") && !canSpawnFluid(fluid))
				continue;
			
			if (fluid.getTemperature() != temperature)
				fluid.setTemperature(temperature);
			
			if (weight > 0)
			{
				for (int i = 0; i < weight; i++)
				{
					if (fluid.getTemperature() >= FluidRegistry.WATER.getTemperature() && fluid.getTemperature() < FluidRegistry.LAVA.getTemperature())
						spawnableFluidsOver.add(fluid);
					
					if (fluid.getTemperature() < FluidRegistry.WATER.getTemperature())
						spawnableFluidsEnd.add(fluid);
					
					if (fluid.getTemperature() >= FluidRegistry.LAVA.getTemperature())
						spawnableFluidsNether.add(fluid);
				}
			}
		}

		if (config.hasChanged())
			config.save();
	}

	private static int getWeightByRarity(EnumRarity rarity)
	{
		switch(rarity)
		{
			case COMMON:
				return 4;
			case UNCOMMON:
				return 3;
			case RARE:
				return 2;
			case EPIC:
				return 1;
			default:
				return 4;
		}
	}

	public static Fluid[] getSpawnableFluids(BiomeDictionary.Type type)
	{
		switch(type)
		{
			case NETHER:
				return spawnableFluidsNether.toArray(new Fluid[spawnableFluidsNether.size()]);
			case END:
				return spawnableFluidsEnd.toArray(new Fluid[spawnableFluidsEnd.size()]);
			default:
				return spawnableFluidsOver.toArray(new Fluid[spawnableFluidsOver.size()]);
		}
	}
	
	public static void registerAddonConfig()
	{
		if (Loader.isModLoaded("tconstruct"))
		{
			config.setCategoryComment("TConstruct Addon", "Controls how the mod reacts with Tinkers' Contruct");
			allowBreeding = config.getBoolean("AllowBreeding", "TConstruct Addon", true, "Should breeding moos be allowed? (Breeding Fluid Moos gives the alloy of both fluids)");
			noAlloySpawn = config.getBoolean("NoAlloySpawn", "TConstruct Addon", false, "Should alloy fluids be prevented from spawning?");
		}
		
		if (Loader.isModLoaded("mineaddons"))
		{
			config.setCategoryComment("MineAddons Addon", "Controls how the mod reacts with MineAddons");
			allowEntityConversion = config.getBoolean("AllowEntityConversion", "MineAddons Addon", true, "Should Mini Moos be converted when touching cursed or sacred water?");
		}
		
		if (Loader.isModLoaded("minefactoryreloaded"))
		{
			config.setCategoryComment("MFR Addon", "Controls how the mod reacts with MineAddons");
			harvestPenalty = config.getBoolean("HarvestPenalty", "MFR Addon", false, "Should there be a penalty on the cooldown when harvesting? (true = twice as long to harvest with a rancher)");
			areMoosRanchable = config.getBoolean("AreMoosRanchable", "MFR Addon", true, "Should Fluid Moos be ranchable?");
			areExactCopiesAllowed = config.getBoolean("AreExactCopiesAllowed", "MFR Addon", true, "Should spawning of Exact Copies be Allowed?");
		}
	}
	
	private static boolean canSpawnFluid(Fluid fluid)
	{
		if (!noAlloySpawn)
			return true;
		
		return !AlloyManager.isAlloy(fluid);
	}
}
