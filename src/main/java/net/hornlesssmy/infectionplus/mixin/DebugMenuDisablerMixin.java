package net.hornlesssmy.infectionplus.mixin;

import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public abstract class DebugMenuDisablerMixin {
    @Inject(
            method = "onKey",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/DebugHud;toggleDebugHud()V"
            ),
            cancellable = true
    )
    private void disableDebugMenu(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (key == 292) { // 292 is F3 keycode
            ci.cancel();
        }
    }
}