package net.hornlesssmy.infectionplus.infection;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Handles infection progression timing through server ticks
 */
public class InfectionTickHandler {

    /**
     * Process infection for all players each server-tick.
     */
    public static void onServerTick(MinecraftServer server) {
        long currentTick = server.getOverworld().getTime();

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            InfectionManager.processPlayerInfection(player, currentTick);
        }
    }
}