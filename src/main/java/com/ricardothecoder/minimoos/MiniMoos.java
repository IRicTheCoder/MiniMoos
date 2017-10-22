package com.ricardothecoder.minimoos;

import com.ricardothecoder.minimoos.addons.waila.WailaPlugin;
import com.ricardothecoder.minimoos.entities.EntityFluidMoo;
import com.ricardothecoder.minimoos.entities.EntityMiniMoo;
import com.ricardothecoder.minimoos.fluids.FluidColorManager;
import com.ricardothecoder.minimoos.models.ModelMiniMoo;
import com.ricardothecoder.minimoos.proxies.CommonProxy;
import com.ricardothecoder.minimoos.renders.RenderMiniMoo;
import com.ricardothecoder.yac.fluids.FluidManager;
import com.ricardothecoder.yac.util.ModLogger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod(modid = References.MODID, version = References.VERSION, 
name = References.NAME, acceptedMinecraftVersions = References.ACCEPTED_VERSION, 
dependencies = References.DEPENDENCIES)
public class MiniMoos
{
	// Add Variables
	@Instance
	public static MiniMoos instance;
	
	public static SimpleNetworkWrapper network;
	
	@SidedProxy(clientSide = "com.ricardothecoder.minimoos.proxies.ClientProxy", serverSide = "com.ricardothecoder.minimoos.proxies.CommonProxy")
	public static CommonProxy proxy;
	
	// Static Stuff
	static 
	{	
		FluidRegistry.enableUniversalBucket();
		FluidRegistry.addBucketForFluid(FluidRegistry.WATER);
		FluidRegistry.addBucketForFluid(FluidRegistry.LAVA);
	}

	// PRE INITIALIZES THE MOD
	// Register all the stuff of the mod
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		ModLogger.info(References.NAME, "PRE-INITIALIZATION");
		Config.checkFile(event.getSuggestedConfigurationFile());
		
		network = NetworkRegistry.INSTANCE.newSimpleChannel(References.MODID);

		proxy.registerGamerules();
		
		proxy.registerItems();
		proxy.registerRecipes();
		
		proxy.registerEntities();
		proxy.registerLootTables();
		
		proxy.registerRenders();
		
		proxy.registerSpawns();
	}

	// INITIALIZES THE MOD
	// Registers things that require base registration to be done
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		ModLogger.info(References.NAME, "INITIALIZATION");
		
		proxy.registerEvents();
		proxy.registerEarlyAddons(event);
		
		Config.setupFluids();
	}
	
	
	// POST INITIALIZES THE MOD
	// Handles inter mod communication and late registration stuff
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		ModLogger.info(References.NAME, "POST-INITIALIZATION");
		
		proxy.registerLaterAddons();
	}
	
	// SERVER STARTING
	// Adds server specific stuff, such as commands and alike
	@EventHandler
	public void serverStart(FMLServerStartingEvent event)
	{
		ModLogger.info(References.NAME, "SERVER STARTING");
		
		proxy.registerCommands(event);
	}
}
