package com.ricardothecoder.minimoos.entities;

import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.minimoos.loot.LootManager;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityFoolMoo extends EntityMiniMoo
{
	public EntityFoolMoo(World worldIn)
	{
		super(worldIn);
	}
	
	@Override
	protected void initEntityAI()
	{
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
	}

	// BREEDING
	@Override
	public boolean canMateWith(EntityAnimal otherAnimal)
	{
		return false;
	}

	@Override
	public EntityCow createChild(EntityAgeable ageable)
	{
		return null;
	}

	// MISC STUFF
	@Override
	protected ResourceLocation getLootTable()
	{
		return LootManager.FOOL_COW;
	}

	@Override
	public ResourceLocation getTexture()
	{
		return new ResourceLocation(References.MODID, "textures/entity/foolcow.png");
	}
}
