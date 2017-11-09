package com.ricardothecoder.minimoos.entities;

import java.awt.Color;
import java.util.Random;

import com.ricardothecoder.minimoos.Config;
import com.ricardothecoder.minimoos.References;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityMiniMoo extends EntityCow
{
	private static final DataParameter<Boolean> FOOL_STATE = EntityDataManager.<Boolean>createKey(EntityMiniMoo.class, DataSerializers.BOOLEAN);
	
	private boolean foolTriggered;
	
	public EntityMiniMoo(World worldIn) 
	{
		super(worldIn);
		this.setScale(0.5F);
	}
	
	// ENTITY CONTROL
	@Override
	protected void initEntityAI()
	{
		this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 2.0D));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
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
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() * 2);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(FOOL_STATE, Boolean.valueOf(false));
	}
	
	@Override
	public void setScaleForAge(boolean child)
	{
		this.setScale(child ? 0.25F : 0.5F);
	}
	
	public Item getBreedingItem()
	{
		return Items.WHEAT;
	}
	
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack)
	{
		if (stack != null && stack.getItem() == Items.SPAWN_EGG)
		{
		    if (!this.worldObj.isRemote)
		    {
		        Class <? extends Entity > oclass = EntityList.NAME_TO_CLASS.get(ItemMonsterPlacer.getEntityIdFromItem(stack));
		
		        if (oclass != null && this.getClass() == oclass)
		        {
		            EntityAgeable entityageable = this.createChild(this);
		
		            if (entityageable != null)
		            {
		                entityageable.setGrowingAge(-24000);
		                entityageable.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
		                this.worldObj.spawnEntityInWorld(entityageable);
		
		                if (stack.hasDisplayName())
		                {
		                    entityageable.setCustomNameTag(stack.getDisplayName());
		                }
		
		                if (!player.capabilities.isCreativeMode)
		                {
		                	if (stack.stackSize > 1)
								player.setHeldItem(hand, new ItemStack(stack.getItem(), --stack.stackSize));
							else
								player.setHeldItem(hand, null);
		                }
		            }
		        }
		    }
		
		    return true;
		}
		else
		{
			return false;
		}
	}
	
	// FOOL STUFF
	public boolean isFool()
	{
		return Config.foolMoo ? this.dataManager.get(FOOL_STATE).booleanValue() : false;
	}
	
	public void setFool(boolean fool)
	{
		this.dataManager.set(FOOL_STATE, Boolean.valueOf(fool));		
	}
	
	public void foolPlayer(EntityPlayer player)
	{
		if (foolTriggered || worldObj.isRemote)
			return;
		
		setDead();
		
		EntityFoolMoo fool = new EntityFoolMoo(worldObj);
		fool.setPositionAndUpdate(posX, posY, posZ);
		worldObj.spawnEntityInWorld(fool);
			
		if (player != null)
		{
			ITextComponent playerName = new TextComponentString(player.getName());
			playerName.getStyle().setColor(TextFormatting.GREEN);
			player.addChatMessage(new TextComponentTranslation("talks.foolmoo.foolplayer", playerName));			
		}
		
		setFool(false);
		foolTriggered = true;
	}
	
	// NBTs & Spawn Data
	@Override
	public void writeEntityToNBT(NBTTagCompound comp)
	{
		super.writeEntityToNBT(comp);
		comp.setBoolean("isFool", isFool());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		setFool(compound.getBoolean("isFool"));
	}
	
	// MISC STUFF
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
	{
		super.onInitialSpawn(difficulty, livingdata);
		
		if (new Random().nextInt(100) <= Config.foolRate && !isChild() && !(this instanceof EntityFoolMoo))
			setFool(true);
		
		return livingdata;
	}

	@Override
	protected ResourceLocation getLootTable()
	{
		return null;
	}

	@Override
	public float getEyeHeight()
	{
		return this.isChild() ? this.height : 0.6F;
	}
	
	@SideOnly(Side.CLIENT)
	public ResourceLocation getTexture()
	{
		return new ResourceLocation(References.MODID, "textures/entity/minicow.png");
	}
	
	@SideOnly(Side.CLIENT)
	public ResourceLocation getOverlayTexture()
	{
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public Color getOverlayColor()
	{
		return Color.WHITE;
	}
	
	@Override
	protected float getSoundPitch()
	{
		return this.isChild() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 2.0F : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F;
	}
}
