package net.hornlesssmy.infectionplus.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.hornlesssmy.infectionplus.InfectionPlus;
import net.hornlesssmy.infectionplus.effect.InfectionEffect;
import net.hornlesssmy.infectionplus.points.PointsManager;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.scoreboard.ScoreHolder;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;

import java.util.Objects;

public class PlayerDeathHandler {
    public static void onPlayerDeath(ServerPlayerEntity player, DamageSource source) {
        Scoreboard scoreboard = player.getScoreboard();

        if (scoreboard.getNullableObjective("deaths") == null) {
            scoreboard.addObjective(
                    "deaths",
                    ScoreboardCriterion.DUMMY,
                    Text.literal("Deaths"),
                    ScoreboardCriterion.RenderType.INTEGER,
                    false,
                    null
            );
        }

        String deathKey = player.getNameForScoreboard();
        int deaths = Objects.requireNonNull(scoreboard.getScore(ScoreHolder.fromName(deathKey), scoreboard.getNullableObjective("deaths"))).getScore();
        deaths++;
        scoreboard.getOrCreateScore(ScoreHolder.fromName(deathKey), scoreboard.getNullableObjective("deaths")).setScore(deaths);

        if (deaths % 20 == 0) {
            PointsManager.addPoints(player, -1);
        }
        if (source.getSource() instanceof ServerPlayerEntity) {
            if (deaths % 2 == 0) {
                PointsManager.updateWorth(player);
            }
        } else {
            if (deaths % 10 == 0) {
                PointsManager.updateWorth(player);
            }
        }
    }

    public static void register() {
        ServerPlayerEvents.ALLOW_DEATH.register((player, source, amount) -> {
            if (player.getWorld().isClient) return true;

            handleInfectionPersistence(player);
            handleInfectionSpread(player, source);

            return !hasInfection(player);
        });
    }

    private static boolean hasInfection(ServerPlayerEntity player) {
        return player.hasStatusEffect(InfectionEffect.INFECTION_1) ||
                player.hasStatusEffect(InfectionEffect.INFECTION_2) ||
                player.hasStatusEffect(InfectionEffect.INFECTION_3) ||
                player.hasStatusEffect(InfectionEffect.INFECTION_4) ||
                player.hasStatusEffect(InfectionEffect.INFECTION_5);
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
        if (player.hasStatusEffect(InfectionEffect.INFECTION_5)) return player.getStatusEffect(InfectionEffect.INFECTION_5);
        if (player.hasStatusEffect(InfectionEffect.INFECTION_4)) return player.getStatusEffect(InfectionEffect.INFECTION_4);
        if (player.hasStatusEffect(InfectionEffect.INFECTION_3)) return player.getStatusEffect(InfectionEffect.INFECTION_3);
        if (player.hasStatusEffect(InfectionEffect.INFECTION_2)) return player.getStatusEffect(InfectionEffect.INFECTION_2);
        if (player.hasStatusEffect(InfectionEffect.INFECTION_1)) return player.getStatusEffect(InfectionEffect.INFECTION_1);
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
                        InfectionEffect.INFECTION_1,
                        InfectionEffectHandler.INFECTION_DURATION,
                        0,
                        false,
                        true
                ));
            }
        }
    }
}