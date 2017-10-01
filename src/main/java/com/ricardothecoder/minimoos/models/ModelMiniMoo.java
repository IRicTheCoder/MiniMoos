package com.ricardothecoder.minimoos.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelMiniMoo extends ModelBase
{
	public ModelRenderer head = new ModelRenderer(this, 0, 0);
	public ModelRenderer body;
	public ModelRenderer leg1;
	public ModelRenderer leg2;
	public ModelRenderer leg3;
	public ModelRenderer leg4;
	protected float childYOffset = 8.0F;
	protected float childZOffset = 4.0F;

	public ModelMiniMoo() 
	{
		int height = 12; 
		float scale = 0.0F;

		this.head.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, scale);
		this.head.setRotationPoint(0.0F, (float)(18 - height), -6.0F);
		this.body = new ModelRenderer(this, 28, 8);
		this.body.addBox(-5.0F, -10.0F, -7.0F, 10, 16, 8, scale);
		this.body.setRotationPoint(0.0F, (float)(17 - height), 2.0F);
		this.leg1 = new ModelRenderer(this, 0, 16);
		this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, height, 4, scale);
		this.leg1.setRotationPoint(-3.0F, (float)(24 - height), 7.0F);
		this.leg2 = new ModelRenderer(this, 0, 16);
		this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, height, 4, scale);
		this.leg2.setRotationPoint(3.0F, (float)(24 - height), 7.0F);
		this.leg3 = new ModelRenderer(this, 0, 16);
		this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, height, 4, scale);
		this.leg3.setRotationPoint(-3.0F, (float)(24 - height), -5.0F);
		this.leg4 = new ModelRenderer(this, 0, 16);
		this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, height, 4, scale);
		this.leg4.setRotationPoint(3.0F, (float)(24 - height), -5.0F);

		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-4.0F, -4.0F, -6.0F, 8, 8, 6, 0.0F);
		this.head.setRotationPoint(0.0F, 4.0F, -8.0F);
		this.head.setTextureOffset(22, 0).addBox(-5.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
		this.head.setTextureOffset(22, 0).addBox(4.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
		this.body = new ModelRenderer(this, 18, 4);
		this.body.addBox(-6.0F, -10.0F, -7.0F, 12, 18, 10, 0.0F);
		this.body.setRotationPoint(0.0F, 5.0F, 2.0F);
		this.body.setTextureOffset(52, 0).addBox(-2.0F, 2.0F, -8.0F, 4, 6, 1);
		--this.leg1.rotationPointX;
		++this.leg2.rotationPointX;
		this.leg1.rotationPointZ += 0.0F;
		this.leg2.rotationPointZ += 0.0F;
		--this.leg3.rotationPointX;
		++this.leg4.rotationPointX;
		--this.leg3.rotationPointZ;
		--this.leg4.rotationPointZ;
		this.childZOffset += 2.0F;
	}

	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

		if (this.isChild)
		{
			float f = 4.0F;
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
			GlStateManager.translate(0.0F, (this.childYOffset * 2.25F) * scale, (this.childZOffset * 1.25F) * scale);
			this.head.render(scale);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.25F, 0.25F, 0.25F);
			GlStateManager.translate(0.0F, 72.0F * scale, 0.0F);
			this.body.render(scale);
			this.leg1.render(scale);
			this.leg2.render(scale);
			this.leg3.render(scale);
			this.leg4.render(scale);
			GlStateManager.popMatrix();
		}
		else
		{
			float f = 2.0F;
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, this.childYOffset * scale, this.childZOffset * scale);
			this.head.render(scale);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
			this.body.render(scale);
			this.leg1.render(scale);
			this.leg2.render(scale);
			this.leg3.render(scale);
			this.leg4.render(scale);
			GlStateManager.popMatrix();
		}
	}

	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		this.head.rotateAngleX = headPitch * 0.017453292F;
		this.head.rotateAngleY = netHeadYaw * 0.017453292F;
		this.body.rotateAngleX = ((float)Math.PI / 2F);
		this.leg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leg2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		this.leg3.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		this.leg4.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
	}
}