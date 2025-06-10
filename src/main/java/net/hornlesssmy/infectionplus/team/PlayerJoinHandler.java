package net.hornlesssmy.infectionplus.team;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.hornlesssmy.infectionplus.infection.InfectionManager;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Random;

public class PlayerJoinHandler {
    private static final Random RANDOM = new Random();
    private static final double ZOMBIE_CHANCE = 0.15;

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.player;
            // Use the server parameter directly
            Scoreboard scoreboard = server.getScoreboard();

            // Initialize teams if not already done
            TeamManager.initializeTeams(server);

            Team humanTeam = scoreboard.getTeam(TeamManager.HUMAN_TEAM_NAME);
            Team zombieTeam = scoreboard.getTeam(TeamManager.ZOMBIE_TEAM_NAME);

            // Load infection data for returning player
            InfectionManager.loadPlayerData(player);

            // Check if player is new (no team assigned)
            Team currentTeam = scoreboard.getScoreHolderTeam(player.getNameForScoreboard());
            if (currentTeam == null) {
                // Check if there are any zombies
                boolean zombiesExist = server.getPlayerManager().getPlayerList().stream()
                        .anyMatch(p -> {
                            assert zombieTeam != null;
                            return zombieTeam.equals(scoreboard.getScoreHolderTeam(p.getNameForScoreboard()));
                        });

                if (!zombiesExist && RANDOM.nextDouble() < ZOMBIE_CHANCE) {
                    // Make this player the first zombie
                    TeamHandler.switchToZombieTeam(player);
                    server.getPlayerManager().broadcast(
                            Text.literal(player.getName().getString() + " has become the first Zombie!")
                                    .formatted(Formatting.DARK_RED),
                            false
                    );
                } else {
                    // Assign to the human team
                    TeamHandler.switchToHumanTeam(player);
                }
            }
        });
    }
}