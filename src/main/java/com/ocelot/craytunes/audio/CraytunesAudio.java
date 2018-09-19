package com.ocelot.craytunes.audio;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public class CraytunesAudio implements ISound {

	@Override
	public ResourceLocation getSoundLocation() {
		return null;
	}

	@Override
	public SoundEventAccessor createAccessor(SoundHandler handler) {
		return null;
	}

	@Override
	public Sound getSound() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SoundCategory getCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canRepeat() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getRepeatDelay() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getVolume() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getPitch() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getXPosF() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getYPosF() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getZPosF() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AttenuationType getAttenuationType() {
		// TODO Auto-generated method stub
		return null;
	}

}
