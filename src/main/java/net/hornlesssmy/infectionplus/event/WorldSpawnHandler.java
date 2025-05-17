package net.hornlesssmy.infectionplus.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.hornlesssmy.infectionplus.InfectionPlus;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;

public class WorldSpawnHandler {
    private static final int MAX_ATTEMPTS = 50;
    private static final int SEARCH_RADIUS = 500;
    private static final int MIN_SKY_VISIBILITY = 10;

    public static void register() {
        ServerWorldEvents.LOAD.register((server, world) -> {
            if (world.getRegistryKey() == ServerWorld.OVERWORLD) {
                setNewWorldSpawn(world);
            }
        });
    }

    private static void setNewWorldSpawn(ServerWorld world) {
        BlockPos newSpawn = findValidSpawnPosition(world);
        if (newSpawn != null) {
            world.setSpawnPos(newSpawn, 0.0f);
            InfectionPlus.LOGGER.info("Set new world spawn at: {}", newSpawn);
        } else {
            InfectionPlus.LOGGER.warn("Failed to find valid spawn location, using default");
        }
    }

    private static BlockPos findValidSpawnPosition(ServerWorld world) {
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            int x = world.getRandom().nextInt(SEARCH_RADIUS * 2) - SEARCH_RADIUS;
            int z = world.getRandom().nextInt(SEARCH_RADIUS * 2) - SEARCH_RADIUS;
            int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z);
            BlockPos candidatePos = new BlockPos(x, y, z);

            if (isValidSpawnPosition(world, candidatePos)) {
                return candidatePos;
            }
        }
        return null;
    }

    private static boolean isValidSpawnPosition(ServerWorld world, BlockPos pos) {
        // Check if the block below is solid
        if (!world.getBlockState(pos.down()).isSolidBlock(world, pos.down())) {
            return false;
        }

        // Check for open sky above
        for (int i = 1; i <= MIN_SKY_VISIBILITY; i++) {
            if (world.getBlockState(pos.up(i)).isOpaque()) {
                return false;
            }
        }

        // Check for liquids using modern methods
        return !world.getBlockState(pos).isLiquid() &&
                !world.getBlockState(pos.up()).isLiquid() &&
                world.isSkyVisible(pos);
    }
}