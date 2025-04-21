package net.hornlesssmy.infectionplus.mixin;

import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void disableChatRendering(CallbackInfo ci) {
        ci.cancel(); // Prevents chat from rendering
    }
}