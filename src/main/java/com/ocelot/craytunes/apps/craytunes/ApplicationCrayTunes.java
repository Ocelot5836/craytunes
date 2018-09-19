package com.ocelot.craytunes.apps.craytunes;

import java.awt.Color;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Icons;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.Layout.Background;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.Slider;
import com.ocelot.api.utils.SoundUtils;
import com.ocelot.craytunes.apps.component.SmoothItemList;
import com.ocelot.craytunes.audio.CraytunesAudio;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

public class ApplicationCrayTunes extends Application {

	private static final Map<ResourceLocation, CraytunesAudio> AUDIO = Maps.<ResourceLocation, CraytunesAudio>newHashMap();

	private Background mainbg;

	private Layout main;
	private SmoothItemList<Playlist> playlistList;
	private SmoothItemList<SoundTrack> musicList;
	private Button mainPlaySound;
	private Button mainStopSounds;
	private Button mainResume;
	private Button mainPause;
	private Slider mainVolumeSlider;

	private int selectedPlaylist;
	private int selectedTrack;
	private boolean paused;
	private float volume;

	@Override
	public void init(@Nullable NBTTagCompound intent) {
		mainbg = new Background() {
			@Override
			public void render(Gui gui, Minecraft mc, int x, int y, int width, int height, int mouseX, int mouseY, boolean windowActive) {
				gui.drawRect(x, y, x + main.width, y + main.height, 0xffffffff);
			}
		};

		main = new Layout(250, 125);
		main.setBackground(mainbg);

		this.selectedPlaylist = -1;
		this.selectedTrack = -1;
		this.paused = false;
		this.setVolume(1.0F);

		musicList = new SmoothItemList<SoundTrack>(5, 42, main.width - 45, main.height);
		musicList.setItemClickListener((playlist, index, mouseButton) -> {
			selectedTrack = index;
		});
		main.addComponent(musicList);

		playlistList = new SmoothItemList<Playlist>(0, 0, 40, main.height);
		playlistList.setItemClickListener((playlist, index, mouseButton) -> {
			selectedPlaylist = index;
			reloadSounds();
		});
		main.addComponent(playlistList);

		mainPlaySound = new Button(main.width - 35, main.height - 25, 30, 16, I18n.format("gui.play"));
		mainPlaySound.setClickListener((int mouseX, int mouseY, int mouseButton) -> {
			if (mouseButton == 0) {
				SoundTrack track = this.getCurrentTrack();
				if (track != null && musicList.getSelectedItem() != null) {
					CraytunesAudio audio = this.playSound(track.getSoundLocation());
					main.setTitle(this.info.getName() + " - Now Playing " + track.getName());
				}
			}
		});
		main.addComponent(mainPlaySound);

		mainStopSounds = new Button(main.width - 35, main.height - 45, 30, 16, I18n.format("gui.stop"));
		mainStopSounds.setClickListener((int mouseX, int mouseY, int mouseButton) -> {
			if (mouseButton == 0) {
				SoundUtils.stopAllSounds();
			}
		});
		main.addComponent(mainStopSounds);

		mainVolumeSlider = new Slider(45, 5, 100);
		mainVolumeSlider.setSliderColor(new Color(0xEBEBEB));
		mainVolumeSlider.setSlideListener((percentage) -> {
			this.setVolume(percentage);
			markDirty();
		});
		main.addComponent(mainVolumeSlider);

		mainResume = new Button(23, 5, Icons.PLAY);
		mainPause = new Button(23, 5, Icons.PAUSE);

		mainResume.setClickListener((int mouseX, int mouseY, int mouseButton) -> {
			if (mouseButton == 0) {
				paused = false;
			}
		});
		mainPause.setClickListener((int mouseX, int mouseY, int mouseButton) -> {
			if (mouseButton == 0) {
				paused = true;
			}
		});
		main.addComponent(mainResume);
		main.addComponent(mainPause);

		setCurrentLayout(main);
	}

	@Override
	public void onTick() {
		super.onTick();
		if (paused) {
			mainResume.setEnabled(true);
			mainResume.setVisible(true);
			mainPause.setEnabled(false);
			mainPause.setVisible(false);
			SoundUtils.pauseAllSounds();
		} else {
			mainResume.setEnabled(false);
			mainResume.setVisible(false);
			mainPause.setEnabled(true);
			mainPause.setVisible(true);
			SoundUtils.resumeAllSounds();
		}
	}

	@Override
	public void onClose() {
		super.onClose();
		mainbg = null;
		main.clear();
		main = null;
		AUDIO.clear();
	}

	public void reloadSounds() {
		Playlist playlist = this.getCurrentPlaylist();
		if (playlist != null) {
			musicList.setItems(playlist.getTracks());
		}
	}

	private void loadDefaults() {
		Playlist vanilla = new Playlist();
		// vanilla.add
		this.playlistList.addItem(vanilla);
	}

	private void setVolume(float volume) {
		this.volume = volume;
		SoundTrack track = this.getCurrentTrack();
		if (track != null) {
			AUDIO.get(track.getSoundLocation()).setVolume(volume);
		}
	}

	private CraytunesAudio playSound(ResourceLocation location) {
		if (AUDIO.containsKey(location)) {
			AUDIO.get(location).stop();
		}
		CraytunesAudio audio = new CraytunesAudio(location, this.volume);
		AUDIO.put(location, audio);
		return audio;
	}

	@Nullable
	private Playlist getCurrentPlaylist() {
		return this.selectedPlaylist == -1 ? null : this.playlistList.getItem(this.selectedPlaylist);
	}

	@Nullable
	private SoundTrack getCurrentTrack() {
		return this.selectedPlaylist == -1 || this.selectedTrack == -1 ? null : this.playlistList.getItem(this.selectedPlaylist).get(this.selectedTrack);
	}

	@Override
	public void save(NBTTagCompound nbt) {
		nbt.setFloat("volume", volume);
		nbt.setBoolean("defaults", true);

		NBTTagList categories = new NBTTagList();
		for (Playlist playList : this.playlistList) {
			categories.appendTag(playList.serializeNBT());
		}
		nbt.setTag("playlists", categories);
	}

	@Override
	public void load(NBTTagCompound nbt) {
		volume = nbt.getFloat("volume");
		mainVolumeSlider.setPercentage(volume);

		if (!nbt.hasKey("defaults", Constants.NBT.TAG_BYTE)) {
			this.loadDefaults();
		}

		NBTTagList categories = nbt.getTagList("playlists", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < categories.tagCount(); i++) {
			this.playlistList.addItem(Playlist.fromTag(categories.getCompoundTagAt(i)));
		}
	}
}