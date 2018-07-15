package com.transcendence.game;

import com.transcendence.entities.craftables.Craftable;

public interface TranscendenceListener {
	public static int SCAVENGE_ACTION = 0;
	public static int MOVE_ACTION = 1;

	public void contextAction(int action, int x, int y);
	public void buildAction(Craftable bp);
	
}
