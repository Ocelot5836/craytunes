package com.ocelot.craytunes.util;

import javax.annotation.Nullable;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class Lib {

	@Nullable
	public static ModContainer getMod(String modId) {
		return Loader.instance().getIndexedModList().get(modId);
	}
	
	@Nullable
	public static String getModName(String modId) {
		ModContainer container = getMod(modId);
		return container != null ? container.getName() : null;
	}
}