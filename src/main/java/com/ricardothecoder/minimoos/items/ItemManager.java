package com.ricardothecoder.minimoos.items;

public class ItemManager
{
	public static DemonSoul demonSoul;
	public static GoldenEssence goldenEssence;
	public static ForsakenFruit forsakenFruit;
	
	public static MiniWheat miniWheat;
	
	public static FluidMooCatalogue mooCatalogue;
	public static FluidMooCatalogue creativeMooCatalogue;
	
	public static void createItems()
	{
		demonSoul = new DemonSoul();
		goldenEssence = new GoldenEssence();
		forsakenFruit = new ForsakenFruit();
		
		miniWheat = new MiniWheat();
		
		mooCatalogue = new FluidMooCatalogue(false);
		creativeMooCatalogue = new FluidMooCatalogue(true);
	}
}
