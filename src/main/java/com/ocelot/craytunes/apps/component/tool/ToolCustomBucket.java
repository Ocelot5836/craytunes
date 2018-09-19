package com.ocelot.craytunes.apps.component.tool;

import com.mrcrayfish.device.object.Canvas;
import com.ocelot.craytunes.apps.component.CustomCanvas;

public class ToolCustomBucket extends CustomTool {

	@Override
	public void handleClick(CustomCanvas canvas, int x, int y) {
		fill(canvas, x, y, canvas.getPixel(x, y), canvas.getCurrentColour());
	}

	@Override
	public void handleRelease(CustomCanvas canvas, int x, int y) {
	}

	@Override
	public void handleDrag(CustomCanvas canvas, int x, int y) {
	}

	public void fill(CustomCanvas canvas, int x, int y, int target, int replacement) {
		if (x < 0 || y < 0 || x >= canvas.picture.getWidth() || y >= canvas.picture.getHeight())
			return;

		if (target == replacement)
			return;

		if (canvas.getPixel(x, y) != target)
			return;

		canvas.setPixel(x, y, replacement);

		fill(canvas, x + 1, y, target, replacement);
		fill(canvas, x - 1, y, target, replacement);
		fill(canvas, x, y + 1, target, replacement);
		fill(canvas, x, y - 1, target, replacement);
	}
}