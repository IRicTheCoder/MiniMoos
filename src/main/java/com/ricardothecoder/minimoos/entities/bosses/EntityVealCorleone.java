package com.ricardothecoder.minimoos.entities.bosses;

import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.minimoos.entities.EntityMiniMoo;
import com.ricardothecoder.minimoos.loot.LootManager;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityVealCorleone extends EntityMiniMoo
{
	public EntityVealCorleone(World worldIn)
	{
		super(worldIn);
	}
	
	@Override
	protected void initEntityAI()
	{
		this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAITempt(this, 1.25D, getBreedingItem(), false));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40D);
	}
	
	// Entity Control
	@Override
	public Item getBreedingItem()
	{
		return Items.DIAMOND;
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
	
	// Damage Stuff
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount)
	{
		super.damageEntity(damageSrc, damageAmount);
		if (damageSrc.getEntity() instanceof EntityPlayer && !(damageSrc.getEntity() instanceof FakePlayer))
		{
			affectPlayer((EntityPlayer) damageSrc.getEntity());
		}
	}
	
	private void affectPlayer(EntityPlayer player)
	{
		ITextComponent playerName = new TextComponentString(player.getName());
		playerName.getStyle().setColor(TextFormatting.GREEN);
		
		ITextComponent bossName = new TextComponentString(TextFormatting.RED + getName() + TextFormatting.WHITE + ": ");
		
		switch(this.rand.nextInt(3))
		{
			case 0:
				player.addChatMessage(bossName.appendSibling(new TextComponentTranslation("talks.bossmoo.corleone.what", playerName)));
				player.addPotionEffect(new PotionEffect(MobEffects.INSTANT_DAMAGE, 2, 1, true, true));
				break;
			case 1:
				player.addChatMessage(bossName.appendSibling(new TextComponentTranslation("talks.bossmoo.corleone.nice", playerName)));
				worldObj.createExplosion(null, player.posX, player.posY, player.posZ, 1, false);
				break;
			case 2:
				player.addChatMessage(bossName.appendSibling(new TextComponentTranslation("talks.bossmoo.corleone.thisday", playerName)));
				player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 60, 2, true, true));
				break;
		}
	}
	
	// Fool Stuff
	@Override
	public boolean isFool()
	{
		return false;
	}
	
	// Misc Stuff
	@Override
	protected ResourceLocation getLootTable()
	{
		return LootManager.VEAL_CORLEONE;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ResourceLocation getTexture()
	{
		return new ResourceLocation(References.MODID, "textures/entity/bosses/vealCorleone.png");
	}
}
