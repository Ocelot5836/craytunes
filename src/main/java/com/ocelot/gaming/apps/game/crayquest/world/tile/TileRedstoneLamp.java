package com.ocelot.gaming.apps.game.crayquest.world.tile;

import com.ocelot.gaming.apps.game.crayquest.gfx.Screen;
import com.ocelot.gaming.apps.game.crayquest.gfx.Sprite;
import com.ocelot.gaming.apps.game.crayquest.world.property.PropBool;

public class TileRedstoneLamp extends BasicTile {

	public TileRedstoneLamp() {
		super(Sprite.REDSTONE_LAMP);
		addProperty("Active", new PropBool());
	}

	@Override
	public void render(int x, int y, Screen screen) {
		Sprite sprite = getProperty("Active").toBool().getProperty() ? Sprite.REDSTONE_LAMP_LIT : Sprite.REDSTONE_LAMP;
		screen.render(x << 4, y << 4, sprite, 0x00);
	}

	public boolean isActive() {
		return getProperty("Active").toBool().getProperty();
	}

	public void setActive(boolean active) {
		getProperty("Active").toBool().setProperty(active);
	}
}