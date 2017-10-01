package com.ricardothecoder.minimoos.addons.moofluids;

import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.yac.util.ModLogger;
import com.ricardothecoder.yac.util.SpawnUtil;
import com.robrit.moofluids.common.entity.EntityFluidCow;

import net.minecraft.entity.EnumCreatureType;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class MooFluidsPlugin
{
	public static void iniMF()
	{
		ModLogger.info(References.NAME, "Found Moo Fluids Installed. Registering converter.");
		SpawnUtil.removeSpawnByType(EntityFluidCow.class, EnumCreatureType.CREATURE, BiomeDictionary.Type.values());		
	}
}
