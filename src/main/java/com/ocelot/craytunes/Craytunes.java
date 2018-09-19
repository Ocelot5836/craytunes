package com.ocelot.craytunes;

import org.apache.logging.log4j.Logger;

import com.mrcrayfish.device.api.ApplicationManager;
import com.ocelot.craytunes.apps.craytunes.ApplicationCrayTunes;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author Ocelot5836
 */
@Mod(modid = Craytunes.MOD_ID, name = Craytunes.NAME, version = Craytunes.VERSION, useMetadata = true)
public class Craytunes {

	/** The id for the gaming mod. */
	public static final String MOD_ID = "craytunes";
	/** The name for the gaming mod. */
	public static final String NAME = "Craytunes";
	/** The version of the gaming mod. */
	public static final String VERSION = "2.0";

	private static Logger logger;

	@EventHandler
	public void pre(FMLPreInitializationEvent event) {
		logger = event.getModLog();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		ApplicationManager.registerApplication(new ResourceLocation(MOD_ID, "craytunes"), ApplicationCrayTunes.class);
	}

	public static Logger logger() {
		return logger;
	}
}