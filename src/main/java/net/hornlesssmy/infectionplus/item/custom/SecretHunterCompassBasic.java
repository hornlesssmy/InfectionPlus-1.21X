package net.hornlesssmy.infectionplus.item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CompassItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.List;

public class SecretHunterCompassBasic extends CompassItem {
    
    public SecretHunterCompassBasic(Settings settings) {
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
                // For now, show the message with distance and direction
                double distance = Math.sqrt(serverPlayer.squaredDistanceTo(nearestPlayer));
                BlockPos playerPos = serverPlayer.getBlockPos();
                BlockPos targetPos = nearestPlayer.getBlockPos();
                
                String direction = getDirectionString(playerPos, targetPos);
                
                serverPlayer.sendMessage(Text.literal("SecretHunter found " + nearestPlayer.getName().getString() + " " + 
                    String.format("%.1f", distance) + " blocks " + direction + "!"), true);
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
