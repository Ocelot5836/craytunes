package com.ocelot.craytunes.apps.craytunes;

import java.awt.Color;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.IIcon;
import com.mrcrayfish.device.api.app.Icons;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.Layout.Background;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.Slider;
import com.mrcrayfish.device.api.app.renderer.ListItemRenderer;
import com.mrcrayfish.device.core.Laptop;
import com.ocelot.api.utils.GuiUtils;
import com.ocelot.api.utils.TextureUtils;
import com.ocelot.craytunes.apps.component.SmoothItemList;
import com.ocelot.craytunes.audio.CraytunesAudio;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
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
	private int playingTrack;
	private int selectedTrack;
	private boolean paused;
	private float volume;

	private int mouseX;
	private int mouseY;
	private boolean loaded;

	@Override
	public void init(@Nullable NBTTagCompound intent) {
		mainbg = new Background() {
			@Override
			public void render(Gui gui, Minecraft mc, int x, int y, int width, int height, int mouseX, int mouseY, boolean windowActive) {
				gui.drawRect(x, y, x + main.width, y + main.height, 0xffffffff);
			}
		};

		main = new Layout(362, 164);
		main.setBackground(mainbg);

		this.selectedPlaylist = -1;
		this.playingTrack = -1;
		this.selectedTrack = -1;
		this.paused = false;
		this.setVolume(1.0F);

		this.mouseX = 0;
		this.mouseY = 0;
		this.loaded = false;

		playlistList = new SmoothItemList<Playlist>(0, 0, 80, main.height);
		playlistList.setScrollSpeed(6);
		playlistList.setItemClickListener((playlist, index, mouseButton) -> {
			if (this.getCurrentPlaylist() != playlist) {
				selectedPlaylist = index;
				reloadSounds();
			}
		});
		main.addComponent(playlistList);

		musicList = new SmoothItemList<SoundTrack>(playlistList.left + playlistList.getWidth() + 5, 42, main.width - playlistList.left - playlistList.getWidth() - 10, main.height - 47);
		musicList.setScrollSpeed(6);
		musicList.setListItemRenderer(new ListItemRenderer<SoundTrack>(18) {
			@Override
			public void render(SoundTrack track, Gui gui, Minecraft mc, int x, int y, int width, int height, boolean selected) {
				String title = null;
				String name = "null";
				boolean playing = track == getPlayingTrack();
				selected = track == getSelectedTrack();

				int centerY = (int) (y + 18f / 2f - (float) mc.fontRenderer.FONT_HEIGHT / 2f) + 1;
				int fontColor = 0xffffffff;

				if (selected) {
					gui.drawRect(x, y, x + width, y + height, 0xff2687FB);
				}

				if (track != null) {
					ResourceLocation location = track.getSoundLocation();
					if (location != null) {
						title = TextFormatting.YELLOW + location.getResourceDomain().substring(0, 1).toUpperCase() + location.getResourceDomain().substring(1);
						name = location.getResourcePath();
					}
				}

				GlStateManager.color(1, 1, 1, 1);
				TextureUtils.bindTexture("textures/misc/unknown_pack.png");
				gui.drawScaledCustomSizeModalRect(x + 2, y + 2, 0, 0, 256, 256, 14, 14, 256, 256);

				if (GuiUtils.isMouseInside(musicList.xPosition, musicList.yPosition, musicList.getWidth(), musicList.getHeight(), mouseX, mouseY) && GuiUtils.isMouseInside(x, y, 18, 18, mouseX, mouseY)) {
					IIcon icon = selected && !paused ? Icons.PAUSE : Icons.PLAY;
					TextureUtils.bindTexture(icon.getIconAsset());
					gui.drawScaledCustomSizeModalRect(x + 4, y + 4, icon.getU(), icon.getV(), icon.getIconSize(), icon.getIconSize(), 10, 10, icon.getSourceWidth(), icon.getSourceHeight());
				} else if (playing) {
					IIcon icon = paused ? Icons.VOLUME_OFF : Icons.VOLUME_ON;
					TextureUtils.bindTexture(icon.getIconAsset());
					gui.drawRect(x + 2, y + 2, x + 16, y + 16, 0xdd999999);
					gui.drawScaledCustomSizeModalRect(x + 4, y + 4, icon.getU(), icon.getV(), icon.getIconSize(), icon.getIconSize(), 10, 10, icon.getSourceWidth(), icon.getSourceHeight());
				}

				if (title != null) {
					mc.fontRenderer.drawString(title, x + 20, centerY - mc.fontRenderer.FONT_HEIGHT / 2, fontColor);
					mc.fontRenderer.drawString(name, x + 20, centerY + mc.fontRenderer.FONT_HEIGHT / 2, fontColor);
				} else {
					mc.fontRenderer.drawString(name, x + 20, centerY, fontColor);
				}
			}
		});
		musicList.setItemClickListener((track, index, mouseButton) -> {
			if (mouseX - musicList.xPosition < 20) {
				paused = selectedTrack == index ? !paused : false;
				playingTrack = index;
			}
			selectedTrack = index;
		});
		main.addComponent(musicList);

		mainPlaySound = new Button(main.width - 35, main.height - 25, 30, 16, I18n.format("gui.play"));
		mainPlaySound.setClickListener((int mouseX, int mouseY, int mouseButton) -> {
			if (mouseButton == 0) {
				SoundTrack track = this.getPlayingTrack();
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
			}
		});
		main.addComponent(mainStopSounds);

		mainVolumeSlider = new Slider(45, 5, 100);
		mainVolumeSlider.setSliderColor(new Color(0xEBEBEB));
		mainVolumeSlider.setSlideListener((percentage) -> {
			this.setVolume(percentage);
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
		} else {
			mainResume.setEnabled(false);
			mainResume.setVisible(false);
			mainPause.setEnabled(true);
			mainPause.setVisible(true);
		}

		if (!this.loaded) {
			this.loadDefaults();
			this.loaded = true;
		}
	}

	@Override
	public void render(Laptop laptop, Minecraft mc, int x, int y, int mouseX, int mouseY, boolean active, float partialTicks) {
		super.render(laptop, mc, x, y, mouseX, mouseY, active, partialTicks);
		this.mouseX = mouseX;
		this.mouseY = mouseY;
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
		for (SoundEvent sound : SoundEvent.REGISTRY) {
			vanilla.add(sound.getSoundName());
		}
		this.playlistList.addItem(vanilla);
		this.markDirty();
	}

	private void setVolume(float volume) {
		this.volume = volume;
		SoundTrack track = this.getPlayingTrack();
		if (track != null) {
			AUDIO.get(track.getSoundLocation()).setVolume(volume);
		}
		this.markDirty();
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
	private SoundTrack getPlayingTrack() {
		return this.selectedPlaylist == -1 || this.playingTrack == -1 ? null : this.playlistList.getItem(this.selectedPlaylist).get(this.playingTrack);
	}

	@Nullable
	private SoundTrack getSelectedTrack() {
		return this.selectedPlaylist == -1 || this.selectedTrack == -1 ? null : this.playlistList.getItem(this.selectedPlaylist).get(this.selectedTrack);
	}

	@Override
	public void save(NBTTagCompound nbt) {
		nbt.setFloat("volume", volume);

		NBTTagList categories = new NBTTagList();
		for (Playlist playList : this.playlistList) {
			categories.appendTag(playList.serializeNBT());
		}
		nbt.setTag("playlists", categories);
	}

	@Override
	public void load(NBTTagCompound nbt) {
		loaded = true;
		volume = nbt.getFloat("volume");
		mainVolumeSlider.setPercentage(volume);

		NBTTagList categories = nbt.getTagList("playlists", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < categories.tagCount(); i++) {
			this.playlistList.addItem(Playlist.fromTag(categories.getCompoundTagAt(i)));
		}
	}
}