package com.transcendence.orders;

public abstract class Workable {

	public static int DEFAULT_WORK = 4;
	protected int workRemaining;
	
	public Workable()
	{
		workRemaining = DEFAULT_WORK;
	}
	
	public void doWork(int workAmount) {
		workRemaining -= workAmount;
	}
	
	public boolean isCompleted()
	{
		return workRemaining <= 0;
	}

	public void setWorkRemaining(int wr)
	{
		workRemaining = wr;
	}
	
	
}
