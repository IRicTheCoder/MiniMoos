package com.ricardothecoder.minimoos.proxies;

import java.util.ArrayList;
import java.util.Arrays;

import com.ricardothecoder.minimoos.MiniMoos;
import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.minimoos.addons.mfr.MFRPlugin;
import com.ricardothecoder.minimoos.addons.mfr.MooMessage;
import com.ricardothecoder.minimoos.addons.moofluids.FluidCowConverter;
import com.ricardothecoder.minimoos.addons.moofluids.MooFluidsPlugin;
import com.ricardothecoder.minimoos.addons.tconstruct.TConstructPlugin;
import com.ricardothecoder.minimoos.addons.waila.WailaPlugin;
import com.ricardothecoder.minimoos.entities.EntityDemonMoo;
import com.ricardothecoder.minimoos.entities.EntityFluidMoo;
import com.ricardothecoder.minimoos.entities.EntityFoolMoo;
import com.ricardothecoder.minimoos.entities.EntitySacredMoo;
import com.ricardothecoder.minimoos.items.ItemManager;
import com.ricardothecoder.minimoos.loot.LootManager;
import com.ricardothecoder.yac.util.ColorUtil;
import com.ricardothecoder.yac.util.GameruleManager;
import com.ricardothecoder.yac.util.SpawnUtil;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class CommonProxy
{
	public void registerGamerules()
	{
		GameruleManager.registerGameRule("spawnMiniMoos", "true", References.NAME);
	}
	
	public void registerLootTables()
	{
		LootManager.DEMON_COW = LootTableList.register(new ResourceLocation(References.MODID, "demon_cow"));
		LootManager.SACRED_COW = LootTableList.register(new ResourceLocation(References.MODID, "sacred_cow"));
		LootManager.FOOL_COW = LootTableList.register(new ResourceLocation(References.MODID, "fool_cow"));
	}
	
	public void registerEntities()
	{
		int distance = 4;
		
		int entityID = 0;
		EntityRegistry.registerModEntity(EntityFluidMoo.class, "fluidmoo", ++entityID, MiniMoos.instance, distance * 16, 4, true, ColorUtil.getRGBInteger(50, 50, 255), ColorUtil.getRGBInteger(255, 128, 0));
		EntityRegistry.registerModEntity(EntityFoolMoo.class, "foolmoo", ++entityID, MiniMoos.instance, distance * 16, 4, true, ColorUtil.getRGBInteger(128, 128, 128), ColorUtil.getRGBInteger(255, 50, 50));
		EntityRegistry.registerModEntity(EntityDemonMoo.class, "demonmoo", ++entityID, MiniMoos.instance, distance * 16, 4, true, ColorUtil.getRGBInteger(255, 128, 0), ColorUtil.getRGBInteger(255, 50, 50));
		EntityRegistry.registerModEntity(EntitySacredMoo.class, "sacredmoo", ++entityID, MiniMoos.instance, distance * 16, 4, true, ColorUtil.getRGBInteger(50, 50, 255), ColorUtil.getRGBInteger(50, 150, 255));
	}
	
	public void registerItems()
	{
		ItemManager.createItems();
		
		GameRegistry.register(ItemManager.demonSoul);
		GameRegistry.register(ItemManager.goldenEssence);
		GameRegistry.register(ItemManager.miniWheat);
		
		GameRegistry.addShapelessRecipe(new ItemStack(ItemManager.miniWheat, 16), new Object[] {Items.WHEAT, Items.BLAZE_POWDER});
	}
	
	public void registerSpawns()
	{
		SpawnUtil.addSpawnByType(EntityFluidMoo.class, 8, 4, 4, EnumCreatureType.CREATURE, BiomeDictionary.Type.values());
		SpawnUtil.addSpawnByType(EntityDemonMoo.class, 1, 1, 2, EnumCreatureType.CREATURE, BiomeDictionary.Type.NETHER);
		
		ArrayList<BiomeDictionary.Type> biome = new ArrayList<BiomeDictionary.Type>();
		biome.addAll(Arrays.asList(BiomeDictionary.Type.values()));
		biome.remove(BiomeDictionary.Type.NETHER);
		biome.remove(BiomeDictionary.Type.END);
		SpawnUtil.addSpawnByType(EntitySacredMoo.class, 1, 1, 2, EnumCreatureType.CREATURE, biome.toArray(new BiomeDictionary.Type[biome.size()]));
	}
	
	public void registerEvents() { }
	public void unregisterEvents() { }
	
	public void registerEarlyAddons(FMLInitializationEvent event) 
	{ 
		if (Loader.isModLoaded("Waila"))
			WailaPlugin.initWaila();
		
		if (Loader.isModLoaded("minefactoryreloaded"))
		{
			if (event.getSide() == Side.CLIENT)
	        {
	            MiniMoos.network.registerMessage(MooMessage.Handler.class, MooMessage.class, 0, Side.CLIENT);
	        }
			
			MinecraftForge.EVENT_BUS.register(MFRPlugin.initMFR());
		}
		
		if (Loader.isModLoaded("moofluids"))
		{
			MooFluidsPlugin.iniMF();
			MinecraftForge.EVENT_BUS.register(new FluidCowConverter());
		}
	}
	
	public void registerLaterAddons()
	{
		if (Loader.isModLoaded("tconstruct"))
			TConstructPlugin.initTConstruct();
	}
	
	public void registerRenders() { }
}
