package com.ricardothecoder.minimoos.renders;

import org.lwjgl.opengl.GL11;

import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.minimoos.entities.EntityMiniMoo;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSpookyMoo extends RenderLiving<EntityMiniMoo>
{
    public RenderSpookyMoo(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }
    
    @Override
    public void doRender(EntityMiniMoo entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
    	GlStateManager.enableBlend();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    	GlStateManager.disableBlend();
    }
    
    @Override
    protected ResourceLocation getEntityTexture(EntityMiniMoo entity)
    {
        return entity.getTexture();
    }
}