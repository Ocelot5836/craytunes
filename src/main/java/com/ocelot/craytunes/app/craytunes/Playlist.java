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
	private String modid;

	private Playlist() {
		this(null, null);
	}

	public Playlist(@Nullable String modid) {
		this(null, modid);
	}

	public Playlist(String name, @Nullable String modid) {
		this.tracks = new ArrayList();
		this.name = name;
		this.modid = modid;
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

		if (this.name != null) {
			nbt.setString("name", this.name);
		}

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		NBTTagList tracks = nbt.getTagList("tracks", Constants.NBT.TAG_STRING);
		for (int i = 0; i < tracks.tagCount(); i++) {
			this.tracks.add(new SoundTrack(this, new ResourceLocation(tracks.getStringTagAt(i))));
		}
		
		if (nbt.hasKey("name", Constants.NBT.TAG_STRING)) {
			this.name = nbt.getString("name");
		}
	}

	public void add(ResourceLocation soundLocation) {
		this.add(new SoundTrack(this, soundLocation));
	}

	public void add(SoundTrack track) {
		if (!this.tracks.contains(track)) {
			this.tracks.add(track);
		}
	}

	public void remove(int index) {
		this.tracks.remove(index);
	}

	public void remove(SoundTrack track) {
		this.tracks.remove(track);
	}

	public List<SoundTrack> getTracks() {
		return tracks;
	}

	@Nullable
	public String getName() {
		return name;
	}

	@Nullable
	public String getModid() {
		return modid;
	}

	public boolean isModGenerated() {
		return modid != null;
	}

	public SoundTrack get(int index) {
		return this.tracks.get(index);
	}

	public static Playlist fromTag(NBTTagCompound nbt) {
		Playlist playlist = new Playlist();
		playlist.deserializeNBT(nbt);
		return playlist;
	}
}