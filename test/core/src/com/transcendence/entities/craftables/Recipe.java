package com.transcendence.entities.craftables;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.transcendence.entities.items.Item;
import com.transcendence.entities.items.ItemStack;

public class Recipe {

	/**
	 * The recipe is a set of ItemStack (Items + qt) and the amount of items that has already been placed to build
	 */
	// TODO: Change this implementation, too troublesome
	protected HashMap<ItemStack, Integer> itemsInRecipe;
	
	
	public Recipe()
	{
		itemsInRecipe = new HashMap<ItemStack, Integer>();
	}
	
	
	public Recipe(Recipe recipe) {
		itemsInRecipe = new HashMap<ItemStack, Integer>();
		
		Iterator<ItemStack> iter = recipe.itemsInRecipe.keySet().iterator();
		
		while (iter.hasNext())
		{
			ItemStack istack = iter.next();
			itemsInRecipe.put(new ItemStack(istack), 0);
		}
	}


	public boolean isComplete()
	{
		Iterator<ItemStack> iter = itemsInRecipe.keySet().iterator();
		
		while (iter.hasNext())
		{
			ItemStack istack = iter.next();
			if (itemsInRecipe.get(istack).intValue() < istack.getItemQt())
				return false;
		}
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


	public int getHauledQuantity(ItemStack istack) {
		return itemsInRecipe.get(istack).intValue();
	}

	
	public void haulItems(ItemStack items)
	{
		Iterator<ItemStack> iter = itemsInRecipe.keySet().iterator();
		ItemStack istack = null;
		int currentQt = 0;
		
		while (iter.hasNext())
		{
			istack = iter.next();
			if (istack.getItem().equals(items.getItem()))
			{
				currentQt = itemsInRecipe.get(istack).intValue();
				break;
			}
		}
		
		if (istack != null)
		{
			itemsInRecipe.remove(istack);
			itemsInRecipe.put(istack, currentQt + items.getItemQt());
		}
		
	}
}
