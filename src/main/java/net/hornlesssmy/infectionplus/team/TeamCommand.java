package net.hornlesssmy.infectionplus.team;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TeamCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("team")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("set")
                        .then(CommandManager.argument("player", StringArgumentType.string())
                                .then(CommandManager.argument("team", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            builder.suggest("human");
                                            builder.suggest("zombie");
                                            builder.suggest("zombietank");
                                            return builder.buildFuture();
                                        })
                                        .executes(context -> {
                                            ServerPlayerEntity player = context.getSource().getServer().getPlayerManager().getPlayer(StringArgumentType.getString(context, "player"));
                                            String team = StringArgumentType.getString(context, "team");

                                            if (player == null) {
                                                context.getSource().sendError(Text.literal("Player not found"));
                                                return 0;
                                            }

                                            switch (team.toLowerCase()) {
                                                case "human":
                                                    TeamHandler.switchToHumanTeam(player);
                                                    break;
                                                case "zombie":
                                                    TeamHandler.switchToZombieTeam(player);
                                                    break;
                                                case "zombietank":
                                                    TeamHandler.switchToZombieTankTeam(player);
                                                    break;
                                                default:
                                                    context.getSource().sendError(Text.literal("Invalid team. Use human, zombie, or zombietank"));
                                                    return 0;
                                            }

                                            context.getSource().sendFeedback(() -> Text.literal("Successfully moved " + player.getName().getString() + " to " + team + " team"), true);
                                            return 1;
                                        })
                                )
                        )
                )
        ); // Fixed: Added missing closing parenthesis
    }
}