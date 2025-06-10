package net.hornlesssmy.infectionplus.infection;

import net.hornlesssmy.infectionplus.InfectionPlus;
import net.hornlesssmy.infectionplus.team.TeamHandler;
import net.hornlesssmy.infectionplus.team.TeamManager;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manager class for handling infection logic and player data persistence
 */
public class InfectionManager {
    private static final Map<UUID, InfectionData> playerInfections = new HashMap<>();
    private static final String NBT_KEY = "infectionplus:infection_data";

    /**
     * Infect a player with stage 1 infection
     */
    public static void infectPlayer(ServerPlayerEntity player, long currentTick) {
        // Only infect human team players
        Team team = player.getScoreboardTeam();
        if (team == null || !team.getName().equals(TeamManager.HUMAN_TEAM_NAME)) {
            return;
        }

        UUID playerId = player.getUuid();
        InfectionData data = new InfectionData(InfectionStage.getFirstStage(), currentTick);
        playerInfections.put(playerId, data);

        applyInfectionEffects(player, data.getCurrentStage());

        player.sendMessage(
                Text.literal("You have been infected! Stage 1/5")
                        .formatted(Formatting.RED),
                false
        );

        InfectionPlus.LOGGER.info("Player {} infected with stage 1", player.getName().getString());
    }

    /**
     * Remove infection from a player (called when using zombie cure)
     */
    public static void curePlayer(ServerPlayerEntity player) {
        UUID playerId = player.getUuid();
        InfectionData data = playerInfections.get(playerId);

        if (data != null && data.isInfected()) {
            data.clearInfection();
            playerInfections.put(playerId, data);

            // Remove infection effects
            removeInfectionEffects(player);

            player.sendMessage(
                    Text.literal("You have been cured of the infection!")
                            .formatted(Formatting.GREEN),
                    false
            );

            InfectionPlus.LOGGER.info("Player {} cured of infection", player.getName().getString());
        }
    }

    /**
     * Check if a player is infected
     */
    public static boolean isPlayerInfected(ServerPlayerEntity player) {
        InfectionData data = playerInfections.get(player.getUuid());
        return data != null && data.isInfected();
    }

    /**
     * Get player's current infection stage
     */
    public static InfectionStage getPlayerInfectionStage(ServerPlayerEntity player) {
        InfectionData data = playerInfections.get(player.getUuid());
        return data != null && data.isInfected() ? data.getCurrentStage() : null;
    }

    /**
     * Process infection progression for a player
     */
    public static void processPlayerInfection(ServerPlayerEntity player, long currentTick) {
        UUID playerId = player.getUuid();
        InfectionData data = playerInfections.get(playerId);

        if (data == null || !data.isInfected()) {
            return;
        }

        // Check if player should convert to zombie (after stage 5 expires)
        if (data.shouldConvertToZombie(currentTick)) {
            convertPlayerToZombie(player);
            return;
        }

        // Check if player should progress to next stage
        if (data.shouldProgressToNextStage(currentTick)) {
            InfectionStage currentStage = data.getCurrentStage();
            InfectionStage nextStage = currentStage.getNextStage();

            if (nextStage != null) {
                data.progressToNextStage(currentTick);
                applyInfectionEffects(player, nextStage);

                player.sendMessage(
                        Text.literal("Infection progressed to Stage " + nextStage.getStageNumber() + "/5")
                                .formatted(Formatting.DARK_RED),
                        false
                );

                InfectionPlus.LOGGER.info("Player {} infection progressed to stage {}",
                        player.getName().getString(), nextStage.getStageNumber());
            }
        }
    }

    /**
     * Apply status effects for a given infection stage
     */
    private static void applyInfectionEffects(ServerPlayerEntity player, InfectionStage stage) {
        // Remove existing infection effects first
        removeInfectionEffects(player);

        // Apply new stage effects
        for (int i = 0; i < stage.getEffects().length; i++) {
            player.addStatusEffect(new StatusEffectInstance(
                    stage.getEffects()[i],
                    InfectionStage.STAGE_DURATION_TICKS + 100, // Add buffer to ensure effects don't expire early
                    stage.getAmplifiers()[i],
                    true, // ambient
                    false
            ));
        }
    }

    /**
     * Remove all infection-related status effects
     */
    private static void removeInfectionEffects(ServerPlayerEntity player) {
        // Remove all possible infection effects
        player.removeStatusEffect(net.minecraft.entity.effect.StatusEffects.HUNGER);
        player.removeStatusEffect(net.minecraft.entity.effect.StatusEffects.SLOWNESS);
        player.removeStatusEffect(net.minecraft.entity.effect.StatusEffects.WEAKNESS);
    }

    /**
     * Convert infected player to the zombie team
     */
    private static void convertPlayerToZombie(ServerPlayerEntity player) {
        UUID playerId = player.getUuid();
        InfectionData data = playerInfections.get(playerId);

        if (data != null) {
            data.clearInfection();
            playerInfections.put(playerId, data);
        }

        // Remove infection effects and switch to the zombie team
        removeInfectionEffects(player);
        TeamHandler.switchToZombieTeam(player);

        player.sendMessage(
                Text.literal("The infection has consumed you! You are now a zombie!")
                        .formatted(Formatting.DARK_RED),
                false
        );

        // Broadcast to all players
        player.getServer().getPlayerManager().broadcast(
                Text.literal(player.getName().getString() + " has turned into a zombie!")
                        .formatted(Formatting.DARK_RED),
                false
        );

        InfectionPlus.LOGGER.info("Player {} converted to zombie due to infection", player.getName().getString());
    }

    /**
     * Save infection data to player NBT
     */
    public static void savePlayerData(ServerPlayerEntity player) {
        UUID playerId = player.getUuid();
        InfectionData data = playerInfections.get(playerId);

        if (data != null) {
            NbtCompound playerData = player.writeNbt(new NbtCompound());
            playerData.put(NBT_KEY, data.writeToNbt());
            player.readNbt(playerData);
        }
    }

    /**
     * Load infection data from player NBT
     */
    public static void loadPlayerData(ServerPlayerEntity player) {
        NbtCompound playerData = player.writeNbt(new NbtCompound());

        if (playerData.contains(NBT_KEY)) {
            NbtCompound infectionNbt = playerData.getCompound(NBT_KEY);
            InfectionData data = InfectionData.readFromNbt(infectionNbt);
            playerInfections.put(player.getUuid(), data);

            // Reapply effects if still infected
            if (data.isInfected()) {
                applyInfectionEffects(player, data.getCurrentStage());
            }
        }
    }

    /**
     * Clean up the data for disconnected player
     */
    public static void cleanupPlayerData(UUID playerId) {
        // Keep data for when player reconnects, don't remove it
        // playerInfections.remove(playerId);
    }
}