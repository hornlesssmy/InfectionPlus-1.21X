package net.hornlesssmy.infectionplus.item.custom;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class GhostlyFishItem extends Item {

    public GhostlyFishItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof PlayerEntity player) {
            // Apply the food effects first
            super.finishUsing(stack, world, user);
            
            // Schedule the item to be replaced on the next tick
            world.getServer().execute(() -> {
                ItemStack newGhostlyFish = new ItemStack(this, 1);
                
                // Find the first empty slot or the slot that had this item
                for (int i = 0; i < player.getInventory().size(); i++) {
                    ItemStack slotStack = player.getInventory().getStack(i);
                    if (slotStack.isEmpty()) {
                        player.getInventory().setStack(i, newGhostlyFish);
                        break;
                    }
                }
                
                // Force inventory sync
                if (player instanceof ServerPlayerEntity serverPlayer) {
                    serverPlayer.currentScreenHandler.sendContentUpdates();
                }
            });
            
            return stack;
        }
        return super.finishUsing(stack, world, user);
    }
}