package com.natamus.setworldspawnpoint.forge.events;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.setworldspawnpoint.events.WorldSpawnEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ForgeWorldSpawnEvent {
	@SubscribeEvent
	public void onWorldLoad(LevelEvent.CreateSpawnPosition e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return;
		}

		if (WorldSpawnEvent.onWorldLoad((ServerLevel)level, (ServerLevelData)level.getLevelData())) {
			e.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		Player player = e.getEntity();
		if (player.level.isClientSide) {
			return;
		}
		
		WorldSpawnEvent.onPlayerRespawn(null, (ServerPlayer)player, false);
	}
	
	@SubscribeEvent
	public void onEntityJoin(EntityJoinLevelEvent e) {
		WorldSpawnEvent.onEntityJoin(e.getLevel(), e.getEntity());
	}
}
