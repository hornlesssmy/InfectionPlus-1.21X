package net.hornlesssmy.infectionplus.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.hornlesssmy.infectionplus.InfectionPlus;
import net.minecraft.server.world.ServerWorld;

public class WorldSpawnHandler {
    public static void register() {
        ServerWorldEvents.LOAD.register((server, world) -> {
            if (world.getRegistryKey() == ServerWorld.OVERWORLD) {
                // Just log that the world loaded, spawn is now handled by PlayerJoinHandler
                InfectionPlus.LOGGER.info("Overworld loaded, spawn will be set when players join");
            }
        });
    }
}