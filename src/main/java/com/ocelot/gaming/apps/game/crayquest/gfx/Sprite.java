package com.ocelot.gaming.apps.game.crayquest.gfx;

import java.awt.image.BufferedImage;

import com.ocelot.gaming.utils.LoadingUtils;

/**
 * This class represents an image in the game that can be rendered.
 * 
 * @author Ocelot5836
 */
public class Sprite {

	protected final int size;
	protected int x, y;
	protected int width, height;
	public int[] pixels;
	protected SpriteSheet sheet;

	public static final Sprite PLAYER_UP = new Sprite(32, 0, 0, SpriteSheet.SPRITE_SHEET);
	public static final Sprite PLAYER_SIDE = new Sprite(32, 1, 0, SpriteSheet.SPRITE_SHEET);
	public static final Sprite PLAYER_DOWN = new Sprite(32, 2, 0, SpriteSheet.SPRITE_SHEET);

	public static final Sprite TILE_VOID = new Sprite(16, 0x1B87E0);
	public static final Sprite MISSING = new Sprite(16, LoadingUtils.MISSING_IMAGE);
	
	public static final Sprite GLOWSTONE = new Sprite(16, 13, 0, SpriteSheet.BLOCKS);
	public static final Sprite REDSTONE_LAMP_LIT = new Sprite(16, 14, 0, SpriteSheet.BLOCKS);
	public static final Sprite NETHERRACK = new Sprite(16, 15, 0, SpriteSheet.BLOCKS);
	public static final Sprite CAGE = new Sprite(16, 0, 1, SpriteSheet.BLOCKS);
	public static final Sprite REDSTONE_LAMP = new Sprite(16, 14, 1, SpriteSheet.BLOCKS);

	protected Sprite(SpriteSheet sheet, int width, int height) {
		this.size = width == height ? width : -1;
		this.sheet = sheet;
		this.width = width;
		this.height = height;
	}

	/**
	 * Creates a new sprite with a custom size.
	 * 
	 * @param x
	 *            The x of the sprite
	 * @param y
	 *            The y of the sprite
	 * @param size
	 *            The size of the sprite
	 * @param sheet
	 *            The sheet the sprite is located
	 */
	public Sprite(int size, int x, int y, SpriteSheet sheet) {
		this.size = size;
		this.width = this.height = size;
		this.x = x * size;
		this.y = y * size;
		this.sheet = sheet;
		this.pixels = new int[size * size];
		load();
	}

	/**
	 * Creates a new sprite with a custom width, height, and color.
	 * 
	 * @param width
	 *            The width of the sprite
	 * @param height
	 *            The height of the sprite
	 * @param color
	 *            The color of the sprite
	 */
	public Sprite(int width, int height, int color) {
		this.size = width == height ? width : -1;
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
		setColor(color);
	}

	/**
	 * Creates a new sprite with a color.
	 * 
	 * @param size
	 *            The size of the sprite
	 * @param sheet
	 *            The sheet the sprite is located
	 */
	public Sprite(int size, int color) {
		this.size = size;
		this.width = this.height = size;
		this.pixels = new int[size * size];
		setColor(color);
	}

	/**
	 * Creates a new sprite with a buffered image.
	 * 
	 * @param size
	 *            The size of the sprite
	 * @param image
	 *            The image of the sprite
	 */
	public Sprite(int size, BufferedImage image) {
		this.size = size;
		this.width = this.height = size;
		this.pixels = new int[size * size];
		setPixels(image);
	}

	public Sprite(int[] pixels, int width, int height) {
		this.size = width == height ? width : -1;
		this.pixels = pixels;
		this.width = width;
		this.height = height;
	}

	/**
	 * Loads a sprite form a sprite sheet.
	 */
	private void load() {
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				pixels[x + y * size] = sheet.pixels[(x + this.x) + (y + this.y) * sheet.width];
			}
		}
	}

	/**
	 * @return The size of the sprite
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @return The width of the sprite
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return The height of the sprite
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the color of the sprite to the specified color.
	 * 
	 * @param color
	 *            The color to set it to
	 */
	private void setColor(int color) {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = color;
		}
	}

	/**
	 * Sets the pixels to a buffered image.
	 * 
	 * @param image
	 *            The image to get the colors from
	 */
	private void setPixels(BufferedImage image) {
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				pixels[x + y * size] = image.getRGB(x, y);
			}
		}
	}
}