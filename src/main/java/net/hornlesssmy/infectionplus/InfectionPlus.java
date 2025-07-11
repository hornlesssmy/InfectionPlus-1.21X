package net.hornlesssmy.infectionplus;

import net.fabricmc.api.ModInitializer;
import net.hornlesssmy.infectionplus.command.ThornvayneCleanupCommand;
import net.hornlesssmy.infectionplus.event.PlayerJoinHandler;
import net.hornlesssmy.infectionplus.event.PlayerRespawnHandler;
import net.hornlesssmy.infectionplus.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfectionPlus implements ModInitializer {
	public static final String MOD_ID = "infectionplus";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		PlayerJoinHandler.register();
		PlayerRespawnHandler.register();
		ThornvayneCleanupCommand.register();
		// SecretHunterNetworking.registerServerReceivers();
		LOGGER.info("InfectionPlus initialized!");
	}
}