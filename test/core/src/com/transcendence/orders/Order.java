package com.transcendence.orders;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Order extends Workable {

	protected int x, y;

	public abstract void render(SpriteBatch batch);	

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public abstract Rectangle getRectangle();

}
