package com.transcendence.orders;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.transcendence.entities.characters.GameCharacter;
import com.transcendence.entities.craftables.Recipe;
import com.transcendence.entities.items.ItemStack;
import com.transcendence.game.GameWorld;

public class Haul extends Order {

	protected ItemStack itemsToHaul;
	protected int originX, originY, destinationX, destinationY;
	protected boolean movingToDestination;
	
	public Haul(ItemStack istack, int origx, int origy, int destx, int desty)
	{
		workRemaining = 1;
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
		workRemaining = 1;
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
	
	@Override
	public boolean doWork(int workAmount, GameWorld world, GameCharacter gc)
	{
		super.doWork(workAmount, world, gc);
		
		if (!this.isMovingToDestination())
		{	
			System.out.println(gc.getName() + " found some materials to haul");
			gc.pickupItems(world.getTile(this.getX(), this.getY()), this.getQuantityToHaul());
			this.setMovingToDestination(true);
			return false;
		}
		else
		{
			Build b = world.getTile(this.getX(), this.getY()).getBuild();
			if (b != null)
			{	
				System.out.println(gc.getName() + " is adding items to recipe, and leaving it free to be picked up");
				b.getCraftable().getRecipe().haulItems(gc.dropCarryingItems());
				b.setBeingAddressed(false);
			}
			else
			{
				System.out.println(gc.getName() + " is dumping items because there is no recipe nearby");
				Recipe r = new Recipe();
				ItemStack carryingItems = gc.dropCarryingItems();
				r.addItem(carryingItems.getItem(), carryingItems.getItemQt());;
				world.dumpItems(r, this.getX(), this.getY());
			}

			gc.stopWorking();
		}
		return true;
	}
}
