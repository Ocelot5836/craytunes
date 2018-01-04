package com.ocelot.gaming;

import com.mrcrayfish.device.api.ApplicationManager;
import com.ocelot.gaming.apps.ApplicationCrayTunes;
import com.ocelot.gaming.apps.game.ApplicationGameWindow;
import com.ocelot.gaming.apps.game.crayquest.GameCrayQuest;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

/**
 * The base mod class for the mod.<br>
 * <em><b>Copyright (c) 2017 Ocelot5836.</b></em>
 * 
 * @author Ocelot5836
 */
@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = Reference.ACCEPTED_VERSIONS, dependencies = Reference.DEPENDENCIES)
public class GenericApplications {

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		ApplicationManager.registerApplication(new ResourceLocation(Reference.MOD_ID, "craytunes"), ApplicationCrayTunes.class);

		ApplicationGameWindow.registerGame(new GameCrayQuest(), "CrayQuest");
		ApplicationManager.registerApplication(new ResourceLocation(Reference.MOD_ID, "game_handler"), ApplicationGameWindow.class);
	}
}