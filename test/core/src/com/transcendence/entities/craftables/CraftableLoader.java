package com.transcendence.entities.craftables;

import java.util.ArrayList;

import com.transcendence.entities.items.Item;
import com.transcendence.entities.places.Tile;
import com.transcendence.game.TextureManager;

public class CraftableLoader {

	private static ArrayList<Craftable> craftables;
	
	
	public static ArrayList<Craftable> getCraftables()
	{
		if (craftables == null)
		{
			craftables = new ArrayList<Craftable>();
			
			// TODO: Load craftables from a configuration file instead
			loadHardcodedCraftables();
		}
		
		return craftables;
	}

	
	/**
	 * Temporary method to load some craftables
	 */
	private static void loadHardcodedCraftables() {
		Craftable craft;
		
		// BUILDING
		craft = new Craftable("Building", TextureManager.createSprite("building", Tile.TILE_SIZE, Tile.TILE_SIZE), Tile.TILE_SIZE, Tile.TILE_SIZE);
		craft.addItemsToRecipe(Item.getNewItem(Item.STONE), 25);
		craftables.add(craft);
		
		// BED
		craft = new Craftable("Bed", TextureManager.createSprite("bed_double", Tile.TILE_SIZE*2, Tile.TILE_SIZE*2), Tile.TILE_SIZE*2, Tile.TILE_SIZE*2);
		craft.addItemsToRecipe(Item.getNewItem(Item.WOOD), 50);
		craftables.add(craft);
	}
	
	
	
}
