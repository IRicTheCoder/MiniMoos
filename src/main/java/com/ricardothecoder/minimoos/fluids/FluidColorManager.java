package com.ricardothecoder.minimoos.fluids;

import java.awt.Color;
import java.util.HashMap;

import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.yac.fluids.FluidManager;
import com.ricardothecoder.yac.util.ColorUtil;
import com.ricardothecoder.yac.util.ModLogger;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FluidColorManager
{
	private static HashMap<String, Color> texColors = new HashMap<String, Color>();
	
	public static Color getFluidColor(Fluid fluid)
	{
		int fluidColor = fluid.getColor();
		if (fluidColor != 0xFFFFFFFF)
			return new Color((fluidColor >> 16) & 0xFF, (fluidColor >> 8) & 0xFF, (fluidColor) & 0xFF, 255);
		else
			return getFluidTexColor(fluid);
	}
	
	public static Color getFluidTexColor(Fluid fluid)
	{		
		if (texColors.containsKey(fluid.getUnlocalizedName()))
			return texColors.get(fluid.getUnlocalizedName());
		else
			return Color.white;
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onTextureStich(TextureStitchEvent.Post event)
	{
		for (Fluid fluid : FluidManager.getContainableFluids())
		{
			TextureAtlasSprite fluidIcon = event.getMap().getAtlasSprite(fluid.getStill().toString());
			
			Color texColor = ColorUtil.getTextureColor(fluidIcon.getFrameTextureData(0));
			//ModLogger.info(References.MODID, "Registering a texture color (" + texColor + ") for fluid '" + fluid.getLocalizedName(new FluidStack(fluid, 0)) + "'");
			texColors.put(fluid.getUnlocalizedName(), texColor);
		}
	}
}
