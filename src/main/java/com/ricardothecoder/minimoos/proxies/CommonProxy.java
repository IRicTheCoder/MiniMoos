package com.ricardothecoder.minimoos.proxies;

import java.util.ArrayList;
import java.util.Arrays;

import com.ricardothecoder.minimoos.Config;
import com.ricardothecoder.minimoos.MiniMoos;
import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.minimoos.addons.mfr.MFRPlugin;
import com.ricardothecoder.minimoos.addons.mfr.MooMessage;
import com.ricardothecoder.minimoos.addons.moofluids.FluidCowConverter;
import com.ricardothecoder.minimoos.addons.moofluids.MooFluidsPlugin;
import com.ricardothecoder.minimoos.addons.tconstruct.TConstructPlugin;
import com.ricardothecoder.minimoos.addons.waila.WailaPlugin;
import com.ricardothecoder.minimoos.commands.CommandSpawnMoo;
import com.ricardothecoder.minimoos.entities.EntityDemonMoo;
import com.ricardothecoder.minimoos.entities.EntityFluidMoo;
import com.ricardothecoder.minimoos.entities.EntityFoolMoo;
import com.ricardothecoder.minimoos.entities.EntityForsakenMoo;
import com.ricardothecoder.minimoos.entities.EntitySacredMoo;
import com.ricardothecoder.minimoos.items.ItemManager;
import com.ricardothecoder.minimoos.loot.LootManager;
import com.ricardothecoder.yac.util.ColorUtil;
import com.ricardothecoder.yac.util.EntityUtil;
import com.ricardothecoder.yac.util.SpawnUtil;
import com.ricardothecoder.yac.world.GameruleManager;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy
{
	public void registerGamerules()
	{
		GameruleManager.registerGameRule("spawnMiniMoos", "true", References.NAME);
	}
	
	public void registerLootTables()
	{
		LootManager.FORSAKEN_COW = LootTableList.register(new ResourceLocation(References.MODID, "forsaken_cow"));
		LootManager.DEMON_COW = LootTableList.register(new ResourceLocation(References.MODID, "demon_cow"));
		LootManager.SACRED_COW = LootTableList.register(new ResourceLocation(References.MODID, "sacred_cow"));
		LootManager.FOOL_COW = LootTableList.register(new ResourceLocation(References.MODID, "fool_cow"));
	}
	
	public void registerEntities()
	{
		int distance = 4;
		
		EntityUtil.registerEntity(EntityFluidMoo.class, "fluidmoo", MiniMoos.instance, distance * 16, 4, true, ColorUtil.getRGBInteger(50, 50, 255), ColorUtil.getRGBInteger(255, 128, 0));
		EntityUtil.registerEntity(EntityFoolMoo.class, "foolmoo", MiniMoos.instance, distance * 16, 4, true, ColorUtil.getRGBInteger(128, 128, 128), ColorUtil.getRGBInteger(255, 50, 50));
		EntityUtil.registerEntity(EntityDemonMoo.class, "demonmoo", MiniMoos.instance, distance * 16, 4, true, ColorUtil.getRGBInteger(255, 128, 0), ColorUtil.getRGBInteger(255, 50, 50));
		EntityUtil.registerEntity(EntitySacredMoo.class, "sacredmoo", MiniMoos.instance, distance * 16, 4, true, ColorUtil.getRGBInteger(50, 50, 255), ColorUtil.getRGBInteger(50, 150, 255));
		EntityUtil.registerEntity(EntityForsakenMoo.class, "forsakenmoo", MiniMoos.instance, distance * 16, 4, true, ColorUtil.getRGBInteger(50, 50, 50), ColorUtil.getRGBInteger(200, 200, 200));
	}
	
	public void registerItems()
	{
		ItemManager.createItems();
		
		GameRegistry.register(ItemManager.demonSoul);
		GameRegistry.register(ItemManager.goldenEssence);
		GameRegistry.register(ItemManager.miniWheat);
		GameRegistry.register(ItemManager.mooCatalogue);
		GameRegistry.register(ItemManager.creativeMooCatalogue);
		GameRegistry.register(ItemManager.forsakenFruit);
		
		GameRegistry.addShapelessRecipe(new ItemStack(ItemManager.miniWheat, 16), new Object[] {Items.WHEAT, Items.BLAZE_POWDER});
		GameRegistry.addShapelessRecipe(new ItemStack(ItemManager.mooCatalogue, 1), new Object[] {Items.BOOK, ItemManager.miniWheat, ItemManager.miniWheat, ItemManager.miniWheat});
	}
	
	public void registerSpawns()
	{
		SpawnUtil.addSpawnByType(EntityFluidMoo.class, 8, 4, 4, EnumCreatureType.CREATURE, BiomeDictionary.Type.values());
		SpawnUtil.addSpawnByType(EntityForsakenMoo.class, Config.forsakenRate, 1, 2, EnumCreatureType.CREATURE, BiomeDictionary.Type.END);
		SpawnUtil.addSpawnByType(EntityDemonMoo.class, Config.demonRate, 1, 2, EnumCreatureType.CREATURE, BiomeDictionary.Type.NETHER);
		
		ArrayList<BiomeDictionary.Type> biome = new ArrayList<BiomeDictionary.Type>();
		biome.addAll(Arrays.asList(BiomeDictionary.Type.values()));
		biome.remove(BiomeDictionary.Type.NETHER);
		biome.remove(BiomeDictionary.Type.END);
		SpawnUtil.addSpawnByType(EntitySacredMoo.class, Config.sacredRate, 1, 2, EnumCreatureType.CREATURE, biome.toArray(new BiomeDictionary.Type[biome.size()]));
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
	
	public void registerCommands(FMLServerStartingEvent event) 
	{ 
		event.registerServerCommand(new CommandSpawnMoo());
	}
}
