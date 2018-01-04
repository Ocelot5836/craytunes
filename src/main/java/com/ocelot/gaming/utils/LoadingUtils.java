package com.ocelot.gaming.utils;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

/**
 * This class loads images, text, and other things using {@link ImageIO} and it catches the exceptions.
 * 
 * @author Ocelot5836
 */
public class LoadingUtils {

	public static final BufferedImage MISSING_IMAGE = createMissingImage(16, 16);

	/**
	 * Loads text form a path into an array.
	 * 
	 * @param path
	 *            The path to the file
	 * @return The text from the file in the form as an array
	 */
	public static String[] loadTextToArray(String path) {
		ArrayList<String> lines = new ArrayList<String>();
		String[] result = new String[0];
		try {
			InputStream input = Class.class.getResourceAsStream(path);
			Scanner scanner = new Scanner(input);

			while (scanner.hasNextLine()) {
				lines.add(scanner.nextLine());
			}
			scanner.close();
		} catch (Exception e) {
			Utils.getLogger().warning("Error loading text to array!");
			e.printStackTrace();
		}
		result = new String[lines.size()];

		for (int i = 0; i < lines.size(); i++) {
			result[i] = lines.get(i);
		}

		return result;
	}

	/**
	 * Loads text to an array from a website URL.
	 * 
	 * @param path
	 *            The path to the file
	 * @return The text from the file in the form as an array
	 */
	public static String[] loadTextToArrayFromURL(String pageURL) {
		ArrayList<String> lines = new ArrayList<String>();
		String[] result = new String[0];
		try {
			URL url = new URL(pageURL);
			Scanner scanner = new Scanner(url.openStream());

			while (scanner.hasNext()) {
				lines.add(scanner.nextLine());
			}
			scanner.close();
		} catch (Exception e) {
			Utils.getLogger().warning("Error loading text to array!");
			e.printStackTrace();
		}
		result = new String[lines.size()];

		for (int i = 0; i < lines.size(); i++) {
			result[i] = lines.get(i);
		}

		return result;
	}

	/**
	 * Loads an image and returns that image found. If no image is found it returns the empty image.
	 * 
	 * @param path
	 *            The path to the file
	 * @return The image returned by {@link ImageIO}
	 */
	public static BufferedImage loadImage(String path) {
		BufferedImage image = null;
		try {
			InputStream input = Class.class.getResourceAsStream(path);
			image = ImageIO.read(input);
		} catch (Exception e) {
			Utils.getLogger().warning(String.format("Missing image: [%s]", path));
			image = MISSING_IMAGE;
		}

		return image;
	}

	/**
	 * Creates an image that is the classic black and pink image.
	 * 
	 * @param width
	 *            The width of the image
	 * @param height
	 *            The height of the image
	 * @return The image created
	 */
	public static BufferedImage createMissingImage(int width, int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				if ((x < image.getWidth() / 2 && y < image.getHeight() / 2) || (x >= image.getWidth() / 2 && y >= image.getHeight() / 2)) {
					image.setRGB(x, y, 0);
				} else {
					image.setRGB(x, y, 0xff00ff);
				}
			}
		}

		return image;
	}
}