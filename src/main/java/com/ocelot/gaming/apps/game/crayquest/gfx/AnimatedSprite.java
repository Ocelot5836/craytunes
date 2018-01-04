package com.ocelot.gaming.apps.game.crayquest.gfx;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.logging.Logger;

import com.ocelot.gaming.utils.Utils;

public class AnimatedSprite extends Sprite {

	private int frame;
	private Sprite sprite;
	private int rate = -1;
	private int time = 0;
	private int length = -1;

	public AnimatedSprite(SpriteSheet sheet, int width, int height, String animationFileName) {
		super(sheet, width, height);
		loadAnimationData("/assets/textures/animation/" + animationFileName + ".animation");
		if (length > sheet.getSprite().length) {
			length = sheet.getSprite().length;
			Utils.getLogger().warning("Length of animation is too long!");
		}
		sprite = sheet.getSprite()[0];
		frame = 0;
	}

	protected void loadAnimationData(String animationPath) {
		try {
			InputStream in = getClass().getResourceAsStream(animationPath);
			Scanner scanner = new Scanner(new InputStreamReader(in));

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				String[] data = line.split(":");

				if (data[0].equalsIgnoreCase("rate")) {
					this.rate = Integer.parseInt(data[1]);
				}

				if (data[0].equalsIgnoreCase("length")) {
					this.length = Integer.parseInt(data[1]) - 1;
				}

				if (line.equalsIgnoreCase("end")) {
					scanner.close();
					break;
				}
			}

			if (rate == -1) {
				rate = 5;
			}

			if (length == -1) {
				Utils.getLogger().severe(String.format("Length cannot be -1! The issue was found in animation file [%s]", animationPath));
			}

			scanner.close();
		} catch (Exception e) {
			Utils.getLogger().severe(String.format("Could not load animation [%s]!", animationPath));
		}
	}

	public void update() {
		time++;
		if (time % rate == 0) {
			if (frame >= length) {
				frame = 0;
			} else {
				frame++;
			}
			sprite = sheet.getSprite()[frame];
		}
	}

	public int getFrame() {
		return frame;
	}
	
	public Sprite getSprite() {
		return sprite;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}
}