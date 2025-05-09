package net.hornlesssmy.infectionplus.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.hornlesssmy.infectionplus.InfectionPlus;
import net.hornlesssmy.infectionplus.points.PointsManager;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.scoreboard.ScoreHolder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.server.command.CommandManager.*;

public class PointsCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("points")
                .requires(source -> source.hasPermissionLevel(2))
                .then(literal("add")
                        .then(argument("player", EntityArgumentType.player())
                                .then(argument("amount", IntegerArgumentType.integer())
                                        .executes(ctx -> {
                                            ServerPlayerEntity player = EntityArgumentType.getPlayer(ctx, "player");
                                            int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                            PointsManager.addPoints(player, amount);
                                            return 1;
                                        }))))
                .then(literal("setworth")
                        .then(argument("player", EntityArgumentType.player())
                                .then(argument("amount", IntegerArgumentType.integer())
                                        .executes(ctx -> {
                                            ServerPlayerEntity player = EntityArgumentType.getPlayer(ctx, "player");
                                            int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                            player.getScoreboard().getOrCreateScore(
                                                    ScoreHolder.fromName(player.getNameForScoreboard()),
                                                    player.getScoreboard().getNullableObjective(InfectionPlus.BASE_WORTH_OBJ)
                                            ).setScore(amount);
                                            return 1;
                                        })))));
    }
}