package com.ocelot.gaming.apps.game.crayquest.world;

public class WorldSpawn extends World {

	public WorldSpawn() {
		super("/assets/maps/spawn.map");
	}

	@Override
	public void initializeWorld() {
		for (int i = 0; i < 1; i++) {
			// add(new Chaser(this, spawn.getTileX(), spawn.getTileY()));
		}

		// add(new EntityItem(this, Items.FLAME_SWORD, spawn.getTileX(), spawn.getTileY() + 32));
	}
}