package net.hornlesssmy.infectionplus.team;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TeamHandler {
    public static void switchToTeam(ServerPlayerEntity player, String teamName) {
        Scoreboard scoreboard = player.getScoreboard();
        Team newTeam = scoreboard.getTeam(teamName);

        if (newTeam != null) {
            Team currentTeam = scoreboard.getScoreHolderTeam(player.getNameForScoreboard());
            if (currentTeam != null) {
                scoreboard.removeScoreHolderFromTeam(player.getNameForScoreboard(), currentTeam);
            }

            scoreboard.addScoreHolderToTeam(player.getNameForScoreboard(), newTeam);
            player.clearStatusEffects();

            // Apply team-specific effects
            if (teamName.equals(TeamManager.ZOMBIE_TEAM_NAME)) {
                applyZombieEffects(player);
            } else if (teamName.equals(TeamManager.ZOMBIE_TANK_TEAM_NAME)) {
                applyZombieTankEffects(player);
            }

            player.sendMessage(
                    Text.literal("You've been moved to the " + newTeam.getDisplayName().getString() + " team!")
                            .formatted(newTeam.getColor()),
                    false
            );
        }
    }

    public static void switchToHumanTeam(ServerPlayerEntity player) {
        switchToTeam(player, TeamManager.HUMAN_TEAM_NAME);
    }

    public static void switchToZombieTeam(ServerPlayerEntity player) {
        switchToTeam(player, TeamManager.ZOMBIE_TEAM_NAME);
    }

    public static void switchToZombieTankTeam(ServerPlayerEntity player) {
        switchToTeam(player, TeamManager.ZOMBIE_TANK_TEAM_NAME);
    }

    public static void applyZombieEffects(ServerPlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.HUNGER, -1, 1, true, true));
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.NIGHT_VISION, -1, 0, true, true));
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.RESISTANCE, -1, 0, true, true));
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.SLOWNESS, -1, 0, true, true));
    }

    public static void applyZombieTankEffects(ServerPlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.HUNGER, -1, 2, true, true));
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.NIGHT_VISION, -1, 0, true, true));
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.RESISTANCE, -1, 1, true, true));
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.SLOWNESS, -1, 1, true, true));
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.HEALTH_BOOST, -1, 10, true, true));
    }
}