package net.hornlesssmy.infectionplus;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.hornlesssmy.infectionplus.effect.ModEffects;
import net.hornlesssmy.infectionplus.event.*;
import net.hornlesssmy.infectionplus.item.ModItems;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfectionPlus implements ModInitializer {
		public static final String MOD_ID = "infectionplus";
		public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
		public static final String HUMAN_TEAM_NAME = "infectionplus_human";
		public static final String ZOMBIE_TEAM_NAME = "infectionplus_zombie";
		public static final String ZOMBIE_TANK_TEAM_NAME = "infectionplus_zombietank";
		public static boolean hasZombie = false;
	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}


	@Override
	public void onInitialize() {

		WorldSpawnHandler.register();
		ModEffects.registerEffects();

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			if (!handler.player.hasStatusEffect(Registries.STATUS_EFFECT.getEntry(ModEffects.INFECTION_1))) {
				handler.player.addStatusEffect(new StatusEffectInstance(
						Registries.STATUS_EFFECT.getEntry(ModEffects.INFECTION_2),
						InfectionEffectHandler.INFECTION_DURATION,
						0,
						false,
						false
				));
			}
		});

		// Register effect expiration handler
		ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
			if (entity instanceof ServerPlayerEntity player) {
				player.getActiveStatusEffects().forEach((effect, instance) -> {
					if (instance.getDuration() <= 1) {
						if (effect.value() == ModEffects.INFECTION_1) {
							InfectionEffectHandler.onEffectExpire(player, instance);
						}
						if (effect.value() == ModEffects.INFECTION_2) {
							InfectionEffectHandler.onEffectExpire(player, instance);
						}
						if (effect.value() == ModEffects.INFECTION_3) {
							InfectionEffectHandler.onEffectExpire(player, instance);
						}
						if (effect.value() == ModEffects.INFECTION_4) {
							InfectionEffectHandler.onEffectExpire(player, instance);
						}
						if (effect.value() == ModEffects.INFECTION_5) {
							InfectionEffectHandler.onEffectExpire(player, instance);
						}
					}
				});
			}
		});

		ModItems.registerModItems();

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			InfectionPlus.hasZombie = false;


		});

		PlayerJoinHandler.register();
		ServerTickEvents.START_SERVER_TICK.register(server -> server.getPlayerManager().getPlayerList().forEach(PlayerTickHandler::onPlayerTick));

		PlayerDeathHandler.register();

	}
}