package com.ricardothecoder.minimoos.entities.stats;

import com.ricardothecoder.yac.entities.stats.EntityStats;

public class FluidMooStats extends EntityStats
{
	private final static String FLUID_NAME_ID = "FluidName";
	private final static String BUCKET_AMOUNT_ID = "BucketAmount";
	private final static String DELAY_ID = "Delay";
	private final static String QUANTITY_ID = "Quantity";
	private final static String EFFICIENCY_ID = "Efficiency";
	
	public FluidMooStats() 
	{
		stats.setString(FLUID_NAME_ID, "None");
		stats.setInteger(BUCKET_AMOUNT_ID, 1);
		stats.setInteger(DELAY_ID, 0);
		stats.setInteger(QUANTITY_ID, 1);
		stats.setInteger(EFFICIENCY_ID, 1);
	}
	
	public String getFluidName()
	{
		return stats.getString(FLUID_NAME_ID);
	}
	
	public void setFluidName(String fluidName)
	{
		stats.setString(FLUID_NAME_ID, fluidName);
	}
	
	public int getBuckets()
	{
		return stats.getInteger(BUCKET_AMOUNT_ID);
	}
	
	public void setBuckets(int buckets)
	{
		stats.setInteger(BUCKET_AMOUNT_ID, buckets);
		
		if (buckets < 0)
			stats.setInteger(BUCKET_AMOUNT_ID, 0);
	}
	
	public int getDelay()
	{
		return stats.getInteger(DELAY_ID);
	}
	
	public void setDelay(int delay)
	{
		stats.setInteger(DELAY_ID, delay);
	}
	
	public int getQuantity()
	{
		return stats.getInteger(QUANTITY_ID);
	}
	
	public void setQuantity(int quantity)
	{
		stats.setInteger(QUANTITY_ID, quantity);
	}
	
	public int getEfficiency()
	{
		return stats.getInteger(EFFICIENCY_ID);
	}
	
	public void setEfficiency(int efficiency)
	{
		stats.setInteger(EFFICIENCY_ID, efficiency);
	}
}
