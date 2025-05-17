package net.hornlesssmy.infectionplus.item.custom;

import net.hornlesssmy.infectionplus.effect.InfectionEffect;
import net.hornlesssmy.infectionplus.event.PlayerJoinHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class ZombieCureItem extends Item {
    public ZombieCureItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof ServerPlayerEntity player) {
            if (hasAnyInfection(player)) {
                PlayerJoinHandler.cureInfection(player);
                player.sendMessage(Text.literal("You have been cured!"), false);
            }
        }
        return super.finishUsing(stack, world, user);
    }

    private boolean hasAnyInfection(ServerPlayerEntity player) {
        return player.hasStatusEffect(InfectionEffect.INFECTION_1) ||
                player.hasStatusEffect(InfectionEffect.INFECTION_2) ||
                player.hasStatusEffect(InfectionEffect.INFECTION_3) ||
                player.hasStatusEffect(InfectionEffect.INFECTION_4) ||
                player.hasStatusEffect(InfectionEffect.INFECTION_5);
    }
}