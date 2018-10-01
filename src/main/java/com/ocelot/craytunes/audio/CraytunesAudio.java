package com.ocelot.craytunes.audio;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public class CraytunesAudio extends PositionedSoundRecord implements ITickableSound {

	private boolean isDonePlaying;

	public CraytunesAudio(ResourceLocation sound, float volume) {
		super(sound, SoundCategory.PLAYERS, volume, 1.0F, true, 0, AttenuationType.NONE, (float) Minecraft.getMinecraft().player.posX, (float) Minecraft.getMinecraft().player.posY, (float) Minecraft.getMinecraft().player.posZ);
	}

	@Override
	public void update() {

	}

	public void stop() {
		this.isDonePlaying = true;
		this.repeat = false;
	}

	@Override
	public boolean isDonePlaying() {
		return isDonePlaying;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public void setLoop(boolean loop) {
		this.repeat = loop;
	}
}