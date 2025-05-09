package net.hornlesssmy.infectionplus.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void modifyPlayerList(CallbackInfo ci) {
        PlayerListHud hud = (PlayerListHud)(Object)this;
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.getNetworkHandler() == null) return;

        List<PlayerListEntry> entries = new ArrayList<>(client.getNetworkHandler().getPlayerList());
        entries.sort((a, b) -> {
            int aPoints = getPlayerPoints(a.getProfile().getName());
            int bPoints = getPlayerPoints(b.getProfile().getName());
            return Integer.compare(bPoints, aPoints);
        });
    }

    private int getPlayerPoints(String playerName) {
        // Implement your point lookup logic here
        return 0; // Placeholder
    }
}