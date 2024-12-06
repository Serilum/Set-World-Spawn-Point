package com.natamus.setworldspawnpoint.neoforge.events;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.setworldspawnpoint.events.WorldSpawnEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public class NeoForgeWorldSpawnEvent {
	@SubscribeEvent
	public static void onWorldLoad(LevelEvent.CreateSpawnPosition e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return;
		}

		if (WorldSpawnEvent.onWorldLoad((ServerLevel)level, (ServerLevelData)level.getLevelData())) {
			e.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void onPlayerRespawn(PlayerRespawnEvent e) {
		Player player = e.getEntity();
		if (player.level().isClientSide) {
			return;
		}
		
		WorldSpawnEvent.onPlayerRespawn(null, (ServerPlayer)player, false);
	}
	
	@SubscribeEvent
	public static void onEntityJoin(EntityJoinLevelEvent e) {
		WorldSpawnEvent.onEntityJoin(e.getLevel(), e.getEntity());
	}
}
