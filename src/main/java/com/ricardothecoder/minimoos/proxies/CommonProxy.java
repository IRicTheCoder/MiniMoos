package com.ricardothecoder.minimoos.proxies;

import java.util.ArrayList;
import java.util.Arrays;

import com.ricardothecoder.minimoos.Config;
import com.ricardothecoder.minimoos.MiniMoos;
import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.minimoos.addons.avaritia.AvaritiaPlugin;
import com.ricardothecoder.minimoos.addons.hats.HatPlugin;
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
import com.ricardothecoder.minimoos.entities.EntityInteractHandler;
import com.ricardothecoder.minimoos.entities.EntitySacredMoo;
import com.ricardothecoder.minimoos.entities.bosses.EntityBigScalda;
import com.ricardothecoder.minimoos.entities.bosses.EntityEmilkioBarzini;
import com.ricardothecoder.minimoos.entities.bosses.EntityFrankCattlello;
import com.ricardothecoder.minimoos.entities.bosses.EntityTonyMootana;
import com.ricardothecoder.minimoos.entities.bosses.EntityVealCorleone;
import com.ricardothecoder.minimoos.entities.bosses.EntityYakCapone;
import com.ricardothecoder.minimoos.entities.halloween.EntityCostumeMoo;
import com.ricardothecoder.minimoos.entities.halloween.EntitySpookyMoo;
import com.ricardothecoder.minimoos.entities.halloween.EntityStatueMoo;
import com.ricardothecoder.minimoos.feed.FeedRecipe;
import com.ricardothecoder.minimoos.fluids.Fluids;
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
	private EntityInteractHandler interactHandler;
	
	// REGISTER GAME RULES
	// spawnMiniMoos - Prevents the spawn of Mini Moos
	public void registerGamerules()
	{
		GameruleManager.registerGameRule("spawnMiniMoos", "true", References.NAME);
	}
	
	// REGISTER LOOT TABLES
	public void registerLootTables()
	{
		LootManager.FORSAKEN_COW = LootTableList.register(new ResourceLocation(References.MODID, "forsaken_cow"));
		LootManager.DEMON_COW = LootTableList.register(new ResourceLocation(References.MODID, "demon_cow"));
		LootManager.SACRED_COW = LootTableList.register(new ResourceLocation(References.MODID, "sacred_cow"));
		LootManager.FOOL_COW = LootTableList.register(new ResourceLocation(References.MODID, "fool_cow"));
		
		LootManager.EMILKIO_BARZINI = LootTableList.register(new ResourceLocation(References.MODID, "barzini"));
		LootManager.FRANK_CATTLELLO = LootTableList.register(new ResourceLocation(References.MODID, "costello"));
		LootManager.TONY_MOOTANA = LootTableList.register(new ResourceLocation(References.MODID, "montana"));
		LootManager.VEAL_CORLEONE = LootTableList.register(new ResourceLocation(References.MODID, "corleone"));
		LootManager.YAK_CAPONE = LootTableList.register(new ResourceLocation(References.MODID, "capone"));
		
		LootManager.SPOOKY_COW = LootTableList.register(new ResourceLocation(References.MODID, "spooky_cow"));
		LootManager.STATUE_COW_SVEN = LootTableList.register(new ResourceLocation(References.MODID, "svenieke_cow"));
		LootManager.STATUE_COW_BYSCO = LootTableList.register(new ResourceLocation(References.MODID, "bysco_cow"));
	}
	
	// REGISTER ENTITIES
	public void registerEntities()
	{
		int distance = 4;
		
		EntityUtil.registerEntity(EntityFluidMoo.class, "fluidmoo", MiniMoos.instance, distance * 16, 4, true, ColorUtil.getRGBInteger(50, 50, 255), ColorUtil.getRGBInteger(255, 128, 0));
		EntityUtil.registerEntity(EntityFoolMoo.class, "foolmoo", MiniMoos.instance, distance * 16, 4, true, ColorUtil.getRGBInteger(128, 128, 128), ColorUtil.getRGBInteger(255, 50, 50));
		EntityUtil.registerEntity(EntityDemonMoo.class, "demonmoo", MiniMoos.instance, distance * 16, 4, true, ColorUtil.getRGBInteger(255, 128, 0), ColorUtil.getRGBInteger(255, 50, 50));
		EntityUtil.registerEntity(EntitySacredMoo.class, "sacredmoo", MiniMoos.instance, distance * 16, 4, true, ColorUtil.getRGBInteger(50, 50, 255), ColorUtil.getRGBInteger(50, 150, 255));
		EntityUtil.registerEntity(EntityForsakenMoo.class, "forsakenmoo", MiniMoos.instance, distance * 16, 4, true, ColorUtil.getRGBInteger(50, 50, 50), ColorUtil.getRGBInteger(200, 200, 200));
		
		EntityUtil.registerEntity(EntityFrankCattlello.class, "boss.costello", MiniMoos.instance, distance * 16, 4, true, ColorUtil.getRGBInteger(50, 50, 50), ColorUtil.getRGBInteger(0, 0, 200));
		EntityUtil.registerEntity(EntityTonyMootana.class, "boss.montana", MiniMoos.instance, distance * 16, 4, true, ColorUtil.getRGBInteger(50, 50, 50), ColorUtil.getRGBInteger(150, 150, 150));
		EntityUtil.registerEntity(EntityYakCapone.class, "boss.capone", MiniMoos.instance, distance * 16, 4, true, ColorUtil.getRGBInteger(10, 10, 10), ColorUtil.getRGBInteger(225, 225, 225));
		EntityUtil.registerEntity(EntityVealCorleone.class, "boss.corleone", MiniMoos.instance, distance * 16, 4, true, ColorUtil.getRGBInteger(0, 128, 255), ColorUtil.getRGBInteger(100, 100, 0));
		EntityUtil.registerEntity(EntityEmilkioBarzini.class, "boss.barzini", MiniMoos.instance, distance * 16, 4, true, ColorUtil.getRGBInteger(128, 128, 0), ColorUtil.getRGBInteger(255, 192, 0));
		EntityUtil.registerEntity(EntityBigScalda.class, "boss.scalda", MiniMoos.instance, distance * 16, 4, true, ColorUtil.getRGBInteger(100, 100, 100), ColorUtil.getRGBInteger(255, 200, 0));
		
		EntityUtil.registerEntity(EntitySpookyMoo.class, "halloween.spookymoo", MiniMoos.instance, distance * 16, 4, true, ColorUtil.getRGBInteger(200, 200, 200), ColorUtil.getRGBInteger(255, 255, 255));
		EntityUtil.registerEntity(EntityCostumeMoo.class, "halloween.costumemoo", MiniMoos.instance, distance * 16, 4, true, ColorUtil.getRGBInteger(50, 50, 50), ColorUtil.getRGBInteger(0, 255, 0));
		EntityUtil.registerEntity(EntityStatueMoo.class, "halloween.statuemoo", MiniMoos.instance, distance * 16, 4, true, ColorUtil.getRGBInteger(0, 255, 0), ColorUtil.getRGBInteger(255, 0, 255));
	}
	
	// REGISTER ITEMS
	public void registerItems()
	{
		ItemManager.createItems();
		
		GameRegistry.register(ItemManager.demonSoul);
		GameRegistry.register(ItemManager.goldenEssence);
		GameRegistry.register(ItemManager.miniWheat);
		GameRegistry.register(ItemManager.mooCatalogue);
		GameRegistry.register(ItemManager.creativeMooCatalogue);
		GameRegistry.register(ItemManager.forsakenFruit);
		
		if (Loader.isModLoaded("avaritia"))
		{
			Fluids.createFluids();
		}
	}
	
	// REGISTER RECIPES
	public void registerRecipes()
	{
		GameRegistry.addShapelessRecipe(new ItemStack(ItemManager.miniWheat, 16), new Object[] {Items.WHEAT, Items.BLAZE_POWDER});
		GameRegistry.addShapelessRecipe(new ItemStack(ItemManager.mooCatalogue, 1), new Object[] {Items.BOOK, ItemManager.miniWheat, ItemManager.miniWheat, ItemManager.miniWheat});
	}
	
	// REGISTER SPAWNS
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
		
		if (Config.bossMoo)
		{
			SpawnUtil.addSpawnByType(EntityFrankCattlello.class, 2, 1, 1, EnumCreatureType.CREATURE, BiomeDictionary.Type.values());
			SpawnUtil.addSpawnByType(EntityTonyMootana.class, 1, 1, 1, EnumCreatureType.CREATURE, BiomeDictionary.Type.values());
			SpawnUtil.addSpawnByType(EntityYakCapone.class, 3, 1, 1, EnumCreatureType.CREATURE, BiomeDictionary.Type.values());
			SpawnUtil.addSpawnByType(EntityVealCorleone.class, 2, 1, 1, EnumCreatureType.CREATURE, BiomeDictionary.Type.values());
			SpawnUtil.addSpawnByType(EntityEmilkioBarzini.class, 2, 1, 1, EnumCreatureType.CREATURE, BiomeDictionary.Type.values());
			SpawnUtil.addSpawnByType(EntityBigScalda.class, 1, 1, 1, EnumCreatureType.CREATURE, BiomeDictionary.Type.values());
		}
		
		if (Config.hallowenMoo)
		{
			SpawnUtil.addSpawnByType(EntitySpookyMoo.class, 4, 2, 4, EnumCreatureType.CREATURE, BiomeDictionary.Type.values());
			SpawnUtil.addSpawnByType(EntityCostumeMoo.class, 4, 2, 4, EnumCreatureType.CREATURE, BiomeDictionary.Type.values());
			SpawnUtil.addSpawnByType(EntityStatueMoo.class, 2, 1, 1, EnumCreatureType.CREATURE, BiomeDictionary.Type.values());
		}
	}
	
	
	
	// REGISTER EARLY ADDONS
	// Waila - To show the info of the moos
	// Mine Factory Reloeaded - To allow the the Rancher and Exact Copies
	// Moo Fluids - To convert Fluid Cows into Fluid Moos
	// Avaritia - Creates a fluid for obtaining Neutronium (Only usefull with tinkers)
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
		
		if (Loader.isModLoaded("avaritia"))
		{
			AvaritiaPlugin.init();
		}
		
		if (Loader.isModLoaded("hats"))
		{
			HatPlugin.init();
		}
	}
	
	// REGISTER LATER ADDONS
	// Tinker's Construct - To allow alloy breeding
	public void registerLaterAddons()
	{
		if (Loader.isModLoaded("tconstruct"))
			TConstructPlugin.initTConstruct();
	}
	
	// REGISTER COMMANDS
	// /spawnmoo [fluidName] [x] [y] [z] - Spawns a moo with the given fuild
	public void registerCommands(FMLServerStartingEvent event) 
	{ 
		event.registerServerCommand(new CommandSpawnMoo());
	}
	
	// REGISTER EVENTS
	public void registerEvents() 
	{ 
		interactHandler = new EntityInteractHandler();
		
		MinecraftForge.EVENT_BUS.register(interactHandler);
	}
	
	// UNUSED ON THE COMMON SIDE
	public void unregisterEvents() { }
	public void registerRenders() { }
}
