package net.hornlesssmy.infectionplus.item.custom;

import net.hornlesssmy.infectionplus.infection.InfectionManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public class ZombieCureItem extends Item {
    public ZombieCureItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof ServerPlayerEntity player) {
            // Check if player is infected
            if (InfectionManager.isPlayerInfected(player)) {
                // Cure the infection
                InfectionManager.curePlayer(player);

                // Apply the regeneration effect from the food component
                return super.finishUsing(stack, world, user);
            } else {
                // Player is not infected
                player.sendMessage(
                        Text.literal("You are not infected, but the cure still provides healing.")
                                .formatted(Formatting.YELLOW),
                        false
                );

                // Still apply the regeneration effect
                return super.finishUsing(stack, world, user);
            }
        }
        return super.finishUsing(stack, world, user);
    }
}
