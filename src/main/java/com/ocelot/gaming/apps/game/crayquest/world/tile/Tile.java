package com.ocelot.gaming.apps.game.crayquest.world.tile;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.ocelot.gaming.apps.game.crayquest.gfx.Screen;
import com.ocelot.gaming.apps.game.crayquest.gfx.Sprite;
import com.ocelot.gaming.apps.game.crayquest.world.property.Prop;
import com.ocelot.gaming.apps.game.crayquest.world.property.PropRotation;
import com.ocelot.gaming.utils.Utils;

import net.minecraft.nbt.NBTTagCompound;

/**
 * This class holds all the tiles and is the basis for all tiles in the game.
 * 
 * @author Ocelot5836
 */
public abstract class Tile {

	/** The array that holds all the tiles. */
	public static final Tile[] TILES = new Tile[256];
	public static final Random RANDOM = new Random();

	public static final int TILE_SIZE = 16;

	private static int nextId = 0;

	protected int x;
	protected int y;
	protected int id;
	protected Sprite sprite;
	private Map<String, Prop> properties;

	public static final Tile VOID = new BasicTile(Sprite.TILE_VOID).setSolid();
	public static final Tile MISSING = new BasicTile(Sprite.MISSING);

	public static final Tile GLOWSTONE = new BasicTile(Sprite.GLOWSTONE);
	public static final Tile REDSTONE_LAMP = new TileRedstoneLamp();
	public static final Tile NETHERRACK = new BasicTile(Sprite.NETHERRACK).setRandomRotation();
	public static final Tile CAGE = new BasicTile(Sprite.CAGE);

	/**
	 * Creates a new tile.
	 * 
	 * @param sprite
	 *            The sprite image for the tile
	 */
	public Tile(Sprite sprite) {
		id = nextId;
		if (TILES[nextId] != null)
			Utils.getLogger().severe("Duplicate tile id at [" + nextId + "]");
		this.sprite = sprite;
		this.properties = new HashMap<String, Prop>();
		TILES[nextId] = this;
		nextId++;
	}

	public abstract void update();

	public abstract void render(int x, int y, Screen screen);

	public abstract void load(NBTTagCompound nbt);

	public abstract void save(NBTTagCompound nbt);

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getId() {
		return id;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public boolean isSolid() {
		return false;
	}

	public boolean isBreakable() {
		return false;
	}

	public boolean allowTextureRotation() {
		return false;
	}

	public boolean emitsLight() {
		return false;
	}

	public double getLight() {
		return 0;
	}

	public void addProperty(String name, Prop property) {
		properties.put(name, property.setName(name));
	}
	
	public Prop getProperty(String name) {
		return properties.get(name);
	}
	
	public Map<String, Prop> getProperties() {
		return properties;
	}

	public static Tile getTile(int index) {
		if (Tile.TILES[index] == null)
			return MISSING;
		return Tile.TILES[index];
	}

	public void setProperties(Prop[] properties) {
		this.properties.clear();
		for (int i = 0; i < properties.length; i++) {
			this.properties.put(properties[i].getName(), properties[i]);
		}
	}
}