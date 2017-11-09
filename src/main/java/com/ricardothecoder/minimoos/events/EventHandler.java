package com.ricardothecoder.minimoos.events;

import java.util.List;
import java.util.UUID;

import com.google.common.base.Predicate;
import com.ricardothecoder.minimoos.entities.EntityFluidMoo;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler
{
	@SubscribeEvent
	public void onEntitySpawn(EntityJoinWorldEvent event)
	{
		if (event.getWorld().isRemote)
			return;
		
		if (event.getEntity() instanceof EntityFluidMoo)
		{
			World world = event.getWorld();
			final Entity moo = event.getEntity();
			
			Predicate<EntityFluidMoo> predicate = new Predicate<EntityFluidMoo>()
			{
				@Override
				public boolean apply(EntityFluidMoo input)
				{
					return moo.getUniqueID().equals(input.getUniqueID());
				}
			};
			
			List<EntityFluidMoo> list = world.getEntities(EntityFluidMoo.class, predicate);
			
			if (!list.isEmpty())
			{
				for (Entity e : list)
				{
					if (e.isEntityEqual(moo))
					{
						event.setCanceled(true);
						return;
					}
				}
				
				moo.setUniqueId(UUID.randomUUID());
			}
		}
	}
}
