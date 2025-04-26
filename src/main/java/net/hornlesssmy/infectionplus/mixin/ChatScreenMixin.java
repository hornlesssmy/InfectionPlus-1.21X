package net.hornlesssmy.infectionplus.mixin;

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
    // Required constructor
    private ChatScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void preventChatCrash(CallbackInfo ci) {
        if (this.client != null) {
            ci.cancel(); // Safely close if somehow opened
        }
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void preventChatInit(CallbackInfo ci) {
        if (this.client != null && this.client.player != null) {
            this.client.player.sendMessage(Text.literal("Chat is currently disabled").formatted(Formatting.RED), false);
            ci.cancel();
        }
    }
}