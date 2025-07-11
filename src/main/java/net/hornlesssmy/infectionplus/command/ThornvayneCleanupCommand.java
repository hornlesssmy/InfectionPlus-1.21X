package net.hornlesssmy.infectionplus.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.hornlesssmy.infectionplus.item.ModItems;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;

public class ThornvayneCleanupCommand {
    
    public static void register() {
        CommandRegistrationCallback.EVENT.register(ThornvayneCleanupCommand::registerCommands);
    }
    
    private static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("thornvayne")
            .then(CommandManager.literal("cleanup")
                .requires(source -> source.hasPermissionLevel(2)) // Requires op level 2
                .executes(ThornvayneCleanupCommand::cleanup)
            )
        );
    }
    
    private static int cleanup(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        final int[] totalRemoved = {0};
        
        // Remove all Thornvayne item entities from all worlds
        for (ServerWorld world : source.getServer().getWorlds()) {
            // Use a very large box to cover the entire world
            Box worldBox = new Box(-30000000, -2048, -30000000, 30000000, 2048, 30000000);
            world.getEntitiesByClass(ItemEntity.class, worldBox, entity -> {
                ItemStack stack = entity.getStack();
                return stack.getItem() == ModItems.THORNVAYNE;
            }).forEach(entity -> {
                entity.discard();
                totalRemoved[0]++;
            });
        }
        
        // Remove Thornvayne from all player inventories (keep only one per player)
        for (ServerPlayerEntity player : source.getServer().getPlayerManager().getPlayerList()) {
            boolean hasOne = false;
            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack stack = player.getInventory().getStack(i);
                if (stack.getItem() == ModItems.THORNVAYNE) {
                    if (hasOne) {
                        // Remove duplicate
                        player.getInventory().setStack(i, ItemStack.EMPTY);
                        totalRemoved[0]++;
                    } else {
                        hasOne = true;
                    }
                }
            }
        }
        
        source.sendFeedback(() -> Text.literal("Removed " + totalRemoved[0] + " duplicate Thornvayne items"), true);
        return totalRemoved[0];
    }
}
