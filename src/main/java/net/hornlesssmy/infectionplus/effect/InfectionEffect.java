package net.hornlesssmy.infectionplus.effect;

import net.hornlesssmy.infectionplus.InfectionPlus;
import net.hornlesssmy.infectionplus.event.InfectionEffectHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;

public class InfectionEffect extends StatusEffect {
    public InfectionEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof ServerPlayerEntity player) {
            Team team = player.getScoreboardTeam();
            if (team != null && team.getName().equals(InfectionPlus.ZOMBIE_TEAM_NAME)) {
                player.removeStatusEffect((RegistryEntry<StatusEffect>) this);
                return false;
            }

            // Reapply the effect constantly to prevent removal
            player.addStatusEffect(new StatusEffectInstance(
                    (RegistryEntry<StatusEffect>) this, // Use 'this' directly
                    InfectionEffectHandler.INFECTION_DURATION,
                    amplifier,
                    false,
                    false
            ));


            // Apply stage-specific effects
            if (this == ModEffects.INFECTION_1) {
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.HUNGER, 40, 0, false, false));
            }
            else if (this == ModEffects.INFECTION_2) {
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.HUNGER, 40, 0, false, false));
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.SLOWNESS, 40, 0, false, false));
            }
            else if (this == ModEffects.INFECTION_3) {
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.HUNGER, 40, 1, false, false));
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.SLOWNESS, 40, 0, false, false));
            }
            else if (this == ModEffects.INFECTION_4) {
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.HUNGER, 40, 1, false, false));
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.SLOWNESS, 40, 1, false, false));
            }
            else if (this == ModEffects.INFECTION_5) {
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.HUNGER, 40, 2, false, false));
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.SLOWNESS, 40, 1, false, false));
            }
        }
        return true;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    // Prevent effect removal by milk
    public boolean isRemovable() {
        return false;
    }
}