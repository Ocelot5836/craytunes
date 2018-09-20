package com.ocelot.craytunes.apps.craytunes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

public class Playlist implements INBTSerializable<NBTTagCompound> {

	private List<SoundTrack> tracks;

	public Playlist() {
		this.tracks = new ArrayList();
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();

		NBTTagList tracks = new NBTTagList();
		for (int i = 0; i < this.tracks.size(); i++) {
			if (this.tracks.get(i).getSoundLocation() != null) {
				tracks.appendTag(new NBTTagString(this.tracks.get(i).getSoundLocation().toString()));
			}
		}
		nbt.setTag("tracks", tracks);

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		NBTTagList tracks = nbt.getTagList("tracks", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < tracks.tagCount(); i++) {
			this.tracks.add(new SoundTrack(new ResourceLocation(tracks.getStringTagAt(i))));
		}
	}

	public void add(ResourceLocation soundLocation) {
		this.add(new SoundTrack(soundLocation));
	}

	public void add(SoundTrack track) {
		if (!this.tracks.contains(track)) {
			this.tracks.add(track);
		}
	}

	public List<SoundTrack> getTracks() {
		return this.tracks;
	}

	public SoundTrack get(int currentSound) {
		return this.tracks.get(currentSound);
	}

	public static Playlist fromTag(NBTTagCompound nbt) {
		Playlist playlist = new Playlist();
		playlist.deserializeNBT(nbt);
		return playlist;
	}
}