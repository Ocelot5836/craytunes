package com.ocelot.craytunes.audio;

import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public class CraytunesAudio extends PositionedSound implements ITickableSound {

	private boolean isDonePlaying;

	public CraytunesAudio(ResourceLocation sound, float volume) {
		super(sound, SoundCategory.PLAYERS);
		this.pitch = 1.0F;
		this.volume = volume;
		this.isDonePlaying = false;
		this.repeat = true;
	}

	@Override
	public void update() {
	}

	@Override
	public boolean isDonePlaying() {
		return isDonePlaying;
	}

	public void stop() {
		this.isDonePlaying = true;
		this.repeat = false;
	}
	
	public void setVolume(float volume) {
		this.volume = volume;
	}
}