package net.hornlesssmy.infectionplus;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
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
				InfectionPlus.LOGGER.info("✅ Created Human team");
			} else {
				InfectionPlus.LOGGER.info("ℹ️ Human team already exists");
			}
		});
	}
}