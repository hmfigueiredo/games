package com.transcendence.game;

import java.util.HashMap;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.transcendence.entities.items.Item;

public class Inventory extends Table {
	
	private HashMap<Item, Integer> inventory;
	
	public Inventory()
	{
		super();
		inventory = new HashMap<Item, Integer>();
	}
	
	public void updateInventory(GameWorld world)
	{
	}
	
	

}
