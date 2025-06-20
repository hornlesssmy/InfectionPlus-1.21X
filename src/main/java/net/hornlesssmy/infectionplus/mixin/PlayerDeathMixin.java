package net.hornlesssmy.infectionplus.mixin;

import net.hornlesssmy.infectionplus.effect.ModEffects;
import net.hornlesssmy.infectionplus.team.TeamManager;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class PlayerDeathMixin {
    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onPlayerDeath(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayerEntity deadPlayer = (ServerPlayerEntity) (Object) this;

        if (damageSource.getAttacker() instanceof ServerPlayerEntity attacker) {
            Team deadPlayerTeam = deadPlayer.getScoreboardTeam();
            Team attackerTeam = attacker.getScoreboardTeam();

            if (deadPlayerTeam != null && attackerTeam != null &&
                    deadPlayerTeam.getName().equals(TeamManager.HUMAN_TEAM_NAME) &&
                    (attackerTeam.getName().equals(TeamManager.ZOMBIE_TEAM_NAME) ||
                            attackerTeam.getName().equals(TeamManager.ZOMBIE_TANK_TEAM_NAME))) {

                // Apply infection effect (6 hours = 72'000 ticks)
                deadPlayer.addStatusEffect(new StatusEffectInstance(
                        ModEffects.INFECTION,
                        72000, // 6 hours
                        0,
                        false,
                        true,
                        true
                ));
            }
        }
    }
}