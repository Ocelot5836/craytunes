package com.ocelot.gaming.apps.game.crayquest.world.tile;

public class TileCoord {

	private int x;
	private int y;

	public TileCoord() {
		this(0, 0);
	}

	public TileCoord(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getTileX() {
		return x * Tile.TILE_SIZE;
	}

	public int getTileY() {
		return y * Tile.TILE_SIZE;
	}

	public int[] getPosition() {
		return new int[] { getX(), getY() };
	}

	public int[] getTilePosition() {
		return new int[] { getTileX(), getTileY() };
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
}