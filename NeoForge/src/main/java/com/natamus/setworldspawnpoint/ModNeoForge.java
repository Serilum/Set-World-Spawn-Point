package com.natamus.setworldspawnpoint;

import com.natamus.collective.check.RegisterMod;
import com.natamus.setworldspawnpoint.neoforge.config.IntegrateNeoForgeConfig;
import com.natamus.setworldspawnpoint.neoforge.events.NeoForgeWorldSpawnEvent;
import com.natamus.setworldspawnpoint.util.Reference;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod(Reference.MOD_ID)
public class ModNeoForge {
	
	public ModNeoForge(IEventBus modEventBus) {
		modEventBus.addListener(this::loadComplete);

		setGlobalConstants();
		ModCommon.init();

		IntegrateNeoForgeConfig.registerScreen(ModLoadingContext.get());

		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}

	private void loadComplete(final FMLLoadCompleteEvent event) {
		NeoForge.EVENT_BUS.register(NeoForgeWorldSpawnEvent.class);
	}

	private static void setGlobalConstants() {

	}
}