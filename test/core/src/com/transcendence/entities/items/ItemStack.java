package com.transcendence.entities.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.transcendence.entities.places.Tile;

public class ItemStack {
	
	public static int MAX_ITEM_STACK = 64;
	private Item item;
	private int itemQt;
	private int x, y;

	public ItemStack(Item it, int qt)
	{
		item = it;
		itemQt = qt;
	}

	public ItemStack(ItemStack istack) {
		// TODO: will probably need to clone this item to support properties
		item = istack.item;
		itemQt = istack.getItemQt();
	}

	public void render(SpriteBatch batch) {
		if (item != null && item.getItemSprite() != null)
		{
			item.getItemSprite().setPosition(x*Tile.TILE_SIZE, y*Tile.TILE_SIZE);
			item.getItemSprite().draw(batch);
		}
	}
	
	public void setPosition(int aX, int aY)
	{
		x = aX;
		y = aY;
	}
	
	public int getItemQt()
	{
		return itemQt;
	}
	
	public Item getItem()
	{
		return item;
	}

	public void setItemQt(int qt) 
	{
		this.itemQt = qt;
	}
}
