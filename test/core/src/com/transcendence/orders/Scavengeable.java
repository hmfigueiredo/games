package com.transcendence.orders;

import com.transcendence.entities.craftables.Recipe;

public interface Scavengeable {

	public static int DEFAULT_DECONSTRUCT = 1;
	
	public Recipe scavenge();

}
