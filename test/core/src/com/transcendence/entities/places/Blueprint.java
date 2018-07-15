package com.transcendence.entities.places;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.transcendence.entities.blocks.Block;

public class Blueprint {
	private Block block;
	int x, y;
	

	public Blueprint(Block b, int posX, int posY)
	{
		super();
		block = b;
		x = posX;
		y = posY;
	}

	public void render(SpriteBatch batch) {
		
		
		block.render(batch, true);		
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj !=null && obj instanceof Blueprint)
		{
			Blueprint other = (Blueprint)obj;
			
			// TODO: This needs to be enhanced as different blueprints might start on different places?
			if ((x == other.getX()) && (y == other.getY()))
				return true;
		}
		return false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}


	public Rectangle getRectangle()
	{
		return block.getRectangle();
	}
	
	
	public Block getBlock()
	{
		return block;
	}

	public static Sprite turnToBlueprint(Sprite s) {
		s.setColor(Color.BLUE);		
		s.setAlpha(0.3f);
		
		return s;
	}
}
