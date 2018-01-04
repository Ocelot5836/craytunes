package com.ocelot.gaming.apps.game.crayquest.world;

import com.ocelot.gaming.apps.game.crayquest.world.tile.Tile;

public class WorldDebug extends World {

	public WorldDebug() {
		super(16, 16);
	}

	@Override
	protected void generateWorld() {
		int tileArraySize = 0;
		for (int i = 0; i < tiles.length; i++) {
			if (Tile.TILES[i] != null) {
				tiles[i] = Tile.TILES[i].getId();
				tileArraySize = i;
			} else {
				tiles[i] = Tile.TILES[tileArraySize].getId();
			}
		}
	}
}