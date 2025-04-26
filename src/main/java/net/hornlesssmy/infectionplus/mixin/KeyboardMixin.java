package net.hornlesssmy.infectionplus.mixin;


import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    private void blockChatKeys(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        // Block both T and / keys
        if ((key == GLFW.GLFW_KEY_T || key == GLFW.GLFW_KEY_SLASH) && action == GLFW.GLFW_PRESS) {
            client.player.sendMessage(
                    Text.literal("Chat and commands are disabled").formatted(Formatting.RED),
                    false
            );
            ci.cancel();
        }
    }
}