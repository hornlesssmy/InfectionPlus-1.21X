package net.hornlesssmy.infectionplus.mixin;

import net.hornlesssmy.infectionplus.item.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerInventoryMixin {

    @Inject(method = "dropInventory", at = @At("HEAD"))
    private void removeThornvayneBeforeDrop(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        
        // Remove Thornvayne from inventory before dropping items
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.getItem() == ModItems.THORNVAYNE) {
                player.getInventory().setStack(i, ItemStack.EMPTY);
            }
        }
    }
}
