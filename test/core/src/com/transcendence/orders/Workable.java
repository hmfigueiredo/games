package com.transcendence.orders;

import com.transcendence.entities.characters.GameCharacter;
import com.transcendence.game.GameWorld;

public abstract class Workable {

	public static int DEFAULT_WORK = 4;
	protected int workRemaining;
	
	public Workable()
	{
		workRemaining = DEFAULT_WORK;
	}
	
	/**
	 * 
	 * @param workAmount
	 * @param world
	 * @param gc
	 * @return true if work is complete, false otherwise
	 */
	public boolean doWork(int workAmount, GameWorld world, GameCharacter gc) {
		workRemaining -= workAmount;
		
		return this.isCompleted();
	}
	
	public boolean isCompleted()
	{
		return workRemaining <= 0;
	}

	public void setWorkRemaining(int wr)
	{
		workRemaining = wr;
	}

	public abstract int getX();
	public abstract int getY();
	
	
}
