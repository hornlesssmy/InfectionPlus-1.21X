package net.hornlesssmy.infectionplus.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class PointsSyncPacket {
    public static final Identifier ID = Identifier.of("infectionplus", "points_sync");

    private final Map<String, Integer> playerPoints;

    public PointsSyncPacket(Map<String, Integer> playerPoints) {
        this.playerPoints = playerPoints;
    }

    public Map<String, Integer> getPlayerPoints() {
        return playerPoints;
    }

    public static PointsSyncPacket decode(Object buf) {
        Map<String, Integer> points = new HashMap<>();
        int size = buf.readVarInt();
        for (int i = 0; i < size; i++) {
            points.put(buf.readString(), buf.readVarInt());
        }
        return new PointsSyncPacket(points);
    }

    public PacketByteBuf encode() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(playerPoints.size());
        playerPoints.forEach((name, points) -> {
            buf.writeString(name);
            buf.writeVarInt(points);
        });
        return buf;
    }
}