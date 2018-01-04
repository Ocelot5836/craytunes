package com.ocelot.gaming.apps.game.crayquest;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.ocelot.gaming.apps.game.ApplicationGame;
import com.ocelot.gaming.apps.game.crayquest.entity.Player;
import com.ocelot.gaming.apps.game.crayquest.gfx.Screen;
import com.ocelot.gaming.apps.game.crayquest.world.World;
import com.ocelot.gaming.apps.game.crayquest.world.WorldRandom;
import com.ocelot.gaming.utils.TextureUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class GameCrayQuest extends ApplicationGame {

	public static final int WIDTH = 300;
	public static final int HEIGHT = WIDTH / 16 * 9;

	private Screen screen;
	private World world;
	private Player player;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	@Override
	public void init() {
		super.init(WIDTH, HEIGHT);

		screen = new Screen(WIDTH, HEIGHT);
		world = new WorldRandom(64, 64);
		player = new Player(world, 0, 0, Minecraft.getMinecraft().playerController);
		world.add(player);
	}

	@Override
	protected void onMousePressed(int mouseX, int mouseY, int mouseButton) {

	}

	@Override
	protected void onMouseReleased(int mouseX, int mouseY, int mouseButton) {

	}

	@Override
	protected void onKeyPressed(char character, int code) {

	}

	@Override
	protected void onKeyReleased(char character, int code) {

	}

	@Override
	protected void update() {
		world.update();
	}

	@Override
	protected void render(Gui gui, Minecraft mc, int mouseX, int mouseY, boolean active, float partialTicks) {
		world.render(player.x, player.y, screen);

		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		ResourceLocation texture = TextureUtils.createBufferedImageTexture(image);
		gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, WIDTH, HEIGHT, WIDTH, HEIGHT);
		TextureUtils.deleteTexture(texture);
		screen.clear();
	}

	@Override
	public void load(NBTTagCompound nbt) {
		world.load(nbt);
	}

	@Override
	public void save(NBTTagCompound nbt) {
		world.save(nbt);
	}
}