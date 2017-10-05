package com.ricardothecoder.minimoos.entities;

import java.awt.Color;

import javax.annotation.Nullable;

import com.ricardothecoder.minimoos.Config;
import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.minimoos.addons.tconstruct.BreedRecipe;
import com.ricardothecoder.minimoos.entities.stats.FluidMooStats;
import com.ricardothecoder.minimoos.fluids.FluidColorManager;
import com.ricardothecoder.minimoos.items.ItemManager;
import com.ricardothecoder.yac.world.GameruleManager;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.util.Random;

public class EntityFluidMoo extends EntityMiniMoo
{
	public FluidMooStats stats;

	private Fluid fluid;
	private Fluid resultFluid;

	private boolean catchFire;
	private boolean hurtByFall;

	public EntityFluidMoo(World worldIn) 
	{
		super(worldIn);
		if (stats == null)
			stats = new FluidMooStats();
	}
	private void configureMoo()
	{
		if (fluid == null)
			setFluid(new Random().nextInt(2) > 0 ? FluidRegistry.LAVA : FluidRegistry.WATER);

		if (Config.spawnReady)
			setDelay(0);
		else
			setDelay(Config.maxUseDelay);

		catchFire = !(fluid.getTemperature() >= FluidRegistry.LAVA.getTemperature());
		isImmuneToFire = catchFire;
		hurtByFall = !(fluid.isGaseous());
	}

	// VALUES MANIPULATION
	public void setFluid(Fluid cowFluid)
	{
		if (cowFluid == null)
		{
			setupFluid();
			return;
		}
		
		fluid = cowFluid;
		stats.setFluidName(cowFluid.getName());

		catchFire = !(fluid.getTemperature() >= FluidRegistry.LAVA.getTemperature());
		isImmuneToFire = catchFire;
		hurtByFall = !(fluid.isGaseous());
	}

	public Fluid getFluid()
	{
		return fluid;
	}

	public void setDelay(int newDelay)
	{
		stats.setDelay(newDelay);

		if (newDelay <= 0)
			stats.setBuckets(stats.getQuantity());
	}

	public int getDelay()
	{
		return stats.getDelay();
	}

	public void decreaseBuckets()
	{
		stats.setBuckets(stats.getBuckets() - 1);
	}

	// ENTITY CONTROL
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (stats.getDelay() > 0) {
			setDelay(stats.getDelay() - 1);
		}
	}
	
	
	// SPAWN STUFF
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
	{
		IEntityLivingData data = super.onInitialSpawn(difficulty, livingdata);
		
		setupFluid();
		
		return data;
	}
	
	private void setupFluid()
	{
		if (getFluid() == null)
		{
			stats = new FluidMooStats();
			
			int fluidID = new Random().nextInt(Config.getSpawnableFluids(Type.MAGICAL).length);
			Fluid fluid = Config.getSpawnableFluids(Type.MAGICAL)[fluidID];
			
			if (this.worldObj.provider instanceof WorldProviderEnd)
			{
				fluidID = new Random().nextInt(Config.getSpawnableFluids(Type.END).length);
				fluid = Config.getSpawnableFluids(Type.END)[fluidID];
			}
			
			if (this.worldObj.provider instanceof WorldProviderHell)
			{
				fluidID = new Random().nextInt(Config.getSpawnableFluids(Type.NETHER).length);
				fluid = Config.getSpawnableFluids(Type.NETHER)[fluidID];
			}
			
			setFluid(fluid);
			configureMoo();
		}
	}

	// INTERACTIONS
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack)
	{
		if (this.isChild())
		{
			if (isBreedingItem(stack) && Config.allowBreeding)
			{
				player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
				
				this.ageUp((int)((float)(-this.getGrowingAge() / 20) * 0.1F), true);
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
			
			return false;
		}
		
		if (stack == null)
			return false;

		if (this.isFool() && stack.getItem() != Items.SPAWN_EGG)
		{
			foolPlayer(player);
			return true;
		}

		if (stack.getItem() == ItemManager.demonSoul)  
		{
			if (stats.getEfficiency() >= Config.maxEfficiency && Config.maxEfficiency > 0)
				return false;

			player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);

			stats.setEfficiency(stats.getEfficiency() + 1);
			player.swingArm(hand);

			if (!player.capabilities.isCreativeMode)
			{
				setDelay(MathHelper.ceiling_double_int(Config.maxUseDelay / stats.getEfficiency()));

				if (stack.stackSize > 1)
					player.setHeldItem(hand, new ItemStack(stack.getItem(), --stack.stackSize));
				else
					player.setHeldItem(hand, null);
			}

			return true;
		}

		if (stack.getItem() == ItemManager.goldenEssence)  
		{
			if (stats.getQuantity() >= Config.maxQuantity && Config.maxQuantity > 0)
				return false;

			player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);

			stats.setQuantity(stats.getQuantity() + 1);
			player.swingArm(hand);
			
			if (getDelay() <= 0)
				stats.setBuckets(stats.getQuantity());

			if (!player.capabilities.isCreativeMode)
			{
				setDelay(MathHelper.ceiling_double_int(Config.maxUseDelay / stats.getEfficiency()));

				if (stack.stackSize > 1)
					player.setHeldItem(hand, new ItemStack(stack.getItem(), --stack.stackSize));
				else
					player.setHeldItem(hand, null);
			}

			return true;
		}

		if (stack.getItem() == Items.GOLDEN_APPLE)
		{
			player.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1.0F, 1.0F);

			heal(getMaxHealth());
			player.swingArm(hand);

			if (!player.capabilities.isCreativeMode)
			{
				setDelay(MathHelper.ceiling_double_int(Config.maxUseDelay / stats.getEfficiency()));

				if (stack.stackSize > 1)
					player.setHeldItem(hand, new ItemStack(stack.getItem(), --stack.stackSize));
				else
					player.setHeldItem(hand, null);
			}

			return true;
		}
		
		if (isBreedingItem(stack) && Config.allowBreeding)
		{
			if (isInLove() || getGrowingAge() < 0)
				return false;

			player.playSound(SoundEvents.ENTITY_COW_AMBIENT, 1.0F, 1.0F);

			setInLove(player);
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

		if (getDelay() <= 0)
		{
			if (stack.getItem() == Items.BUCKET || stack.getItem() == ForgeModContainer.getInstance().universalBucket)
			{
				if (!player.capabilities.isCreativeMode)
				{
					if (stats.getBuckets() <= 1)
					{
						setDelay(MathHelper.ceiling_double_int(Config.maxUseDelay / stats.getEfficiency()));
						decreaseBuckets();
					}
					else
						decreaseBuckets();
				}

				player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);

				milkMoo(player, hand, stack);
				player.swingArm(hand);

				return true;
			}
		}
		
		return false;
	}

	private void milkMoo(EntityPlayer player, EnumHand hand, ItemStack stack)
	{
		if (FluidRegistry.isUniversalBucketEnabled())
		{
			ItemStack result = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, fluid);

			if (result.getDisplayName().equals("Universal Bucket") || result.getDisplayName().equals("Bucket"))
				return;
			
			if (stack.stackSize > 1)
			{
				player.setHeldItem(hand, new ItemStack(stack.getItem(), --stack.stackSize));

				if (!player.inventory.addItemStackToInventory(result.copy()))
					player.dropItem(result.copy(), false);
			}
			else
				player.setHeldItem(hand, result.copy());
		}
	}

	// MATING
	@Override
	public boolean isBreedingItem(ItemStack stack)
	{
		return stack.getItem() == ItemManager.miniWheat;  
	}

	@Override
	public Item getBreedingItem()
	{
		return ItemManager.miniWheat;  
	}

	@Override
	public boolean canMateWith(EntityAnimal entityAnimal) 
	{
		if (entityAnimal != this)
		{
			if (isInLove() && entityAnimal.isInLove()) 
			{
				if (entityAnimal instanceof EntityFluidMoo) 
				{
					BreedRecipe recipe = BreedRecipe.canMateMoos(this, ((EntityFluidMoo) entityAnimal));
					if (recipe != null) 
					{
						resultFluid = recipe.resultFluid;
						return true;
					}
					else
					{
						resetInLove();
						entityAnimal.resetInLove();
						return false;
					}
				}
			}
		}

		return false;
	}

	@Override
	public EntityCow createChild(EntityAgeable ageable) 
	{
		if (resultFluid != null)
		{
			EntityFluidMoo moo = new EntityFluidMoo(this.worldObj);
			moo.setFluid(resultFluid);
			moo.setFool(false);
			resultFluid = null;

			return moo;
		}

		return null;
	}
	
	@Override
	protected void onGrowingAdult()
	{
		setFool(false);
	}

	// ALTERS FIRE
	@Override
	protected void dealFireDamage(int amount)
	{
		if (catchFire)
			super.dealFireDamage(amount);
	}

	@Override
	public void setFire(int seconds)
	{
		if (catchFire)
			super.setFire(seconds);
	}

	@Override
	protected void setOnFireFromLava()
	{
		if (catchFire)
			super.setOnFireFromLava();
	}

	// ALTERS FALL DAMAGE
	@Override
	public int getMaxFallHeight()
	{
		return !hurtByFall ? Integer.MAX_VALUE : super.getMaxFallHeight();
	}

	@Override
	public void fall(float distance, float damageMultiplier)
	{
		super.fall(distance, !hurtByFall ? 0 : damageMultiplier);
	}

	// NBTs & Spawn Data
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		NBTTagCompound comp = super.writeToNBT(compound);
		stats.saveStats(comp);
		return comp;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		stats = new FluidMooStats();
		
		stats.readStats(compound);

		if (!stats.getFluidName().equals("None"))
			setFluid(FluidRegistry.getFluid(stats.getFluidName()));
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) 
	{
		if (getFluid() == null)
			setupFluid();
		
		ByteBufUtils.writeUTF8String(buffer, getFluid().getName());
		ByteBufUtils.writeVarInt(buffer, stats.getDelay(), 4);
		ByteBufUtils.writeVarInt(buffer, stats.getQuantity(), 4);
		ByteBufUtils.writeVarInt(buffer, stats.getEfficiency(), 4);
		ByteBufUtils.writeVarInt(buffer, stats.getBuckets(), 4);

		super.writeSpawnData(buffer);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) 
	{		
		stats = new FluidMooStats();
		
		setFluid(FluidRegistry.getFluid(ByteBufUtils.readUTF8String(additionalData)));
		setDelay(ByteBufUtils.readVarInt(additionalData, 4));
		stats.setQuantity(ByteBufUtils.readVarInt(additionalData, 4));
		stats.setEfficiency(ByteBufUtils.readVarInt(additionalData, 4));
		stats.setBuckets(ByteBufUtils.readVarInt(additionalData, 4));

		super.readSpawnData(additionalData);
	}

	// MISC STUFF
	@SideOnly(Side.CLIENT)
	@Override
	public ResourceLocation getOverlayTexture()
	{
		return new ResourceLocation(References.MODID, "textures/entity/minicow_overlay.png");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Color getOverlayColor()
	{
		return FluidColorManager.getFluidColor(fluid);
	}

	@Override
	public String getName()
	{
		return fluid.getLocalizedName(new FluidStack(fluid, 0)).replace("Molten ", "").replace("Liquid ", "") + " Mini Moo";
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
        
        IBlockState state = this.worldObj.getBlockState(blockpos.down());

        if (this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox()) && this.worldObj.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.containsAnyLiquid(this.getEntityBoundingBox()))
        {
	        if ((this.worldObj.provider instanceof WorldProviderHell && Config.getSpawnableFluids(Type.NETHER).length > 0) || (this.worldObj.provider instanceof WorldProviderEnd && Config.getSpawnableFluids(Type.END).length > 0))
	        	return true;
	        else
	        	return state.getBlock() == Blocks.GRASS && this.worldObj.getLight(blockpos) > 8;
        }
        
        return false;
	}
}
