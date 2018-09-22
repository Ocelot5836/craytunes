package com.ocelot.craytunes.apps.craytunes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.IIcon;
import com.mrcrayfish.device.api.app.Icons;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.Slider;
import com.mrcrayfish.device.api.app.component.TextField;
import com.mrcrayfish.device.api.app.renderer.ListItemRenderer;
import com.mrcrayfish.device.api.utils.RenderUtil;
import com.mrcrayfish.device.core.Laptop;
import com.ocelot.api.utils.GuiUtils;
import com.ocelot.api.utils.TextureUtils;
import com.ocelot.craytunes.apps.component.SmoothItemList;
import com.ocelot.craytunes.audio.CraytunesAudio;
import com.ocelot.craytunes.util.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;

public class ApplicationCrayTunes extends Application {

	private Layout main;
	private List<SoundTrack> musicListCopy;
	private SmoothItemList<Playlist> playlistList;
	private SmoothItemList<SoundTrack> musicList;
	private Button mainResume;
	private Button mainPause;
	private Slider mainVolumeSlider;

	private TextField musicSearchBar;

	private Playlist selectedPlaylist;
	private SoundTrack playingTrack;
	private SoundTrack selectedTrack;
	private boolean paused;
	private float volume;

	private CraytunesAudio playingAudio;
	private int mouseX;
	private int mouseY;

	@Override
	public void init(@Nullable NBTTagCompound intent) {
		main = new Layout(362, 164);

		this.selectedPlaylist = null;
		this.playingTrack = null;
		this.selectedTrack = null;
		this.pause(false);
		this.setVolume(1.0F);

		this.mouseX = 0;
		this.mouseY = 0;

		musicListCopy = new ArrayList<SoundTrack>();

		playlistList = new SmoothItemList<Playlist>(0, 0, 80, main.height);
		playlistList.setBackgroundColor(new Color(Laptop.getSystem().getSettings().getColorScheme().getItemBackgroundColor()));
		playlistList.setScrollSpeed(25);
		playlistList.setListItemRenderer(new ListItemRenderer<Playlist>(12) {
			@Override
			public void render(Playlist playlist, Gui gui, Minecraft mc, int x, int y, int width, int height, boolean selected) {
				String name = StringUtils.isNullOrEmpty(playlist.getName()) ? "Playlist" : playlist.getName();

				if (selected) {
					gui.drawRect(x, y, x + width, y + height, Laptop.getSystem().getSettings().getColorScheme().itemHighlightColor);
				}

				RenderUtil.drawStringClipped(name, x + 2, (int) (y + 12f / 2f - (float) mc.fontRenderer.FONT_HEIGHT / 2f) + 1, width - 4, 0xffffffff, false);
			}
		});
		playlistList.setItemClickListener((playlist, index, mouseButton) -> {
			if (this.getCurrentPlaylist() != playlist) {
				this.pause(true);
				this.selectedTrack = null;
				this.selectedPlaylist = playlist;
				this.musicSearchBar.clear();
				this.stopPlayingTrack();
				this.reloadSounds();
			}
		});
		main.addComponent(playlistList);

		musicList = new SmoothItemList<SoundTrack>(playlistList.left + playlistList.getWidth() + 5, 47, main.width - playlistList.left - playlistList.getWidth() - 10, main.height - 52);
		musicList.setBackgroundColor(new Color(Laptop.getSystem().getSettings().getColorScheme().getItemBackgroundColor()));
		musicList.setScrollSpeed(50);
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
					gui.drawRect(x, y, x + width, y + height, Laptop.getSystem().getSettings().getColorScheme().itemHighlightColor);
				}

				if (track != null) {
					ResourceLocation location = track.getSoundLocation();
					if (location != null) {
						title = TextFormatting.YELLOW + ("minecraft".equals(location.getResourceDomain()) ? "Minecraft" : Lib.getModName(location.getResourceDomain()));
						name = location.getResourcePath();
					}
				}

				GlStateManager.color(1, 1, 1, 1);
				TextureUtils.bindTexture("textures/misc/unknown_pack.png");
				gui.drawScaledCustomSizeModalRect(x + 2, y + 2, 0, 0, 256, 256, 14, 14, 256, 256);

				if (GuiUtils.isMouseInside(musicList.xPosition, musicList.yPosition, musicList.getWidth(), musicList.getHeight(), mouseX, mouseY) && GuiUtils.isMouseInside(x, y, 18, 18, mouseX, mouseY)) {
					IIcon icon = playing && !paused ? Icons.PAUSE : Icons.PLAY;
					TextureUtils.bindTexture(icon.getIconAsset());
					gui.drawScaledCustomSizeModalRect(x + 4, y + 4, icon.getU(), icon.getV(), icon.getIconSize(), icon.getIconSize(), 10, 10, icon.getSourceWidth(), icon.getSourceHeight());
				} else if (playing) {
					IIcon icon = paused ? Icons.VOLUME_OFF : Icons.VOLUME_ON;
					TextureUtils.bindTexture(icon.getIconAsset());
					gui.drawRect(x + 2, y + 2, x + 16, y + 16, 0xdd999999);
					gui.drawScaledCustomSizeModalRect(x + 4, y + 4, icon.getU(), icon.getV(), icon.getIconSize(), icon.getIconSize(), 10, 10, icon.getSourceWidth(), icon.getSourceHeight());
				}

				if (title != null) {
					RenderUtil.drawStringClipped(title, x + 20, centerY - mc.fontRenderer.FONT_HEIGHT / 2, width - 22, fontColor, false);
					RenderUtil.drawStringClipped(name, x + 20, centerY + mc.fontRenderer.FONT_HEIGHT / 2, width - 22, fontColor, false);
				} else {
					RenderUtil.drawStringClipped(name, x + 20, centerY, width - 22, fontColor, false);
				}
			}
		});
		musicList.setItemClickListener((track, index, mouseButton) -> {
			if (mouseX - musicList.xPosition < 20) {
				if (this.getPlayingTrack() != track) {
					this.stopPlayingTrack();
					this.pause(false);
					this.setPlayingTrack(track);
				} else {
					this.pause(!this.paused);
				}
			}
			selectedTrack = track;
		});
		main.addComponent(musicList);

		mainVolumeSlider = new Slider(playlistList.left + playlistList.getWidth() + 30, 6, 100);
		mainVolumeSlider.setPercentage(this.volume);
		mainVolumeSlider.setSliderColor(new Color(0xEBEBEB));
		mainVolumeSlider.setSlideListener((percentage) -> {
			this.setVolume(percentage);
		});
		main.addComponent(mainVolumeSlider);

		musicSearchBar = new TextField(musicList.left, musicList.top - 16 - 5, musicList.getWidth());
		musicSearchBar.setPlaceholder(I18n.format("app.ocm.craytunes.search_tracks"));
		musicSearchBar.setKeyListener((c) -> {
			String text = musicSearchBar.getText();
			if (!StringUtils.isNullOrEmpty(text)) {
				Predicate<SoundTrack> filter = track -> org.apache.commons.lang3.StringUtils.containsIgnoreCase(track.getSoundLocation().getResourceDomain(), text) || org.apache.commons.lang3.StringUtils.containsIgnoreCase(track.getSoundLocation().getResourcePath(), text) || org.apache.commons.lang3.StringUtils.containsIgnoreCase(track.getSoundLocation().toString(), text) || org.apache.commons.lang3.StringUtils.containsIgnoreCase(("minecraft".equals(track.getSoundLocation().getResourceDomain()) ? "Minecraft" : Lib.getModName(track.getSoundLocation().getResourceDomain())), text);
				List<SoundTrack> filteredItems = this.musicListCopy.stream().filter(filter).collect(Collectors.toList());
				musicList.setItems(filteredItems);
			} else {
				musicList.setItems(musicListCopy);
			}
			return false;
		});
		main.addComponent(musicSearchBar);

		int pauseResumeX = playlistList.left + playlistList.getWidth() + 5;
		int pauseResumeY = 5;

		mainResume = new Button(pauseResumeX, pauseResumeY, Icons.PLAY);
		mainPause = new Button(pauseResumeX, pauseResumeY, Icons.PAUSE);

		mainResume.setClickListener((int mouseX, int mouseY, int mouseButton) -> {
			if (mouseButton == 0) {
				this.pause(false);
			}
		});
		mainPause.setClickListener((int mouseX, int mouseY, int mouseButton) -> {
			if (mouseButton == 0) {
				this.pause(true);
			}
		});
		main.addComponent(mainResume);
		main.addComponent(mainPause);

		setCurrentLayout(main);

		this.loadDefaults();
	}

	@Override
	public void onTick() {
		super.onTick();
		this.mainResume.setEnabled(this.paused);
		this.mainResume.setVisible(this.paused);
		this.mainPause.setEnabled(!this.paused);
		this.mainPause.setVisible(!this.paused);
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
		main.clear();
		main = null;
		this.stopPlayingTrack();
	}

	private void loadDefaults() {
		Map<String, Playlist> playlists = new HashMap<String, Playlist>();
		Playlist playlistAll = new Playlist("All", true);
		for (SoundEvent sound : SoundEvent.REGISTRY) {
			ResourceLocation name = sound.getSoundName();
			if (name != null) {
				Playlist playlist = playlists.get(name.getResourceDomain());
				if (playlist == null) {
					playlist = new Playlist(Lib.getModName(name.getResourceDomain()), true);
					playlists.put(name.getResourceDomain(), playlist);
				}
				playlist.add(sound.getSoundName());
				playlistAll.add(sound.getSoundName());
			}

		}
		for (String id : playlists.keySet()) {
			this.playlistList.addItem(playlists.get(id));
		}
		this.playlistList.addItem(playlistAll);
	}

	private void reloadSounds() {
		Playlist playlist = this.getCurrentPlaylist();
		if (playlist != null) {
			musicListCopy.clear();
			musicListCopy.addAll(playlist.getTracks());
			musicList.setItems(playlist.getTracks());
		}

		this.stopPlayingTrack();
	}

	private void stopPlayingTrack() {
		if (this.playingAudio != null) {
			this.playingAudio.stop();
			this.playingAudio = null;
			this.playingTrack = null;
		}
	}

	private void setPlayingTrack(SoundTrack track) {
		this.stopPlayingTrack();
		if (track != null) {
			this.playingTrack = track;
			this.playSound(track.getSoundLocation());
		}
	}

	private void setVolume(float volume) {
		this.volume = volume;
		if (this.playingAudio != null) {
			this.playingAudio.setVolume(volume);
		}
		this.markDirty();
	}

	private void pause(boolean paused) {
		this.paused = paused;
		if (this.playingAudio != null) {
			if (paused) {
				Minecraft.getMinecraft().getSoundHandler().pauseSounds();
			} else {
				Minecraft.getMinecraft().getSoundHandler().resumeSounds();
			}
		}
	}

	private void playSound(ResourceLocation location) {
		this.playingAudio = new CraytunesAudio(location, this.volume);
		Minecraft.getMinecraft().getSoundHandler().playSound(this.playingAudio);
	}

	@Nullable
	private Playlist getCurrentPlaylist() {
		return this.selectedPlaylist == null ? null : this.selectedPlaylist;
	}

	@Nullable
	private SoundTrack getPlayingTrack() {
		return this.playingTrack == null ? null : this.playingTrack;
	}

	@Nullable
	private SoundTrack getSelectedTrack() {
		return this.selectedTrack == null ? null : this.selectedTrack;
	}

	@Override
	public void save(NBTTagCompound nbt) {
		nbt.setFloat("volume", volume);

		NBTTagList categories = new NBTTagList();
		for (Playlist playlist : this.playlistList) {
			if (!playlist.isModGenerated()) {
				categories.appendTag(playlist.serializeNBT());
			}
		}
		nbt.setTag("playlists", categories);
	}

	@Override
	public void load(NBTTagCompound nbt) {
		this.volume = nbt.getFloat("volume");
		this.mainVolumeSlider.setPercentage(volume);

		NBTTagList categories = nbt.getTagList("playlists", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < categories.tagCount(); i++) {
			this.playlistList.addItem(Playlist.fromTag(categories.getCompoundTagAt(i)));
		}
	}
}