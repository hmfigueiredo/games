package com.transcendence.orders;

import com.transcendence.entities.items.ItemStack;

public interface Scavengeable {

	public static int DEFAULT_DECONSTRUCT = 1;
	
	public ItemStack scavenge();

}
