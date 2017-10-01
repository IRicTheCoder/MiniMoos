package com.ricardothecoder.minimoos;

import com.ricardothecoder.minimoos.addons.waila.WailaPlugin;
import com.ricardothecoder.minimoos.entities.EntityFluidMoo;
import com.ricardothecoder.minimoos.entities.EntityMiniMoo;
import com.ricardothecoder.minimoos.fluids.FluidColorManager;
import com.ricardothecoder.minimoos.models.ModelMiniMoo;
import com.ricardothecoder.minimoos.proxies.CommonProxy;
import com.ricardothecoder.minimoos.renders.RenderMiniMoo;
import com.ricardothecoder.yac.util.ModLogger;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
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
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod(modid = References.MODID, version = References.VERSION, name = References.NAME, acceptedMinecraftVersions="[1.10,)", dependencies="after:*;")
public class MiniMoos
{
	@Instance
	public static MiniMoos instance;
	
	public static SimpleNetworkWrapper network;
	
	@SidedProxy(clientSide = "com.ricardothecoder.minimoos.proxies.ClientProxy", serverSide = "com.ricardothecoder.minimoos.proxies.CommonProxy")
	public static CommonProxy proxy;
	
	static 
	{	
		FluidRegistry.enableUniversalBucket();
		FluidRegistry.addBucketForFluid(FluidRegistry.WATER);
		FluidRegistry.addBucketForFluid(FluidRegistry.LAVA);
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		ModLogger.info(References.NAME, "PRE-INITIALIZATION");
		
		Config.load(event.getSuggestedConfigurationFile());
		
		network = NetworkRegistry.INSTANCE.newSimpleChannel(References.MODID);

		proxy.registerEntities();		
		proxy.registerLootTables();
		
		proxy.registerItems();
		
		proxy.registerRenders();
		
		proxy.registerSpawns();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		ModLogger.info(References.NAME, "INITIALIZATION");
		
		Config.lateLoad();
		
		proxy.registerEvents();
		proxy.registerEarlyAddons(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		ModLogger.info(References.NAME, "POST-INITIALIZATION");
		
		proxy.registerLaterAddons();
	}
}
