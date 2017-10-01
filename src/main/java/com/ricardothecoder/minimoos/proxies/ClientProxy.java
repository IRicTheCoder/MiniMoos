package com.ricardothecoder.minimoos.proxies;

import com.ricardothecoder.minimoos.MiniMoos;
import com.ricardothecoder.minimoos.entities.EntityDemonMoo;
import com.ricardothecoder.minimoos.entities.EntityFluidMoo;
import com.ricardothecoder.minimoos.entities.EntityFoolMoo;
import com.ricardothecoder.minimoos.entities.EntityMiniMoo;
import com.ricardothecoder.minimoos.entities.EntitySacredMoo;
import com.ricardothecoder.minimoos.fluids.FluidColorManager;
import com.ricardothecoder.minimoos.items.ItemManager;
import com.ricardothecoder.minimoos.models.ModelMiniMoo;
import com.ricardothecoder.minimoos.renders.RenderMiniMoo;
import com.ricardothecoder.yac.util.ColorUtil;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ClientProxy extends CommonProxy
{
	public static FluidColorManager fColorManager;
	
	@Override
	public void registerRenders() 
	{
		ModelLoader.setCustomModelResourceLocation(ItemManager.demonSoul, 0, new ModelResourceLocation(ItemManager.demonSoul.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ItemManager.goldenEssence, 0, new ModelResourceLocation(ItemManager.goldenEssence.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ItemManager.miniWheat, 0, new ModelResourceLocation(ItemManager.miniWheat.getRegistryName(), "inventory"));
		
		RenderingRegistry.registerEntityRenderingHandler(EntityMiniMoo.class, new IRenderFactory() 
		{
			@Override
			public Render createRenderFor(RenderManager manager) {
				return new RenderMiniMoo(manager, new ModelMiniMoo(), 0.5f);
			}
			
		});
	}
	
	@Override
	public void registerEvents()
	{
		super.registerEvents();
		fColorManager = new FluidColorManager();
		
		MinecraftForge.EVENT_BUS.register(fColorManager);
	}
}
