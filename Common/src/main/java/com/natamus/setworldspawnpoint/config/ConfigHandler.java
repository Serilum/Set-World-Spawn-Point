package com.natamus.setworldspawnpoint.config;

import com.natamus.collective.config.DuskConfig;
import com.natamus.setworldspawnpoint.util.Reference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler extends DuskConfig {
	public static HashMap<String, List<String>> configMetaData = new HashMap<String, List<String>>();

	@Entry public static boolean _forceExactSpawn = true;
	@Entry(min = -100000, max = 100000) public static int xCoordSpawnPoint = 0;
	@Entry(min = -1, max = 256) public static int yCoordSpawnPoint = -1;
	@Entry(min = -100000, max = 100000) public static int zCoordSpawnPoint = 0;

	public static void initConfig() {
		configMetaData.put("_forceExactSpawn", Arrays.asList(
			"If enabled, spawns players on the exact world spawn instead of around it."
		));
		configMetaData.put("xCoordSpawnPoint", Arrays.asList(
			"The X coordinate of the spawn point of newly created worlds."
		));
		configMetaData.put("yCoordSpawnPoint", Arrays.asList(
			"The Y coordinate of the spawn point of newly created worlds. By default -1, which means it'll be the first solid block descending from y=256."
		));
		configMetaData.put("zCoordSpawnPoint", Arrays.asList(
			"The Z coordinate of the spawn point of newly created worlds."
		));

		DuskConfig.init(Reference.NAME, Reference.MOD_ID, ConfigHandler.class);
	}
}