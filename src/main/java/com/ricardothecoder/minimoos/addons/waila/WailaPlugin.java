package com.ricardothecoder.minimoos.addons.waila;

import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.minimoos.entities.EntityFluidMoo;
import com.ricardothecoder.yac.util.ModLogger;

import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class WailaPlugin
{
	private final static String CLASSPATH = "com.ricardothecoder.minimoos.addons.waila.WailaPlugin.registerWaila";
	
	public static void initWaila()
	{
		ModLogger.info(References.NAME, "Found WAILA Installed. Registering addon.");
		FMLInterModComms.sendMessage("Waila", "register", CLASSPATH);
	}
	
	public static void registerWaila(IWailaRegistrar registrar)
	{
		registrar.registerBodyProvider(new FluidMooProvider(), EntityFluidMoo.class);
	}
}
