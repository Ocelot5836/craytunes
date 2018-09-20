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
	private String name;

	public Playlist() {
		this(null);
	}

	public Playlist(String name) {
		this.tracks = new ArrayList();
		this.name = name;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();

		nbt.setString("name", this.name);
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
		this.name = nbt.getString("name");
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
		return tracks;
	}

	public String getName() {
		return name;
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