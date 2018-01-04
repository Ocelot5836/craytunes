package com.ocelot.gaming.apps.game;

import com.mrcrayfish.device.api.app.Layout;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;

/**
 * The base class for any game that will be rendered onto the screen.
 * 
 * @author Ocelot5836
 */
public abstract class ApplicationGame {

	private Layout main;
	private String formattedName;

	/**
	 * The default initialization method.
	 */
	public void init() {
		init(200, 100);
	}

	/**
	 * Initializes the game with a specified width and height.
	 * 
	 * @param width
	 *            The width of the window
	 * @param height
	 *            The height of the window
	 */
	public final void init(int width, int height) {
		main = new Layout(width, height);
	}

	/**
	 * Called every time the mouse is pressed.
	 * 
	 * @param mouseX
	 *            The x position of the mouse
	 * @param mouseY
	 *            The y position of the mouse
	 * @param mouseButton
	 *            The button id currently pressed on the mouse
	 */
	protected abstract void onMousePressed(int mouseX, int mouseY, int mouseButton);

	/**
	 * Called every time the mouse is released.
	 * 
	 * @param mouseX
	 *            The x position of the mouse
	 * @param mouseY
	 *            The y position of the mouse
	 * @param mouseButton
	 *            The button id currently released on the mouse
	 */
	protected abstract void onMouseReleased(int mouseX, int mouseY, int mouseButton);

	/**
	 * Called every time a key is pressed.
	 * 
	 * @param character
	 *            The character pressed
	 * @param code
	 *            The key code pressed
	 */
	protected abstract void onKeyPressed(char character, int code);

	/**
	 * Called every time a key is released.
	 * 
	 * @param character
	 *            The character released
	 * @param code
	 *            The key code released
	 */
	protected abstract void onKeyReleased(char character, int code);

	/**
	 * Updates the game's logic.
	 */
	protected abstract void update();

	/**
	 * Renders the game to the screen.
	 * 
	 * @param laptop
	 *            The laptop instance
	 * @param mc
	 *            The minecraft instance
	 * @param mouseX
	 *            The x position of the mouse
	 * @param mouseY
	 *            The y position of the mouse
	 * @param active
	 *            Whether or not the window is activated or not
	 * @param partialTicks
	 *            The partial ticks
	 */
	protected abstract void render(Gui gui, Minecraft mc, int mouseX, int mouseY, boolean active, float partialTicks);

	public abstract void load(NBTTagCompound nbt);

	public abstract void save(NBTTagCompound nbt);

	public Layout getLayout() {
		return main;
	}

	public String getFormattedName() {
		return formattedName;
	}

	public void setFormattedName(String formattedName) {
		this.formattedName = formattedName;
	}
}