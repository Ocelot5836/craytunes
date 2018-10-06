package com.ocelot.craytunes.app.craytunes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.mrcrayfish.device.api.app.Alphabet;
import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Dialog;
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
import com.ocelot.craytunes.app.component.SmoothItemList;
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
import net.minecraftforge.common.util.Constants;

public class ApplicationCrayTunes extends Application {

	private static ApplicationCrayTunes app;

	private Layout main;
	private SmoothItemList<Playlist> playlistList;
	private SmoothItemList<SoundTrack> musicList;
	private Button resume;
	private Button pause;
	private Slider volumeSlider;

	private TextField musicSearchBar;
	private Button newPlaylist;
	private Button addPlaylistTrack;

	private Playlist selectedPlaylist;
	private SoundTrack playingTrack;
	private SoundTrack selectedTrack;
	private boolean paused;
	private float volume;

	private CraytunesAudio playingAudio;
	private List<SoundTrack> musicListCopy;
	private int mouseX;
	private int mouseY;

	@Override
	public void init(@Nullable NBTTagCompound intent) {
		app = this;

		this.main = new Layout(362, 164);

		this.selectedPlaylist = null;
		this.playingTrack = null;
		this.selectedTrack = null;
		this.pause(false);
		this.setVolume(1.0F);

		this.playingAudio = null;
		this.musicListCopy = new ArrayList<SoundTrack>();

		this.playlistList = new SmoothItemList<Playlist>(0, 0, 80, main.height);
		this.playlistList.setScrollSpeed(25);
		this.playlistList.setListItemRenderer(new ListItemRenderer<Playlist>(12) {
			@Override
			public void render(Playlist playlist, Gui gui, Minecraft mc, int x, int y, int width, int height, boolean selected) {
				String name = StringUtils.isNullOrEmpty(playlist.getName()) ? I18n.format("app." + ApplicationCrayTunes.this.getInfo().getFormattedId() + ".playlist.default") : playlist.getName();

				if (selected) {
					gui.drawRect(x, y, x + width, y + height, Laptop.getSystem().getSettings().getColorScheme().itemHighlightColor);
				}

				RenderUtil.drawStringClipped(name, x + 2, (int) (y + 12f / 2f - (float) mc.fontRenderer.FONT_HEIGHT / 2f) + 1, width - 4, Laptop.getSystem().getSettings().getColorScheme().getTextColor(), false);
			}
		});
		this.playlistList.setItemClickListener((playlist, index, mouseButton) -> {
			if (this.getCurrentPlaylist() != playlist) {
				this.pause(true);
				this.selectedTrack = null;
				this.selectedPlaylist = playlist;
				this.musicSearchBar.clear();
				this.stopPlayingTrack();
				this.reloadSounds();
			}
		});
		this.main.addComponent(playlistList);

		this.musicList = new SmoothItemList<SoundTrack>(playlistList.left + playlistList.getWidth() + 5, 47, main.width - playlistList.left - playlistList.getWidth() - 10, main.height - 52);
		this.musicList.setScrollSpeed(50);
		this.musicList.setListItemRenderer(new ListItemRenderer<SoundTrack>(18) {
			@Override
			public void render(SoundTrack track, Gui gui, Minecraft mc, int x, int y, int width, int height, boolean selected) {
				String title = null;
				String name = "null";
				boolean playing = track == getPlayingTrack();
				selected = track == getSelectedTrack();

				int centerY = (int) (y + 18f / 2f - (float) mc.fontRenderer.FONT_HEIGHT / 2f) + 1;
				int fontColor = Laptop.getSystem().getSettings().getColorScheme().getTextColor();

				if (selected) {
					gui.drawRect(x, y, x + width, y + height, Laptop.getSystem().getSettings().getColorScheme().itemHighlightColor);
				}

				if (track != null) {
					ResourceLocation location = track.getSoundLocation();
					if (location != null) {
						title = Lib.getModName(location.getResourceDomain());
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
					RenderUtil.drawStringClipped(title, x + 20, centerY - mc.fontRenderer.FONT_HEIGHT / 2, width - 22, new Color(fontColor).darker().getRGB(), false);
					RenderUtil.drawStringClipped(name, x + 20, centerY + mc.fontRenderer.FONT_HEIGHT / 2, width - 22, fontColor, false);
				} else {
					RenderUtil.drawStringClipped(name, x + 20, centerY, width - 22, fontColor, false);
				}
			}
		});
		this.musicList.setItemClickListener((track, index, mouseButton) -> {
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
		this.main.addComponent(musicList);

		this.volumeSlider = new Slider(playlistList.left + playlistList.getWidth() + 30, 6, 100);
		this.volumeSlider.setPercentage(this.volume);
		this.volumeSlider.setSlideListener((percentage) -> {
			this.setVolume(percentage);
		});
		this.main.addComponent(volumeSlider);

		this.musicSearchBar = new TextField(musicList.left, musicList.top - 16 - 5, musicList.getWidth());
		this.musicSearchBar.setPlaceholder(I18n.format("app." + ApplicationCrayTunes.this.getInfo().getFormattedId() + ".search_tracks"));
		this.musicSearchBar.setKeyListener((c) -> {
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
		this.main.addComponent(musicSearchBar);

		int pauseResumeX = playlistList.left + playlistList.getWidth() + 5;
		int buttonsY = 5;

		this.resume = new Button(pauseResumeX, buttonsY, Icons.PLAY);
		this.resume.setClickListener((mouseX, mouseY, mouseButton) -> {
			if (mouseButton == 0) {
				this.pause(false);
			}
		});
		this.main.addComponent(resume);

		pause = new Button(pauseResumeX, buttonsY, Icons.PAUSE);
		pause.setClickListener((mouseX, mouseY, mouseButton) -> {
			if (mouseButton == 0) {
				this.pause(true);
			}
		});
		this.main.addComponent(pause);

		this.newPlaylist = new Button(main.width - 21, buttonsY, Icons.NEW_FOLDER);
		this.newPlaylist.setToolTip(I18n.format("app." + this.getInfo().getFormattedId() + ".newPlaylist.tooltip.title"), I18n.format("app." + this.getInfo().getFormattedId() + ".newPlaylist.tooltip.subtitle"));
		this.newPlaylist.setClickListener((mouseX, mouseY, mouseButton) -> {
			if (mouseButton == 0) {
				this.openNewPlaylistDialog();
			}
		});
		this.main.addComponent(newPlaylist);

		this.addPlaylistTrack = new Button(main.width - 40, buttonsY, Icons.PLUS);
		this.addPlaylistTrack.setToolTip(I18n.format("app." + this.getInfo().getFormattedId() + ".addPlaylistTrack.tooltip.title"), I18n.format("app." + this.getInfo().getFormattedId() + ".addPlaylistTrack.tooltip.subtitle"));
		this.addPlaylistTrack.setClickListener((mouseX, mouseY, mouseButton) -> {
			if (mouseButton == 0) {
				this.openAddToCurrentPlaylistDialog();
			}
		});
		this.main.addComponent(addPlaylistTrack);

		this.setCurrentLayout(main);
		this.loadDefaults();
	}

	@Override
	public void onTick() {
		super.onTick();
		this.resume.setVisible(this.paused && this.selectedPlaylist != null);
		this.pause.setVisible(!this.paused && this.selectedPlaylist != null);
		this.volumeSlider.setVisible(this.selectedPlaylist != null);
		this.addPlaylistTrack.setVisible(this.selectedPlaylist != null && !this.selectedPlaylist.isModGenerated());
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
		app = null;
		this.stopPlayingTrack();
		this.main.clear();
		this.main = null;
	}

	public void addPlaylist(String name) {
		Playlist playlist = new Playlist(name, false);
		this.playlistList.addItem(playlist);
		this.markDirty();
	}

	public void addPlaylistTrack(ResourceLocation soundLocation) {
		if (this.selectedPlaylist != null && !this.selectedPlaylist.isModGenerated()) {
			this.selectedPlaylist.add(soundLocation);
			this.reloadSounds();
			this.markDirty();
		}
	}

	public void openNewPlaylistDialog() {
		Dialog.Input dialog = new Dialog.Input(I18n.format("app." + this.getInfo().getFormattedId() + ".newPlaylist.dialog"));
		dialog.setResponseHandler((success, input) -> {
			if (success && !StringUtils.isNullOrEmpty(input)) {
				int id = 0;
				for (int i = 0; i < this.playlistList.size(); i++) {
					Playlist playlist = this.playlistList.getItem(i);
					if (!StringUtils.isNullOrEmpty(playlist.getName()) && (playlist.getName().equals(input)) || playlist.getName().equals(input + " (" + id + ")")) {
						id++;
					}
				}

				this.addPlaylist(id > 0 ? input + " (" + id + ")" : input);
				return true;
			}
			return false;
		});
		this.openDialog(dialog);
	}

	public void openAddToCurrentPlaylistDialog() {
		Dialog.Input dialog = new Dialog.Input(I18n.format("app." + this.getInfo().getFormattedId() + ".addPlaylistTrack.dialog"));
		dialog.setResponseHandler((success, input) -> {
			if (success && !StringUtils.isNullOrEmpty(input)) {
				ResourceLocation soundLocation = new ResourceLocation(input);
				if (SoundEvent.REGISTRY.containsKey(soundLocation)) {
					this.addPlaylistTrack(soundLocation);
					return true;
				}
			}
			return false;
		});
		this.openDialog(dialog);
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
			musicList.removeAll();
			musicListCopy.addAll(playlist.getTracks());
			musicList.setItems(playlist.getTracks());
		}

		this.stopPlayingTrack();
	}

	private void stopPlayingTrack() {
		if (this.playingAudio != null) {
			this.playingAudio.stop();
			this.playingAudio = null;
		}
		this.playingTrack = null;
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
		this.volumeSlider.setPercentage(volume);

		NBTTagList categories = nbt.getTagList("playlists", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < categories.tagCount(); i++) {
			this.playlistList.addItem(Playlist.fromTag(categories.getCompoundTagAt(i)));
		}
	}

	public static ApplicationCrayTunes getApp() {
		return app;
	}
}