package com.transcendence.entities.items;

import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.transcendence.game.TextureManager;

public class Item {

	
	public static final int STONE = 0;
	public static final int WOOD = 1;

	private static final int ITEM_SIZE = 32;
	private HashMap<String, String> properties;
	private Sprite itemSprite;
	
	
	private Item(Sprite sprite)
	{
		properties = new HashMap<String,String>();
		itemSprite = sprite;
	}

	private Item(Item providedItem) {
		this(new Sprite(providedItem.itemSprite));
		
		properties = new HashMap<String, String>();
		Iterator<String> keys = providedItem.properties.keySet().iterator();
		
		while (keys.hasNext())
		{
			String currentKey = keys.next();
			properties.put(currentKey, providedItem.properties.get(currentKey));
		}
	}

	public Sprite getItemSprite()
	{
		return itemSprite;
	}
	
	// TODO: Load this information dynamically
	public static Item getNewItem(int itemId) 
	{
		Item newItem = null;
		switch (itemId)
		{
		case STONE:
			newItem = new Item(TextureManager.createSprite("stone_stack", Item.ITEM_SIZE, Item.ITEM_SIZE));
			break;
		case WOOD:
			newItem = new Item(TextureManager.createSprite("wood_stack", Item.ITEM_SIZE, Item.ITEM_SIZE));
			break;
		}
		return newItem;
	}
	
	// TODO: Add item properties (like hit points for decay, etc)
}
