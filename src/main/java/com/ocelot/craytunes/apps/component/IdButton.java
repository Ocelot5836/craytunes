package com.ocelot.craytunes.apps.component;

import com.mrcrayfish.device.api.app.component.Button;
import com.ocelot.craytunes.Craytunes;

import net.minecraft.util.ResourceLocation;

@Deprecated
public class IdButton extends Button {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Craytunes.MOD_ID, "textures/gui/icons.png");

	private int id;

	public IdButton(int id, int x, int y) {
		super(x, y, TEXTURE, id * 10, 0, 10, 10);
		this.id = id;
	}

	public IdButton(int id, int x, int y, int width, int height, String text) {
		super(x, y, width, height, text);
		this.id = id;
	}

	public int getId() {
		return id;
	}
}