package com.ricardothecoder.minimoos.addons.hats;

import com.ricardothecoder.minimoos.entities.EntityMiniMoo;

import me.ichun.mods.hats.api.RenderOnEntityHelper;
import net.minecraft.entity.EntityLivingBase;

public class MiniMooHatRender extends RenderOnEntityHelper
{
	@Override
	public Class helperForClass()
	{
		return EntityMiniMoo.class;
	}
	
	@Override
	public boolean canWearHat(EntityLivingBase living)
	{
		return false;
	}
}
