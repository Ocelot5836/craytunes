package com.ocelot.gaming.apps.game.crayquest.entity;

import java.util.Random;
import java.util.UUID;

import com.ocelot.gaming.apps.game.crayquest.gfx.Screen;
import com.ocelot.gaming.apps.game.crayquest.world.World;

import net.minecraft.nbt.NBTTagCompound;

public abstract class Entity {

	public int x, y;
	private boolean isDead = false;
	protected World world;
	protected Random random = new Random();
	protected UUID uuid = UUID.randomUUID();

	public Entity(World world) {
		this.world = world;
	}

	public void update() {

	}

	public void render(Screen screen) {

	}

	public void load(NBTTagCompound nbt) {
		this.x = nbt.getInteger("x");
		this.y = nbt.getInteger("y");
	}

	public void save(NBTTagCompound nbt) {
		nbt.setInteger("x", this.x);
		nbt.setInteger("y", this.y);
	}

	public boolean isDead() {
		return isDead;
	}

	public void remove() {
		this.isDead = true;
	}
}