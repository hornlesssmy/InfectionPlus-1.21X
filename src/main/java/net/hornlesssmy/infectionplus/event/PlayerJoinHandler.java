package net.hornlesssmy.infectionplus.event;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.hornlesssmy.infectionplus.InfectionPlus;
import net.hornlesssmy.infectionplus.effect.InfectionEffect;
import net.hornlesssmy.infectionplus.points.PointsManager;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FluidState;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;

import java.util.Random;

public class PlayerJoinHandler {
    private static final Random RANDOM = new Random();
    private static final double ZOMBIE_CHANCE = 0.15;
    private static final int MAX_SPAWN_ATTEMPTS = 50;
    private static final int SPAWN_SEARCH_RADIUS = 500;
    private static final int MIN_SKY_VISIBILITY = 10;

    public static void onPlayerJoin(ServerPlayerEntity player) {
        long playTicks = player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.PLAY_TIME));
        if (playTicks >= 24000 * 14 &&
                player.getScoreboardTeam() != null &&
                player.getScoreboardTeam().getName().equals(InfectionPlus.HUMAN_TEAM_NAME))
        {
            PointsManager.addPoints(player, 4);
        }

        // Set new spawn location for the world
        setNewPlayerSpawn(player);
    }

    private static void setNewPlayerSpawn(ServerPlayerEntity player) {
        if (player.getWorld() instanceof ServerWorld serverWorld) {
            BlockPos newSpawn = findValidSpawnPosition(serverWorld);
            if (newSpawn != null) {
                serverWorld.setSpawnPos(newSpawn, 0.0f);
                InfectionPlus.LOGGER.info("Set new world spawn at: {} for player {}", newSpawn, player.getName().getString());
            } else {
                InfectionPlus.LOGGER.warn("Failed to find valid spawn location for player {}", player.getName().getString());
            }
        }
    }

    private static BlockPos findValidSpawnPosition(ServerWorld world) {
        for (int attempt = 0; attempt < MAX_SPAWN_ATTEMPTS; attempt++) {
            int x = world.getRandom().nextInt(SPAWN_SEARCH_RADIUS * 2) - SPAWN_SEARCH_RADIUS;
            int z = world.getRandom().nextInt(SPAWN_SEARCH_RADIUS * 2) - SPAWN_SEARCH_RADIUS;
            int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z);
            BlockPos candidatePos = new BlockPos(x, y, z);

            if (isValidSpawnPosition(world, candidatePos)) {
                return candidatePos;
            }
        }
        return null;
    }

    private static boolean isValidSpawnPosition(ServerWorld world, BlockPos pos) {
        // Check if the block below is solid
        if (!world.getBlockState(pos.down()).isSolidBlock(world, pos.down())) {
            return false;
        }

        // Check for open sky above
        for (int i = 1; i <= MIN_SKY_VISIBILITY; i++) {
            if (world.getBlockState(pos.up(i)).isOpaque()) {
                return false;
            }
        }

        // Check for liquids using the new method
        FluidState currentFluid = world.getFluidState(pos);
        FluidState aboveFluid = world.getFluidState(pos.up());

        return !currentFluid.isEmpty() &&
                !aboveFluid.isEmpty() &&
                world.isSkyVisible(pos);
    }

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            Scoreboard scoreboard = server.getScoreboard();

            InfectionPlus.LOGGER.info("[Team System] Player joined: {}", player.getNameForScoreboard());

            Team humanTeam = scoreboard.getTeam(InfectionPlus.HUMAN_TEAM_NAME);
            Team zombieTeam = scoreboard.getTeam(InfectionPlus.ZOMBIE_TEAM_NAME);

            if (humanTeam == null || zombieTeam == null) {
                InfectionPlus.LOGGER.error("[Team System] Teams don't exist!");
                return;
            }

            boolean zombiesExist = server.getPlayerManager().getPlayerList().stream()
                    .anyMatch(p -> zombieTeam.equals(scoreboard.getScoreHolderTeam(p.getNameForScoreboard())));

            if (!zombiesExist) {
                double roll = RANDOM.nextDouble();
                InfectionPlus.LOGGER.info("[Random Chance] Roll for {}: {} (needs < {})",
                        player.getNameForScoreboard(), roll, ZOMBIE_CHANCE);

                if (roll < ZOMBIE_CHANCE) {
                    Team currentTeam = scoreboard.getScoreHolderTeam(player.getNameForScoreboard());
                    if (currentTeam != null) {
                        scoreboard.removeScoreHolderFromTeam(player.getNameForScoreboard(), currentTeam);
                    }

                    scoreboard.addScoreHolderToTeam(player.getNameForScoreboard(), zombieTeam);
                    InfectionPlus.LOGGER.info("[Random Chance] {} became the first zombie!",
                            player.getNameForScoreboard());

                    applyZombieEffects(player);
                    player.sendMessage(
                            Text.literal("You've been infected! You are now a Zombie!")
                                    .formatted(Formatting.RED),
                            false
                    );

                    server.getPlayerManager().broadcast(
                            Text.literal(player.getName().getString() + " has become the first Zombie!")
                                    .formatted(Formatting.DARK_RED),
                            false
                    );
                    return;
                }
            }

            Team currentTeam = scoreboard.getScoreHolderTeam(player.getNameForScoreboard());
            if (currentTeam == null) {
                scoreboard.addScoreHolderToTeam(player.getNameForScoreboard(), humanTeam);
                InfectionPlus.LOGGER.info("[Team System] Assigning {} to human team",
                        player.getNameForScoreboard());
                player.sendMessage(
                        Text.literal("You've been assigned to the Human team!").formatted(Formatting.AQUA),
                        false
                );
            } else {
                InfectionPlus.LOGGER.info("[Team System] Player {} remains in {} team",
                        player.getNameForScoreboard(), currentTeam.getName());
            }
        });
    }

    public static void applyZombieEffects(ServerPlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.HUNGER,
                600,
                2,
                true,
                false
        ));

        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.NIGHT_VISION,
                600,
                0,
                true,
                false
        ));

        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.RESISTANCE,
                600,
                0,
                true,
                false
        ));

        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.SLOWNESS,
                600,
                0,
                true,
                false
        ));
    }

    public static void cureInfection(ServerPlayerEntity player) {
        player.removeStatusEffect(InfectionEffect.INFECTION_1);
        player.removeStatusEffect(InfectionEffect.INFECTION_2);
        player.removeStatusEffect(InfectionEffect.INFECTION_3);
        player.removeStatusEffect(InfectionEffect.INFECTION_4);
        player.removeStatusEffect(InfectionEffect.INFECTION_5);

        Team team = player.getScoreboardTeam();
        if (team != null && team.getName().equals(InfectionPlus.ZOMBIE_TEAM_NAME)) {
            switchToHumanTeam(player);
        }
    }

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
            if (teamName.equals(InfectionPlus.ZOMBIE_TEAM_NAME)) {
                applyZombieEffects(player);
                InfectionPlus.hasZombie = true;
            }

            Formatting color = newTeam.getColor();
            player.sendMessage(
                    Text.literal("You've been moved to the " + newTeam.getDisplayName().getString() + " team!")
                            .formatted(color),
                    false
            );
        }
    }

    public static void switchToHumanTeam(ServerPlayerEntity player) {
        switchToTeam(player, InfectionPlus.HUMAN_TEAM_NAME);
    }

    @SuppressWarnings("unused")
    public static void switchToZombieTeam(ServerPlayerEntity player) {
        switchToTeam(player, InfectionPlus.ZOMBIE_TEAM_NAME);
    }
}