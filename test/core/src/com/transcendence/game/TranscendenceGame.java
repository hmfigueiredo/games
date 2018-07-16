package com.transcendence.game;


import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.transcendence.entities.blocks.Block;
import com.transcendence.entities.characters.GameCharacter;
import com.transcendence.entities.craftables.Craftable;
import com.transcendence.entities.places.Blueprint;
import com.transcendence.entities.places.Tile;
import com.transcendence.orders.Build;
import com.transcendence.orders.Order;
import com.transcendence.orders.Scavenge;


public class TranscendenceGame extends ApplicationAdapter implements InputProcessor, TranscendenceListener {
	private SpriteBatch batch;
	private GameWorld world;
	private TranscendenceCamera camera;
	private String currentText;
	
	private TranscendenceStage stage;
		
	private ArrayList<GameCharacter> characters;
	private ArrayList<Order> orders;
	
	private boolean isBuildingMode;
	private boolean dragging;
	
	private Craftable craftItem;
	
	@Override
	public void create () {
//	    rotationSpeed = 0.5f;
		
		Gdx.graphics.setWindowedMode(1024, 768);
		
		
	    batch = new SpriteBatch();
	    stage = new TranscendenceStage(this);

		world = new GameWorld();
		world.addObstacles(10, Block.BLOCK_TYPES.TREE);
		world.addObstacles(10, Block.BLOCK_TYPES.ROCK);

		camera = new TranscendenceCamera(world.getHorizontalSize(), world.getVerticalSize());

	    currentText = "";
	    isBuildingMode = false;
	    dragging = false;
		
		characters = new ArrayList<GameCharacter>();
		orders = new ArrayList<Order>();
	    		
		// TODO: Just add a new default character (human) for now
		GameCharacter gc = new GameCharacter(TextureManager.createSprite("character", Tile.TILE_SIZE, Tile.TILE_SIZE), world.getHorizontalSize()/2, world.getVerticalSize()/2);
		characters.add(gc);
				
		
		// center the camera on Character 1
		camera.position.x = characters.get(0).getAbsoluteX();
		camera.position.y = characters.get(0).getAbsoluteY();
		
		
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(multiplexer);
		
		camera.update();

	}
	
	@Override
	public void render () {
		
		handleInput();
		
		// update camera
		camera.update();
						
		// tell the SpriteBatch to render in the
	    // coordinate system specified by the camera.
	    batch.setProjectionMatrix(camera.combined);
	
		// Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		

		batch.begin();		
		
		// render base layer and objects
		world.render(batch);

		// render orders
		Iterator<Order> iterorder = orders.iterator();
		while (iterorder.hasNext())
		{
			Order order = iterorder.next();
			order.render(batch);
		}		
		
		
		// Render characters
		world.findMaterialsToHaul(orders);
		checkOrders();
		Iterator<GameCharacter> iter = characters.iterator();
		iter = characters.iterator();
		
		while (iter.hasNext())
		{
			GameCharacter character = iter.next();
			if (character.isMoving())
			{
				character.keepMoving();
			}
			character.render(batch);
			
			if (character.isSelected())
			{
				Sprite circle = TextureManager.createSprite("circle", Tile.TILE_SIZE, Tile.TILE_SIZE);
				batch.draw(circle, character.getAbsoluteX(), character.getAbsoluteY());
			}
			if (character.isMoving())
			{
				character.keepMoving();
			}
		}
		
		
		// render selections
		// If building mode, show item blueprint under mouse cursor
		if (isBuildingMode && craftItem != null)
		{
			Vector3 clickPosition = new Vector3();
			camera.unproject(clickPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			// Sprite s = TextureManager.createSprite("building", Tile.TILE_SIZE, Tile.TILE_SIZE);
			// Sprite s = TextureManager.createSprite("bed_double", Tile.TILE_SIZE, Tile.TILE_SIZE*2);
			
			// batch.draw(s, clickPosition.x, clickPosition.y, Tile.TILE_SIZE, Tile.TILE_SIZE);
			Sprite s = new Sprite(craftItem.getSprite());
			Blueprint.turnToBlueprint(s);
			
			s.setPosition(clickPosition.x - (clickPosition.x%Tile.TILE_SIZE), clickPosition.y - (clickPosition.y%Tile.TILE_SIZE));
			s.draw(batch);
		}

		batch.end();
		
		// render UI
		stage.setText(currentText);
		stage.setFps(Gdx.graphics.getFramesPerSecond());
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}
	

	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.translate(-8, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			camera.translate(8, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			camera.translate(0, -8, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			camera.translate(0, 8, 0);
		}
		
		// currentText = "Camera position is ("+camera.position.x+","+camera.position.y+") - zoom level "+camera.zoom+" - vpwidth "+camera.viewportWidth;
		// stage.setText(currentText);
	}

	@Override
	public void resize(int width, int height) {
		// TODO: resize should show more cells but not increase cell size... Also stage is becoming wrong
		camera.viewportWidth = 30f;
		camera.viewportHeight = 30f * height/width;
		camera.update();
		
		stage.getViewport().update(width, height, true);
	}
	
	
	private void checkMouseLeftClick(float x, float y) {
		if (!isBuildingMode)
		{
			unselectAll();

			Tile t = world.getTile((int)x/Tile.TILE_SIZE, (int)y/Tile.TILE_SIZE);
			if (t.getBlock() != null)
			{
				t.setSelected(true);
			}
			
			Iterator<GameCharacter> iter = characters.iterator();
			
			while (iter.hasNext())
			{
				GameCharacter character = iter.next();
				
				if ( character.contains(x, y) )
				{			
					// currentText = "character ("+character.getX()+","+character.getY()+") selected at ("+x+", "+y+")";
					// stage.setText(currentText);
					character.setSelected(true);
				}
				else 
				{
					// currentText = "character ("+character.getX()+","+character.getY()+") unselected at ("+x+", "+y+")";
					// stage.setText(currentText);
					character.setSelected(false);
				}
			}
		}
		else
		{
			addBlueprint(craftItem, x, y);
		}
	}
	
	
	private void checkMouseRightClick(float x, float y)
	{
		boolean charactersSelected = false;
		stage.hideMenus();
		
		Iterator<GameCharacter> iter = characters.iterator();
		while (iter.hasNext())
		{
			GameCharacter character = iter.next();

			if (character.isSelected())
			{
				character.moveTo(world, (int)Math.floor(x/Tile.TILE_SIZE), (int)Math.floor(y/Tile.TILE_SIZE));
				charactersSelected = true;
			}
		}
		
		if (!charactersSelected)
		{
			Vector3 v = camera.project(new Vector3(x, y, 0));
			stage.getContextMenu().addMenuItem("Scavenge", this, TranscendenceListener.SCAVENGE_ACTION, (int)x/Tile.TILE_SIZE, (int)y/Tile.TILE_SIZE);
			stage.getContextMenu().addMenuItem("Go here", this, TranscendenceListener.MOVE_ACTION, (int)x/Tile.TILE_SIZE, (int)y/Tile.TILE_SIZE);
			stage.getContextMenu().addMenuItem("Temp option", this, TranscendenceListener.MOVE_ACTION, (int)x/Tile.TILE_SIZE, (int)y/Tile.TILE_SIZE);
			// 
			stage.getContextMenu().show("Block options", v.x, v.y);
		}
	}
	
    

	@Override
	public void dispose () {
		batch.dispose();
		
		// TODO: dispose everything
		
//		img.dispose();
	}

	@Override
	public boolean keyDown(int keycode) 
	{
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.ESCAPE)
		{
			setBuildingMode(false);
			unselectAll();
			return true;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Input.Buttons.LEFT)
		{
			Vector3 clickPosition = new Vector3();
			camera.unproject(clickPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			dragging = true;
			return true;
		}
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button == Input.Buttons.LEFT)
		{
			Vector3 clickPosition = new Vector3();
			camera.unproject(clickPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			// checkMouseLeftClick(Gdx.input.getX(), Gdx.input.getY());
			checkMouseLeftClick(clickPosition.x, clickPosition.y);
		}
		else if (button == Input.Buttons.RIGHT)
		{
			Vector3 clickPosition = new Vector3();
			camera.unproject(clickPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			// checkMouseLeftClick(Gdx.input.getX(), Gdx.input.getY());
			checkMouseRightClick(clickPosition.x, clickPosition.y);
			
		}
		
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (!dragging) 
			return false;
		Vector3 clickPosition = new Vector3();
		camera.unproject(clickPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		
		if (isBuildingMode)
		{
			addBlueprint(craftItem, clickPosition.x, clickPosition.y);
		}
		
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {

		camera.zoom(1.0f*amount);

		// currentText = "Camera position is ("+camera.position.x+","+camera.position.y+") - zoom level "+camera.zoom+" - vpwidth "+camera.viewportWidth;
		// stage.setText(currentText);
		
		return true;
	}

	public void setBuildingMode(boolean b) {
		isBuildingMode = b;
		unselectAll();
	}
	
	public void unselectAll()
	{
		world.unselectAll();
		stage.hideMenus();
		
		Iterator<GameCharacter> iter = characters.iterator();
		while (iter.hasNext())
		{
			GameCharacter character = iter.next();
			character.setSelected(false);
		}
	}
	
	
	private void addBlueprint(Craftable craft, float x, float y)
	{
		Tile targetTile = world.getTile((int)Math.floor(x/Tile.TILE_SIZE), (int)Math.floor(y/Tile.TILE_SIZE));
		
		if (isBuildingMode)
		{			
//			Block b = Block.createBlock(craft, targetTile.getX(), targetTile.getY());
//			Blueprint bp = new Blueprint(b, targetTile.getX()*Tile.TILE_SIZE, targetTile.getY()*Tile.TILE_SIZE);		
//			Build build = new Build(bp, targetTile.getX(), targetTile.getY());

			Build build = new Build(new Craftable(craft), targetTile.getX(), targetTile.getY());
			if (world.canGetBlueprint(build))
			{
				orders.add(build);
				world.addBlueprint(build);
			}
		}
	}
	
	
	private void checkOrders()
	{
		// If there are no orders to follow, return
		if (orders.isEmpty())
			return;
		
		// Let all characters pick up outstanding orders	
		Iterator<GameCharacter> iterc = characters.iterator();
		while (iterc.hasNext())
		{
			GameCharacter gc = iterc.next();
			gc.checkOrders(orders);
			gc.followOrders(world);
		}
		
		// If orders were completed, remove them from the list
		ArrayList<Order> completedOrders = new ArrayList<Order>();
		Iterator<Order> iterOrder = orders.iterator();
		while (iterOrder.hasNext())
		{
			Order order = iterOrder.next();
			if (order.isCompleted())
			{
				completedOrders.add(order);
			}
		}
		orders.removeAll(completedOrders);
	}

	@Override
	public void contextAction(int action, int x, int y) {
		
		switch (action)
		{
			case TranscendenceListener.SCAVENGE_ACTION:
				Scavenge sc = new Scavenge(x, y);
				orders.add(sc);
				break;
			default:
				currentText = "No action selected!";
		}
		
		stage.hideMenus();
		
		// 
	}

	@Override
	public void buildAction(Craftable craft) {
		craftItem = craft;
		isBuildingMode = true;
		stage.hideMenus();
	}
	
}
