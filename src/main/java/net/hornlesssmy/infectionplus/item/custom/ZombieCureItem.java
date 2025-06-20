package net.hornlesssmy.infectionplus.item.custom;

import net.hornlesssmy.infectionplus.effect.ModEffects;
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
            // Remove infection effect if present
            if (player.hasStatusEffect(ModEffects.INFECTION)) {
                // Special removal that bypasses isRemovable()
                player.removeStatusEffect(ModEffects.INFECTION);
                player.sendMessage(
                        Text.literal("You have been cured of the infection!")
                                .formatted(Formatting.GREEN),
                        false
                );
            } else {
                player.sendMessage(
                        Text.literal("You are not infected, but the cure still provides healing.")
                                .formatted(Formatting.YELLOW),
                        false
                );
            }
        }
        return super.finishUsing(stack, world, user);
    }
}