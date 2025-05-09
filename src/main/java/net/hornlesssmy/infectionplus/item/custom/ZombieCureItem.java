package net.hornlesssmy.infectionplus.item.custom;

import net.hornlesssmy.infectionplus.effect.ModEffects;
import net.hornlesssmy.infectionplus.event.PlayerJoinHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
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
            if (player.hasStatusEffect((RegistryEntry<StatusEffect>) ModEffects.INFECTION_1) ||
                    player.hasStatusEffect((RegistryEntry<StatusEffect>) ModEffects.INFECTION_2) ||
                    player.hasStatusEffect((RegistryEntry<StatusEffect>) ModEffects.INFECTION_3) ||
                    player.hasStatusEffect((RegistryEntry<StatusEffect>) ModEffects.INFECTION_4) ||
                    player.hasStatusEffect((RegistryEntry<StatusEffect>) ModEffects.INFECTION_5)) {

                PlayerJoinHandler.cureInfection(player);
                player.sendMessage(Text.literal("You have been cured!"), false);
            }
        }
        return super.finishUsing(stack, world, user);
    }
}