package net.hornlesssmy.infectionplus.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ThornvayneSword extends SwordItem {
    
    // Static maps to track player data
    private static final Map<UUID, Integer> hitCountMap = new HashMap<>();
    private static final Map<UUID, Long> lastHitTimeMap = new HashMap<>();
    private static final Map<UUID, Boolean> infinitySquaredActiveMap = new HashMap<>();
    
    public ThornvayneSword(Settings settings) {
        super(ToolMaterials.NETHERITE, settings.attributeModifiers(
            SwordItem.createAttributeModifiers(ToolMaterials.NETHERITE, 8, -1.9f)
        ));
    }
    
    // Remove getDefaultStack override - we'll handle enchantments through recipe/crafting
    
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player && !player.getWorld().isClient) {
            applyLimitlessEffect(player, target, stack);
        }
        return super.postHit(stack, target, attacker);
    }
    
    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        // One-shot boats and minecarts
        if (!world.isClient) {
            world.getEntitiesByClass(BoatEntity.class, new net.minecraft.util.math.Box(pos), entity -> true)
                .forEach(boat -> boat.kill());
            world.getEntitiesByClass(AbstractMinecartEntity.class, new net.minecraft.util.math.Box(pos), entity -> true)
                .forEach(minecart -> minecart.kill());
        }
        return super.postMine(stack, world, state, pos, miner);
    }
    
    private void applyLimitlessEffect(PlayerEntity player, LivingEntity target, ItemStack stack) {
        UUID playerId = player.getUuid();
        long currentTime = System.currentTimeMillis();
        
        // Check if the last hit was within 10 seconds
        Long lastHitTime = lastHitTimeMap.get(playerId);
        if (lastHitTime == null || (currentTime - lastHitTime) > 10000) {
            // Reset hit count if more than 10 seconds have passed
            hitCountMap.put(playerId, 1);
        } else {
            // Increment hit count
            int currentHits = hitCountMap.getOrDefault(playerId, 0) + 1;
            hitCountMap.put(playerId, currentHits);
            
            // Apply Euphoria effect every 10 consecutive hits
            if (currentHits % 10 == 0) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 4, false, false)); // Regen 5 for 5 seconds, invisible
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 100, 4, false, false)); // Saturation 5 for 5 seconds, invisible
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 100, 2, false, false)); // Strength 3 for 5 seconds, invisible
            }
        }
        
        lastHitTimeMap.put(playerId, currentTime);
        
        // Calculate and apply damage based on hit count
        int hitCount = hitCountMap.get(playerId);
        float totalDamage = hitCount * hitCount + 4; // t = x² + 4
        
        // Apply the calculated damage
        target.damage(player.getDamageSources().playerAttack(player), totalDamage - 4); // Subtract base damage to avoid double damage
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient && user.isSneaking()) {
            toggleInfinitySquared(user);
            return TypedActionResult.success(user.getStackInHand(hand));
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }
    
    private void toggleInfinitySquared(PlayerEntity player) {
        UUID playerId = player.getUuid();
        boolean currentState = infinitySquaredActiveMap.getOrDefault(playerId, false);
        infinitySquaredActiveMap.put(playerId, !currentState);
        
        if (!currentState) {
            // Activate Infinity²
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
            player.sendMessage(Text.literal("§4Infinity² Activated"), true);
        } else {
            // Deactivate Infinity²
            player.removeStatusEffect(StatusEffects.INVISIBILITY);
            player.sendMessage(Text.literal("§4Infinity² Deactivated"), true);
        }
    }
    
    // Special mining behaviors will be handled through other methods
    
    // Static method to check if a player has Infinity² active
    public static boolean hasInfinitySquaredActive(UUID playerId) {
        return infinitySquaredActiveMap.getOrDefault(playerId, false);
    }
    
    // Static method to get hit count for damage calculation
    public static int getHitCount(UUID playerId) {
        return hitCountMap.getOrDefault(playerId, 0);
    }
}
