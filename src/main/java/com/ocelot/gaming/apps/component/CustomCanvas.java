package com.ocelot.gaming.apps.component;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.core.Laptop;
import com.mrcrayfish.device.object.Canvas;
import com.mrcrayfish.device.util.GuiHelper;
import com.ocelot.gaming.apps.component.tool.CustomTool;
import com.ocelot.gaming.apps.component.tool.ToolCustomBucket;
import com.ocelot.gaming.apps.component.tool.ToolCustomEraser;
import com.ocelot.gaming.apps.component.tool.ToolCustomEyeDropper;
import com.ocelot.gaming.apps.component.tool.ToolCustomPencil;

import net.minecraft.client.Minecraft;

public class CustomCanvas extends Canvas {

	private CustomTool currentTool;
	public static final CustomTool PENCIL = new ToolCustomPencil();
	public static final CustomTool BUCKET = new ToolCustomBucket();
	public static final CustomTool ERASER = new ToolCustomEraser();
	public static final CustomTool EYE_DROPPER = new ToolCustomEyeDropper();

	public int[] pixels;
	private int red, green, blue;
	private int currentColour = Color.BLACK.getRGB();

	private int width;
	private int height;

	private boolean drawing = false;
	private boolean showGrid = false;
	private boolean existingImage = false;

	public CustomPicture picture;

	public CustomCanvas(int left, int top, int width, int height) {
		super(left, top);
		this.width = width;
		this.height = height;
		this.currentTool = PENCIL;
	}

	public void createPicture(String name, String author, int width, int height) {
		if (width > this.width)
			width = this.width;
		if (height > this.height)
			height = this.height;

		this.existingImage = false;
		this.picture = new CustomPicture(name, author, width, height);
		this.pixels = ((DataBufferInt) picture.copyImage().getData().getDataBuffer()).getData();
	}

	public void setPicture(CustomPicture picture) {
		this.existingImage = true;
		this.picture = picture;
		this.pixels = ((DataBufferInt) picture.copyImage().getData().getDataBuffer()).getData();
	}

	@Override
	public void init(Layout layout) {
	}

	@Override
	public void render(Laptop laptop, Minecraft mc, int x, int y, int mouseX, int mouseY, boolean windowActive, float partialTicks) {
		// drawRect(xPosition, yPosition, xPosition + picture.getWidth() * picture.getPixelWidth() + 2, yPosition + picture.getHeight() * picture.getPixelHeight() + 2, Color.DARK_GRAY.getRGB());
		// drawRect(xPosition + 1, yPosition + 1, xPosition + picture.getWidth() * picture.getPixelWidth() + 1, yPosition + picture.getHeight() * picture.getPixelHeight() + 1, Color.WHITE.getRGB());
		// for (int i = 0; i < picture.getHeight(); i++) {
		// for (int j = 0; j < picture.getWidth(); j++) {
		// int pixelX = xPosition + j * picture.getPixelWidth() + 1;
		// int pixelY = yPosition + i * picture.getPixelHeight() + 1;
		// drawRect(pixelX, pixelY, pixelX + picture.getPixelWidth(), pixelY + picture.getPixelHeight(), pixels[j + i * picture.size.width]);
		// if (showGrid) {
		// drawRect(pixelX, pixelY, pixelX + picture.getPixelWidth(), pixelY + 1, gridColour);
		// drawRect(pixelX, pixelY, pixelX + 1, pixelY + picture.getPixelHeight(), gridColour);
		// }
		// }
		// }
		if (picture != null)
			picture.render(x, y, width, height, showGrid);
	}

	@Override
	public void handleMouseClick(int mouseX, int mouseY, int mouseButton) {
		int startX = xPosition + 1;
		int startY = yPosition + 1;
		int endX = startX + picture.getWidth() * picture.getPixelWidth() - 1;
		int endY = startY + picture.getHeight() * picture.getPixelHeight() - 1;
		if (GuiHelper.isMouseInside(mouseX, mouseY, startX, startY, endX, endY)) {
			this.drawing = true;
			int pixelX = (mouseX - startX) / picture.getPixelWidth();
			int pixelY = (mouseY - startY) / picture.getPixelHeight();
			System.out.println(pixelX);
			this.currentTool.handleClick(this, pixelX, pixelY);
		}
	}

	@Override
	public void handleMouseRelease(int mouseX, int mouseY, int mouseButton) {
		this.drawing = false;
	}

	@Override
	public void handleMouseDrag(int mouseX, int mouseY, int mouseButton) {
		int startX = xPosition + 1;
		int startY = yPosition + 1;
		int endX = startX + picture.getWidth() * picture.getPixelWidth() - 1;
		int endY = startY + picture.getHeight() * picture.getPixelHeight() - 1;
		if (GuiHelper.isMouseInside(mouseX, mouseY, startX, startY, endX, endY)) {
			int pixelX = (mouseX - startX) / picture.getPixelWidth();
			int pixelY = (mouseY - startY) / picture.getPixelHeight();
			this.currentTool.handleDrag(this, pixelX, pixelY);
		}
	}

	public int[] getPixels() {
		return this.pixels;
	}

	public int getPixel(int x, int y) {
		return picture.image.getRGB(x, y);
	}

	public void setPixel(int x, int y, int color) {
		picture.image.setRGB(x, y, color);
	}

	public boolean isExistingImage() {
		return existingImage;
	}

	public void setColour(Color color) {
		this.currentColour = color.getRGB();
	}

	public void setColour(int color) {
		this.currentColour = color;
	}

	public void setRed(float red) {
		this.red = (int) (255 * Math.min(1.0, red));
		compileColor();
	}

	public void setGreen(float green) {
		this.green = (int) (255 * Math.min(1.0, green));
		compileColor();
	}

	public void setBlue(float blue) {
		this.blue = (int) (255 * Math.min(1.0, blue));
		compileColor();
	}

	public void compileColor() {
		this.currentColour = ((255 & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | ((blue & 0xFF) << 0);
	}

	public int getCurrentColour() {
		return currentColour;
	}

	public void setCurrentTool(CustomTool currentTool) {
		this.currentTool = currentTool;
	}

	public void setShowGrid(boolean showGrid) {
		this.showGrid = showGrid;
	}

	public void clear() {
		if (pixels != null) {
			for (int i = 0; i < pixels.length; i++) {
				pixels[i] = 0;
			}
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}