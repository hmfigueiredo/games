package com.transcendence.orders;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.transcendence.entities.characters.GameCharacter;
import com.transcendence.entities.craftables.Recipe;
import com.transcendence.entities.places.Tile;
import com.transcendence.game.GameWorld;
import com.transcendence.game.TextureManager;

public class Scavenge extends Order {

	public static int SCAVENGE_ICON_SIZE = 24;
	public static int SCAVENGE_DEFAULT_WORK = 2;
	
	private Sprite scavengeSprite;
	private int x, y;
	
	public Scavenge(int ax, int ay) {
		x = ax;
		y = ay;
		scavengeSprite = TextureManager.createSprite("cross", SCAVENGE_ICON_SIZE, SCAVENGE_ICON_SIZE);
		scavengeSprite.setAlpha(0.3f);
		scavengeSprite.setPosition(ax*Tile.TILE_SIZE, ay*Tile.TILE_SIZE);
		scavengeSprite.setSize(Tile.TILE_SIZE, Tile.TILE_SIZE);
		super.workRemaining = SCAVENGE_DEFAULT_WORK;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Scavenge)
		{
			if (((Scavenge)obj).x == this.x && ((Scavenge)obj).y == this.y)
				return true;
		}
		
		return false;
	}

	@Override
	public void render(SpriteBatch batch) {
		scavengeSprite.draw(batch);
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public Rectangle getRectangle() {
		return scavengeSprite.getBoundingRectangle();
	}

	@Override
	public boolean canBeCompleted() {
		return true;
	}

	@Override
	public boolean doWork(int workAmount, GameWorld world, GameCharacter gc)
	{
		super.doWork(workAmount, world, gc);
		
		if (this.isCompleted())
		{
			Recipe items = world.getTile(this.getX(), this.getY()).destroyBlock();
			if (items != null)
			{
				world.dumpItems(items, getX(), getY());
				gc.stopWorking();
			}
				
			// TODO: only recompute neighbors of this block
			world.recomputeNeighbors();
		}
		
		return false;
	}
}
