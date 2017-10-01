package com.ricardothecoder.minimoos.renders;

import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.minimoos.entities.EntityMiniMoo;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMiniMoo extends RenderLiving<EntityMiniMoo>
{
    public RenderMiniMoo(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
        this.addLayer(new FluidLayer(this));
    }
    
    @Override
    protected ResourceLocation getEntityTexture(EntityMiniMoo entity)
    {
        return entity.getTexture();
    }
}