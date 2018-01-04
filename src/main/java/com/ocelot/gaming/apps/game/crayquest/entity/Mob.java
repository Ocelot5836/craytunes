package com.ocelot.gaming.apps.game.crayquest.entity;

import java.util.UUID;

import com.ocelot.gaming.apps.game.crayquest.gfx.Sprite;
import com.ocelot.gaming.apps.game.crayquest.world.World;

public abstract class Mob extends Entity {

	protected Sprite sprite;
	protected int speed;
	protected int dir = 0;
	protected int numSteps = 0;
	protected boolean moving = false;
	protected int xa, ya;

	public Mob(World world) {
		super(world);
	}

	public void move(int xa, int ya) {
		if (xa != 0 && ya != 0) {
			move(xa, 0);
			move(0, ya);
			numSteps--;
			return;
		}

		if (xa > 0)
			dir = 1;
		if (xa < 0)
			dir = 3;
		if (ya > 0)
			dir = 2;
		if (ya < 0)
			dir = 0;

		if (!hasCollided()) {
			x += xa;
			y += ya;
			numSteps++;
			moving = true;
		}
	}

	@Override
	public void update() {
		if (xa == 0 && ya == 0)
			moving = false;
	}

	protected boolean hasCollided() {
		return false;
	}
}