package com.transcendence.game;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.transcendence.entities.blocks.Block;
import com.transcendence.entities.craftables.Recipe;
import com.transcendence.entities.items.ItemStack;
import com.transcendence.entities.places.ManhattanDistanceHeuristic;
import com.transcendence.entities.places.Tile;
import com.transcendence.orders.Build;
import com.transcendence.orders.Haul;
import com.transcendence.orders.Order;

public class GameWorld implements IndexedGraph<Tile> {

	public static int MIN_HORIZONTAL_SIZE = 16;
	public static int MIN_VERTICAL_SIZE = 16;
	
	public static int DEFAULT_HORIZONTAL_SIZE = 40;
	public static int DEFAULT_VERTICAL_SIZE = 40;
	
	public static int MAX_HORIZONTAL_SIZE = 200;
	public static int MAX_VERTICAL_SIZE = 200;
	
	public static int MAX_FIND_RADIUS = 50;

	private Tile[][] world;
	private ManhattanDistanceHeuristic mHeuristic;
	private IndexedAStarPathFinder<Tile> mPathFinder;

	
	// Support for pathfinder
	private int nodeCount;
	
	
	public GameWorld()
	{
		this(DEFAULT_HORIZONTAL_SIZE, DEFAULT_VERTICAL_SIZE);
	}
	
	public GameWorld(int w, int h)
	{
		// Prepare game world
		generateWorld(MathUtils.clamp(w, MIN_HORIZONTAL_SIZE, MAX_HORIZONTAL_SIZE), MathUtils.clamp(h, MIN_VERTICAL_SIZE, MAX_VERTICAL_SIZE));
        mHeuristic = new ManhattanDistanceHeuristic();
		mPathFinder = new IndexedAStarPathFinder<Tile>(this, true);
	}

	public int getHorizontalSize()
	{
		if (world != null)
			return world.length;
		else
			return 0;
	}
	
	public int getVerticalSize()
	{
		if (world != null && world[0] != null)
			return world[0].length;
		else
			return 0;
	}
	
	/**
	 * 
	 * Add nObstacles number of obstacles to the world in random positions - trees for now
	 * @param nObstacles - number of obstacles to add randomly to the world
	 */
	public void addObstacles(int nObstacles, Block.BLOCK_TYPES blockType)
	{
		for (int i=0; i<nObstacles; i++)
		{
			Tile t = world[(int)(Math.random()*(this.getHorizontalSize()-1))][(int)(Math.random()*(this.getVerticalSize()-1))];
			
			if (t.getBlock() == null)
			{
				t.setTraversable(false);
				t.setBlock(Block.createBlock(blockType, t.getX(), t.getY()), true);
				System.out.println("Adding obstacle at "+t);
			}
		}
		
		recomputeNeighbors();
	}

	public void recomputeNeighbors() {
		// Add connection to every neighbor of this node. 
        for (int x = 0; x < world.length; x++) {
            for (int y = 0; y < world[0].length; y++) {
                if (null != world[x][y]) {
                	world[x][y].clearNeighbours();
                	
                    addNodeNeighbour(world[x][y], x - 1, y); // Node to left
                    addNodeNeighbour(world[x][y], x + 1, y); // Node to right
                    addNodeNeighbour(world[x][y], x, y - 1); // Node below
                    addNodeNeighbour(world[x][y], x, y + 1); // Node above
                    // Add diagonals
                    addNodeNeighbour(world[x][y], x - 1, y - 1); 
                    addNodeNeighbour(world[x][y], x - 1, y + 1); 
                    addNodeNeighbour(world[x][y], x + 1, y - 1); 
                    addNodeNeighbour(world[x][y], x + 1, y + 1);
                }
            }
        }
		
	}

	private void addNodeNeighbour(Tile tile, int aX, int aY) {
		// Make sure that we are within our array bounds. 
        if (aX >= 0 && aX < world.length && aY >=0 && aY < world[0].length) 
        {
            tile.addNeighbour(world[aX][aY]);
        }
	}

	@Override
	public Array<Connection<Tile>> getConnections(Tile fromNode) {
		return fromNode.getConnections();
	}

	@Override
	public int getIndex(Tile node) {
		return node.getIndex();
	}

	@Override
	public int getNodeCount() {
		return nodeCount;
	}
	
	public Tile getTile(int x, int y) 
	{
		return world[x][y];
	}

	public void addBlock(Block block, int x, int y) {
		
		for (int i=x; i<x+Math.round(block.getRectangle().width / Tile.TILE_SIZE); i++)
		{
			for (int j=y; j<y+Math.round(block.getRectangle().height / Tile.TILE_SIZE); j++)
			{
				world[i][j].setTraversable(false);
				world[i][j].setBlock(block, i==x && j==y);
				world[i][j].setBlueprint(null);
			}
		}
			
		// TODO: recompute only neighbors of added block
		recomputeNeighbors();
		
	}
	
	
	private void generateWorld(int w, int h) {
		world = new Tile[w][h];
		
		int index = 0;
		
		for (int i=0; i<this.getHorizontalSize(); i++)
		{
			for (int j=0; j<this.getVerticalSize(); j++)
			{
				world[i][j] = new Tile(TextureManager.createSprite("plain", Tile.TILE_SIZE, Tile.TILE_SIZE), i, j,index++);
			}
		}
		
		
		nodeCount = index;
		recomputeNeighbors();
	}

	

	
	
	public void render(SpriteBatch batch)
	{
		// Render base layer
		for (int w=0; w<this.getHorizontalSize(); w++)
		{
			for (int h=0; h<this.getVerticalSize(); h++)
			{
				world[w][h].render(batch);
				if (world[w][h].isSelected())
				{
					Sprite s = TextureManager.createSprite("circle", Tile.TILE_SIZE, Tile.TILE_SIZE);
					s.setPosition(w*Tile.TILE_SIZE, h*Tile.TILE_SIZE);
					s.draw(batch);
				}
			}
		}
		
		// Render blocks and Items
		for (int w=0; w<this.getHorizontalSize(); w++)
		{
			for (int h=0; h<this.getVerticalSize(); h++)
			{
				world[w][h].renderBlocksAndItems(batch);
			}
		}
	}

	public void unselectAll() {
		for (int w=0; w<this.getHorizontalSize(); w++)
		{
			for (int h=0; h<this.getVerticalSize(); h++)
			{
				world[w][h].setSelected(false);
			}
		}
	}

	
	/**
	 * Dump items around position (x, y)
	 * @param items
	 * @param x
	 * @param y
	 */
	public void dumpItems(Recipe items, int x, int y) {
		Iterator<ItemStack> iter  = items.getItems().iterator();
		
		while (iter.hasNext())
		{
			ItemStack theItem = iter.next();
			
			Tile tileForStack = findFreePlace(x, y);
			if (tileForStack != null)
			{
				tileForStack.addItems(theItem);
			}
		}
		
	}
	
	/**
	 * Finds a free spot (no block or itemstack) in an increasing radius around (x, y)
	 * @param x
	 * @param y
	 * @return
	 */
	private Tile findFreePlace(int x, int y)
	{		
		int radius = 0;
		while (radius < MAX_FIND_RADIUS)
		{
			for (int i=x-radius; i<=x+radius; i++)
			{
				for (int j=y-radius; j<=y+radius; j++)
				{
					if (i>=0 && j>=0 && world[i][j] != null)
					{
						if (world[i][j].getBlock() == null && world[i][j].getItems() == null)
						{
							return world[i][j];
						}
					}
				}
			}
				
			radius++;
		}
		
		return null;
	}

	public void addBlueprint(Build build) 
	{
		for (int i=build.getX(); i<build.getX()+Math.round(build.getRectangle().width / Tile.TILE_SIZE); i++)
		{
			for (int j=build.getY(); j<build.getY()+Math.round(build.getRectangle().height / Tile.TILE_SIZE); j++)
			{
				world[i][j].setBlueprint(build);
			}
		}
		
	}
	
	public boolean canGetBlueprint(Build build)
	{
		for (int i=build.getX(); i<build.getX()+Math.round(build.getRectangle().width / Tile.TILE_SIZE); i++)
		{
			for (int j=build.getY(); j<build.getY()+Math.round(build.getRectangle().height / Tile.TILE_SIZE); j++)
			{
				if (!world[i][j].isEmpty())
					return false;
			}
		}
		
		return true;
	}

	public void findMaterialsToHaul(ArrayList<Order> orders) 
	{
		Iterator<Order> iter = orders.iterator();
		ArrayList<Order> ordersToAdd = new ArrayList<Order>();
		
		while (iter.hasNext())
		{
			Order order = iter.next();
			
			if (order instanceof Build)
			{
				if (!((Build)order).canBeCompleted() && !order.isBeingAddressed())
				{
					Recipe rec = ((Build)order).getCraftable().getRecipe();
					
					if (!rec.isComplete())
					{
						Iterator<ItemStack> istacks = rec.getItems().iterator();
						
						while (istacks.hasNext())
						{
							ItemStack istack = istacks.next();
							if (rec.getHauledQuantity(istack) != istack.getItemQt())
							{
								Tile t = findTileWithMaterials(istack, orders);
								if (t != null)
								{
									System.out.println("A blueprint needs "+istack.getItemQt()+" "+istack.getItem().getName());
									Haul haul = new Haul(new ItemStack(istack), t.getX(), t.getY(), order.getX(), order.getY());
									ordersToAdd.add(haul);
									order.setBeingAddressed(true);
									t.setBeingHauled(true);
								}
							}
						}
					}
				}
			}
		}
		
		orders.addAll(ordersToAdd);
	}

	
	// TODO: change algorithm to find nearest, highest priority item stack
	private Tile findTileWithMaterials(ItemStack istack, ArrayList<Order> orders) {
		for (int w=0; w<this.getHorizontalSize(); w++)
		{
			for (int h=0; h<this.getVerticalSize(); h++)
			{
				if (world[w][h] != null && world[w][h].getItems() != null && world[w][h].getItems().getItem().equals(istack.getItem()))
				{
					if (!world[w][h].isBeingHauled())
						return world[w][h];
				}
			}
		}
		
		return null;
	}

	
	
	public DefaultGraphPath<Tile> calculatePath(int fromX, int fromY, int toX, int toY) {
        Tile startNode = this.getTile(fromX, fromY);
        Tile endNode = this.getTile(toX, toY); 

        
        System.out.println("Trying to calculate path from "+startNode+" to "+endNode);
        
		DefaultGraphPath<Tile> mPath;
		mPath = new DefaultGraphPath<Tile>();

        mPath.clear();

        mPathFinder.searchNodePath(startNode, endNode, mHeuristic, mPath);

        if (mPath.nodes.size == 0) {
        	// if no path is found, try to find a reachable neighbor
            Iterator<Connection<Tile>> iter = endNode.getConnections().iterator();
            while (iter.hasNext())
            {
              	Tile nextNode = iter.next().getToNode();
              	System.out.println("Trying to find accessible neighbor at "+nextNode.toString());
              	mPath.clear();
            	mPathFinder.searchNodePath(startNode, nextNode, mHeuristic, mPath);
            	if (mPath.nodes.size !=0)
            		return mPath;
            }
        	System.out.println("No path found");
        } else {
            // System.out.println("-----Found path-----");
        }
        // Loop throw every node in the solution and select it.
        for (Tile node : mPath.nodes) {
            System.out.println(node);
        }
        
        return mPath;
    }
	
}
