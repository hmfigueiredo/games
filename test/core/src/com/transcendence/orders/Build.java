package com.transcendence.orders;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.transcendence.entities.places.Blueprint;

public class Build extends Order {
	
	protected Blueprint blueprint;

	public Build(Blueprint bp, int ax, int ay) {
		blueprint = bp;
		x = ax;
		y = ay;
	}

	@Override
	public void render(SpriteBatch batch) 
	{
		if (blueprint != null)
		{
			blueprint.render(batch);
	
		}
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Build && !(blueprint == null))
		{
			if (!(((Build)obj).blueprint == null))
			{
				if (((Build)obj).blueprint.equals(this.blueprint))
					return true;
			}
		}
		
		return false;
	}


	@Override
	public Rectangle getRectangle() {
		if (blueprint != null)
		{
			return blueprint.getRectangle();
		}
		return null;
	}


	public Blueprint getBlueprint()
	{
		return blueprint;
	}


}
