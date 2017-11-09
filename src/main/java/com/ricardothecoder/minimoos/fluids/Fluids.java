package com.ricardothecoder.minimoos.fluids;

import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.yac.blocks.BlockCustomFluid;
import com.ricardothecoder.yac.fluids.CustomFluid;
import com.ricardothecoder.yac.fluids.FluidManager;

import net.minecraft.item.EnumRarity;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Loader;

public class Fluids
{
	public static Fluid liquifiedNeutronium;
	public static BlockCustomFluid blockLiquifiedNeutronium;
	
	public static Fluid liquidIskallium;
	public static BlockCustomFluid blockLiquidIskallium;
	
	public static void createFluids()
	{
		if (Loader.isModLoaded("avaritia"))
		{
			blockLiquifiedNeutronium = FluidManager.registerPlacableCustomFluid("neutronium", References.MODID);		
			liquifiedNeutronium = blockLiquifiedNeutronium.getFluid();
			liquifiedNeutronium.setTemperature(4000).setRarity(EnumRarity.EPIC);
		}
		
		if (Loader.isModLoaded("iskalliumreactors"))
		{
			blockLiquidIskallium = FluidManager.registerPlacableCustomFluid("iskallium", References.MODID);
			liquidIskallium = blockLiquidIskallium.getFluid();
			liquidIskallium.setTemperature(50).setRarity(EnumRarity.EPIC);
		}
	}
}
