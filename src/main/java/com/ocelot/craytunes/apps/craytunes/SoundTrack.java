package com.ocelot.craytunes.apps.craytunes;

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

	public String getName() {
		SoundEventAccessor accessor = Minecraft.getMinecraft().getSoundHandler().getAccessor(this.soundLocation);
		String subtitle = accessor == null || accessor.getSubtitle() == null ? null : accessor.getSubtitle().getFormattedText();
		return subtitle == null ? this.soundLocation.getResourcePath() : subtitle;
	}

	public ResourceLocation getSoundLocation() {
		return soundLocation;
	}
}