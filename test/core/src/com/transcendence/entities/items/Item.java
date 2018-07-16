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
	private int itemTypeId;
	
	
	private Item(int itemTypeId, Sprite sprite)
	{
		properties = new HashMap<String,String>();
		itemSprite = sprite;
		this.itemTypeId = itemTypeId;
	}

	private Item(Item providedItem) {
		this(providedItem.itemTypeId, new Sprite(providedItem.itemSprite));
		
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
			newItem = new Item(STONE, TextureManager.createSprite("stone_stack", Item.ITEM_SIZE, Item.ITEM_SIZE));
			break;
		case WOOD:
			newItem = new Item(WOOD, TextureManager.createSprite("wood_stack", Item.ITEM_SIZE, Item.ITEM_SIZE));
			break;
		}
		return newItem;
	}
	
	public int getItemTypeId()
	{
		return itemTypeId;
	}
	
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Item)
		{
			if (obj != null && ((Item)obj).itemTypeId == itemTypeId)
				return true;
		}
		
		return false;
	}

	public String getName() {
		switch (itemTypeId)
		{
		case WOOD: return "wood stack";
		case STONE: return "stone stack";
		default: return "unknown";
		}
	}
	
	// TODO: Add item properties (like hit points for decay, etc)
}
