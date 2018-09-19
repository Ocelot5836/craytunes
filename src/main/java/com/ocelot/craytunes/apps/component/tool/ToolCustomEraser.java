package com.ocelot.craytunes.apps.component.tool;

import com.ocelot.craytunes.apps.component.CustomCanvas;

public class ToolCustomEraser extends CustomTool {

	@Override
	public void handleClick(CustomCanvas canvas, int x, int y) {
		canvas.setPixel(x, y, 0);
	}

	@Override
	public void handleRelease(CustomCanvas canvas, int x, int y) {
	}

	@Override
	public void handleDrag(CustomCanvas canvas, int x, int y) {
		canvas.setPixel(x, y, 0);
	}
}