package com.transcendence.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.transcendence.entities.craftables.Craftable;

public class GameMenu extends Window 
{

	Table optionsTable;
	
	public GameMenu(String title, Skin skin) {
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

	
	public void addBuildItem(final TranscendenceListener listener, final Craftable craft)
	{
		optionsTable.row();
		Label l = new Label(craft.getName(), super.getSkin());
		l.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) 
			{
				listener.buildAction(craft);
			}
			
		});
		optionsTable.add(l).top().left();
		this.setHeight(24*(optionsTable.getRows()+2));
	}
	

}
