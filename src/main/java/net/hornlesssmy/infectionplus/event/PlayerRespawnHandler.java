package net.hornlesssmy.infectionplus.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.hornlesssmy.infectionplus.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Box;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerRespawnHandler {
    
    // Track players who had Thornvayne when they died
    private static final Set<UUID> playersWithThornvayne = new HashSet<>();

    public static void register() {
        // Track when players die and had Thornvayne
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (entity instanceof ServerPlayerEntity player) {
                // Check if player had Thornvayne in their inventory
                if (hadThornvayne(player)) {
                    playersWithThornvayne.add(player.getUuid());
                }
            }
        });
        
        // Give back Thornvayne on respawn
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            if (playersWithThornvayne.contains(newPlayer.getUuid())) {
                // Remove any existing Thornvayne items from the world first
                removeAllThornvayneFromWorld(newPlayer);
                
                // Give them a new Thornvayne
                ItemStack thornvayne = new ItemStack(ModItems.THORNVAYNE);
                
                // Try to add to inventory, or drop if full
                if (!newPlayer.getInventory().insertStack(thornvayne)) {
                    newPlayer.dropItem(thornvayne, false);
                }
                
                // Remove from tracking set
                playersWithThornvayne.remove(newPlayer.getUuid());
            }
        });
    }
    
    private static boolean hadThornvayne(ServerPlayerEntity player) {
        // Check if player had Thornvayne in their inventory
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.getItem() == ModItems.THORNVAYNE) {
                return true;
            }
        }
        return false;
    }
    
    private static void removeAllThornvayneFromWorld(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();
        
        // Search in a large area around the player (death location might be different from respawn location)
        Box searchBox = new Box(
            player.getX() - 500, player.getY() - 100, player.getZ() - 500,
            player.getX() + 500, player.getY() + 100, player.getZ() + 500
        );
        
        // Remove all Thornvayne item entities from the world
        world.getEntitiesByClass(ItemEntity.class, searchBox, itemEntity -> {
            ItemStack stack = itemEntity.getStack();
            return stack.getItem() == ModItems.THORNVAYNE;
        }).forEach(ItemEntity::discard);
        
        // Also check all loaded chunks for item entities (in case graves mod puts them elsewhere)
        world.getServer().execute(() -> {
            for (ServerWorld serverWorld : world.getServer().getWorlds()) {
                // Use a very large box to cover the entire world
                Box worldBox = new Box(-30000000, -2048, -30000000, 30000000, 2048, 30000000);
                serverWorld.getEntitiesByClass(ItemEntity.class, worldBox, entity -> {
                    ItemStack stack = entity.getStack();
                    return stack.getItem() == ModItems.THORNVAYNE;
                }).forEach(ItemEntity::discard);
            }
        });
        
        // Remove Thornvayne from all player inventories (in case graves mod gives items to other players)
        for (ServerPlayerEntity otherPlayer : world.getServer().getPlayerManager().getPlayerList()) {
            if (otherPlayer != player) {
                for (int i = 0; i < otherPlayer.getInventory().size(); i++) {
                    ItemStack stack = otherPlayer.getInventory().getStack(i);
                    if (stack.getItem() == ModItems.THORNVAYNE) {
                        otherPlayer.getInventory().setStack(i, ItemStack.EMPTY);
                    }
                }
            }
        }
    }
}
