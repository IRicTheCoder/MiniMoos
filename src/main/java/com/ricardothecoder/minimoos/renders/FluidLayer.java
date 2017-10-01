package com.ricardothecoder.minimoos.renders;

import java.awt.Color;

import com.ricardothecoder.minimoos.entities.EntityMiniMoo;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FluidLayer implements LayerRenderer<EntityMiniMoo> 
{
    private final RenderMiniMoo renderer;

    public FluidLayer(RenderMiniMoo rendererIn)
    {
        this.renderer = rendererIn;
    }

    public void doRenderLayer(EntityMiniMoo entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	if (entitylivingbaseIn.getOverlayTexture() == null)
    		return;
    	
        this.renderer.bindTexture(entitylivingbaseIn.getOverlayTexture());
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE);
        
        Color color = entitylivingbaseIn.getOverlayColor();
        GlStateManager.color((float)color.getRed()/255F, (float)color.getGreen()/255F, (float)color.getBlue()/255F, 0.8F);
        
        this.renderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}
