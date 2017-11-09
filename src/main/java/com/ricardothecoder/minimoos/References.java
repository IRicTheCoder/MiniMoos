package com.ricardothecoder.minimoos;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldProviderSurface;

public class References 
{
	// Mod Information
	public static final String MODID = "minimoos";
	public static final String NAME = "MiniMoos";
    public static final String VERSION = "1.4.1";
    public static final int CFG_VERSION = 3;
    
    // Version Control & Dependencies
    public static final String ACCEPTED_VERSION = "[1.10,)";
    public static final String DEPENDENCIES = "required-after:yac@[1.3.1,);after:*;";
    
    // Debug Info
    public static final boolean DEBUG = false;
    
    // Known Fluids
    public static final HashMap<DimensionType, ArrayList<String>> fluidMap = new HashMap<DimensionType, ArrayList<String>>();
    
    static
    {
    	// OVERWORLD
    	ArrayList<String> fluidsOver = new ArrayList<String>();
    	fluidsOver.add("stone");
    	fluidsOver.add("water");
    	fluidsOver.add("biofuel");
    	fluidsOver.add("purpleslime");
    	fluidsOver.add("liquid_sunshine");
    	fluidsOver.add("xpjuice");
    	fluidsOver.add("rocket_fuel");
    	fluidsOver.add("sludge");
    	fluidsOver.add("pink_slime");
    	fluidsOver.add("sewage");
    	fluidsOver.add("glass");
    	fluidsOver.add("mushroom_soup");
    	fluidsOver.add("hootch");
    	fluidsOver.add("milk");
    	fluidsOver.add("meat");
    	fluidsOver.add("dirt");
    	fluidsOver.add("chocolate_milk");
    	fluidsOver.add("clay");
    	fluidsOver.add("blueslime");
    	fluidsOver.add("blood");
    	fluidMap.put(DimensionType.OVERWORLD, fluidsOver);
    	
    	// NETHER
    	ArrayList<String> fluidsNether = new ArrayList<String>();
    	fluidsNether.add("cobalt");
    	fluidsNether.add("conductiveiron");
    	fluidsNether.add("fire_water");
    	fluidsNether.add("gold");
    	fluidsNether.add("ardite");
    	fluidsNether.add("glowstone");
    	fluidsNether.add("redstonealloy");
    	fluidsNether.add("electricalsteel");
    	fluidsNether.add("emerald");
    	fluidsNether.add("manyullyn");
    	fluidsNether.add("vibrantalloy");
    	fluidsNether.add("pigiron");
    	fluidsNether.add("iron");
    	fluidsNether.add("redstone");
    	fluidsNether.add("knightslime");
    	fluidsNether.add("darksteel");
    	fluidsNether.add("lava");
    	fluidsNether.add("obsidian");
    	fluidsNether.add("soularium");
    	fluidsNether.add("pulsatingiron");
    	fluidsNether.add("energeticalloy");
    	fluidsNether.add("steel");
    	fluidsNether.add("terrasteel");
    	fluidsNether.add("tin");
    	fluidsNether.add("silver");
    	fluidsNether.add("mirion");
    	fluidsNether.add("copper");
    	fluidsNether.add("enderium");
    	fluidsNether.add("lead");
    	fluidsNether.add("brass");
    	fluidsNether.add("aluminum");
    	fluidsNether.add("electrum");
    	fluidsNether.add("iridium");
    	fluidsNether.add("platinum");
    	fluidsNether.add("alubrass");
    	fluidsNether.add("zinc");
    	fluidsNether.add("signalum");
    	fluidsNether.add("xu_demonic_metal");
    	fluidsNether.add("bronze");
    	fluidsNether.add("elementium");
    	fluidsNether.add("invar");
    	fluidsNether.add("manasteel");
    	fluidsNether.add("nickel");
    	fluidsNether.add("alumite");
    	fluidsNether.add("lumium");
    	fluidMap.put(DimensionType.NETHER, fluidsNether);
    	
    	// END
    	ArrayList<String> fluidsEnd = new ArrayList<String>();
    	fluidsEnd.add("cursedwater");
    	fluidsEnd.add("ender");
    	fluidsEnd.add("ender_distillation");
    	fluidsEnd.add("vapor_of_levity");
    	fluidsEnd.add("cloud_seed");
    	fluidsEnd.add("mob_essence");
    	fluidsEnd.add("cloud_seed_concentrated");
    	fluidsEnd.add("sacredwater");
    	fluidsEnd.add("nutrient_distillation");
    	fluidsOver.add("hot_spring_water");
    	fluidMap.put(DimensionType.THE_END, fluidsEnd);
    }
}
