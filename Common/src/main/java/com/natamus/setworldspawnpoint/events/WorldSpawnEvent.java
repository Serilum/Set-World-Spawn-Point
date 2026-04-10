package com.natamus.setworldspawnpoint.events;

import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.functions.PlayerFunctions;
import com.natamus.collective.services.Services;
import com.natamus.setworldspawnpoint.config.ConfigHandler;
import com.natamus.setworldspawnpoint.util.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.storage.LevelData.RespawnData;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;

public class WorldSpawnEvent {
	public static boolean onWorldLoad(ServerLevel serverLevel, ServerLevelData serverLevelData) {
		if (Services.MODLOADER.isModLoaded("villagespawnpoint") || Services.MODLOADER.isModLoaded("biomespawnpoint")) {
			return false;
		}

		int x = ConfigHandler.xCoordSpawnPoint;
		int y = ConfigHandler.yCoordSpawnPoint;
		int z = ConfigHandler.zCoordSpawnPoint;
		
		if (y < 0) {
			BlockPos surfacePos = BlockPosFunctions.getSurfaceBlockPos(serverLevel, x, z);
			y = surfacePos.getY();
		}
		
		BlockPos spawnPos = new BlockPos(x, y, z);

		RespawnData previousRespawnData = serverLevel.getRespawnData();
		RespawnData newRespawnData = RespawnData.of(previousRespawnData.dimension(), spawnPos, previousRespawnData.yaw(), previousRespawnData.pitch());

		serverLevel.setRespawnData(newRespawnData);
		return true;
	}
	
	public static void onPlayerRespawn(ServerPlayer oldPlayer, ServerPlayer player, boolean alive) {
		ServerLevel serverLevel = player.level();
		if (ConfigHandler._forceExactSpawn) {
			RespawnData respawnData = serverLevel.getRespawnData();

			BlockPos respawnlocation = respawnData.pos(); // get spawn point
			Vec3 respawnvec = new Vec3(respawnlocation.getX(), respawnlocation.getY(), respawnlocation.getZ());

			ServerPlayer.RespawnConfig respawnConfig = player.getRespawnConfig();
			if (respawnConfig != null) {
				BlockPos bedpos = respawnConfig.respawnData().pos();
				if (bedpos != null) {
					TeleportTransition optionalbed = player.findRespawnPositionAndUseSpawnBlock(true, TeleportTransition.DO_NOTHING);
					if (!optionalbed.missingRespawnBlock()) {
						Vec3 bedlocation = optionalbed.position();
						BlockPos bl = BlockPos.containing(bedlocation.x(), bedlocation.y(), bedlocation.z());

						Iterator<BlockPos> it = BlockPos.betweenClosedStream(bl.getX() - 1, bl.getY() - 1, bl.getZ() - 1, bl.getX() + 1, bl.getY() + 1, bl.getZ() + 1).iterator();
						while (it.hasNext()) {
							BlockPos np = it.next();
							BlockState state = serverLevel.getBlockState(np);
							Block block = state.getBlock();
							if (block instanceof BedBlock) {
								if (state.getValue(BedBlock.PART).equals(BedPart.FOOT)) {
									bedlocation = new Vec3(np.getX() + 0.5, np.getY(), np.getZ() + 0.5);
									break;
								}
							}
						}

						respawnvec = new Vec3(bedlocation.x(), bedlocation.y(), bedlocation.z());
					}
				}
			}
			
			player.teleportTo(respawnvec.x, respawnvec.y, respawnvec.z);
		}
	}
	
	public static void onEntityJoin(Level level, Entity entity) {
		if (level.isClientSide()) {
			return;
		}
		
		if (!ConfigHandler._forceExactSpawn) {
			return;
		}
		
		if (!(entity instanceof Player player)) {
			return;
		}

        if (!PlayerFunctions.isJoiningWorldForTheFirstTime(player, Reference.MOD_ID)) {
			return;
		}
		
		ServerLevel serverLevel = (ServerLevel)level;

		RespawnData respawnData = serverLevel.getRespawnData();
		
		BlockPos wspos = respawnData.pos();
		BlockPos ppos = player.blockPosition();
		BlockPos cpos = new BlockPos(ppos.getX(), wspos.getY(), ppos.getZ());
		
		if (cpos.closerThan(wspos, 50)) {
			player.teleportTo(wspos.getX(), wspos.getY(), wspos.getZ());
		}
	}
}
