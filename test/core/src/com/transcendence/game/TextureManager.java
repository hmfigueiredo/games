package com.transcendence.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class TextureManager {

	private static TextureAtlas atlas;
	
	/*
	 * Load textures from assets files into sprites. Will be converted to use the texture atlas
	 */
	private static void loadTextures()
	{
		Texture characterTexture = new Texture("character.png");
		Texture circleTexture = new Texture("circle.png");
		Texture buildingTexture = new Texture("blocks/bunker_1x1_bottom.png");
		Texture treeTexture = new Texture("blocks/tree.png");
		Texture plainTexture = new Texture("tiles/grass_tile.jpg");
		Texture rockTexture = new Texture("blocks/rock.png");
		Texture crossTexture = new Texture("actions/cross_small.png");
		Texture stoneStack = new Texture("blocks/stone_stack.png");
		Texture woodStack = new Texture("blocks/wood_stack.png");
		Texture gameActions = new Texture("ui/game_actions.png");
		Texture gameActionsClosed = new Texture("ui/game_actions_closed.png");
		Texture statusBar = new Texture("ui/status_bar.png");
		

		atlas = new TextureAtlas();
		atlas.addRegion("character", characterTexture, 0, 0, 32, 32);
		atlas.addRegion("circle", circleTexture, 0, 0, 32, 32);
		atlas.addRegion("building", buildingTexture, 0, 0, 92, 92);
		atlas.addRegion("tree", treeTexture, 0, 0, 68, 68);
		atlas.addRegion("plain", plainTexture, 0, 0, 32, 32);
		atlas.addRegion("rock", rockTexture, 0, 0, 59, 59);
		atlas.addRegion("cross", crossTexture, 0, 0, 32, 32);
		atlas.addRegion("stone_stack", stoneStack, 0, 0, 32, 32);
		atlas.addRegion("wood_stack", woodStack, 0, 0, 32, 32);
		
		atlas.addRegion("game_actions", gameActions, 0, 0, 300, 812);
		atlas.addRegion("game_actions_closed", gameActionsClosed, 0, 0, 39, 806);
		atlas.addRegion("status_bar", statusBar, 0, 0, 941, 70);
	}
	
	
	public static Sprite createSprite(String name, float width, float height)
	{
		if (atlas == null)
		{
			atlas = new TextureAtlas();
			loadTextures();
		}
		
		
		Sprite sprite = atlas.createSprite(name);
		sprite.setSize(width, height);

		return sprite;
	}
	
}
