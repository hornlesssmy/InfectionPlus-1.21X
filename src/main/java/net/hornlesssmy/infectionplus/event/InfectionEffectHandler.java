package net.hornlesssmy.infectionplus.event;

import net.hornlesssmy.infectionplus.effect.ModEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;

public class InfectionEffectHandler {
    public static final int INFECTION_DURATION = 20 * 60 * 96; // 1.6 hours in ticks

    public static void onEffectExpire(ServerPlayerEntity player, StatusEffectInstance effect) {
        if (effect.getEffectType().value() == ModEffects.INFECTION_1) {
            player.addStatusEffect(new StatusEffectInstance(
                    Registries.STATUS_EFFECT.getEntry(ModEffects.INFECTION_2),
                    INFECTION_DURATION, 0, false, false
            ));
        }
        else if (effect.getEffectType().value() == ModEffects.INFECTION_2) {
            player.addStatusEffect(new StatusEffectInstance(
                    Registries.STATUS_EFFECT.getEntry(ModEffects.INFECTION_3),
                    INFECTION_DURATION, 0, false, false
            ));
        }
        else if (effect.getEffectType().value() == ModEffects.INFECTION_3) {
            player.addStatusEffect(new StatusEffectInstance(
                    Registries.STATUS_EFFECT.getEntry(ModEffects.INFECTION_4),
                    INFECTION_DURATION, 0, false, false
            ));
        }
        else if (effect.getEffectType().value() == ModEffects.INFECTION_4) {
            player.addStatusEffect(new StatusEffectInstance(
                    Registries.STATUS_EFFECT.getEntry(ModEffects.INFECTION_5),
                    INFECTION_DURATION, 0, false, false
            ));
        }
    }
}