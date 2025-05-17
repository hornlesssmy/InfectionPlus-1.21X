package net.hornlesssmy.infectionplus.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin {
    @Unique
    private static final Map<String, Integer> infectionPlus$playerPoints = new ConcurrentHashMap<>();

    @Inject(method = "render", at = @At("HEAD"))
    private void modifyPlayerList(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() == null) return;

        List<PlayerListEntry> entries = new ArrayList<>(client.getNetworkHandler().getPlayerList());
        entries.sort((a, b) -> {
            int aPoints = infectionPlus$getPlayerPoints(a.getProfile().getName());
            int bPoints = infectionPlus$getPlayerPoints(b.getProfile().getName());
            return Integer.compare(bPoints, aPoints);
        });
    }

    @Unique
    private static int infectionPlus$getPlayerPoints(String playerName) {
        return infectionPlus$playerPoints.getOrDefault(playerName, 0);
    }

    @Unique
    public static void infectionPlus$updatePoints(Map<String, Integer> points) {
        infectionPlus$playerPoints.clear();
        infectionPlus$playerPoints.putAll(points);
    }
}