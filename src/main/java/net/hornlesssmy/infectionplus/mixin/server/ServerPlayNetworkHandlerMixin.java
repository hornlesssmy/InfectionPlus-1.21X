package net.hornlesssmy.infectionplus.mixin.server;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Inject(
            method = "handleDecoratedMessage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void blockAllChat(SignedMessage message, CallbackInfo ci) {
        ci.cancel();

        // Get the player instance
        ServerPlayNetworkHandler handler = (ServerPlayNetworkHandler)(Object)this;

        // Send system message (bypasses chat restrictions)
        handler.getPlayer().sendMessage(
                Text.literal("Chat is disabled").formatted(Formatting.RED),
                false // Not in action bar
        );
    }
}