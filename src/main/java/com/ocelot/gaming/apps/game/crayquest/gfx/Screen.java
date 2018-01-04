package com.ocelot.gaming.apps.game.crayquest.gfx;

/**
 * This class represents the screen. It renders stuff to the pixels array and sets the color in the image to that color.
 * 
 * @author Ocelot5836
 *
 */
public class Screen {

	public int width;
	public int height;

	public int[] pixels;

	public static final int MAP_SIZE = 64;
	public static final int MAP_SIZE_MASK = MAP_SIZE - 1;

	public static final byte BIT_MIRROR_X = 0x01;
	public static final byte BIT_MIRROR_Y = 0x02;

	public int xOffset;
	public int yOffset;

	/**
	 * Creates a new screen object that can render images to the screen.
	 * 
	 * @param width
	 *            The width of the screen
	 * @param height
	 *            The height of the screen
	 */
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
	}

	/**
	 * Clears the pixels on the screen so they don't drag.
	 */
	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}

	/**
	 * Renders a sprite sheet to the screen.
	 * 
	 * @param xPosition
	 *            The x position of the sheet
	 * @param yPosition
	 *            The y position of the sheet
	 * @param sheet
	 *            The sheet to render
	 * @param fixed
	 *            Whether or not the sheet should move with the world
	 * @param mirrorDir
	 *            The mirroring of the sheet
	 */
	public void renderSheet(int xPosition, int yPosition, SpriteSheet sheet, boolean fixed, int mirrorDir) {
		if (fixed) {
			xPosition -= xOffset;
			yPosition -= yOffset;
		}

		boolean mirrorX = (mirrorDir & BIT_MIRROR_X) > 0;
		boolean mirrorY = (mirrorDir & BIT_MIRROR_Y) > 0;

		for (int y = 0; y < sheet.height; y++) {
			int ya = y + yPosition;
			int ys = y;
			if (mirrorY)
				ys = sheet.height - 1 - y;

			for (int x = 0; x < sheet.width; x++) {
				int xa = x + xPosition;
				int xs = x;
				if (mirrorX)
					xs = sheet.width - 1 - x;

				if (xa < 0 || xa >= width || ya < 0 || ya >= height)
					continue;

				pixels[xa + ya * width] = sheet.pixels[xs + ys * sheet.width];
			}
		}
	}

	/**
	 * Renders a sprite to the screen.
	 * 
	 * @param xPosition
	 *            The x position to render at
	 * @param yPosition
	 *            The y position to render at
	 * @param sprite
	 *            The sprite to render
	 * @param mirrorDir
	 *            The mirroring of the sheet
	 */
	public void render(int xPosition, int yPosition, Sprite sprite, int mirrorDir) {
		render(xPosition, yPosition, sprite, true, mirrorDir);
	}

	/**
	 * Renders a sprite to the screen.
	 * 
	 * @param xPosition
	 *            The x position to render at
	 * @param yPosition
	 *            The y position to render at
	 * @param sprite
	 *            The sprite to render
	 * @param fixed
	 *            Whether or not the sheet should move with the world
	 * @param mirrorDir
	 *            The mirroring of the sheet
	 */
	public void render(int xPosition, int yPosition, Sprite sprite, boolean fixed, int mirrorDir) {
		if (fixed) {
			xPosition -= xOffset;
			yPosition -= yOffset;
		}

		boolean mirrorX = (mirrorDir & BIT_MIRROR_X) > 0;
		boolean mirrorY = (mirrorDir & BIT_MIRROR_Y) > 0;

		for (int y = 0; y < sprite.getHeight(); y++) {
			int ya = y + yPosition;
			int ys = y;
			if (mirrorY)
				ys = sprite.getHeight() - 1 - y;

			for (int x = 0; x < sprite.getWidth(); x++) {
				int xa = x + xPosition;
				int xs = x;
				if (mirrorX)
					xs = sprite.getWidth() - 1 - x;

				if (xa < 0 || xa >= width || ya < 0 || ya >= height)
					continue;

				int color = sprite.pixels[xs + ys * sprite.getWidth()];
				if (color != 0xffff00ff && color != 0xff7F007F) {
					pixels[xa + ya * width] = color;
				}
			}
		}
	}

	/**
	 * Sets the offset for the work to the specified offsets.
	 * 
	 * @param xOffset
	 *            The x offset to set to
	 * @param yOffset
	 *            The t offset to set to
	 */
	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	/**
	 * Renders a rectangle to the screen.
	 * 
	 * @param drawX
	 *            The x of the rectangle
	 * @param drawY
	 *            The y of the rectangle
	 * @param rectWidth
	 *            The width of the rectangle
	 * @param rectHeight
	 *            The height of the rectangle
	 * @param color
	 *            The color of the rectangle
	 */
	public void drawRect(int drawX, int drawY, int rectWidth, int rectHeight, int color) {
		for (int y = drawY; y < rectHeight; y++) {
			for (int x = drawX; x < rectWidth; x++) {
				if (y < 0 || y >= height)
					return;
				if (x < 0 || x >= width)
					return;

				pixels[x + y * width] = color;
			}
		}
	}
}