package com.ocelot.gaming.apps.component;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.mrcrayfish.device.api.app.Component;
import com.mrcrayfish.device.api.io.File;
import com.ocelot.gaming.utils.TextureUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class CustomPicture extends Component {

	private File source;
	private String name;
	private String author;
	private int width;
	private int height;

	public BufferedImage image;

	private int gridColour = new Color(200, 200, 200, 150).getRGB();

	public CustomPicture(String name, String author, int width, int height) {
		super(width, height);
		this.name = name;
		this.author = author;
		this.width = width;
		this.height = height;
		init();
	}

	private void init() {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}

	public File getSource() {
		return source;
	}

	public String getName() {
		return name;
	}

	public String getAuthor() {
		return author;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public BufferedImage copyImage() {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		image.copyData(image.getRaster().getWritableParent());
		return image;
	}

	public void render(int x, int y, int width, int height, boolean showGrid) {
		ResourceLocation texture = TextureUtils.createBufferedImageTexture(image);
		Minecraft.getMinecraft().ingameGUI.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
		TextureUtils.deleteTexture(texture);
		// if (showGrid) {
		// drawRect(x, y, x + getPixelWidth(), y + 1, gridColour);
		// drawRect(x, y, x + 1, y + getPixelHeight(), gridColour);
		// }
	}

	@Override
	public String toString() {
		return name;
	}

	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setString("Name", getName());
		nbt.setString("Author", getAuthor());
		nbt.setIntArray("Pixels", ((DataBufferInt) image.getData().getDataBuffer()).getData());
		nbt.setInteger("Width", width);
		nbt.setInteger("Height", height);
	}

	public static CustomPicture fromFile(File file) {
		NBTTagCompound data = file.getData();
		CustomPicture picture = new CustomPicture(data.getString("Name"), data.getString("Author"), data.getInteger("Width"), data.getInteger("Height"));
		picture.source = file;

		int[] pixels = data.getIntArray("Pixels");
		for (int x = 0; x < picture.width; x++) {
			for (int y = 0; y < picture.height; y++) {
				picture.image.setRGB(x, y, pixels[x + y * picture.image.getWidth()]);
			}
		}
		return picture;
	}

	public int getPixelWidth() {
		return (width - 1) * image.getWidth();
	}

	public int getPixelHeight() {
		return (height - 1) * image.getHeight();
	}
}