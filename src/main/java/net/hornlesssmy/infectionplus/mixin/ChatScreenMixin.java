package net.hornlesssmy.infectionplus.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {
    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void handleChatScreen(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();

        // Block all chat screen initialization
        if (client.player != null) {
            // Special case: Don't show message during sleep
            if (!client.player.isSleeping()) {
                client.player.sendMessage(
                        Text.literal("Chat is disabled").formatted(Formatting.RED),
                        false
                );
            }
            ci.cancel();
        }
    }
}