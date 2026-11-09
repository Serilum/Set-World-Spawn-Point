package com.serilum.setworldspawnpoint.util;

import com.natamus.collective.config.DuskConfigModToggles;
import com.natamus.collective.services.Services;

public class Util {
	public static boolean isOtherSpawnPointModEnabled() {
		return isSerilumModEnabled("villagespawnpoint") || isSerilumModEnabled("biomespawnpoint");
	}

	public static boolean isSerilumModEnabled(String modId) {
		return Services.MODLOADER.isModLoaded(modId) && !DuskConfigModToggles.isDisabledSerilumMod(modId);
	}
}
