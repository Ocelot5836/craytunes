package com.ocelot.gaming.apps.component;

import com.mrcrayfish.device.api.app.component.Button;
import com.ocelot.gaming.Reference;

public class IdButton extends Button {

	private int id;

	public IdButton(int id, int x, int y) {
		super(x, y, Reference.CRAYTUNES_TEXTURE, id * 10, 0, 10, 10);
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