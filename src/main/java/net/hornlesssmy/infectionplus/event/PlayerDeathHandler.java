package net.hornlesssmy.infectionplus.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.hornlesssmy.infectionplus.InfectionPlus;
import net.hornlesssmy.infectionplus.effect.ModEffects;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.scoreboard.Team;

public class PlayerDeathHandler {
    public static void register() {
        ServerPlayerEvents.ALLOW_DEATH.register((player, source, amount) -> {
            if (player.getWorld().isClient) return true;

            // Infection persistence and spread logic
            handleInfectionPersistence(player);
            handleInfectionSpread(player, source);

            return !hasInfection(player); // Prevent death if infected
        });
    }

    private static boolean hasInfection(ServerPlayerEntity player) {
        return player.hasStatusEffect((RegistryEntry<StatusEffect>) ModEffects.INFECTION_1) ||
                player.hasStatusEffect((RegistryEntry<StatusEffect>) ModEffects.INFECTION_2) ||
                player.hasStatusEffect((RegistryEntry<StatusEffect>) ModEffects.INFECTION_3) ||
                player.hasStatusEffect((RegistryEntry<StatusEffect>) ModEffects.INFECTION_4) ||
                player.hasStatusEffect((RegistryEntry<StatusEffect>) ModEffects.INFECTION_5);
    }

    private static void handleInfectionPersistence(ServerPlayerEntity player) {
        if (hasInfection(player)) {
            ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
                StatusEffectInstance currentInfection = getCurrentInfection(oldPlayer);
                if (currentInfection != null) {
                    newPlayer.addStatusEffect(new StatusEffectInstance(
                            currentInfection.getEffectType(),
                            currentInfection.getDuration(),
                            currentInfection.getAmplifier(),
                            false,
                            false
                    ));
                }
            });
        }
    }

    private static StatusEffectInstance getCurrentInfection(ServerPlayerEntity player) {
        if (player.hasStatusEffect((RegistryEntry<StatusEffect>) ModEffects.INFECTION_5)) return player.getStatusEffect((RegistryEntry<StatusEffect>) ModEffects.INFECTION_5);
        if (player.hasStatusEffect((RegistryEntry<StatusEffect>) ModEffects.INFECTION_4)) return player.getStatusEffect((RegistryEntry<StatusEffect>) ModEffects.INFECTION_4);
        if (player.hasStatusEffect((RegistryEntry<StatusEffect>) ModEffects.INFECTION_3)) return player.getStatusEffect((RegistryEntry<StatusEffect>) ModEffects.INFECTION_3);
        if (player.hasStatusEffect((RegistryEntry<StatusEffect>) ModEffects.INFECTION_2)) return player.getStatusEffect((RegistryEntry<StatusEffect>) ModEffects.INFECTION_2);
        if (player.hasStatusEffect((RegistryEntry<StatusEffect>) ModEffects.INFECTION_1)) return player.getStatusEffect((RegistryEntry<StatusEffect>) ModEffects.INFECTION_1);
        return null;
    }

    private static void handleInfectionSpread(ServerPlayerEntity victim, DamageSource source) {
        if (source.getSource() instanceof ServerPlayerEntity killer) {
            Team killerTeam = killer.getScoreboardTeam();
            Team victimTeam = victim.getScoreboardTeam();

            if (killerTeam != null && victimTeam != null &&
                    killerTeam.getName().equals(InfectionPlus.ZOMBIE_TEAM_NAME) &&
                    victimTeam.getName().equals(InfectionPlus.HUMAN_TEAM_NAME)) {

                InfectionPlus.LOGGER.info("[Infection] Zombie {} killed human {}",
                        killer.getName().getString(),
                        victim.getName().getString());

                victim.addStatusEffect(new StatusEffectInstance(
                        ((RegistryEntry<StatusEffect>) ModEffects.INFECTION_1),
                        InfectionEffectHandler.INFECTION_DURATION,
                        0,
                        false,
                        true
                ));
            }
        }
    }
}