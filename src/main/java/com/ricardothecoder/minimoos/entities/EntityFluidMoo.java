package com.ricardothecoder.minimoos.entities;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.ricardothecoder.minimoos.Config;
import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.minimoos.addons.tconstruct.BreedRecipe;
import com.ricardothecoder.minimoos.feed.FeedRecipe;
import com.ricardothecoder.minimoos.fluids.FluidColorManager;
import com.ricardothecoder.minimoos.items.FluidMooCatalogue;
import com.ricardothecoder.minimoos.items.ItemManager;
import com.ricardothecoder.yac.util.ModLogger;
import com.ricardothecoder.yac.world.GameruleManager;

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
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityFluidMoo extends EntityMiniMoo
{
	private static final DataParameter<String> FLUID_NAME = EntityDataManager.<String>createKey(EntityFluidMoo.class, DataSerializers.STRING);
	private static final DataParameter<Integer> BUCKET_AMOUNT = EntityDataManager.<Integer>createKey(EntityFluidMoo.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> DELAY = EntityDataManager.<Integer>createKey(EntityFluidMoo.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> QUANTITY = EntityDataManager.<Integer>createKey(EntityFluidMoo.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> EFFICIENCY = EntityDataManager.<Integer>createKey(EntityFluidMoo.class, DataSerializers.VARINT);
	
	public final static String FLUID_NAME_ID = "FluidName";
	public final static String BUCKET_AMOUNT_ID = "BucketAmount";
	public final static String DELAY_ID = "Delay";
	public final static String QUANTITY_ID = "Quantity";
	public final static String EFFICIENCY_ID = "Efficiency";

	private Fluid resultFluid;

	private boolean catchFire;
	private boolean hurtByFall;

	public EntityFluidMoo(World worldIn) 
	{
		super(worldIn);
	}

	private void configureMoo()
	{
		if (getFluid() == null)
			setFluid(this.rand.nextInt(2) > 0 ? FluidRegistry.LAVA : FluidRegistry.WATER);

		if (Config.spawnReady)
			setDelay(0);
		else
			setDelay(Config.maxUseDelay);

		catchFire = !(getFluid().getTemperature() >= FluidRegistry.LAVA.getTemperature());
		isImmuneToFire = catchFire;
		hurtByFall = !(getFluid().isGaseous());
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(FLUID_NAME, String.valueOf("None"));
		this.dataManager.register(BUCKET_AMOUNT, Integer.valueOf(1));
		this.dataManager.register(DELAY, Integer.valueOf(0));
		this.dataManager.register(QUANTITY, Integer.valueOf(1));
		this.dataManager.register(EFFICIENCY, Integer.valueOf(1));
	}

	// VALUES MANIPULATION
	public void setFluid(Fluid cowFluid)
	{
		if (cowFluid == null)
		{
			setupFluid();
			return;
		}

		this.dataManager.set(FLUID_NAME, cowFluid.getName());

		catchFire = !(getFluid().getTemperature() >= FluidRegistry.LAVA.getTemperature());
		isImmuneToFire = catchFire;
		hurtByFall = !(getFluid().isGaseous());
	}

	public Fluid getFluid()
	{
		if (this.dataManager.get(FLUID_NAME).toString().equals("None"))
			return null;
		
		return FluidRegistry.getFluid(this.dataManager.get(FLUID_NAME).toString());
	}

	public void setDelay(int newDelay)
	{
		this.dataManager.set(DELAY, Integer.valueOf(newDelay));

		if (newDelay <= 0)
			setBuckets(getQuantity());
	}

	public int getDelay()
	{
		return this.dataManager.get(DELAY).intValue();
	}
	
	public void setBuckets(int buckets)
	{
		this.dataManager.set(BUCKET_AMOUNT, Integer.valueOf(buckets));
	}
	
	public int getBuckets()
	{		
		return this.dataManager.get(BUCKET_AMOUNT).intValue();
	}

	public void decreaseBuckets()
	{
		setBuckets(getBuckets() - 1);
	}
	
	public void setQuantity(int quantity)
	{
		this.dataManager.set(QUANTITY, Integer.valueOf(quantity));
	}
	
	public int getQuantity()
	{
		return this.dataManager.get(QUANTITY).intValue();
	}
	
	public void setEfficiency(int efficiency)
	{
		this.dataManager.set(EFFICIENCY, Integer.valueOf(efficiency));
	}
	
	public int getEfficiency()
	{
		return this.dataManager.get(EFFICIENCY).intValue();
	}

	// ENTITY CONTROL
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (getDelay() > 0) {
			setDelay(getDelay() - 1);
		}
	}


	// SPAWN STUFF
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
	{		
		IEntityLivingData data = super.onInitialSpawn(difficulty, livingdata);

		if (!worldObj.isRemote)
			setupFluid();

		return data;
	}

	private void setupFluid()
	{
		if (getFluid() == null)
		{
			int fluidID = this.rand.nextInt(Config.getSpawnableFluids(this.worldObj.provider.getDimensionType()).length);
			Fluid fluid = Config.getSpawnableFluids(this.worldObj.provider.getDimensionType())[fluidID];

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

		if (this.isFool() && validItem(stack))
		{
			foolPlayer(player);
			return true;
		}

		if (FeedRecipe.recipes.containsKey(stack.getItem()))
		{
			FeedRecipe recipe = FeedRecipe.recipes.get(stack.getItem());

			if (stack.getMetadata() == recipe.meta && !getFluid().getName().equals(recipe.resultFluid.getName()))
			{
				if (this.rand.nextInt(recipe.total) <= recipe.chance)
				{
					setFluid(recipe.resultFluid);
				}

				if (!player.capabilities.isCreativeMode)
				{
					setDelay(MathHelper.ceiling_double_int(Config.maxUseDelay / getEfficiency()));

					if (stack.stackSize > 1)
					{
						ItemStack newStack = stack.copy();
						newStack.stackSize -= 1;
						player.setHeldItem(hand, newStack);
					}
					else
						player.setHeldItem(hand, null);
				}

				return true;
			}
		}

		if (stack.getItem() == ItemManager.mooCatalogue)
		{
			if (stack.hasTagCompound() && stack.getTagCompound().hasKey("owner"))
			{
				if (!stack.getTagCompound().getString("owner").equals(player.getName()))
					return true;
			}

			player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
			player.swingArm(hand);

			if (FeedRecipe.getItemFromFluid(getFluid()) == null)
			{
				String world = "";
				if (this.worldObj.provider.getDimensionType() == DimensionType.OVERWORLD) world = TextFormatting.GREEN + "Overworld";
				if (this.worldObj.provider.getDimensionType() == DimensionType.NETHER) world = TextFormatting.RED + "Nether";
				if (this.worldObj.provider.getDimensionType() == DimensionType.THE_END) world = TextFormatting.LIGHT_PURPLE + "End";

				List<Fluid> endFluids = Arrays.asList(Config.getSpawnableFluids(DimensionType.THE_END));
				List<Fluid> netherFluids = Arrays.asList(Config.getSpawnableFluids(DimensionType.NETHER));
				List<Fluid> overworldFluids = Arrays.asList(Config.getSpawnableFluids(DimensionType.OVERWORLD));

				String natural = "";
				if (endFluids.contains(getFluid())) natural = TextFormatting.LIGHT_PURPLE + "End";
				if (netherFluids.contains(getFluid())) natural = TextFormatting.RED + "Nether";
				if (overworldFluids.contains(getFluid())) natural = TextFormatting.GREEN + "Overworld";

				((FluidMooCatalogue)stack.getItem()).addEntry(getFluid().getLocalizedName(new FluidStack(getFluid(), 0)) + ";none;You found it on the " + world + TextFormatting.WHITE + ". Natural from the " + natural, stack);
			}
			else
			{
				ItemStack item = FeedRecipe.getItemStackFromFluid(getFluid());
				((FluidMooCatalogue)stack.getItem()).addEntry(getFluid().getLocalizedName(new FluidStack(getFluid(), 0)) + ";none;Made by feeding a Fluid Moo with " + TextFormatting.GOLD + item.getDisplayName(), stack);
			}
			return false;
		}

		if (stack.getItem() == ItemManager.demonSoul)  
		{
			if (getEfficiency() >= Config.maxEfficiency && Config.maxEfficiency > 0)
				return false;

			player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);

			setEfficiency(getEfficiency() + 1);
			player.swingArm(hand);

			if (!player.capabilities.isCreativeMode)
			{
				setDelay(MathHelper.ceiling_double_int(Config.maxUseDelay / getEfficiency()));

				if (stack.stackSize > 1)
					player.setHeldItem(hand, new ItemStack(stack.getItem(), --stack.stackSize));
				else
					player.setHeldItem(hand, null);
			}

			return true;
		}

		if (stack.getItem() == ItemManager.goldenEssence)  
		{
			if (getQuantity() >= Config.maxQuantity && Config.maxQuantity > 0)
				return false;

			player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);

			setQuantity(getQuantity() + 1);
			player.swingArm(hand);

			if (getDelay() <= 0)
				setBuckets(getQuantity());

			if (!player.capabilities.isCreativeMode)
			{
				setDelay(MathHelper.ceiling_double_int(Config.maxUseDelay / getEfficiency()));

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
				setDelay(MathHelper.ceiling_double_int(Config.maxUseDelay / getEfficiency()));

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
					if (getBuckets() <= 1)
					{
						setDelay(MathHelper.ceiling_double_int(Config.maxUseDelay / getEfficiency()));
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
			ItemStack result = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, getFluid());

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
	
	private boolean validItem(ItemStack stack)
	{
		return FeedRecipe.recipes.containsKey(stack.getItem()) || stack.getItem() == ItemManager.mooCatalogue || stack.getItem() == ItemManager.demonSoul || 
				stack.getItem() == ItemManager.goldenEssence || stack.getItem() == Items.GOLDEN_APPLE || isBreedingItem(stack) || stack.getItem() == Items.BUCKET || 
				stack.getItem() == ForgeModContainer.getInstance().universalBucket;
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
					EntityFluidMoo another = ((EntityFluidMoo) entityAnimal);
					
					BreedRecipe recipe = BreedRecipe.canMateMoos(this, another);
					if (recipe != null) 
					{
						resultFluid = recipe.resultFluid;
						return true;
					}
					else
					{
						if (getFluid().getName().equals(another.getFluid().getName()))
						{
							if (this.rand.nextInt(100) >= 50)
							{
								resultFluid = getFluid();
								return true;
							}
							else
							{
								resetInLove();
								entityAnimal.resetInLove();
								return false;
							}
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
	public void writeEntityToNBT(NBTTagCompound comp)
	{
		super.writeEntityToNBT(comp);
		comp.setString(FLUID_NAME_ID, getFluid().getName());
		comp.setInteger(BUCKET_AMOUNT_ID, getBuckets());
		comp.setInteger(DELAY_ID, getDelay());
		comp.setInteger(QUANTITY_ID, getQuantity());
		comp.setInteger(EFFICIENCY_ID, getEfficiency());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);

		String fluidName = compound.getString(FLUID_NAME_ID);
		
		if (!fluidName.equals("None"))
			setFluid(FluidRegistry.getFluid(fluidName));
		
		setQuantity(compound.getInteger(QUANTITY_ID));
		setEfficiency(compound.getInteger(EFFICIENCY_ID));
		setDelay(compound.getInteger(DELAY_ID));
		setBuckets(compound.getInteger(BUCKET_AMOUNT_ID));
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
		return FluidColorManager.getFluidColor(getFluid());
	}

	@Override
	public String getName()
	{
		if (hasCustomName())
			return getCustomNameTag();
		
		return getFluid().getLocalizedName(new FluidStack(getFluid(), 0)).replace("Molten ", "").replace("Liquid ", "") + " " + I18n.translateToLocal("entity.minimoos.fluidmoo.name.extend");
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
			if (Config.getSpawnableFluids(this.worldObj.provider.getDimensionType()).length > 0)
				return false;

			if ((this.worldObj.provider.getDimensionType() == DimensionType.NETHER) || (this.worldObj.provider.getDimensionType() == DimensionType.THE_END))
				return true;
			else
				return state.getBlock() == Blocks.GRASS && this.worldObj.getLight(blockpos) > 8;
		}

		return false;
	}
}
