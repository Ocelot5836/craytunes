package com.ocelot.gaming.apps.game.crayquest.gfx;

import java.awt.image.BufferedImage;

import com.ocelot.gaming.Reference;
import com.ocelot.gaming.utils.LoadingUtils;

/**
 * This class handles a sprite sheet that is going to render sprites in the game.
 * 
 * @author Ocelot5836
 */
public class SpriteSheet {

	private String path;

	/** The width of the sheet */
	public int width;
	/** The height of the sheet */
	public int height;
	/** The pixels to be rendered to the screen */
	public int[] pixels;

	public static final SpriteSheet ITEMS = new SpriteSheet("items");
	public static final SpriteSheet BLOCKS = new SpriteSheet("blocks");
	public static final SpriteSheet SPRITE_SHEET = new SpriteSheet("spritesheet");

	private Sprite[] sprites;

	public SpriteSheet(SpriteSheet sheet, int x, int y, int width, int height, int spriteSize) {
		int xx = x * spriteSize;
		int yy = y * spriteSize;
		this.width = width * spriteSize;
		this.height = height * spriteSize;

		pixels = new int[this.width * this.height];
		for (int ya = 0; ya < this.height; ya++) {
			int yp = yy + ya;
			for (int xa = 0; xa < this.width; xa++) {
				int xp = xx + xa;
				pixels[xa + ya * this.width] = sheet.pixels[xp + yp * sheet.width];
			}
		}

		int frame = 0;
		sprites = new Sprite[width * height];
		for (int ya = 0; ya < height; ya++) {
			for (int xa = 0; xa < width; xa++) {
				int[] spritePixels = new int[spriteSize * spriteSize];
				for (int y0 = 0; y0 < spriteSize; y0++) {
					for (int x0 = 0; x0 < spriteSize; x0++) {
						spritePixels[x0 + y0 * spriteSize] = pixels[(x0 + xa * spriteSize) + (y0 + ya * spriteSize) * this.width];
					}
				}
				Sprite sprite = new Sprite(spritePixels, spriteSize, spriteSize);
				sprites[frame++] = sprite;
			}
		}
	}

	public SpriteSheet(String name, int width, int height) {
		this.path = "/assets/" + Reference.MOD_ID + "/game/crayquest/textures/" + name + ".png";
		this.width = width;
		this.height = height;
		pixels = new int[width * height];

		BufferedImage image = LoadingUtils.loadImage(path);
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
	}

	/**
	 * This creates a new sprite sheet that can be used to render sprites.
	 * 
	 * @param path
	 *            The location of the image inside the res folder
	 */
	public SpriteSheet(String name) {
		this.path = "/assets/" + Reference.MOD_ID + "/game/crayquest/textures/" + name + ".png";

		BufferedImage image = LoadingUtils.loadImage(path);
		width = image.getWidth();
		height = image.getHeight();
		pixels = new int[width * height];
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
	}

	/**
	 * @return The image path for the sprite sheet
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return The sprites loaded from the sheets
	 */
	public Sprite[] getSprite() {
		return sprites;
	}
}