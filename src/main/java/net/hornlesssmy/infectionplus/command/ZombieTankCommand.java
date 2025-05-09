package net.hornlesssmy.infectionplus.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.literal;

public class ZombieTankCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("become_zt984")
                        .requires(src -> src.hasPermissionLevel(4)) // ops/console only
                        .then(CommandManager.argument("targets", EntityArgumentType.players())
                                .executes(ctx -> {
                                    Collection<ServerPlayerEntity> targets =
                                            EntityArgumentType.getPlayers(ctx, "targets");
                                    int count = 0;

                                    for (ServerPlayerEntity player : targets) {
                                        Scoreboard scoreboard = player.getScoreboard();
                                        Team tankTeam = scoreboard.getTeam("infectionplus_zombietank");
                                        if (tankTeam == null) {
                                            ctx.getSource()
                                                    .sendError(Text.literal("❌ Zombie Tank team does not exist."));
                                            return 0;
                                        }

                                        // Add to the Zombie Tank team
                                        scoreboard.addScoreHolderToTeam(
                                                player.getNameForScoreboard(),
                                                tankTeam
                                        );

                                        // Give standard Zombie Tank effects
                                        player.addStatusEffect(new StatusEffectInstance(
                                                StatusEffects.SLOWNESS, Integer.MAX_VALUE, 1, false, false
                                        ));
                                        player.addStatusEffect(new StatusEffectInstance(
                                                StatusEffects.STRENGTH, Integer.MAX_VALUE, 1, false, false
                                        ));
                                        player.addStatusEffect(new StatusEffectInstance(
                                                StatusEffects.RESISTANCE, Integer.MAX_VALUE, 1, false, false
                                        ));
                                        player.addStatusEffect(new StatusEffectInstance(
                                                StatusEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false
                                        ));

                                        // **New: ** give 20 extra hearts via Absorption level 9
                                        player.addStatusEffect(new StatusEffectInstance(
                                                StatusEffects.ABSORPTION, Integer.MAX_VALUE, 9, false, false
                                        ));

                                        player.sendMessage(
                                                Text.literal("☠️ You have been turned into a Zombie Tank!")
                                                        .formatted(Formatting.DARK_GREEN),
                                                false
                                        );
                                        count++;
                                    }

                                    int finalCount = count;
                                    ctx.getSource().sendFeedback(() ->
                                                    Text.literal("✅ " + finalCount + " player(s) set to Zombie Tank."),
                                            true
                                    );
                                    return finalCount;
                                })
                        )
        );
    }
}
