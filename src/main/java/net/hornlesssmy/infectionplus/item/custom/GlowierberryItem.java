package net.hornlesssmy.infectionplus.item.custom;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class GlowierberryItem extends Item {

    public GlowierberryItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof PlayerEntity player) {
            // Apply the food effects first
            ItemStack result = super.finishUsing(stack, world, user);
            // Find the slot that had the glow berry.
            int slotIndex = -1;
            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack slotStack = player.getInventory().getStack(i);
                if (slotStack.isEmpty() || (slotStack.getItem() == this && slotStack.getCount() == 1)) {
                    slotIndex = i;
                    break;
                }
            }
            // Replace it with a new berry immediately
            ItemStack newGlowierberry = new ItemStack(this, 1);
            if (slotIndex != -1) {
                player.getInventory().setStack(slotIndex, newGlowierberry);
            } else {
                // If we can't find the slot, try to insert or drop
                if (!player.getInventory().insertStack(newGlowierberry)) {
                    player.dropItem(newGlowierberry, false);
                }
            }
            // Sync the inventory to the client immediately
            if (player instanceof ServerPlayerEntity serverPlayer) {
                serverPlayer.currentScreenHandler.sendContentUpdates();
            }
            return result;
        }
        return super.finishUsing(stack, world, user);
    }
}