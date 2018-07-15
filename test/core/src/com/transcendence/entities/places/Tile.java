package com.transcendence.entities.places;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.transcendence.entities.blocks.Block;
import com.transcendence.entities.craftables.Recipe;
import com.transcendence.entities.items.ItemStack;

public class Tile {

	public static int TILE_SIZE = 32;

	protected int index;
	protected int x, y;
	protected boolean isTraversable;

	private Array<Connection<Tile>> mConnections;
	protected Block block;
	protected ItemStack items;
	protected Sprite tileSprite;
	private boolean selected;
	

	public Tile(Sprite sprite, int aX, int aY, int aIndex)
	{
		x = aX;
		y = aY;
		index = aIndex;
		mConnections = new Array<Connection<Tile>>();
		isTraversable = true;
		block = null;
		items = null;
		tileSprite = sprite;
		tileSprite.setPosition(x*Tile.TILE_SIZE, y*Tile.TILE_SIZE);
		selected = false;
	}
	
	
	public Array<Connection<Tile>> getConnections() {
		return mConnections;
	}
	
	public void addNeighbour(Tile aNode) {
	    if (null != aNode && aNode.isTraversable()) {
	        mConnections.add(new DefaultConnection<Tile>(this, aNode));
	    }
	}
	
	public void clearNeighbours()
	{
		mConnections.clear();
	}
	
	public int getIndex() {
		return index;
	}

	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public String toString()
	{
		return "("+x+","+y+")";
	}

	public boolean isTraversable()
	{
		return isTraversable;
	}
	
	public void setTraversable(boolean t)
	{
		isTraversable = t;
	}
	
	
	public ItemStack getItems() {
		return items;
	}
	
	public void addItems(ItemStack itemsToAdd)
	{
		if (items == null)
		{
			items = itemsToAdd;
			items.setPosition(x, y);
		}
	}

	public Block getBlock() {
		return block;
	}
	
	public void setBlock(Block b)
	{
		block = b;
		
		// Set the block position for this block the same as the parent tile
		block.setSpritePosition(x*Tile.TILE_SIZE, y*Tile.TILE_SIZE);
	}
	
	
	public void render(SpriteBatch batch) {
		tileSprite.draw(batch);
		
		if (block != null)
		{
			block.render(batch, false);
		}
		else if (items != null)
		{
			items.render(batch);
		}
			
	}


	public void setSelected(boolean b) {
		selected = b;	
	}


	public boolean isSelected() {
		return selected;
	}


	public Recipe destroyBlock() {
		Recipe resultingItems = new Recipe();
		
		if (block != null)
		{
			resultingItems = block.scavenge();
			block = null;
			this.isTraversable = true;
		}
		
		return resultingItems;
	}
}
