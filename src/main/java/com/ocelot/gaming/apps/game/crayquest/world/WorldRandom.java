package com.ocelot.gaming.apps.game.crayquest.world;

import java.util.Map;

import com.ocelot.gaming.apps.game.crayquest.world.property.Prop;
import com.ocelot.gaming.apps.game.crayquest.world.property.PropBool;
import com.ocelot.gaming.apps.game.crayquest.world.tile.Tile;

/**
 * This class creates a world that generates a random world.
 * 
 * @author Ocelot5836
 */
public class WorldRandom extends World {

	/**
	 * This creates a new randomly generated world.
	 * 
	 * @param width
	 *            The width of the world
	 * @param height
	 *            The height of the world
	 */
	public WorldRandom(int width, int height) {
		super(width, height);
	}

	@Override
	protected void generateWorld() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int tileArraySize = 0;
				for (int i = 0; i < Tile.TILES.length; i++) {
					if (Tile.TILES[i] == null) {
						tileArraySize = i;
						break;
					}
				}

				tiles[x + y * width] = (short) (random.nextInt(tileArraySize));
			}
		}
	}

	@Override
	protected void setProperties() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Map<String, Prop> prop = getTile(x, y).getProperties();
				if (getTile(x, y) == Tile.REDSTONE_LAMP) {
					boolean bool = random.nextBoolean();
					((PropBool) properties.get(x + y * width)[0]).setProperty(bool);
				}
			}
		}
	}
}