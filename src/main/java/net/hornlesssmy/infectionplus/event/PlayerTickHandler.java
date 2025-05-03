package net.hornlesssmy.infectionplus.event;

import net.hornlesssmy.infectionplus.InfectionPlus;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerTickHandler {
    public static void onPlayerTick(ServerPlayerEntity player) {
        Team team = player.getScoreboardTeam();
        ZombieFireHandler.onPlayerTick(player);

        if (team != null && team.getName().equals(InfectionPlus.ZOMBIE_TEAM_NAME)) {
            // Check if player already has effects to avoid spamming
            if (!player.hasStatusEffect(StatusEffects.HUNGER)) {
                PlayerJoinHandler.applyZombieEffects(player);
            }
        }
        if (team != null && team.getName().equals(InfectionPlus.ZOMBIE_TEAM_NAME)) {
            // Check if player is in water (1.21+ method)
            boolean inWater = player.isSubmergedInWater();

            if (inWater) {
                // Apply Slowness V (amplifier 4 = level V)
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.SLOWNESS,
                        40, // 2 second durations (keeps refreshing)
                        4,  // Level V (0=I, 1=II, etc.)
                        false, // No particles
                        false  // Don't show icon
                ));

                // Optional: Add water particles
                if (player.getWorld().getTime() % 10 == 0) {
                    player.getWorld().addParticle(
                            ParticleTypes.BUBBLE,
                            player.getX(),
                            player.getY() + player.getHeight() * 0.5,
                            player.getZ(),
                            0,
                            0.1,
                            0
                    );
                }
            }
        }
    }
}