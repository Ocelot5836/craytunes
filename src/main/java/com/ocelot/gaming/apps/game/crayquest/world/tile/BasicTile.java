package com.ocelot.gaming.apps.game.crayquest.world.tile;

import com.ocelot.gaming.apps.game.crayquest.gfx.Screen;
import com.ocelot.gaming.apps.game.crayquest.gfx.Sprite;
import com.ocelot.gaming.apps.game.crayquest.world.property.PropRotation;

import net.minecraft.nbt.NBTTagCompound;

public class BasicTile extends Tile {

	protected boolean solid;
	protected boolean breakable;
	protected boolean emitter;
	protected boolean randomRotation;

	protected float emitterLight;

	public BasicTile(Sprite sprite) {
		super(sprite);
	}

	@Override
	public void update() {

	}

	@Override
	public void render(int x, int y, Screen screen) {
		if (getProperties().containsKey("Rotation"))
			screen.render(x << 4, y << 4, sprite, ((PropRotation) getProperties().get("Rotation")).getValue());
		else
			screen.render(x << 4, y << 4, sprite, 0x00);
	}

	@Override
	public void load(NBTTagCompound nbt) {

	}

	@Override
	public void save(NBTTagCompound nbt) {

	}

	@Override
	public boolean isSolid() {
		return solid;
	}

	@Override
	public boolean isBreakable() {
		return breakable;
	}

	@Override
	public boolean emitsLight() {
		return emitter;
	}

	@Override
	public boolean allowTextureRotation() {
		return randomRotation;
	}

	public BasicTile setSolid() {
		this.solid = true;
		return this;
	}

	public BasicTile setBreakable() {
		this.breakable = true;
		return this;
	}

	public BasicTile setEmitter(float lightLevel) {
		this.emitter = true;
		this.emitterLight = lightLevel;
		return this;
	}

	public BasicTile setRandomRotation() {
		this.randomRotation = true;
		return this;
	}
}