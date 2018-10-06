package com.ocelot.craytunes.app.craytunes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public class SoundTrack {

	private ResourceLocation soundLocation;

	private SoundTrack() {
	}

	public SoundTrack(ResourceLocation soundLocation) {
		this.soundLocation = soundLocation;
	}

	public ResourceLocation getSoundLocation() {
		return soundLocation;
	}
}