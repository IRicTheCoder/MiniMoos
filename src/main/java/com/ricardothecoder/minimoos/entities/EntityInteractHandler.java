package com.ricardothecoder.minimoos.entities;

import com.ricardothecoder.minimoos.entities.bosses.EntityBigScalda;
import com.ricardothecoder.minimoos.entities.bosses.EntityEmilkioBarzini;
import com.ricardothecoder.minimoos.entities.bosses.EntityFrankCattlello;
import com.ricardothecoder.minimoos.entities.bosses.EntityTonyMootana;
import com.ricardothecoder.minimoos.entities.bosses.EntityVealCorleone;
import com.ricardothecoder.minimoos.entities.bosses.EntityYakCapone;
import com.ricardothecoder.minimoos.entities.halloween.EntityCostumeMoo;
import com.ricardothecoder.minimoos.entities.halloween.EntitySpookyMoo;
import com.ricardothecoder.minimoos.entities.halloween.EntityStatueMoo;

import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityInteractHandler
{
	@SubscribeEvent
	public void onEntityInteract(EntityInteract event)
	{
		if (event.getTarget() instanceof EntityBigScalda)
			event.setCanceled(true);
		
		if (event.getTarget() instanceof EntityEmilkioBarzini)
			event.setCanceled(true);
		
		if (event.getTarget() instanceof EntityFrankCattlello)
			event.setCanceled(true);
		
		if (event.getTarget() instanceof EntityTonyMootana)
			event.setCanceled(true);
		
		if (event.getTarget() instanceof EntityVealCorleone)
			event.setCanceled(true);
		
		if (event.getTarget() instanceof EntityYakCapone)
			event.setCanceled(true);
		
		if (event.getTarget() instanceof EntityCostumeMoo)
			event.setCanceled(true);
		
		if (event.getTarget() instanceof EntitySpookyMoo)
			event.setCanceled(true);
		
		if (event.getTarget() instanceof EntityStatueMoo)
			event.setCanceled(true);
	}
}
