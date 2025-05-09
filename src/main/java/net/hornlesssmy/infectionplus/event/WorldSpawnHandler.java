package net.hornlesssmy.infectionplus.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.hornlesssmy.infectionplus.InfectionPlus;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;

public class WorldSpawnHandler {
    public static void register() {
        ServerWorldEvents.LOAD.register((server, world) -> {
            if (world.getRegistryKey() == ServerWorld.OVERWORLD) {
                setNewWorldSpawn(world);
            }
        });
    }

    private static void setNewWorldSpawn(ServerWorld world) {
        // Get a random position within 500 blocks of origin (0,0)
        int x = world.getRandom().nextInt(1000) - 500;
        int z = world.getRandom().nextInt(1000) - 500;

        // Get the topmost solid block at this position
        int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z);

        // Ensure we're not in water or other liquids
        while (y > world.getBottomY() &&
                !world.getBlockState(new BlockPos(x, y - 1, z)).isSolid()) {
            y--;
        }

        // Set the new spawn point
        BlockPos newSpawn = new BlockPos(x, y, z);
        world.setSpawnPos(newSpawn, 0.0f);

        // Optional: Log the new spawn location
        InfectionPlus.LOGGER.info("Set new world spawn at: {}", newSpawn);
    }
}