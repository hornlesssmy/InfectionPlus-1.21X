package net.hornlesssmy.infectionplus.team;

import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TeamManager {
    public static final String HUMAN_TEAM_NAME = "infectionplus_human";
    public static final String ZOMBIE_TEAM_NAME = "infectionplus_zombie";
    public static final String ZOMBIE_TANK_TEAM_NAME = "infectionplus_zombietank";

    public static void initializeTeams(MinecraftServer server) {
        Scoreboard scoreboard = server.getScoreboard();

        // Create teams if they don't exist
        Team humanTeam = scoreboard.getTeam(HUMAN_TEAM_NAME);
        if (humanTeam == null) {
            humanTeam = scoreboard.addTeam(HUMAN_TEAM_NAME);
            humanTeam.setDisplayName(Text.literal("Humans"));
            humanTeam.setColor(Formatting.BLUE);
        }

        Team zombieTeam = scoreboard.getTeam(ZOMBIE_TEAM_NAME);
        if (zombieTeam == null) {
            zombieTeam = scoreboard.addTeam(ZOMBIE_TEAM_NAME);
            zombieTeam.setDisplayName(Text.literal("Zombies"));
            zombieTeam.setColor(Formatting.GREEN);
        }

        Team zombieTankTeam = scoreboard.getTeam(ZOMBIE_TANK_TEAM_NAME);
        if (zombieTankTeam == null) {
            zombieTankTeam = scoreboard.addTeam(ZOMBIE_TANK_TEAM_NAME);
            zombieTankTeam.setDisplayName(Text.literal("Zombie Tanks"));
            zombieTankTeam.setColor(Formatting.DARK_GREEN);
        }
    }
}