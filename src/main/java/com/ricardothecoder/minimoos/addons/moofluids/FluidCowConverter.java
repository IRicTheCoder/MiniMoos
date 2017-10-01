package com.ricardothecoder.minimoos.addons.moofluids;

import com.ricardothecoder.minimoos.entities.EntityFluidMoo;
import com.robrit.moofluids.common.entity.EntityFluidCow;

import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FluidCowConverter
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onEntitySpawn(EntityJoinWorldEvent event)
	{
		if (event.getEntity().worldObj.isRemote)
			return;
		
		if (event.getEntity() instanceof EntityFluidCow)
		{
			EntityFluidCow cow = (EntityFluidCow)event.getEntity();
			
			EntityFluidMoo moo = new EntityFluidMoo(cow.worldObj);
			moo.setPositionAndUpdate(cow.posX, cow.posY, cow.posZ);
			
			if (cow.getEntityFluid() != null)
			{
				moo.setFluid(cow.getEntityFluid());
				moo.setFool(false);
			}
			
			cow.worldObj.spawnEntityInWorld(moo);
			cow.setDead();
		}
	}
}