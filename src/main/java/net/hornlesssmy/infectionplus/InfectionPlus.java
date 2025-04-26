package net.hornlesssmy.infectionplus;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.hornlesssmy.infectionplus.event.PlayerJoinHandler;
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

	@Override
	public void onInitialize() {
		ModItems.registerModItems();

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			Scoreboard scoreboard = server.getScoreboard();
			Team team = scoreboard.getTeam(HUMAN_TEAM_NAME);

			if (team == null) {
				team = scoreboard.addTeam(HUMAN_TEAM_NAME);
				team.setDisplayName(Text.literal("Human").formatted(Formatting.AQUA));

				// Set these critical properties:
				team.setColor(Formatting.AQUA);              // Tab list/chat color
				team.setPrefix(Text.literal("[H] ").formatted(Formatting.AQUA)); // Name prefix
				team.setCollisionRule(Team.CollisionRule.PUSH_OTHER_TEAMS); // Optional

				InfectionPlus.LOGGER.info("✅ Created Human team (Color: AQUA)");
			} else {
				// Update existing team settings
				team.setColor(Formatting.AQUA);
				team.setPrefix(Text.literal("[H] ").formatted(Formatting.AQUA));
				InfectionPlus.LOGGER.info("ℹ️ Updated Human team color to AQUA");
			}
		});


		// ACTUALLY REGISTER THE JOIN HANDLER (MISSING IN YOUR CODE)
		PlayerJoinHandler.register(); // ← THIS LINE WAS MISSING
	}
}