package com.ricardothecoder.minimoos.entities.halloween;

import java.time.LocalDate;
import java.util.List;

import com.ricardothecoder.minimoos.Config;
import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.minimoos.entities.EntityMiniMoo;
import com.ricardothecoder.minimoos.loot.LootManager;

import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class EntitySpookyMoo extends EntityMiniMoo
{
	public EntitySpookyMoo(World worldIn)
	{
		super(worldIn);
	}

	// ENTITY CONTROL
	@Override
	protected void initEntityAI()
	{
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (ticksExisted % 300 == 0)
		{
			if (this.rand.nextInt(100) <= 25)
			{
				List<EntityPlayer> players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().expand(10, 10, 10));
				for (EntityPlayer player : players)
				{
					if (player instanceof FakePlayer)
						continue;
					
					ITextComponent name = new TextComponentString(TextFormatting.DARK_PURPLE + getName() + TextFormatting.WHITE + ": ");
					player.addChatMessage(name.appendSibling(new TextComponentTranslation("talks.spookymoo.moooh")));
				}
			}
		}
	}
	
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount)
	{
		super.damageEntity(damageSrc, damageAmount);
		if (damageSrc.getEntity() instanceof EntityPlayer && !(damageSrc.getEntity() instanceof FakePlayer))
			((EntityPlayer)damageSrc.getEntity()).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 10, 0, false, false));
	}
	
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack)
	{
		if (stack == null)
			return false;
		
		if (stack.getItem() == Items.SPAWN_EGG)
			return false;
		
		return super.processInteract(player, hand, stack);
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
	public boolean isFool()
	{
		return false;
	}
	
	@Override
	protected ResourceLocation getLootTable()
	{
		return LootManager.SPOOKY_COW;
	}

	@Override
	public ResourceLocation getTexture()
	{
		return new ResourceLocation(References.MODID, "textures/entity/halloween/spookycow.png");
	}
	
	@Override
	public boolean getCanSpawnHere()
	{
		LocalDate date = LocalDate.now();
		boolean isHalloween = (date.getMonthValue() == 10);
		
		if (isHalloween)	
			return super.getCanSpawnHere();
		else
		{
			if (Config.hallowenMoosSpawnAllYear)
			{
				return super.getCanSpawnHere() && this.rand.nextInt(100) <= 5;
			}
			
			return false;
		}
	}
}
