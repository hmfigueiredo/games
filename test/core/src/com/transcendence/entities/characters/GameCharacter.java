package com.transcendence.entities.characters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.transcendence.entities.items.ItemStack;
import com.transcendence.entities.places.Tile;
import com.transcendence.game.GameWorld;
import com.transcendence.orders.Order;
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
	
	private ArrayList<Order> characterOrders;
	
	// Work abilities
	private int workAbility;
	
	// Character unique characteristics
	private String name;
	
		
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
		
		characterOrders = new ArrayList<Order>();
		name = "John Doe";
	}
	
	
	public int getX()
	{
		return (int)character.x/Tile.TILE_SIZE;
	}
	
	public int getY()
	{
		return (int)character.y/Tile.TILE_SIZE;
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
		this.followCurrentPath();
	}

	public void stopMoving() {
		this.currentPath = null;
		isMoving = false;
		character.x = Tile.TILE_SIZE*(Math.round(character.x/Tile.TILE_SIZE));
		character.y = Tile.TILE_SIZE*(Math.round(character.y/Tile.TILE_SIZE));
	}

	public void moveTo(GameWorld world, int x, int y) 
	{
		this.currentPath = world.calculatePath(this.getX(), this.getY(), x, y);
		this.followCurrentPath();
	}

	
	private void followCurrentPath()
	{	
		if (currentPath != null && currentPath.getCount() > 1)
		{
			// The next tile is the second in the graph - first is the origin
			Tile nextTile = currentPath.nodes.get(1);
			
			
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
				currentPath.nodes.removeIndex(1);
			}
					
			
			isMoving = true;
		}
		else
		{
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


	public void doWork(Workable work, GameWorld world) {
		if (this.isNextTo(work.getX(), work.getY()))
		{			
			long t = Calendar.getInstance().getTimeInMillis();
			isWorking = true;
			isMoving = false;
			
			// Only continue working if cooldown period is over
			if ((t-lastWorkTime) > DEFAULT_WORK_COOLDOWN)
			{
				work.doWork(workAbility, world, this);
				lastWorkTime = t;
			}
		}
		else if (!this.isMoving)
		{
			this.moveTo(world, work.getX(), work.getY());
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
			System.out.println(this.name+" just picked up "+carryingItems.getItemQt()+" "+carryingItems.getItem().getName());
		}
		else
		{
			System.out.println(this.name+" could not pick up items due to being too far away!");
		}
	}
	
	
	/**
	 * If character is not working, selects next available order
	 * @param orders
	 */
	public void checkOrders(ArrayList<Order> orders)
	{
		if (this.isBusy())
			return;
		
		// TODO: Follow priorities defined for the character
		Iterator<Order> iter = orders.iterator();
		while (iter.hasNext())
		{
			Order order = iter.next();
			if (!order.isBeingAddressed() && order.canBeCompleted())
			{
				this.characterOrders.add(order);
				order.setBeingAddressed(true);
			}
		}
	}
	
	
	/**
	 * Forces a character to work on a specific order (e.g. prioritized work)
	 * @param order
	 */
	public void forceOrder(Order order)
	{
		// Stop everything character is doing...
		this.stopWorking();
		this.stopMoving();
		this.characterOrders.clear();
		
		// ... and add the new order
		this.characterOrders.add(order);
	}
	
	
	/**
	 * Follows the next order in the list
	 */
	public void followOrders(GameWorld world)
	{
		if (!characterOrders.isEmpty())
		{
			Order nextOrder = characterOrders.get(0);
			nextOrder.setBeingAddressed(true);
			this.doWork(nextOrder, world);
			
			if (nextOrder.isCompleted())
			{
				characterOrders.remove(0);
			}
		}
	}


	public String getName() {
		return name;
	}


	public ItemStack getCarryingItems() {
		return carryingItems;
	}


	public ItemStack dropCarryingItems() {
		ItemStack returnStack = carryingItems;
		carryingItems = null;
		
		return returnStack;
	}


	public float getAbsoluteX() {
		return character.x;
	}
	
	public float getAbsoluteY() {
		return character.y;
	}
}
