package com.ocelot.gaming.apps.component.tool;

import com.ocelot.gaming.apps.component.CustomCanvas;

public abstract class CustomTool {

	public abstract void handleClick(CustomCanvas canvas, int x, int y);

	public abstract void handleRelease(CustomCanvas canvas, int x, int y);

	public abstract void handleDrag(CustomCanvas canvas, int x, int y);
}
