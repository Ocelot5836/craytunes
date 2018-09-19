package com.ocelot.craytunes.audio;

import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public class CraytunesAudio extends PositionedSound implements ITickableSound {

	private boolean isDonePlaying;

	public CraytunesAudio(ResourceLocation sound, float volume) {
		super(sound, SoundCategory.PLAYERS);
		this.volume = volume;
		this.isDonePlaying = false;
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
	}
	
	public void setVolume(float volume) {
		this.volume = volume;
	}
}