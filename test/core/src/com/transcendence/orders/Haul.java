package com.transcendence.orders;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.transcendence.entities.items.ItemStack;

public class Haul extends Order {

	protected ItemStack itemsToHaul;
	protected int originX, originY, destinationX, destinationY;
	protected boolean movingToDestination;
	
	public Haul(ItemStack istack, int origx, int origy, int destx, int desty)
	{
		workRemaining = 0;
		itemsToHaul = istack;
		originX = origx;
		originY = origy;
		destinationX = destx;
		destinationY = desty;
		x = origx;
		y = origy;
		movingToDestination = false;
	}
	
	@Override
	public Rectangle getRectangle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canBeCompleted() {
		return true;
	}

	@Override
	public void render(SpriteBatch batch)
	{
	}


	public void setMovingToDestination(boolean b)
	{
		movingToDestination = true;
		x = destinationX;
		y = destinationY;
	}

	public boolean isMovingToDestination() {
		return movingToDestination;
	}
	
	
	public int getQuantityToHaul()
	{
		if (itemsToHaul != null)
		{
			return itemsToHaul.getItemQt();
		}
		return 0;
	}
}
