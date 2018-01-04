package com.ocelot.gaming.apps;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Component;
import com.mrcrayfish.device.api.app.Icons;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.Layout.Background;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.ItemList;
import com.mrcrayfish.device.api.app.component.Slider;
import com.mrcrayfish.device.api.app.listener.ClickListener;
import com.mrcrayfish.device.api.app.listener.SlideListener;
import com.ocelot.gaming.apps.component.IdButton;
import com.ocelot.gaming.utils.SoundUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class ApplicationCrayTunes extends Application {

	private Background mainbg;
	private Color backgroundColor = Color.WHITE;

	private Layout main;
	private ItemList<String> musicList;
	private Button mainPlaySound;
	private Button mainStopSounds;
	private Button mainResume;
	private Button mainPause;
	private Slider mainVolumeSlider;

	private int selectedTab = 0;
	private float volume = 1;
	private boolean paused;
	private SoundEvent currentSound;

	private List<List<SoundEvent>> sounds;
	private List<SoundEvent> master;
	private List<SoundEvent> music;
	private List<SoundEvent> record;
	private List<SoundEvent> weather;
	private List<SoundEvent> items;
	private List<SoundEvent> blocks;
	private List<SoundEvent> entity;
	private List<SoundEvent> ambient;
	private List<SoundEvent> vanilla;
	private List<SoundEvent> modded;

	@Override
	public void init() {
		mainbg = new Background() {
			@Override
			public void render(Gui gui, Minecraft mc, int x, int y, int width, int height, int mouseX, int mouseY, boolean windowActive) {
				gui.drawRect(x, y, x + main.width, y + main.height, backgroundColor.getRGB());
			}
		};

		main = new Layout(250, 125);
		main.setBackground(mainbg);

		sounds = new ArrayList<List<SoundEvent>>();
		master = new ArrayList<SoundEvent>();
		music = new ArrayList<SoundEvent>();
		record = new ArrayList<SoundEvent>();
		weather = new ArrayList<SoundEvent>();
		items = new ArrayList<SoundEvent>();
		blocks = new ArrayList<SoundEvent>();
		entity = new ArrayList<SoundEvent>();
		ambient = new ArrayList<SoundEvent>();
		vanilla = new ArrayList<SoundEvent>();
		modded = new ArrayList<SoundEvent>();

		for (int i = 0; i < SoundEvent.REGISTRY.getKeys().size(); i++) {
			SoundEvent event = SoundEvent.REGISTRY.getObjectById(i);

			if (event.getRegistryName() != null) {
				if (event.getRegistryName().getResourcePath().split("music").length > 1) {
					music.add(event);
				} else if (event.getRegistryName().getResourcePath().split("record").length > 1) {
					record.add(event);
				} else if (event.getRegistryName().getResourcePath().split("weather").length > 1) {
					weather.add(event);
				} else if (event.getRegistryName().getResourcePath().split("item").length > 1 && event.getRegistryName().getResourcePath().split("entity").length == 1) {
					items.add(event);
				} else if (event.getRegistryName().getResourcePath().split("block").length > 1) {
					blocks.add(event);
				} else if (event.getRegistryName().getResourcePath().split("entity").length > 1) {
					entity.add(event);
				} else if (event.getRegistryName().getResourcePath().split("ambient").length > 1) {
					ambient.add(event);
				} else {
					master.add(event);
				}

				if (event.getRegistryName().getResourceDomain().equals("minecraft")) {
					vanilla.add(event);
				} else {
					modded.add(event);
				}
			}
		}

		sounds.add(master);
		sounds.add(music);
		sounds.add(record);
		sounds.add(weather);
		sounds.add(items);
		sounds.add(blocks);
		sounds.add(entity);
		sounds.add(ambient);
		sounds.add(vanilla);
		sounds.add(modded);

		paused = false;

		musicList = new ItemList<String>(5, 42, main.width - (5 + 40), (main.height - 50) / 14);
		reloadSounds();
		main.addComponent(musicList);

		mainPlaySound = new Button(main.width - 35, main.height - 25, 30, 16, I18n.format("gui.play"));
		mainPlaySound.setClickListener(new ClickListener() {
			@Override
			public void onClick(Component c, int mouseButton) {
				if (mouseButton == 0) {
					if (musicList.getSelectedIndex() != -1 && sounds.get(selectedTab) != null && sounds.get(selectedTab).get(musicList.getSelectedIndex()) != null) {
						SoundUtils.stopAllSounds();
						Minecraft.getMinecraft().player.world.playSound(Minecraft.getMinecraft().player, Minecraft.getMinecraft().player.getPosition(), sounds.get(selectedTab).get(musicList.getSelectedIndex()), SoundCategory.MASTER, 1, 1);
						main.setTitle(I18n.format("app.ocm.craytunes.name") + " - Now Playing " + musicList.getSelectedItem());
						currentSound = sounds.get(selectedTab).get(musicList.getSelectedIndex());
					}
				}
			}
		});
		main.addComponent(mainPlaySound);

		mainStopSounds = new Button(main.width - 35, main.height - 45, 30, 16, I18n.format("gui.stop"));
		mainStopSounds.setClickListener(new ClickListener() {
			@Override
			public void onClick(Component c, int mouseButton) {
				if (mouseButton == 0) {
					SoundUtils.stopAllSounds();
				}
			}
		});
		main.addComponent(mainStopSounds);

		for (int i = 0; i < sounds.size(); i++) {
			final IdButton button = new IdButton(i, 5 + 18 * i, 25);
			button.setToolTip(I18n.format("app.ocm.craytunes.tab" + i), I18n.format("app.ocm.craytunes.tab.tooltip"));
			button.setClickListener(new ClickListener() {
				@Override
				public void onClick(Component c, int mouseButton) {
					if (mouseButton == 0) {
						musicList.removeAll();
						musicList.setSelectedIndex(-1);
						selectedTab = button.getId();
						reloadSounds();
						markDirty();
					}
				}
			});
			main.addComponent(button);
		}

		mainVolumeSlider = new Slider(45, 5, 100);
		mainVolumeSlider.setSliderColour(new Color(0xEBEBEB));
		mainVolumeSlider.setSlideListener(new SlideListener() {
			@Override
			public void onSlide(float percentage) {
				volume = percentage;
				markDirty();
			}
		});
		main.addComponent(mainVolumeSlider);

		mainResume = new Button(23, 5, Icons.PLAY);
		mainPause = new Button(23, 5, Icons.PAUSE);

		mainResume.setClickListener(new ClickListener() {
			@Override
			public void onClick(Component c, int mouseButton) {
				if (mouseButton == 0) {
					paused = false;
				}
			}
		});
		mainPause.setClickListener(new ClickListener() {
			@Override
			public void onClick(Component c, int mouseButton) {
				if (mouseButton == 0) {
					paused = true;
				}
			}
		});
		main.addComponent(mainResume);
		main.addComponent(mainPause);

		setCurrentLayout(main);
	}

	public void reloadSounds() {
		for (SoundEvent sound : sounds.get(selectedTab)) {
			if (selectedTab == 2) {
				musicList.addItem(I18n.format("item.record." + sound.getSoundName().getResourcePath().substring(7) + ".desc"));
			} else {
				if (!I18n.format("subtitles." + sound.getSoundName().getResourcePath()).equalsIgnoreCase("subtitles." + sound.getSoundName().getResourcePath())) {
					musicList.addItem(I18n.format("subtitles." + sound.getSoundName().getResourcePath()));
				} else {
					musicList.addItem(I18n.format(sound.getSoundName().getResourcePath()));
				}
			}
		}
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

		Minecraft.getMinecraft().getSoundHandler().setSoundLevel(SoundCategory.MASTER, volume + 0.01f);
	}

	@Override
	public void handleKeyTyped(char character, int code) {
		super.handleKeyTyped(character, code);
	}

	@Override
	public void onClose() {
		super.onClose();
		SoundUtils.stopAllSounds();
		SoundUtils.resumeAllSounds();
		Minecraft.getMinecraft().getSoundHandler().setSoundLevel(SoundCategory.MASTER, Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.MASTER));
	}

	@Override
	public void load(NBTTagCompound nbt) {
		volume = nbt.getFloat("volume");
		mainVolumeSlider.setPercentage(volume);
	}

	@Override
	public void save(NBTTagCompound nbt) {
		nbt.setFloat("volume", volume);
	}
}