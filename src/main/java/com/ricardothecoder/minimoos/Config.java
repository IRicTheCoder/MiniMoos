package com.ricardothecoder.minimoos;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.ricardothecoder.minimoos.addons.tconstruct.AlloyManager;
import com.ricardothecoder.yac.fluids.FluidManager;

import net.minecraft.item.EnumRarity;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;

public class Config
{
	// Instance
	public static Configuration config;

	// Version Control
	public static int verCheck;

	// Fluid Moos
	public static boolean spawnReady = true;
	public static int maxUseDelay = 140;
	public static int maxQuantity = 4;
	public static int maxEfficiency = 4;
	public static boolean allowBreeding = true;

	// Fool Moos
	public static boolean foolMoo = true;
	public static int foolRate = 5;

	// Stats Moos
	public static int sacredRate = 2;
	public static int demonRate = 2;
	public static int forsakenRate = 4;
	public static int cloneRate = 75;

	// Other Moos
	public static boolean bossMoo = true;
	public static boolean hallowenMoo = true;
	public static boolean hallowenMoosSpawnAllYear = true;

	// Tinkers' Contruct Addon
	public static boolean allowAlloyBreeding = true;
	public static boolean noAlloySpawn = false;
	public static boolean allowEntityMelting = false;

	// MineAddons Addon
	public static boolean allowEntityConversion = true;

	// Mine Factory Reloaded Addon
	public static boolean harvestPenalty = false;
	public static boolean areMoosRanchable = true;
	public static boolean areExactCopiesAllowed = true;
	public static boolean shouldStatsBeCopied = false;
	public static int exactCopyPercentage = 75;

	// Waila Addon
	public static boolean showWailaInfo = true;
	
	// Avaritia Addon
	public static boolean allowAvaritiaExpansion = true;

	// Fluid Control
	private static boolean resetFluids = true;
	private static HashMap<DimensionType, ArrayList<Fluid>> fluids = new HashMap<DimensionType, ArrayList<Fluid>>();
	public static HashMap<Fluid, Integer> fluidWeight = new HashMap<Fluid, Integer>();
	public static ArrayList<Fluid> excludedFluids = new ArrayList<Fluid>();

	public static void checkFile(File configFile)
	{
		if (config == null)
			config = new Configuration(configFile);

		if (!config.hasKey("Version Check", "Version") && configFile.exists())
		{
			spawnReady = config.getBoolean("SpawnReady", "Fluid Moos", true, "Should the fluid mini moos spawn ready to be used?");
			maxUseDelay = config.getInt("MaxUseDelay", "Fluid Moos", 120, 40, 3600, "What is the max time you should wait before using a cow? (In Seconds)");
			maxQuantity = config.getInt("MaxQuantity", "Fluid Moos", 4, 0, 10, "Cow's give buckets based on the quantity they have, what should be the maximum? (0 is Unlimited)");
			maxEfficiency = config.getInt("MaxEfficiency", "Fluid Moos", 4, 0, 10, "The max use delay is divided by the cow's efficiency, what should be the maximum?  (0 is Unlimited)");
			foolMoo = config.getBoolean("FoolMoo", "Fluid Moos", true, "Should the Fool Moo be allowed to spawn?");

			for (Fluid fluid : FluidManager.getContainableFluids())
			{
				int weight = config.getInt(fluid.getLocalizedName(new FluidStack(fluid, 0)), "Fluid Spawns", 2 * getWeightByRarity(fluid.getRarity()), 0, Integer.MAX_VALUE, "");
				fluidWeight.put(fluid, weight);
			}

			if (Loader.isModLoaded("tconstruct"))
			{
				allowAlloyBreeding = config.getBoolean("AllowBreeding", "TConstruct Addon", true, "Should breeding moos be allowed? (Breeding Fluid Moos gives the alloy of both fluids)");
				noAlloySpawn = config.getBoolean("NoAlloySpawn", "TConstruct Addon", false, "Should alloy fluids be prevented from spawning?");
			}

			if (Loader.isModLoaded("mineaddons"))
			{
				allowEntityConversion = config.getBoolean("AllowEntityConversion", "MineAddons Addon", true, "Should Mini Moos be converted when touching cursed or sacred water?");
			}

			if (Loader.isModLoaded("minefactoryreloaded"))
			{
				harvestPenalty = config.getBoolean("HarvestPenalty", "MFR Addon", false, "Should there be a penalty on the cooldown when harvesting? (true = twice as long to harvest with a rancher)");
				areMoosRanchable = config.getBoolean("AreMoosRanchable", "MFR Addon", true, "Should Fluid Moos be ranchable?");
			}	

			configFile.delete();
		}
		else
		{	
			verCheck = config.get("Version Check", "Version", References.CFG_VERSION, "Version of the Config").getInt();

			if (config.hasChanged())
				config.save();

			if (References.CFG_VERSION != verCheck)
				configFile.delete();
		}

		load(configFile);
	}

	public static void load(File configFile)
	{
		config = new Configuration(configFile);

		// Setting Categories
		config.setCategoryComment("Version Check", "DO NOT CHANGE THIS MANUALLY, OR IT WILL BREAK YOUR CONFIG");
		config.setCategoryComment("General", "General configuration");
		config.setCategoryComment("Special Moos", "Configurations referent to Special Moos");
		config.setCategoryComment("Fluid Rates", "Control which fluids can be used when spawning Mini Moos and theirs rate (0 will disable them)");
		config.setCategoryComment("Fluid Dimension", "Controls where the fluids are spawned");

		if (Loader.isModLoaded("tconstruct")) config.setCategoryComment("Tinkers' Construct", "Configurations for the Tinker's Construct integration");
		if (Loader.isModLoaded("mineaddons")) config.setCategoryComment("Mine Addons", "Configurations for the Mine Addons integration");
		if (Loader.isModLoaded("minefactoryreloaded")) config.setCategoryComment("Mine Factory Reloaded", "Configurations for the Mine Factory Reloaded integration");
		if (Loader.isModLoaded("Waila")) config.setCategoryComment("Waila", "Configurations for the Waila integration");
		if (Loader.isModLoaded("avaritia")) config.setCategoryComment("Avaritia", "Configurations for the Avaritia integration");

		// Do version check
		verCheck = config.get("Version Check", "Version", References.CFG_VERSION, "Version of the Config").getInt();

		// Loading general configs
		spawnReady = config.get("General", "SpawnReady", spawnReady, "Should fluid moos spawn ready to be milked? [Default: true]").getBoolean();
		maxUseDelay = config.get("General", "MaxUseDelay", maxUseDelay, "Max milking cooldown for moos? (In Seconds) [40 ~ 3600; Default: 120]", 40, 3600).getInt() * 20;
		maxQuantity = config.get("General", "MaxQuantity", maxQuantity, "Maximum quantity of buckets that a moo can give? (0 is Unlimited) [0 ~ 10; Default: 4]", 0, 10).getInt();
		maxEfficiency = config.get("General", "MaxEfficiency", maxEfficiency, "Maximum efficiency that a moo can have? (0 is Unlimited) [0 ~ 10; Default: 4]", 0, 10).getInt();
		allowBreeding = config.get("General", "AllowBreeding", allowBreeding, "Should fluid moos breed with the same fluid? [Default: true]").getBoolean();

		// Loading Special Moos
		foolMoo = config.get("Special Moos", "FoolMoo", foolMoo, "Should the Fool Moo be allowed to spawn? [Default: true]").getBoolean();
		foolRate = config.get("Special Moos", "FoolRate", foolRate, "Percentage of Fool Moos being spawned? [5 ~ 25; Default: 5]", 5, 25).getInt();

		sacredRate = config.get("Special Moos", "SacredWeight", sacredRate, "The spawning weight of a Sacred Moo? [1 ~ 8; Default: 2]", 1, 8).getInt();
		demonRate = config.get("Special Moos", "DemonWeight", demonRate, "The spawning weight of a Demon Moo? [1 ~ 8; Default: 2]", 1, 8).getInt();
		forsakenRate = config.get("Special Moos", "ForsakenWeight", forsakenRate, "The spawning weight of a Forsaken Moo? [1 ~ 8; Default: 4]", 1, 8).getInt();

		cloneRate = config.get("Special Moos", "CloneRate", cloneRate, "Percentage of a Demon or Sacred Moo to breed? [1 ~ 100; Default: 75]", 1, 100).getInt();

		bossMoo = config.get("Special Moos", "BossMoo", bossMoo, "Should Boss Moos spawn? [Default: true]").getBoolean();
		hallowenMoo = config.get("Special Moos", "HalloweenMoo", hallowenMoo, "Should Halloween Moos appear? [Default: true]").getBoolean();
		hallowenMoosSpawnAllYear = config.get("Special Moos", "HalloweenMoosSpawnAllYear", hallowenMoosSpawnAllYear, "Should Halloween Moos spawn all year? (Their spawn rate is lower outside halloween) [Default: true]").getBoolean();

		// Loading Tinkers' Construct
		if (Loader.isModLoaded("tconstruct"))
		{
			allowAlloyBreeding = config.get("Tinkers' Construct", "AllowAlloyBreeding", allowAlloyBreeding, "Should alloy breeding be allowed? [Default: true]").getBoolean();
			noAlloySpawn = config.get("Tinkers' Construct", "NoAlloySpawn", noAlloySpawn, "Should alloy fluids be prevented from spawning? [Default: false]").getBoolean();
			allowEntityMelting = config.get("Tinkers' Construct", "AllowEntityMelting", allowEntityMelting, "Should fluid moos melt into their fluid inside the smeltery? [Default: true]").getBoolean();
		}

		// Loading Mine Addons
		if (Loader.isModLoaded("mineaddons"))
		{
			allowEntityConversion = config.get("Mine Addons", "AllowEntityConversion", allowEntityConversion, "Should Mini Moos be converted when touching cursed or sacred water? [Default: true]").getBoolean();
		}

		// Loading Mine Factory Reloaded
		if (Loader.isModLoaded("minefactoryreloaded"))
		{
			areMoosRanchable = config.get("Mine Factory Reloaded", "AreMoosRanchable", areMoosRanchable, "Should Fluid Moos be ranchable? [Default: true]").getBoolean();
			harvestPenalty = config.get("Mine Factory Reloaded", "HarvestPenalty", harvestPenalty, "Should there be a penalty on the cooldown when harvesting? [Default: false]").getBoolean();
			areExactCopiesAllowed = config.get("Mine Factory Reloaded", "AreExactCopiesAllowed", areExactCopiesAllowed, "Should the spawning of Exact Copies be allowed? [Default: true]").getBoolean();
			shouldStatsBeCopied = config.get("Mine Factory Reloaded", "ShouldStatsBeCopied", shouldStatsBeCopied, "Should Exact Copies also copy the stats? [Default: false]").getBoolean();
			exactCopyPercentage = config.get("Mine Factory Reloaded", "ExactCopyPercentage", exactCopyPercentage, "What is the percentage of spawning an Exact Copy? [0 ~ 100; Default: 75]", 0, 100).getInt();
		}

		// Loading Waila
		if (Loader.isModLoaded("Waila"))
		{
			showWailaInfo = config.get("Waila", "ShowWailaInfo", showWailaInfo, "Should waila display information? [Default: true]").getBoolean();
		}
		
		// Loading Avaritia
		if (Loader.isModLoaded("avaritia"))
		{
			allowAvaritiaExpansion = config.get("Avaritia", "AllowAvaritiaExpansion", allowAvaritiaExpansion, "Should the avaritia expansion be enabled? [Default: true]").getBoolean();
		}

		config.save();
	}

	public static void setupFluids()
	{
		Property reset = config.get("Fluid Dimension", "ResetFluids", resetFluids, "SETTING TO TRUE WILL RESET YOUR FLUID CONFIG! USE AT YOUR OWN RISK (IT TURNS OFF AFTER RUNNING WITH IT ON)");
		resetFluids = reset.getBoolean();

		ArrayList<String> spawnableFluidsOver = new ArrayList<String>();
		ArrayList<String> spawnableFluidsNether = new ArrayList<String>();
		ArrayList<String> spawnableFluidsEnd = new ArrayList<String>();
	
		Property overworld = config.get("Fluid Dimension", "OverworldFluids", new String[0], "Which fluids should spawn in the overworld?");
		Property nether = config.get("Fluid Dimension", "NetherFluids", new String[0], "Which fluids should spawn in the nether?");
		Property end = config.get("Fluid Dimension", "EndFluids", new String[0], "Which fluids should spawn in the end?");

		// Reset Fluids
		if (resetFluids)
		{
			for (String name : References.fluidMap.get(DimensionType.OVERWORLD))
			{
				spawnableFluidsOver.add(name);
			}

			for (String name : References.fluidMap.get(DimensionType.NETHER))
			{
				spawnableFluidsNether.add(name);
			}

			for (String name : References.fluidMap.get(DimensionType.THE_END))
			{
				spawnableFluidsEnd.add(name);
			}

			for (Fluid fluid : FluidManager.getContainableFluids())
			{
				if (excludedFluids.contains(fluid))
					continue;
				
				if (spawnableFluidsOver.contains(fluid.getName()) || spawnableFluidsNether.contains(fluid.getName()) || spawnableFluidsEnd.contains(fluid.getName()))
					continue;
				
				if (fluid.getTemperature() >= FluidRegistry.LAVA.getTemperature())
				{
					spawnableFluidsNether.add(fluid.getName());
					continue;
				}

				if (fluid.getTemperature() < FluidRegistry.WATER.getTemperature())
				{
					spawnableFluidsEnd.add(fluid.getName());
					continue;
				}

				spawnableFluidsOver.add(fluid.getName());
			}

			overworld.setValues(spawnableFluidsOver.toArray(new String[0]));
			nether.setValues(spawnableFluidsNether.toArray(new String[0]));
			end.setValues(spawnableFluidsEnd.toArray(new String[0]));
			reset.set(false);
		}

		// Configuration
		List<String> fluidOverworld = Arrays.asList(overworld.getStringList());
		List<String> fluidNether = Arrays.asList(nether.getStringList());
		List<String> fluidEnd = Arrays.asList(end.getStringList());
		
		fluids.put(DimensionType.OVERWORLD, new ArrayList<Fluid>());
		fluids.put(DimensionType.NETHER, new ArrayList<Fluid>());
		fluids.put(DimensionType.THE_END, new ArrayList<Fluid>());

		for (Fluid fluid : FluidManager.getContainableFluids())
		{
			if (excludedFluids.contains(fluid))
				continue;
			
			int weight = 0;
			if (fluidWeight.containsKey(fluid))
			{
				weight = config.get("Fluid Rates", fluid.getLocalizedName(new FluidStack(fluid, 0)), fluidWeight.get(fluid), "", 0, Integer.MAX_VALUE).getInt();
			}
			else
			{
				weight = config.get("Fluid Rates", fluid.getLocalizedName(new FluidStack(fluid, 0)), 2 * getWeightByRarity(fluid.getRarity()), "", 0, Integer.MAX_VALUE).getInt();
				fluidWeight.put(fluid, weight);
			}

			if (Loader.isModLoaded("tconstruct") && !canSpawnFluid(fluid))
				continue;

			if (weight > 0)
			{
				for (int i = 0; i < weight; i++)
				{
					if (fluidOverworld.contains(fluid.getName()))
					{
						fluids.get(DimensionType.OVERWORLD).add(fluid);
						continue;
					}
					
					if (fluidNether.contains(fluid.getName()))
					{
						fluids.get(DimensionType.NETHER).add(fluid);
						continue;
					}
					
					if (fluidEnd.contains(fluid.getName()))
					{
						fluids.get(DimensionType.THE_END).add(fluid);
						continue;
					}
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

	public static Fluid[] getSpawnableFluids(DimensionType type)
	{
		return fluids.get(type).toArray(new Fluid[0]);
	}

	private static boolean canSpawnFluid(Fluid fluid)
	{
		if (!noAlloySpawn)
			return true;

		return !AlloyManager.isAlloy(fluid);
	}
}
