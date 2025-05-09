package net.hornlesssmy.infectionplus.points;

import net.hornlesssmy.infectionplus.InfectionPlus;
import net.minecraft.scoreboard.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class PointsManager {
    public static void addPoints(ServerPlayerEntity player, int amount) {
        Scoreboard scoreboard = player.getScoreboard();
        ScoreboardObjective objective = getOrCreateObjective(scoreboard, InfectionPlus.POINTS_OBJ);
        scoreboard.getOrCreateScore(ScoreHolder.fromName(player.getNameForScoreboard()), objective)
                .incrementScore(amount);
    }

    public static void updateWorth(ServerPlayerEntity player) {
        Scoreboard scoreboard = player.getScoreboard();
        int baseWorth = getScore(scoreboard, player, InfectionPlus.BASE_WORTH_OBJ);
        int currentWorth = getScore(scoreboard, player, InfectionPlus.WORTH_OBJ);
        int newWorth = (currentWorth + baseWorth) / 2;
        setScore(scoreboard, player, InfectionPlus.WORTH_OBJ, newWorth);
    }

    public static void resetBaseWorth(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            Scoreboard scoreboard = player.getScoreboard();
            int currentWorth = getScore(scoreboard, player, InfectionPlus.WORTH_OBJ);
            setScore(scoreboard, player, InfectionPlus.BASE_WORTH_OBJ, currentWorth);
        }
    }

    private static int getScore(Scoreboard scoreboard, ServerPlayerEntity player, String objectiveName) {
        ScoreboardObjective objective = getOrCreateObjective(scoreboard, objectiveName);
        return scoreboard.getOrCreateScore(ScoreHolder.fromName(player.getNameForScoreboard()), objective)
                .getScore();
    }

    private static void setScore(Scoreboard scoreboard, ServerPlayerEntity player, String objectiveName, int value) {
        ScoreboardObjective objective = getOrCreateObjective(scoreboard, objectiveName);
        scoreboard.getOrCreateScore(ScoreHolder.fromName(player.getNameForScoreboard()), objective)
                .setScore(value);
    }

    private static ScoreboardObjective getOrCreateObjective(Scoreboard scoreboard, String name) {
        // Check if the "objectives" exist using the new method
        ScoreboardObjective objective = scoreboard.getNullableObjective(name);
        if (objective == null) {
            // Create new objective with required parameters for 1.21
            objective = scoreboard.addObjective(
                    name,
                    ScoreboardCriterion.DUMMY,
                    Text.literal(name),
                    ScoreboardCriterion.RenderType.INTEGER,
                    false,                      // display auto-update
                    null                        // number format (can be null)
            );
        }
        return objective;
    }

    public static void initializeObjectives(Scoreboard scoreboard) {
        getOrCreateObjective(scoreboard, InfectionPlus.POINTS_OBJ);
        getOrCreateObjective(scoreboard, InfectionPlus.WORTH_OBJ);
        getOrCreateObjective(scoreboard, InfectionPlus.BASE_WORTH_OBJ);
        InfectionPlus.LOGGER.info("[Scoreboard] Initialized objectives");
    }
}