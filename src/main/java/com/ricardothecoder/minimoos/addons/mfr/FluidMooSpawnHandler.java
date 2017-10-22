package com.ricardothecoder.minimoos.addons.mfr;

import com.ricardothecoder.minimoos.Config;
import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.minimoos.entities.EntityFluidMoo;
import com.ricardothecoder.yac.util.ModLogger;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import powercrystals.minefactoryreloaded.api.IMobSpawnHandler;
import scala.util.Random;

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
			
			int percent = new Random().nextInt(100);
			
			if (percent <= Config.exactCopyPercentage)
			{
				EntityFluidMoo moo = (EntityFluidMoo)entity;
				
				if (!Config.shouldStatsBeCopied)
				{
					moo.setEfficiency(1);
					moo.setQuantity(1);
				}
			}
			else
			{
				entity.setDead();
			}
		}
	}

	@Override
	public void onMobSpawn(EntityLivingBase entity) { }
}
