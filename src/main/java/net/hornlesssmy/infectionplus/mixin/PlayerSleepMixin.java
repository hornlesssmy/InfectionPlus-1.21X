package net.hornlesssmy.infectionplus.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerSleepMixin {
    @Inject(method = "trySleep",
            at = @At("HEAD"),
            cancellable = true)
    private void preventSleepChatTrigger(CallbackInfoReturnable<ActionResult> cir) {
        cir.setReturnValue(ActionResult.SUCCESS);
    }
}