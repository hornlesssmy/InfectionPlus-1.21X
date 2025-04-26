package net.hornlesssmy.infectionplus.event;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.hornlesssmy.infectionplus.InfectionPlus;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class PlayerJoinHandler {
    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            Scoreboard scoreboard = server.getScoreboard();

            // Debug: Log player join
            InfectionPlus.LOGGER.info("[Team System] Player joined: {}", player.getNameForScoreboard());

            Team humanTeam = scoreboard.getTeam(InfectionPlus.HUMAN_TEAM_NAME);

            if (humanTeam == null) {
                InfectionPlus.LOGGER.error("[Team System] Human team doesn't exist!");
                return;
            }

            Team currentTeam = scoreboard.getScoreHolderTeam(player.getNameForScoreboard());

            // Debug: Log current team status
            if (currentTeam != null) {
                InfectionPlus.LOGGER.info("[Team System] Player is already in team: {}", currentTeam.getName());
            }

            if (!humanTeam.equals(currentTeam)) {
                // Remove from any existing team first
                if (currentTeam != null) {
                    scoreboard.removeScoreHolderFromTeam(player.getNameForScoreboard(), currentTeam);
                }

                // Add to human team
                scoreboard.addScoreHolderToTeam(player.getNameForScoreboard(), humanTeam);
                InfectionPlus.LOGGER.info("[Team System] Successfully added {} to Human team", player.getNameForScoreboard());

                player.sendMessage(
                        Text.literal("You've been assigned to the Human team!").formatted(Formatting.AQUA),
                        false
                );
            } else {
                InfectionPlus.LOGGER.info("[Team System] Player was already in Human team");
            }
        });
    }
}