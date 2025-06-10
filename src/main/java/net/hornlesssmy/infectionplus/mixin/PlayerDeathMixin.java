package net.hornlesssmy.infectionplus.mixin;

import net.hornlesssmy.infectionplus.infection.InfectionManager;
import net.hornlesssmy.infectionplus.team.TeamManager;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to detect when a zombie team player kills a human team player
 * and apply infection to the human player
 */
@Mixin(ServerPlayerEntity.class)
public class PlayerDeathMixin {

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onPlayerDeath(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayerEntity deadPlayer = (ServerPlayerEntity) (Object) this;

        // Check if the damage source is from another player
        if (damageSource.getAttacker() instanceof ServerPlayerEntity attacker) {
            Team deadPlayerTeam = deadPlayer.getScoreboardTeam();
            Team attackerTeam = attacker.getScoreboardTeam();

            // Check if a zombie killed a human
            if (deadPlayerTeam != null && attackerTeam != null &&
                    deadPlayerTeam.getName().equals(TeamManager.HUMAN_TEAM_NAME) &&
                    (attackerTeam.getName().equals(TeamManager.ZOMBIE_TEAM_NAME) ||
                            attackerTeam.getName().equals(TeamManager.ZOMBIE_TANK_TEAM_NAME))) {

                // Don't infect if already infected
                if (!InfectionManager.isPlayerInfected(deadPlayer)) {
                    long currentTick = deadPlayer.getServerWorld().getTime();
                    InfectionManager.infectPlayer(deadPlayer, currentTick);
                }
            }
        }
    }
}