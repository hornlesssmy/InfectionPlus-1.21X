package net.hornlesssmy.infectionplus.event;

import net.hornlesssmy.infectionplus.InfectionPlus;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.scoreboard.Team;

public class ZombieFireHandler {
    public static void onPlayerTick(ServerPlayerEntity player) {
        Team team = player.getScoreboardTeam();

        if (team != null && team.getName().equals(InfectionPlus.ZOMBIE_TEAM_NAME)) {
            // Check if player is on fire
            if (player.isOnFire()) {
                // Reset fire ticks to maintain burning
                player.setFireTicks(100); // 5 seconds (20 ticks/second)

                // Optional fire particles
                if (player.getWorld().getTime() % 5 == 0) {
                    player.getWorld().addParticle(
                            ParticleTypes.FLAME,
                            player.getX(),
                            player.getY() + player.getHeight(),
                            player.getZ(),
                            0.1,
                            0,
                            0.1
                    );
                }
            }
        }
    }
}