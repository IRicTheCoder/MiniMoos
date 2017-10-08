package com.ricardothecoder.minimoos.items;

import java.util.Arrays;
import java.util.List;

import com.ricardothecoder.minimoos.Config;
import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.yac.fluids.FluidManager;
import com.ricardothecoder.yac.items.ItemCatalogue;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
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
		List<Fluid> endFluids = Arrays.asList(Config.getSpawnableFluids(Type.END));
		List<Fluid> netherFluids = Arrays.asList(Config.getSpawnableFluids(Type.NETHER));
		List<Fluid> overworldFluids = Arrays.asList(Config.getSpawnableFluids(Type.MAGICAL));
		
		for(Fluid fluid : FluidManager.getContainableFluids())
		{
			int weight = 0;
			
			if (endFluids.contains(fluid)) weight = endFluids.lastIndexOf(fluid) - endFluids.indexOf(fluid) + 1;
			if (netherFluids.contains(fluid)) weight = netherFluids.lastIndexOf(fluid) - netherFluids.indexOf(fluid) + 1;
			if (overworldFluids.contains(fluid)) weight = overworldFluids.lastIndexOf(fluid) - overworldFluids.indexOf(fluid) + 1;
			
			String fluidTemp = "";
			if (fluid.getTemperature() >= FluidRegistry.LAVA.getTemperature()) fluidTemp = TextFormatting.GOLD + "" + fluid.getTemperature();
			if (fluid.getTemperature() < FluidRegistry.WATER.getTemperature()) fluidTemp = TextFormatting.LIGHT_PURPLE + "" + fluid.getTemperature();
			if (fluid.getTemperature() >= FluidRegistry.WATER.getTemperature() && fluid.getTemperature() < FluidRegistry.LAVA.getTemperature()) fluidTemp = TextFormatting.GREEN + "" + fluid.getTemperature();
			
			addEntry(fluid.getLocalizedName(new FluidStack(fluid, 0)) + ";/spawnmoo " + fluid.getName() + ";" + TextFormatting.AQUA + "Temperature: " + fluidTemp + TextFormatting.AQUA + "  Weight: " + TextFormatting.WHITE + weight, stack);
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
			super.addInformation(stack, playerIn, tooltip, advanced);
	}
	
	@Override
	public String getTitle()
	{
		return "Fluid Moo Catalogue";
	}
}
