package com.ocelot.gaming;

import net.minecraft.util.ResourceLocation;

/**
 * This class holds all the gaming mod information.<br>
 * <em><b>Copyright (c) 2017 Ocelot5836.</b></em>
 * 
 * @author Ocelot5836
 */
public class Reference {

	/** The id for the gaming mod. */
	public static final String MOD_ID = "ocm";
	/** The name for the gaming mod. */
	public static final String NAME = "Ocelot5836's Genaric Applications Mod";
	/** The version of the gaming mod. */
	public static final String VERSION = "0.0.3";
	/** The accepted minecraft versions for the gaming mod. */
	public static final String ACCEPTED_VERSIONS = "[1.12][1.12.1][1.12.2]";
	/** The dependencies for the gaming mod. */
	public static final String DEPENDENCIES = "required-after:cdm@[0.2.0,)";

	public static final ResourceLocation CRAYTUNES_TEXTURE = textureBase("craytunes");
	public static final ResourceLocation GAME_HANDLER_TEXTURE = textureBase("game_handler");

	private static ResourceLocation textureBase(String name) {
		return new ResourceLocation(Reference.MOD_ID, "textures/gui/" + name + ".png");
	}
}