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

        // Handle zombie tank team water effect
        if (player.isTouchingWater() &&
                team != null &&
                team.getName().equals(InfectionPlus.ZOMBIE_TANK_TEAM_NAME)) {
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SLOWNESS,
                    40,
                    14,
                    true,
                    false
            ));
        }

        // Handle regular zombie team effects
        if (team != null && team.getName().equals(InfectionPlus.ZOMBIE_TEAM_NAME)) {
            // Apply base zombie effects if missing
            if (!player.hasStatusEffect(StatusEffects.HUNGER)) {
                PlayerJoinHandler.applyZombieEffects(player);
            }

            // Handle water effects for regular zombies
            if (player.isSubmergedInWater()) {
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.SLOWNESS,
                        40,
                        4,
                        false,
                        false
                ));

                // Add water particles occasionally
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

        // Handle fire effects for zombies
        ZombieFireHandler.onPlayerTick(player);
    }
}