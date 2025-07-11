package net.hornlesssmy.infectionplus.mixin;

import net.hornlesssmy.infectionplus.item.custom.ThornvayneSword;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class SoundMixin {
    
    @Inject(method = "playSound", at = @At("HEAD"), cancellable = true)
    private void suppressSoundsForInfinitySquared(PlayerEntity source, double x, double y, double z, RegistryEntry<SoundEvent> sound, SoundCategory category, float volume, float pitch, long seed, CallbackInfo ci) {
        suppressSoundLogic(source, x, y, z, ci);
    }
    
    // Note: Additional playSound overloads may exist but need to be verified against actual method signatures
    
    private void suppressSoundLogic(PlayerEntity source, double x, double y, double z, CallbackInfo ci) {
        ServerWorld world = (ServerWorld) (Object) this;
        
        // Check if there's a player with Infinity² deactivated within 100 blocks
        for (ServerPlayerEntity player : world.getPlayers()) {
            if (player != source) {
                Vec3d playerPos = player.getPos();
                double distance = Math.sqrt(Math.pow(x - playerPos.x, 2) + Math.pow(y - playerPos.y, 2) + Math.pow(z - playerPos.z, 2));
                
                if (distance <= 100 && !ThornvayneSword.hasInfinitySquaredActive(player.getUuid())) {
                    // Cancel the sound for this player
                    ci.cancel();
                    return;
                }
            }
        }
    }
}
