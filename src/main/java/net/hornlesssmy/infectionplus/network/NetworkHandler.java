package net.hornlesssmy.infectionplus.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hornlesssmy.infectionplus.InfectionPlus;
import net.hornlesssmy.infectionplus.mixin.PlayerListHudMixin;
import net.minecraft.server.network.ServerPlayerEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NetworkHandler {
    public static void register() {
        // Server-side registration
        ServerPlayNetworking.registerGlobalReceiver(PointsSyncPacket.ID, (server, player, handler, buf, responseSender) -> {
            // Not needed on server
        });

        // Client-side registration
        ClientPlayNetworking.registerGlobalReceiver(PointsSyncPacket.ID, (client, handler, buf, responseSender) -> {
            PointsSyncPacket packet = PointsSyncPacket.decode(buf);
            client.execute(() -> {
                PlayerListHudMixin.infectionPlus$updatePoints(packet.getPlayerPoints());
            });
        });
    }

    public static void sendPointsUpdate(ServerPlayerEntity player) {
        Objects.requireNonNull(player, "Player cannot be null");
        Map<String, Integer> points = new HashMap<>();
        points.put(player.getName().getString(),
                PointsManager.getScore(player.getScoreboard(), player, InfectionPlus.POINTS_OBJ));

        ServerPlayNetworking.send(player, PointsSyncPacket.ID,
                new PointsSyncPacket(points).encode());
    }

    public static void sendAllPoints(ServerPlayerEntity player) {
        Objects.requireNonNull(player, "Player cannot be null");
        if (player.getServer() == null) {
            throw new IllegalStateException("Player is not connected to a server");
        }

        Map<String, Integer> points = new HashMap<>();
        player.getServer().getPlayerManager().getPlayerList().forEach(p -> {
            points.put(p.getName().getString(),
                    PointsManager.getScore(p.getScoreboard(), p, InfectionPlus.POINTS_OBJ));
        });

        ServerPlayNetworking.send(player, PointsSyncPacket.ID,
                new PointsSyncPacket(points).encode());
    }
}