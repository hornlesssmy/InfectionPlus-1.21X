package net.hornlesssmy.infectionplus;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.hornlesssmy.infectionplus.event.PlayerJoinHandler;
import net.hornlesssmy.infectionplus.event.PlayerTickHandler;
import net.hornlesssmy.infectionplus.item.ModItems;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfectionPlus implements ModInitializer {
	public static final String MOD_ID = "infectionplus";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final String HUMAN_TEAM_NAME = "infectionplus_human";
	public static final String ZOMBIE_TEAM_NAME = "infectionplus_zombie"; // New constant
	public static boolean hasZombie = false;

	@Override
	public void onInitialize() {
		ModItems.registerModItems();

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			Scoreboard scoreboard = server.getScoreboard();
			InfectionPlus.hasZombie = false;

			// Human team setup (existing code)
			Team humanTeam = scoreboard.getTeam(HUMAN_TEAM_NAME);
			if (humanTeam == null) {
				humanTeam = scoreboard.addTeam(HUMAN_TEAM_NAME);
				humanTeam.setDisplayName(Text.literal("Human").formatted(Formatting.AQUA));
				humanTeam.setColor(Formatting.AQUA);
				humanTeam.setPrefix(Text.literal("[H] ").formatted(Formatting.AQUA));
				humanTeam.setCollisionRule(Team.CollisionRule.PUSH_OTHER_TEAMS);
				LOGGER.info("✅ Created Human team (Color: AQUA)");
			} else {
				humanTeam.setColor(Formatting.AQUA);
				humanTeam.setPrefix(Text.literal("[H] ").formatted(Formatting.AQUA));
				LOGGER.info("ℹ️ Updated Human team color to AQUA");
			}

			// Zombie team setup (new code)
			Team zombieTeam = scoreboard.getTeam(ZOMBIE_TEAM_NAME);
			if (zombieTeam == null) {
				zombieTeam = scoreboard.addTeam(ZOMBIE_TEAM_NAME);
				zombieTeam.setDisplayName(Text.literal("Zombie").formatted(Formatting.GREEN));
				zombieTeam.setColor(Formatting.GREEN);
				zombieTeam.setPrefix(Text.literal("[Z] ").formatted(Formatting.GREEN));
				zombieTeam.setCollisionRule(Team.CollisionRule.PUSH_OTHER_TEAMS);
				LOGGER.info("✅ Created Zombie team (Color: GREEN)");
			} else {
				zombieTeam.setColor(Formatting.GREEN);
				zombieTeam.setPrefix(Text.literal("[Z] ").formatted(Formatting.GREEN));
				LOGGER.info("ℹ️ Updated Zombie team color to GREEN");
			}
		});

		PlayerJoinHandler.register();
		ServerTickEvents.START_SERVER_TICK.register(server -> server.getPlayerManager().getPlayerList().forEach(PlayerTickHandler::onPlayerTick));
	}
}