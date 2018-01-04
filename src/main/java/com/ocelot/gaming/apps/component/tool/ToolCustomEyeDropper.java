package com.ocelot.gaming.apps.component.tool;

import com.ocelot.gaming.apps.component.CustomCanvas;

public class ToolCustomEyeDropper extends CustomTool {

	@Override
	public void handleClick(CustomCanvas canvas, int x, int y) {
		canvas.setColour(canvas.getPixel(x, y));
	}

	@Override
	public void handleRelease(CustomCanvas canvas, int x, int y) {
	}

	@Override
	public void handleDrag(CustomCanvas canvas, int x, int y) {
	}
}