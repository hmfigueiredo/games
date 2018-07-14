package com.transcendence.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ContextMenu extends Window 
{

	Table optionsTable;
	
	public ContextMenu(String title, Skin skin) {
		super(title, skin);
		
		this.getTitleLabel().setText(title);
		this.setVisible(false);
		optionsTable = new Table();
		optionsTable.setFillParent(true);
		optionsTable.left().top().padTop(20).padLeft(5);
		this.addActor(optionsTable);
	}
	
	public void show(String title, float x, float y)
	{
		this.getTitleLabel().setText(title);
		this.setPosition(x, y);
		this.setVisible(true);
	}

	
	public void addMenuItem(String item, final TranscendenceListener listener, final int action, final int tileX, final int tileY)
	{
		optionsTable.row();
		Label l = new Label(item, super.getSkin());
		l.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) 
			{
				listener.contextAction(action, tileX, tileY);
			}
			
		});
		optionsTable.add(l).top().left();
		this.setHeight(24*(optionsTable.getRows()+2));
	}
	
}
