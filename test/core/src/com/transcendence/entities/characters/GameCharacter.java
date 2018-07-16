package com.transcendence.entities.characters;

import java.util.Calendar;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.transcendence.entities.items.ItemStack;
import com.transcendence.entities.places.Tile;
import com.transcendence.orders.Build;
import com.transcendence.orders.Workable;

public class GameCharacter {

	public static int DEFAULT_WORK_AMOUNT = 1;
	public static int DEFAULT_WORK_COOLDOWN = 500;
	
	protected boolean characterSelected;
	protected int MOVE_RATE = Tile.TILE_SIZE / 16;
	protected Rectangle character;
	protected boolean isMoving, isWorking;
	private DefaultGraphPath<Tile> currentPath;
	private Sprite characterSprite;
	private long lastWorkTime;
	
	private ItemStack carryingItems;
	
	// Work abilities
	private int workAbility;
	
		
	public GameCharacter(Sprite sprite, int posX, int posY)
	{
		// By default place the character in the middle of the screen
		character = new Rectangle();
		character.x = posX * Tile.TILE_SIZE;
		character.y = posY * Tile.TILE_SIZE;
		character.width = Tile.TILE_SIZE;
		character.height = Tile.TILE_SIZE;
		characterSprite = sprite;
		sprite.setSize(character.width, character.height);
		
		characterSelected = false;
		isMoving = false;
		isWorking = false;
		
		workAbility = DEFAULT_WORK_AMOUNT;
		lastWorkTime = 0;
		carryingItems = null;
	}
	
	
	public int getX()
	{
		return (int)character.x;
	}
	
	public int getY()
	{
		return (int)character.y;
	}
	
	public boolean isSelected()
	{
		return characterSelected;
	}
	
	public boolean isMoving()
	{
		return isMoving;
	}


	public Vector3 getCenter() {
		Vector2 v = new Vector2();
		character.getCenter(v);
		
		return new Vector3(v.x, v.y, 0);
		
	}


	public boolean contains(float x, float y) {
		return character.contains(x,y);
	}


	public void setSelected(boolean b) {
		characterSelected = b;
	}

	public Vector2 getNode()
	{
		Vector2 v = new Vector2();
		v.x = character.x / Tile.TILE_SIZE;
		v.y = character.y / Tile.TILE_SIZE;
		
		return v;
	}

	public void keepMoving()
	{
		this.followPath(this.currentPath);
	}

	public void stopMoving() {
		this.currentPath = null;
		isMoving = false;
		character.x = Tile.TILE_SIZE*(Math.round(character.x/Tile.TILE_SIZE));
		character.y = Tile.TILE_SIZE*(Math.round(character.y/Tile.TILE_SIZE));
	}
	
	public void followPath(DefaultGraphPath<Tile> path) 
	{
		if (path != null && path.getCount() > 1)
		{
			// The next tile is the second in the graph - first is the origin
			Tile nextTile = path.nodes.get(1);
			
			
			// Move at MOVE_RATE closer to next tile in path
			Vector2 currentNode = getNode();
			if (currentNode.x < nextTile.getX())
			{
				character.x += MOVE_RATE;
			}
			if (currentNode.x > nextTile.getX())
			{
				character.x -= MOVE_RATE;
			}
			if (currentNode.y < nextTile.getY())
			{
				character.y += MOVE_RATE;
			}
			if (currentNode.y > nextTile.getY())
			{
				character.y -= MOVE_RATE;
			}
			
			// If character has reached next tile, pop it from the graph
			if (currentNode.epsilonEquals(new Vector2(nextTile.getX(), nextTile.getY())))
			{
				path.nodes.removeIndex(1);
			}
					
			
			isMoving = true;
			currentPath = path;
		}
		else
		{
			// TODO: isMoving to keep moving on the update cycle
			isMoving = false;
		}
	}
	
	
	public void render(SpriteBatch batch)
	{
		characterSprite.setPosition(character.x, character.y);
		characterSprite.draw(batch);
	}
	
	public Rectangle getRectangle()
	{
		return character;
	}


	public boolean isBusy()
	{
		return isMoving || isWorking;
	}


	public void startWorking(Workable work) {
		long t = Calendar.getInstance().getTimeInMillis();
		
		// Only continue working if cooldown period is over
		if ((t-lastWorkTime) > DEFAULT_WORK_COOLDOWN)
		{
			isWorking = true;
			isMoving = false;
			work.doWork(workAbility);
			lastWorkTime = t;
		}	
	}
	
	public void stopWorking() {
		isWorking = false;
		this.stopMoving();
		lastWorkTime = 0;
	}


	public boolean isNextTo(int x, int y) {
		// If it's already overlapping...
		Rectangle rect = new Rectangle(x*Tile.TILE_SIZE, y*Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
		if (this.getRectangle().overlaps(rect))
			return true;
		
		// Only consider next to if character is fully contained in a tile
		if (character.x%Tile.TILE_SIZE != 0 || character.y%Tile.TILE_SIZE != 0)
			return false;
		
		int currentX = (int) character.x / Tile.TILE_SIZE;
		int currentY = (int) character.y / Tile.TILE_SIZE;
		if ((currentX == x || currentX == x+1 || currentX == x-1) && (currentY == y|| currentY == y+1 || currentY == y-1) && !(currentX == x && currentY == y))
			return true;
		
		return false;
	}


	public void pickupItems(Tile tile, int qt) {
		if (this.isNextTo(tile.getX(), tile.getY()))
		{
			carryingItems = tile.pickUpItems(qt);
			System.out.println("Someone just picked up "+carryingItems.getItemQt()+" "+carryingItems.getItem().getName());
		}
		
	}


	public void addItemsToRecipe(Build build) {
		System.out.println("Someone is adding "+carryingItems.getItemQt()+" "+carryingItems.getItem().getName()+" to a recipe");
		build.getCraftable().getRecipe().haulItems(carryingItems);;
		
	}
}
