package com.ocelot.craytunes.apps.craytunes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public class SoundTrack implements INBTSerializable<NBTTagCompound> {

	private ResourceLocation soundLocation;

	private SoundTrack() {
	}

	public SoundTrack(ResourceLocation soundLocation) {
		this.soundLocation = soundLocation;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("soundLocation", this.soundLocation.toString());
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.soundLocation = new ResourceLocation(nbt.getString("soundLocation"));
	}

	public static SoundTrack fromTag(NBTTagCompound nbt) {
		SoundTrack soundTrack = new SoundTrack();
		soundTrack.deserializeNBT(nbt);
		return soundTrack;
	}
	
	public String getName() {
		return "";
	}

	public ResourceLocation getSoundLocation() {
		return soundLocation;
	}
}