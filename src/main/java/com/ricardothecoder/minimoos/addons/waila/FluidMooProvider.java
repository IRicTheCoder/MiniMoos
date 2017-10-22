package com.ricardothecoder.minimoos.addons.waila;

import java.util.List;

import com.ricardothecoder.minimoos.Config;
import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.minimoos.entities.EntityFluidMoo;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class FluidMooProvider implements IWailaEntityProvider
{
	@Override
	public Entity getWailaOverride(IWailaEntityAccessor accessor, IWailaConfigHandler config)
	{
		return null;
	}

	@Override
	public List<String> getWailaHead(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) 
	{
		return currenttip;
	}

	@Override
	public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config)
	{
		if (!Config.showWailaInfo)
			return currenttip;
		
		if (accessor.getEntity() instanceof EntityFluidMoo) {
			EntityFluidMoo fluidMoo = (EntityFluidMoo) accessor.getEntity();
			FluidStack fluidStack = new FluidStack(fluidMoo.getFluid(), 0);

			currenttip.add(TextFormatting.AQUA + "Fluid: " + TextFormatting.WHITE + fluidMoo.getFluid().getLocalizedName(fluidStack) + " (" + fluidMoo.getBuckets() + ")");
			currenttip.add(TextFormatting.AQUA + "Delay: " + TextFormatting.WHITE + formatTime(fluidMoo.getDelay()));
			currenttip.add(TextFormatting.AQUA + "Quantity: " + TextFormatting.WHITE + "" + fluidMoo.getQuantity());
			currenttip.add(TextFormatting.AQUA + "Efficiency: " + TextFormatting.WHITE + "" + fluidMoo.getEfficiency());
			
			if (References.DEBUG)
				currenttip.add(TextFormatting.AQUA + "Is Fool: " + TextFormatting.WHITE + "" + fluidMoo.isFool());
		}

		return currenttip;
	}

	@Override
	public List<String> getWailaTail(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) 
	{
		return currenttip;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP entityPlayerMP, Entity entity, NBTTagCompound nbtTagCompound, World world) 
	{
		return null;
	}

	private static String formatTime(int ticks)
	{
		int seconds = ticks / 20;
		int minutes = MathHelper.floor_double(seconds / 60);
		int trueSeconds = seconds - (minutes * 60);
		
		return String.format("%1$02d", minutes) + ":" + String.format("%1$02d", trueSeconds); 
	}
}
