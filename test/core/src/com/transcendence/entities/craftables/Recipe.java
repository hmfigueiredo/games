package com.transcendence.entities.craftables;

import java.util.HashMap;
import java.util.Set;

import com.transcendence.entities.items.Item;
import com.transcendence.entities.items.ItemStack;

public class Recipe {

	/**
	 * The recipe is a set of ItemStack (Items + qt) and the amount of items that has already been placed to build
	 */
	protected HashMap<ItemStack, Integer> itemsInRecipe;
	
	
	public Recipe()
	{
		itemsInRecipe = new HashMap<ItemStack, Integer>();
	}
	
	
	public boolean isComplete()
	{
		return true;
	}


	public void addItem(Item item, int quantity) {
		ItemStack iStack = new ItemStack(item, quantity);
		itemsInRecipe.put(iStack, 0);
	}

	
	public Set<ItemStack> getItems()
	{
		return itemsInRecipe.keySet();
	}
	
}
