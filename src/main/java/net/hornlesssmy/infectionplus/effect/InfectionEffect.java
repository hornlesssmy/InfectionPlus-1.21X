package net.hornlesssmy.infectionplus.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.hornlesssmy.infectionplus.team.TeamHandler;

public class InfectionEffect extends StatusEffect {
    public InfectionEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof ServerPlayerEntity player) {
            // Apply constant effects while infected
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.HUNGER,
                    40,  // Short duration that gets refreshed every tick
                    0,   // Amplifier I
                    false,
                    false
            ));

            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SLOWNESS,
                    40,
                    0,
                    false,
                    false
            ));

            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.WEAKNESS,
                    40,
                    0,
                    false,
                    false
            ));

            // When effect expires, convert to zombie
            if (player.getStatusEffect(ModEffects.INFECTION.value()) != null &&
                    player.getStatusEffect(ModEffects.INFECTION.value()).getDuration() <= 1) {
                TeamHandler.switchToZombieTeam(player);
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    // For Minecraft 1.21+, use the new method name if isRemovable() is deprecated
    @Override
    public boolean isBeneficial() {
        return false; // Makes the effect appear as harmful
    }

    // Alternative approach if you need to prevent removal
    @Override
    public void onRemoved(LivingEntity entity) {
        // Prevent removal by reapplying if needed
        if (entity instanceof ServerPlayerEntity player) {
            player.addStatusEffect(new StatusEffectInstance(
                    ModEffects.INFECTION.value(),
                    100, // Short duration to be extended in applyUpdateEffect
                    0,
                    false,
                    false
            ));
        }
    }
}