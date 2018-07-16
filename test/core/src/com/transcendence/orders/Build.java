package com.transcendence.orders;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.transcendence.entities.blocks.Block;
import com.transcendence.entities.characters.GameCharacter;
import com.transcendence.entities.craftables.Craftable;
import com.transcendence.entities.places.Blueprint;
import com.transcendence.entities.places.Tile;
import com.transcendence.game.GameWorld;

public class Build extends Order {
	
	protected Craftable craft;

	public Build(Craftable aCraft, int ax, int ay) {
		craft = aCraft;
		x = ax;
		y = ay;
	}

	@Override
	public void render(SpriteBatch batch) 
	{
		if (craft != null)
		{
			Sprite s = craft.getSprite();
			Blueprint.turnToBlueprint(s);
			s.setPosition(x*Tile.TILE_SIZE, y*Tile.TILE_SIZE);
			s.draw(batch);	
		}
	}
	


	@Override
	public Rectangle getRectangle() {
		if (craft != null && craft.getSprite() != null)
		{
			return craft.getSprite().getBoundingRectangle();
		}
		return null;
	}


	public Craftable getCraftable()
	{
		return craft;
	}

	@Override
	public boolean canBeCompleted() {
		return craft.getRecipe().isComplete();
	}

	@Override
	public boolean doWork(int workAmount, GameWorld world, GameCharacter gc)
	{
		super.doWork(workAmount, world, gc);
		if (this.isCompleted())
		{
			Block b = Block.createBlock(this.getCraftable(), this.getX(), this.getY());
			world.addBlock(b, this.getX(), this.getY());
			gc.stopWorking();
			
			return true;
		}
		
		return false;
	}

}
