package com.ricardothecoder.minimoos.entities;

import java.util.Random;

import com.iamshift.interfaces.IMobChanger;
import com.ricardothecoder.minimoos.Config;
import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.minimoos.items.ItemManager;
import com.ricardothecoder.minimoos.loot.LootManager;
import com.ricardothecoder.yac.world.GameruleManager;

import net.minecraft.block.BlockGrass;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "com.iamshift.interfaces.IMobChanger", modid = "mineaddons")
public class EntitySacredMoo extends EntityMiniMoo implements IMobChanger
{
	public EntitySacredMoo(World worldIn)
	{
		super(worldIn);
	}

	// ENTITY CONTROL
	@Override
	protected void initEntityAI()
	{
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(3, new EntityAITempt(this, 1.25D, getBreedingItem(), false));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.25D));
		this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() * 1.5D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
	}
	
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount)
	{
		if (isFool() && damageSrc.getEntity() instanceof EntityPlayer && !(damageSrc.getEntity() instanceof FakePlayer))
		{
			foolPlayer((EntityPlayer)damageSrc.getEntity());
			return;
		}
		
		super.damageEntity(damageSrc, damageAmount);
		if (damageSrc.getEntity() instanceof EntityPlayer && !(damageSrc.getEntity() instanceof FakePlayer))
			((EntityPlayer)damageSrc.getEntity()).addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 60, 0, false, false));
	}
	
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack)
	{
		if (isChild() || stack == null)
			return false;
		
		if (stack.getItem() == ItemManager.forsakenFruit && !this.worldObj.isRemote)
		{
			int percent = new Random().nextInt(100);
			
			if (percent <= Config.cloneRate)
			{
				EntityAgeable entityageable = this.createChild(this);
				
	            if (entityageable != null)
	            {
	                entityageable.setGrowingAge(-24000);
	                entityageable.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
	                this.worldObj.spawnEntityInWorld(entityageable);
	            }
			}
			
			player.swingArm(hand);

			if (!player.capabilities.isCreativeMode)
			{
				if (stack.stackSize > 1)
					player.setHeldItem(hand, new ItemStack(stack.getItem(), --stack.stackSize));
				else
					player.setHeldItem(hand, null);
			}
			
			return true;
		}
		
		return super.processInteract(player, hand, stack);
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
		EntitySacredMoo moo = new EntitySacredMoo(this.worldObj);
		moo.setFool(false);

		return moo;
	}
	
	@Override
	public Item getBreedingItem()
	{
		return ItemManager.forsakenFruit;
	}

	// ALTERS FALL DAMAGE
	@Override
	public int getMaxFallHeight()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public void fall(float distance, float damageMultiplier)
	{
		super.fall(distance, 0);
	}

	// MISC STUFF
	@Override
	protected ResourceLocation getLootTable()
	{
		return LootManager.SACRED_COW;
	}

	@Override
	public ResourceLocation getTexture()
	{
		if (!isChild())
			return new ResourceLocation(References.MODID, "textures/entity/sacredcow.png");
		else
			return new ResourceLocation(References.MODID, "textures/entity/sacredcow_baby.png");
	}
	
	@Override
	public boolean getCanSpawnHere()
	{  
		if (GameruleManager.getGameRule(worldObj, "spawnMiniMoos").equals("false"))
			return false;
		
		int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.getEntityBoundingBox().minY);
        int k = MathHelper.floor_double(this.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
		
        return this.worldObj.provider.getDimensionType() == DimensionType.OVERWORLD && this.worldObj.getBlockState(blockpos.down()).getBlock() instanceof BlockGrass && this.worldObj.getLight(blockpos) > 8 && this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox()) && this.worldObj.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.containsAnyLiquid(this.getEntityBoundingBox());
	}
	
	// MINE ADDONS
	@Override
	@Optional.Method(modid = "mineaddons")
	public void cursedWaterEffect()
	{
		if (!Config.allowEntityConversion)
			return;
		
		EntityDemonMoo moo = new EntityDemonMoo(worldObj);
		moo.setPositionAndUpdate(posX, posY, posZ);
		worldObj.spawnEntityInWorld(moo);
		
		setDead();
	}

	@Override
	@Optional.Method(modid = "mineaddons")
	public void sacredWaterEffect()
	{
	}
}
