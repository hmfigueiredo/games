package com.transcendence.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TranscendenceUI extends Actor {

	
	private Sprite gameActions;
	private Sprite gameActionsClosed;
	private Sprite gameStatus;
	
	private boolean actionsOpened;
	
	public TranscendenceUI()
	{
		gameActions = TextureManager.createSprite("game_actions", 300, 812);
		gameActionsClosed = TextureManager.createSprite("game_actions_closed", 39, 806);
		gameStatus = TextureManager.createSprite("status_bar", 941, 70);
		
		gameActions.setSize(150, 400);
		gameActionsClosed.setSize(20,  400);
		gameStatus.setSize(470, 35);
		
		gameActions.setPosition(0, Gdx.graphics.getHeight()/2 - gameActions.getHeight()/2);
		gameActionsClosed.setPosition(0, Gdx.graphics.getHeight()/2 - gameActionsClosed.getHeight()/2);
		gameStatus.setPosition(Gdx.graphics.getWidth()/2 - gameStatus.getWidth()/2, Gdx.graphics.getHeight()-gameStatus.getHeight());
		
		// for now, actions are always opened
		actionsOpened = true;
		
		this.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	@Override
	public void draw (Batch batch, float parentAlpha) 
	{
		if (actionsOpened)
		{
			gameActions.draw(batch);
		}
		else
		{
			gameActionsClosed.draw(batch);
		}
		
		gameStatus.draw(batch);
		
		// TODO: draw the actual status
	}
	

}