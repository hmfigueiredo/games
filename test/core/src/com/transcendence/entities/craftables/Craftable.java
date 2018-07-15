package com.transcendence.entities.craftables;

import java.util.Iterator;

import com.transcendence.entities.items.Item;
import com.transcendence.entities.items.ItemStack;
import com.transcendence.orders.Scavengeable;
import com.transcendence.orders.Workable;

public class Craftable extends Workable implements Scavengeable {

	public static double SCAVENGE_ITEM_PERCENTAGE = 0.75;
	
	protected Recipe recipe;
	protected int tilesHorizontal, tilesVertical;
	
	public Craftable(int aTilesHor, int aTilesVert)
	{
		recipe = new Recipe();
		tilesHorizontal = aTilesHor;
		tilesVertical = aTilesVert;
	}
	
	public void addItemsToRecipe(Item item, int quantity)
	{
		recipe.addItem(item, quantity);
	}
	
	
	public Recipe getRecipe()
	{
		return recipe;
	}

	@Override
	public Recipe scavenge() {
		Recipe result = new Recipe();
		Iterator<ItemStack> iter = recipe.itemsInRecipe.keySet().iterator();
		
		while (iter.hasNext())
		{
			ItemStack currentItem = iter.next();
			result.addItem(currentItem.getItem(), (int)(currentItem.getItemQt()*SCAVENGE_ITEM_PERCENTAGE));
		}
		
		return result;
	}

	
	
}
