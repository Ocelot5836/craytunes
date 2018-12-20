package com.ocelot.craytunes.app.craytunes;

import com.ocelot.api.utils.TextureUtils;

import net.minecraft.util.ResourceLocation;

public class SoundTrack {

	private Playlist playlist;
	private ResourceLocation soundLocation;

	public SoundTrack(Playlist playlist, ResourceLocation soundLocation) {
		this.playlist = playlist;
		this.soundLocation = soundLocation;
	}
	
	public Playlist getPlaylist() {
		return playlist;
	}

	public ResourceLocation getSoundLocation() {
		return soundLocation;
	}

	public void bindIconLocation() {
		TextureUtils.bindTexture("textures/misc/unknown_pack.png");
	}
}