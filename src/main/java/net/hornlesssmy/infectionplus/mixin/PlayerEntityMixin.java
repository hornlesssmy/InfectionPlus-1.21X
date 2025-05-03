package net.hornlesssmy.infectionplus.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Inject(method = "trySleep", at = @At("HEAD"), cancellable = true)
    private void handleSleep(CallbackInfoReturnable<ActionResult> cir) {
        PlayerEntity player = (PlayerEntity)(Object)this;

        // First verify the bed is valid and get position safely
        BlockPos sleepingPos = player.getSleepingPosition().orElse(null);
        if (sleepingPos == null) {
            return; // Let vanilla handle invalid cases
        }

        if (!player.getWorld().isClient) {
            player.sleep(sleepingPos);
            cir.setReturnValue(ActionResult.SUCCESS);
        } else {
            cir.setReturnValue(ActionResult.CONSUME);
        }
    }
}