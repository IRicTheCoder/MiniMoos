package com.ricardothecoder.minimoos.addons.hats;

import me.ichun.mods.hats.common.core.ApiHandler;

public class HatPlugin
{
	public static void init()
	{
		ApiHandler.registerHelper(new MiniMooHatRender());
	}
}
