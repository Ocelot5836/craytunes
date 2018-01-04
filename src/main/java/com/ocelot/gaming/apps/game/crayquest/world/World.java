package com.ocelot.gaming.apps.game.crayquest.world;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ocelot.gaming.apps.game.crayquest.entity.Entity;
import com.ocelot.gaming.apps.game.crayquest.entity.Particle;
import com.ocelot.gaming.apps.game.crayquest.entity.Player;
import com.ocelot.gaming.apps.game.crayquest.gfx.Screen;
import com.ocelot.gaming.apps.game.crayquest.world.property.Prop;
import com.ocelot.gaming.apps.game.crayquest.world.tile.Tile;
import com.ocelot.gaming.apps.game.crayquest.world.tile.TileCoord;
import com.ocelot.gaming.utils.Utils;

import net.minecraft.nbt.NBTTagCompound;

/**
 * This class is a world in the game. Entities, tiles, etc are added here where this class renders everything.
 * 
 * @author Ocelot5836
 */
public class World {

	/** The width of the world */
	protected int width;
	/** The height of the world */
	protected int height;
	/** The tile ids in the world */
	protected int[] tiles;
	/** The rotations for each tile if it is enabled */
	protected byte[] rotations;
	/** The properties for each tile */
	protected List<Prop[]> properties = new ArrayList<Prop[]>();

	/** The spawn location in the world */
	protected TileCoord spawn = new TileCoord();

	/** The array of entities in the world */
	public List<Entity> entities = new ArrayList<Entity>();
	/** The array of entities to be removed next tick */
	public List<Entity> removedEntities = new ArrayList<Entity>();
	/** The array of players in the world */
	public List<Player> players = new ArrayList<Player>();
	/** The array of particles in the world */
	public List<Particle> particles = new ArrayList<Particle>();

	/** A random number generator */
	protected Random random = new Random();

	/**
	 * This creates a new randomly generated world.
	 * 
	 * @param width
	 *            The width of the world
	 * @param height
	 *            The height of the world
	 */
	public World(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new int[width * height];
		rotations = new byte[tiles.length];
		generateWorld();
		initializeWorld();
		initProperties();
		setProperties();
	}

	/**
	 * initializes all the properties.
	 */
	protected final void initProperties() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Tile tile = getTile(y, x);
				List<Prop> classes = new ArrayList<Prop>();
				List<String> names = new ArrayList<String>();
				for (String s : tile.getProperties().keySet()) {
					try {
						classes.add(tile.getProperties().get(s).getClass().newInstance());
						names.add(tile.getProperties().get(s).getName());
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				Prop[] property = new Prop[classes.size()];
				for (int i = 0; i < property.length; i++) {
					property[i] = classes.get(i);
					property[i].setName(names.get(i));
				}
				properties.add(property);
			}
		}
	}

	/**
	 * The last minute chance to set properties.
	 */
	protected void setProperties() {
	}

	/**
	 * Initializes the world to add any things that need to be done after the world is built.
	 */
	public void initializeWorld() {
	}

	/**
	 * This loads a level from a file.
	 * 
	 * @param path
	 *            The path to the level file
	 */
	public World(String path) {
		loadLevel(path);
		initializeWorld();
		initProperties();
		setProperties();
	}

	/**
	 * Generates the world.
	 */
	protected void generateWorld() {
	}

	/**
	 * Loads a level form file and stores it in memory.
	 * 
	 * @param path
	 */
	protected void loadLevel(String path) {
		try {
			InputStream in = getClass().getResourceAsStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			width = Integer.parseInt(br.readLine());
			height = Integer.parseInt(br.readLine());
			tiles = new int[width * height];
			rotations = new byte[tiles.length];
			populateProperties();

			String delims = "\\s+";
			for (int y = 0; y < height; y++) {
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for (int x = 0; x < width; x++) {
					tiles[x + y * width] = Integer.parseInt(tokens[x]);
				}
			}

			br.readLine();

			String spawnx = br.readLine();
			String spawny = br.readLine();

			if (spawnx != null)
				spawn.setX(Integer.parseInt(spawnx));
			if (spawny != null)
				spawn.setY(Integer.parseInt(spawny));

			br.close();
		} catch (Exception e) {
			Utils.getLogger().severe("Could not load map [" + path + "]!");
			e.printStackTrace(System.err);
		}
	}

	private void populateProperties() {

	}

	/** Updates the world. */
	public void update() {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.update();
			if (e.isDead())
				removedEntities.add(e);
		}

		for (int i = 0; i < players.size(); i++) {
			Player p = players.get(i);
			p.update();
			if (p.isDead())
				removedEntities.add(p);
		}

		// for (int i = 0; i < particles.size(); i++) {
		// Particle p = particles.get(i);
		// p.update();
		// if (p.isDead())
		// removedEntities.add(p);
		// }

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				getTile(x, y).update();
			}
		}

		for (Entity e : removedEntities) {
			remove(e);
		}
	}

	/** Renders the world and entities inside of it. */
	public void render(int xScroll, int yScroll, Screen screen) {
		screen.setOffset(xScroll, yScroll);
		int x0 = xScroll >> 4;
		int x1 = (xScroll + screen.width) >> 4;
		int y0 = yScroll >> 4;
		int y1 = (yScroll + screen.height) >> 4;

		for (int y = y0; y < y1 + 1; y++) {
			for (int x = x0; x < x1 + 1; x++) {
				if (!(x < 0 || x >= width || y < 0 || y >= height)) {
					getTile(x, y).setProperties(properties.get(x + y * width));
				}
				getTile(x, y).render(x, y, screen);
			}
		}

		for (Entity e : entities) {
			e.render(screen);
		}

		for (Player p : players) {
			p.render(screen);
		}

		// for (Particle p : particles) {
		// p.render(screen);
		// }
	}

	/** Updates the time internally. */
	protected void time() {
	}

	/**
	 * Checks if an entity has collided with a tile.
	 */
	public boolean tileCollision(int x, int y, int xOffset, int yOffset, int width, int height) {
		for (int c = 0; c < 4; c++) {
			int xt = (x - c % 2 * width + xOffset) >> 4;
			int yt = (y - c / 2 * height + yOffset) >> 4;
			if (getTile(xt, yt).isSolid())
				return true;
		}
		return false;
	}

	/**
	 * @return The array of players in the world.
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * @return The player on the client side.
	 */
	public Player getClientPlayer() {
		return players.get(0);
	}

	public void load(NBTTagCompound nbt) {

	}

	public void save(NBTTagCompound nbt) {

	}

	/**
	 * Returns the tile from he specified position
	 * 
	 * @param x
	 *            The x position to check
	 * @param y
	 *            The y position to check
	 * @return The tile found
	 */
	public Tile getTile(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return Tile.VOID;

		Tile tile = Tile.TILES[tiles[x + y * width]];

		if (tile != null) {
			return tile;
		}

		return Tile.VOID;
	}

	/**
	 * Adds a new entity to the world.
	 * 
	 * @param entity
	 *            The entity to add
	 */
	public void add(Entity entity) {
		if (entity instanceof Player) {
			Player player = (Player) entity;
			player.x = spawn.getTileX();
			player.y = spawn.getTileY();
			players.add(player);
			return;
		}
		// else if (entity instanceof Particle) {
		// particles.add((Particle) entity);
		// }
		else {
			entities.add(entity);
		}
	}

	/**
	 * Removes an enity form the world.
	 * 
	 * @param entity
	 *            The entity to remove
	 */
	public void remove(Entity entity) {
		if (entity instanceof Player) {
			players.remove(entity);
		}
		// else if (entity instanceof Particle) {
		// particles.remove(entity);
		// }
		else {
			entities.remove(entity);
		}
	}

	/**
	 * @return The width of the world
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return The height of the world
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the spawn location to the specified {@link TileCoord}.
	 * 
	 * @param spawn
	 *            The location to set it to
	 */
	public World setSpawnLocation(TileCoord spawn) {
		this.spawn = spawn;
		return this;
	}
}