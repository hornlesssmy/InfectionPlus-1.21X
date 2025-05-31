package net.hornlesssmy.infectionplus.team;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerTickHandler {
    public static void onPlayerTick(ServerPlayerEntity player) {
        Team team = player.getScoreboardTeam();

        // Handle zombie team water effects
        if (team != null && player.isTouchingWater()) {
            if (team.getName().equals(TeamManager.ZOMBIE_TEAM_NAME)) {
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.SLOWNESS, 40, 6, true, false));

                // Add water particles
                if (player.getWorld().getTime() % 10 == 0) {
                    player.getWorld().addParticle(
                            ParticleTypes.BUBBLE,
                            player.getX(),
                            player.getY() + player.getHeight() * 0.5,
                            player.getZ(),
                            0, 0.1, 0
                    );
                }
            }
            else if (team.getName().equals(TeamManager.ZOMBIE_TANK_TEAM_NAME)) {
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.SLOWNESS, 40, 14, true, false));
            }
        }

        // Handle zombie fire effects
        if (team != null &&
                (team.getName().equals(TeamManager.ZOMBIE_TEAM_NAME) ||
                        team.getName().equals(TeamManager.ZOMBIE_TANK_TEAM_NAME))) {

            if (player.isOnFire()) {
                player.setFireTicks(100); // Keep them burning

                // Add fire particles
                if (player.getWorld().getTime() % 5 == 0) {
                    player.getWorld().addParticle(
                            ParticleTypes.FLAME,
                            player.getX(),
                            player.getY() + player.getHeight(),
                            player.getZ(),
                            0.1, 0, 0.1
                    );
                }
            }
        }
    }
}