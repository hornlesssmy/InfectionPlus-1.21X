package net.hornlesssmy.infectionplus.item.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CompassItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import java.util.List;
import java.util.Optional;

public class SecretHunterCompassAdvanced extends CompassItem {
    
    public SecretHunterCompassAdvanced(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        
        if (!world.isClient && user instanceof ServerPlayerEntity serverPlayer) {
            ServerWorld serverWorld = serverPlayer.getServerWorld();
            
            // Find the nearest player
            PlayerEntity nearestPlayer = findNearestPlayer(serverWorld, serverPlayer);
            
            if (nearestPlayer != null) {
                // Set the compass to point to the nearest player using the new component system
                BlockPos targetPos = nearestPlayer.getBlockPos();
                GlobalPos globalPos = GlobalPos.create(serverWorld.getRegistryKey(), targetPos);
                
                // Use the new component system to set the lodestone tracker
                LodestoneTrackerComponent tracker = new LodestoneTrackerComponent(Optional.of(globalPos), true);
                stack.set(DataComponentTypes.LODESTONE_TRACKER, tracker);
                
                // Force stack update to trigger compass needle update
                user.setStackInHand(hand, stack);
                
                // Show distance and direction info
                double distance = Math.sqrt(serverPlayer.squaredDistanceTo(nearestPlayer));
                String direction = getDirectionString(serverPlayer.getBlockPos(), targetPos);
                
                serverPlayer.sendMessage(Text.literal("SecretHunter compass now pointing to " + nearestPlayer.getName().getString() + " (" + 
                    String.format("%.1f", distance) + " blocks " + direction + ")!"), true);
            } else {
                serverPlayer.sendMessage(Text.literal("No other players found!"), true);
            }
        }
        
        return TypedActionResult.success(stack, world.isClient());
    }
    
    private PlayerEntity findNearestPlayer(ServerWorld world, ServerPlayerEntity excludePlayer) {
        List<ServerPlayerEntity> players = world.getServer().getPlayerManager().getPlayerList();
        PlayerEntity nearestPlayer = null;
        double nearestDistance = Double.MAX_VALUE;
        
        for (ServerPlayerEntity player : players) {
            if (player != excludePlayer) {
                double distance = excludePlayer.squaredDistanceTo(player);
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestPlayer = player;
                }
            }
        }
        
        return nearestPlayer;
    }
    
    private String getDirectionString(BlockPos from, BlockPos to) {
        int dx = to.getX() - from.getX();
        int dz = to.getZ() - from.getZ();
        
        String direction = "";
        
        if (dz > 0) {
            direction += "South";
        } else if (dz < 0) {
            direction += "North";
        }
        
        if (dx > 0) {
            direction += "East";
        } else if (dx < 0) {
            direction += "West";
        }
        
        return direction.isEmpty() ? "here" : "to the " + direction;
    }
}
