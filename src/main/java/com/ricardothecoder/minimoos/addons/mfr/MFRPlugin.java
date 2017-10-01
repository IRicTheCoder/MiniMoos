package com.ricardothecoder.minimoos.addons.mfr;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.ricardothecoder.minimoos.Config;
import com.ricardothecoder.minimoos.MiniMoos;
import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.minimoos.entities.EntityFluidMoo;
import com.ricardothecoder.yac.util.ModLogger;

import cofh.core.fluid.FluidTankCore;
import cofh.lib.inventory.IInventoryManager;
import cofh.lib.inventory.InventoryManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import powercrystals.minefactoryreloaded.api.IFactoryRanchable;
import powercrystals.minefactoryreloaded.api.RanchedItem;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityRancher;

public class MFRPlugin implements IFactoryRanchable
{
	@Override
	public Class<? extends EntityLivingBase> getRanchableEntity()
	{
		return EntityFluidMoo.class;
	}

	@Override
	public List<RanchedItem> ranch(World world, EntityLivingBase entity, IInventory rancher)
	{
		if (!Config.areMoosRanchable)
			return null;
		
		if (entity instanceof EntityFluidMoo)
		{
			EntityFluidMoo moo = (EntityFluidMoo)entity;
			
			if (moo.isDead)
				return null;
			
			if (moo.isFool())
			{
				moo.foolPlayer(null);
				return null;
			}
			
			if (moo.getDelay() > 0)
			{
				sendPacket(moo);
				return null;
			}
			
			List<RanchedItem> items = getTankFluid(moo.getFluid(), rancher);
			
			if (items != null)
			{
				int delayMultiplier = Config.harvestPenalty ? 2 : 1;
				if (moo.stats.getBuckets() <= 1)
				{
					moo.setDelay(MathHelper.ceiling_double_int(Config.maxUseDelay / moo.stats.getEfficiency()) * delayMultiplier);
					moo.decreaseBuckets();
				}
				else
					moo.decreaseBuckets();
				
				sendPacket(moo);
				
				return items;
			}
		}
		
		return null;
	}
	
	public List<RanchedItem> getTankFluid(Fluid fluid, IInventory rancher)
	{
		FluidStack fluidStack = new FluidStack(fluid, 1000);
		
		IInventoryManager invManager = InventoryManager.create(rancher, EnumFacing.UP);
		
		if (rancher instanceof TileEntityRancher)
		{
			TileEntityRancher tRancher = (TileEntityRancher)rancher;
			
			FluidTankCore[] tanks = tRancher.getTanks();
			
			for (FluidTankCore tank : tanks)
			{
				if (tank.getFluid() == null || (tank.getFluid().getFluid() == fluid && (tank.getCapacity() - tank.getFluidAmount()) >= 1000)) 
				{
					List<RanchedItem> list = new ArrayList<RanchedItem>();
					list.add(new RanchedItem(fluidStack));
					
					return list;
				}
			}
		}
		
		return null;
	}
	
	public static MFRPlugin initMFR()
	{
		ModLogger.info(References.NAME, "Found MineFactory Reloaded Installed. Registering addon.");
		
		MFRPlugin mfr = new MFRPlugin();
		sendMessage("registerRanchable", mfr);
		return mfr;
	}
	
	public static void sendMessage(String message, Object value)
    {
        if (!Loader.isModLoaded("minefactoryreloaded") || Loader.instance().activeModContainer() == null)
            return;
        try
        {
            Method m = FMLInterModComms.class.getDeclaredMethod("enqueueMessage", Object.class, String.class, FMLInterModComms.IMCMessage.class);
            m.setAccessible(true);
            Constructor<FMLInterModComms.IMCMessage> c = FMLInterModComms.IMCMessage.class.getDeclaredConstructor(String.class, Object.class);
            c.setAccessible(true);
            m.invoke(null, Loader.instance().activeModContainer(), "minefactoryreloaded", c.newInstance(message, value));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
	
	public static void sendPacket(EntityFluidMoo moo)
    {
        NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(moo.dimension, moo.posX, moo.posY, moo.posZ, 128D);

        MiniMoos.network.sendToAllAround(new MooMessage(moo), targetPoint);
    }
	
	@SubscribeEvent
    public void onEntityInteractEvent(EntityInteract event)
    {
        if (event.getTarget() instanceof EntityFluidMoo && event.getEntityPlayer() instanceof FakePlayer)
        {
        	EntityFluidMoo entityFluidCow = (EntityFluidMoo)event.getTarget();

            sendPacket(entityFluidCow);
        }
    }
}
