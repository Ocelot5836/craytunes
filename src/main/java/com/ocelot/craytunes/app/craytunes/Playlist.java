package com.ocelot.craytunes.app.craytunes;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

public class Playlist implements INBTSerializable<NBTTagCompound> {

	private List<SoundTrack> tracks;
	private String name;
	private boolean modGenerated;

	private Playlist() {
		this(null, false);
	}

	public Playlist(boolean modGenerated) {
		this(null, modGenerated);
	}

	public Playlist(String name, boolean modGenerated) {
		this.tracks = new ArrayList();
		this.name = name;
		this.modGenerated = modGenerated;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();

		if (this.name != null) {
			nbt.setString("name", this.name);
		}

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
		if (nbt.hasKey("name", Constants.NBT.TAG_STRING)) {
			this.name = nbt.getString("name");
		}

		NBTTagList tracks = nbt.getTagList("tracks", Constants.NBT.TAG_STRING);
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

	@Nullable
	public String getName() {
		return name;
	}

	public boolean isModGenerated() {
		return modGenerated;
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