package com.ricardothecoder.minimoos.items;

public class ItemManager
{
	public static DemonSoul demonSoul;
	public static GoldenEssence goldenEssence;
	
	public static MiniWheat miniWheat;
	
	public static void createItems()
	{
		demonSoul = new DemonSoul();
		goldenEssence = new GoldenEssence();
		
		miniWheat = new MiniWheat();
	}
}
