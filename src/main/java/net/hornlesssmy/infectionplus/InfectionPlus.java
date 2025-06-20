package net.hornlesssmy.infectionplus;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.hornlesssmy.infectionplus.effect.ModEffects;
import net.hornlesssmy.infectionplus.item.ModItems;
import net.hornlesssmy.infectionplus.team.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfectionPlus implements ModInitializer {
	public static final String MOD_ID = "infectionplus";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModEffects.registerEffects();

		ModItems.registerModItems();

		LOGGER.info("InfectionPlus items registered!");

		ServerLifecycleEvents.SERVER_STARTED.register(TeamManager::initializeTeams);

		CommandRegistrationCallback.EVENT.register(TeamCommand::register);

		PlayerJoinHandler.register();
		PlayerDeathHandler.register();


		ServerTickEvents.START_SERVER_TICK.register(server -> {
			server.getPlayerManager().getPlayerList().forEach(PlayerTickHandler::onPlayerTick);
		});

		LOGGER.info("InfectionPlus initialized!");
	}
}