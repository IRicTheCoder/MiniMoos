package com.ricardothecoder.minimoos.entities.halloween;

import java.time.LocalDate;
import java.util.List;

import com.ricardothecoder.minimoos.Config;
import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.minimoos.entities.EntityMiniMoo;
import com.ricardothecoder.minimoos.loot.LootManager;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.util.FakePlayer;

public class EntityStatueMoo extends EntityMiniMoo
{
	private static final DataParameter<Integer> TYPE = EntityDataManager.<Integer>createKey(EntityCostumeMoo.class, DataSerializers.VARINT);
	
	public EntityStatueMoo(World worldIn)
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
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(TYPE, Integer.valueOf(-1));
	}
	
	public int getType()
	{
		return this.dataManager.get(TYPE).intValue();
	}
	
	public void setType(int type)
	{
		this.dataManager.set(TYPE, Integer.valueOf(type));
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (ticksExisted % 300 == 0)
		{
			if (this.rand.nextInt(100) <= 25)
			{
				setType(getType() == 1 ? 0 : 1);
				
				List<EntityPlayer> players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().expand(10, 10, 10));
				for (EntityPlayer player : players)
				{
					if (player instanceof FakePlayer)
						continue;
					
					ITextComponent name = new TextComponentString(TextFormatting.DARK_PURPLE + getName() + TextFormatting.WHITE + ": ");
					player.addChatMessage(name.appendSibling(new TextComponentTranslation("talks.statuemoo." + (getType() == 1 ? "bysco" : "sven"))));
				}
			}
		}
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
	
	// SPAWN
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
	{
		if (getType() <= -1)
			setType(this.rand.nextInt(2));
		return super.onInitialSpawn(difficulty, livingdata);
	}
	
	// NBTs & Spawn Data
	@Override
	public void writeEntityToNBT(NBTTagCompound comp)
	{
		super.writeEntityToNBT(comp);
		comp.setInteger("type", getType());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		setType(compound.getInteger("type"));
	}

	// MISC STUFF
	@Override
	public boolean isFool()
	{
		return false;
	}
	
	@Override
	public String getName()
	{
		if (hasCustomName())
			return super.getName();
		else
			return I18n.translateToLocal("entity.minimoos.halloween.statuemoo." + (getType() == 1 ? "bysco" : "sven") + ".name");
	}
	
	@Override
	protected ResourceLocation getLootTable()
	{
		switch(getType())
		{
			case 1:
				return LootManager.STATUE_COW_BYSCO;
			case 0:
			default:
				return LootManager.STATUE_COW_SVEN;
		}
	}

	@Override
	public ResourceLocation getTexture()
	{
		switch(getType())
		{
			case 1:
				return new ResourceLocation(References.MODID, "textures/entity/halloween/byscocow.png");
			case 0:
			default:
				return new ResourceLocation(References.MODID, "textures/entity/halloween/sveniekecow.png");
		}
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
