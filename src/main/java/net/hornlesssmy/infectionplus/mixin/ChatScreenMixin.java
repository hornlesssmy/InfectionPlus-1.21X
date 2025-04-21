package net.hornlesssmy.infectionplus.mixin;

import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin {
    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void blockChatOpening(CallbackInfo ci) {
        ci.cancel(); // Prevents chat screen from opening
    }
}