package com.transcendence.entities.blocks;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.transcendence.entities.craftables.Craftable;
import com.transcendence.entities.craftables.Recipe;
import com.transcendence.entities.items.Item;
import com.transcendence.entities.items.ItemStack;
import com.transcendence.entities.places.Blueprint;
import com.transcendence.entities.places.Tile;
import com.transcendence.game.TextureManager;
import com.transcendence.orders.Scavengeable;
import com.transcendence.orders.Workable;

public class Block extends Workable implements Scavengeable {
	
	
	// TODO: LOAD this from file (example https://stackoverflow.com/questions/4908973/java-property-file-as-enum)
	public static enum BLOCK_TYPES { 
		TREE("tree", Item.WOOD, ItemStack.MAX_ITEM_STACK), 
		ROCK("rock", Item.STONE, ItemStack.MAX_ITEM_STACK);
		
		private final String sprite;
		private final int item;
		private final int qt;
		
		BLOCK_TYPES(String s, int i, int q)
		{
			sprite = s;
			item = i;
			qt = q;
		}
		
		public String getSprite()
		{
			return sprite;
		}
		
		public int getItem()
		{
			return item;
		}
		
		public int getQt()
		{
			return qt;
		}
    }
	
	protected Sprite blockSprite;
	// protected int hitPoints;
	protected Recipe itemsProvided;	
	
	protected Block(Sprite sprite, Recipe providedItem) 
	{
		blockSprite = sprite;		
		itemsProvided = providedItem;
	}

	public void render(SpriteBatch batch, boolean renderAsBlueprint)
	{
		if (blockSprite != null)
		{
			Sprite bp = new Sprite(blockSprite);

			// draw the block and force it to fit into the tile
			if (renderAsBlueprint)
			{
				Blueprint.turnToBlueprint(bp);
			}
			bp.draw(batch);
		}
	}

	public void setSpritePosition(float x, float y) {
		blockSprite.setPosition(x, y);
	}

	@Override
	public Recipe scavenge() {
		return itemsProvided;
	}

	public Rectangle getRectangle() {
		return blockSprite.getBoundingRectangle();
	}
	
	
	public static Block createBlock(Block.BLOCK_TYPES theBlock, int x, int y)
	{
		Sprite s = TextureManager.createSprite(theBlock.getSprite(), Tile.TILE_SIZE, Tile.TILE_SIZE);
		s.setPosition(x*Tile.TILE_SIZE, y*Tile.TILE_SIZE);
		// TODO: for now, all blocks return only one item type
		Recipe r = new Recipe();
		r.addItem(Item.getNewItem(theBlock.getItem()), ItemStack.MAX_ITEM_STACK);
		
		return new Block(s, r);
	}
	
	public static Block createBlock(Craftable craft, int x, int y)
	{
		Sprite s = new Sprite(craft.getSprite());
		s.setPosition(x*Tile.TILE_SIZE, y*Tile.TILE_SIZE);

		return new Block(s, craft.getRecipe());
	}

}
