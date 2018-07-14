package com.transcendence.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.transcendence.entities.places.Tile;

public class TranscendenceCamera extends OrthographicCamera {
	
	private int horizontalSize, verticalSize;
	
	public TranscendenceCamera(int w, int h)
	{
		super(1024, 768 * (Gdx.graphics.getHeight() / Gdx.graphics.getWidth()));
		
		this.viewportWidth = 30f;
		this.viewportHeight = 30f * Gdx.graphics.getHeight()/Gdx.graphics.getWidth();

		
		//this.zoom = (GameWorld.DEFAULT_HORIZONTAL_SIZE*Tile.TILE_SIZE)/this.viewportWidth;
		this.zoom = Tile.TILE_SIZE / 2;
		
		horizontalSize = w;
		verticalSize = h;
		
		adjustCamera();
	}

	public void zoom(float f) {
		this.zoom += f;
		adjustCamera();
	}

	
	@Override
	public void translate(float x, float y, float z)
	{
		super.translate(x, y, z);
		adjustCamera();
	}	
	
	
	/**
	 * Ensure we are still within the camera bounds
	 */
	private void adjustCamera()
	{
		// this.zoom = MathUtils.clamp(this.zoom, 10.0f, (GameWorld.DEFAULT_HORIZONTAL_SIZE*Tile.TILE_SIZE)/this.viewportWidth);
		this.zoom = MathUtils.clamp(this.zoom, Tile.TILE_SIZE/2, Math.min(horizontalSize, Tile.TILE_SIZE*2));
		
		float effectiveViewportWidth = this.viewportWidth * this.zoom;
		float effectiveViewportHeight = this.viewportHeight * this.zoom;

		this.position.x = MathUtils.clamp(this.position.x, effectiveViewportWidth / 2f, horizontalSize*Tile.TILE_SIZE - effectiveViewportWidth / 2f);
		this.position.y = MathUtils.clamp(this.position.y, effectiveViewportHeight / 2f, verticalSize*Tile.TILE_SIZE - effectiveViewportHeight / 2f);
	}

}
