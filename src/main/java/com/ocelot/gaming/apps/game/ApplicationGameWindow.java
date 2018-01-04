package com.ocelot.gaming.apps.game;

import java.util.ArrayList;
import java.util.List;

import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Component;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.listener.ClickListener;
import com.mrcrayfish.device.core.Laptop;
import com.ocelot.gaming.apps.component.IdButton;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;

public class ApplicationGameWindow extends Application {

	private static final List<ApplicationGame> GAMES = new ArrayList<ApplicationGame>();

	private Layout main;
	private int selectedId;

	@Override
	public void init() {
		main = new Layout(200, 100);
		selectedId = -1;

		for (int i = 0; i < GAMES.size(); i++) {
			final IdButton button = new IdButton(i, 5 + i * 60 + i * 5, 5 + i / 4 + i / 4 * 5, 60, 15, GAMES.get(i).getFormattedName());
			button.setClickListener(new ClickListener() {
				@Override
				public void onClick(Component c, int mouseButton) {
					if (mouseButton == 0) {
						selectedId = ((IdButton) c).getId();
						ApplicationGame game = GAMES.get(selectedId);
						game.init();
						game.getLayout().setTitle(game.getFormattedName());
						setCurrentLayout(game.getLayout());
					}
				}
			});
			main.addComponent(button);
		}

		setCurrentLayout(main);
	}

	@Override
	public void onTick() {
		super.onTick();
		if (selectedId != -1) {
			GAMES.get(selectedId).update();
		}
	}

	@Override
	public void render(Laptop laptop, Minecraft mc, int x, int y, int mouseX, int mouseY, boolean active, float partialTicks) {
		super.render(laptop, mc, x, y, mouseX, mouseY, active, partialTicks);
		if (selectedId != -1) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, 0);
			GAMES.get(selectedId).render(mc.ingameGUI, mc, mouseX, mouseY, active, partialTicks);
			GlStateManager.popMatrix();
		}
	}

	@Override
	public void handleMouseClick(int mouseX, int mouseY, int mouseButton) {
		super.handleMouseClick(mouseX, mouseY, mouseButton);
		if (selectedId != -1) {
			GAMES.get(selectedId).onMousePressed(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	public void handleMouseRelease(int mouseX, int mouseY, int mouseButton) {
		super.handleMouseRelease(mouseX, mouseY, mouseButton);
		if (selectedId != -1) {
			GAMES.get(selectedId).onMouseReleased(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	public void handleKeyTyped(char character, int code) {
		super.handleKeyTyped(character, code);
		if (selectedId != -1) {
			GAMES.get(selectedId).onKeyPressed(character, code);
		}
	}

	@Override
	public void handleKeyReleased(char character, int code) {
		super.handleKeyReleased(character, code);
		if (selectedId != -1) {
			GAMES.get(selectedId).onKeyReleased(character, code);
		}
	}

	@Override
	public void onClose() {
		markDirty();
		super.onClose();
	}

	@Override
	public void load(NBTTagCompound nbt) {
		if (selectedId != -1) {
			GAMES.get(selectedId).load(nbt);
		}
	}

	@Override
	public void save(NBTTagCompound nbt) {
		if (selectedId != -1) {
			GAMES.get(selectedId).save(nbt);
		}
	}

	public static void registerGame(ApplicationGame game, String name) {
		game.setFormattedName(name);
		GAMES.add(game);
	}
}