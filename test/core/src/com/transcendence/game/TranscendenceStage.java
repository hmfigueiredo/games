package com.transcendence.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class TranscendenceStage extends Stage {

	public static float BUTTON_WIDTH = 100.0f;
	private Label currentText, currentFps;;
	private static Color defaultColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
	private static Color checkedColor = new Color(0f, 0f, 1.0f, 1.0f);
	private TextButton buildButton;
	private Skin skin;
	
	private TranscendenceGame game;
	ContextMenu contextMenu;
	
	
	public TranscendenceStage(TranscendenceGame tgame)
	{
		super();
		
		game = tgame;
				
		TextureAtlas atlas = new TextureAtlas(Gdx.files.local("ui/default/uiskin.atlas")); 
	    skin = new Skin(Gdx.files.local("ui/default/uiskin.json"), atlas);
	    
	    TranscendenceUI ui = new TranscendenceUI();
	    this.addActor(ui);
	    
	    ui.addListener(new InputListener() {
	        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touch started at (" + x + ", " + y + ")");
                return false;
	        }
	 
	        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	        	System.out.println("touch done at (" + x + ", " + y + ")");
	        }
	    });
	    
 		Table table = new Table();
 		table.setFillParent(true);
 		this.addActor(table.top().left());
 		
 		currentText = new Label("", skin);
 		currentFps = new Label("", skin);
 		table.add(currentText).left().pad(10);
 		table.add(currentFps).right().pad(10);
 		table.row();
/* 		
 		// Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
 		buildButton = new TextButton("Build", skin);
 		buildButton.setWidth(BUTTON_WIDTH);
 		table.add(buildButton).expand().bottom().left().pad(10).width(BUTTON_WIDTH);
 		 		
 		// Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
		// Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
		// ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
		// revert the checked state.
 		buildButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if (buildButton.isChecked())
				{
					buildButton.setText("Building...");
					// System.out.println("Color was ("+buildButton.getColor().r+","+buildButton.getColor().g+","+buildButton.getColor().b+","+buildButton.getColor().a+")");
					buildButton.setColor(checkedColor);
					tgame.setBuildingMode(true);
				}
				else
				{
					buildButton.setColor(defaultColor);
					buildButton.setText("Build");
					game.setBuildingMode(false);
				}
			}
		});
		
		*/
	}
	
	public void setText(String text)
	{
		currentText.setText(text);
	}
	
	public void setFps(int fps)
	{
		currentFps.setText("FPS: "+fps);
	}
	
	public void disableBuildingMode()
	{
		buildButton.setColor(defaultColor);
		buildButton.setText("Build");
		buildButton.setChecked(false);
	}

	public ContextMenu getContextMenu()
	{
		if (contextMenu == null)
		{
			contextMenu = new ContextMenu("", skin);
			this.addActor(contextMenu);
		}
		
		return contextMenu;
	}
	
	public void hideContextMenu()
	{
		this.getActors().removeValue(contextMenu, true);
		contextMenu = null;
	}
}
