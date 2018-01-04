package com.ocelot.gaming.apps.game.crayquest.entity;

import org.lwjgl.input.Keyboard;

import com.ocelot.gaming.apps.game.crayquest.gfx.Screen;
import com.ocelot.gaming.apps.game.crayquest.gfx.Sprite;
import com.ocelot.gaming.apps.game.crayquest.world.World;

import net.minecraft.client.multiplayer.PlayerControllerMP;

public class Player extends Mob {

	private PlayerControllerMP controller;

	public Player(World world, PlayerControllerMP controller) {
		this(world, 0, 0, controller);
	}

	public Player(World world, int x, int y, PlayerControllerMP controller) {
		super(world);
		this.sprite = Sprite.PLAYER_DOWN;
		this.x = x;
		this.y = y;
		this.controller = controller;
		this.speed = 2;
	}

	@Override
	public void update() {
		super.update();
		xa = 0;
		ya = 0;

		if (controller != null) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				xa -= speed;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				xa += speed;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				ya -= speed;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				ya += speed;
			}

			if (xa != 0 || ya != 0)
				move(xa, ya);
		}
	}

	@Override
	public void render(Screen screen) {
		screen.render(x, y, sprite, 0x00);
	}
}