package net.hornlesssmy.infectionplus.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class SleepingPlayerEntityMixin {
    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true
    )
    private void preventSleepCrash(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (player.isSleeping() && player.getWorld().isClient) {
            ci.cancel(); // Prevent client-side sleep behavior
        }
    }
}