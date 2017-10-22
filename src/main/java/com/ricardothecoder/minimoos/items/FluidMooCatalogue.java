package com.ricardothecoder.minimoos.items;

import java.util.Arrays;
import java.util.List;

import com.ricardothecoder.minimoos.Config;
import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.minimoos.feed.FeedRecipe;
import com.ricardothecoder.yac.fluids.FluidManager;
import com.ricardothecoder.yac.items.ItemCatalogue;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidMooCatalogue extends ItemCatalogue
{
	public FluidMooCatalogue(boolean creative)
	{
		super(References.MODID, "moocatalogue" + (creative ? ".creative" : ""), creative);
	}
	
	private void setCreativeEntries(ItemStack stack)
	{
		List<Fluid> endFluids = Arrays.asList(Config.getSpawnableFluids(WorldProviderEnd.class));
		List<Fluid> netherFluids = Arrays.asList(Config.getSpawnableFluids(WorldProviderHell.class));
		List<Fluid> overworldFluids = Arrays.asList(Config.getSpawnableFluids(WorldProviderSurface.class));
		
		for(Fluid fluid : FluidManager.getContainableFluids())
		{
			if (Config.excludedFluids.contains(fluid))
				continue;
			
			int weight = Config.fluidWeight.get(fluid);
			
			String spawnPlace = "";
			if (netherFluids.contains(fluid)) spawnPlace = TextFormatting.RED + "Nether";
			if (endFluids.contains(fluid)) spawnPlace = TextFormatting.LIGHT_PURPLE + "End";
			if (overworldFluids.contains(fluid)) spawnPlace = TextFormatting.GREEN + "Overworld";
			
			addEntry(fluid.getLocalizedName(new FluidStack(fluid, 0)) + ";/spawnmoo " + fluid.getName() + ";" + TextFormatting.AQUA + "Spawns in: " + spawnPlace + TextFormatting.AQUA + ". Weight: " + TextFormatting.WHITE + weight, stack);
		}
		
		for(FeedRecipe recipe : FeedRecipe.recipes.values())
		{
			Fluid fluid = recipe.resultFluid;
			ItemStack feedStack = recipe.getStack();
			addEntry(fluid.getLocalizedName(new FluidStack(fluid, 0)) + ";/spawnmoo " + fluid.getName() + ";" + TextFormatting.AQUA + "Created by feeding: "  + TextFormatting.GOLD + feedStack.getDisplayName() + TextFormatting.AQUA + ". Chance: " + TextFormatting.WHITE + recipe.chance + "/" + recipe.total, stack);
		}
		
		NBTTagCompound tag = stack.getTagCompound();
		tag.setBoolean("creative", true);
		stack.setTagCompound(tag);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		if (isCreative && (!itemStackIn.hasTagCompound() || (itemStackIn.hasTagCompound() && !itemStackIn.getTagCompound().hasKey("creative"))))
			setCreativeEntries(itemStackIn);
		
		return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		if (this.isCreative)
		{
			tooltip.add(TextFormatting.GOLD + "Contains all entries");
			tooltip.add(TextFormatting.WHITE + "-" + TextFormatting.DARK_GREEN + " Hover for info");
			tooltip.add(TextFormatting.WHITE + "-" + TextFormatting.DARK_GREEN + " Press to spawn moo with fluid");
		}
		else
		{
			tooltip.add("" + TextFormatting.ITALIC + TextFormatting.YELLOW + "Right click a Fluid Moo to catalogue it");
			super.addInformation(stack, playerIn, tooltip, advanced);
		}
	}
	
	@Override
	public String getTitle()
	{
		return "Fluid Moo Catalogue";
	}
}
