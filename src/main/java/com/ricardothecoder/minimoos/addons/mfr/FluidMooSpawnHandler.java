package com.ricardothecoder.minimoos.addons.mfr;

import com.ricardothecoder.minimoos.Config;
import com.ricardothecoder.minimoos.entities.EntityFluidMoo;

import net.minecraft.entity.EntityLivingBase;
import powercrystals.minefactoryreloaded.api.IMobSpawnHandler;

public class FluidMooSpawnHandler implements IMobSpawnHandler
{

	@Override
	public Class<? extends EntityLivingBase> getMobClass()
	{
		return EntityFluidMoo.class;
	}

	@Override
	public void onMobExactSpawn(EntityLivingBase entity)
	{
		if (entity instanceof EntityFluidMoo)
		{
			if (!Config.areExactCopiesAllowed)
			{
				entity.setDead();
				return;
			}
		}
	}

	@Override
	public void onMobSpawn(EntityLivingBase entity) { }
}
