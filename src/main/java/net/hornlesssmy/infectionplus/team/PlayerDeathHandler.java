package net.hornlesssmy.infectionplus.team;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.random.Random;

public class PlayerDeathHandler {
    private static final Item[] ZOMBIE_CURE_ITEMS = {
            Items.HEART_OF_THE_SEA,
            Items.EMERALD,
            Items.DIAMOND_BLOCK,
            Items.GOLDEN_APPLE,
            Items.HONEY_BOTTLE,
            Items.RESPAWN_ANCHOR,
            Items.EGG,
            Items.CHAIN,
            Items.ROTTEN_FLESH,
            Items.HEART_OF_THE_SEA,
            Items.EMERALD,
            Items.GOLDEN_APPLE,
            Items.HONEY_BOTTLE,
            Items.EGG,
            Items.CHAIN,
            Items.ROTTEN_FLESH
    };

    public static void register() {
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            if (!alive) {
                Team team = oldPlayer.getScoreboardTeam();

                // Drop random zombie cure ingredient for zombies
                if (team != null && team.getName().equals(TeamManager.ZOMBIE_TEAM_NAME)) {
                    Random random = oldPlayer.getRandom();
                    Item dropItem = ZOMBIE_CURE_ITEMS[random.nextInt(ZOMBIE_CURE_ITEMS.length)];
                    oldPlayer.dropItem(dropItem, 1);
                }

                // Drop random zombie cure ingredient for zombie tanks
                if (team != null && team.getName().equals(TeamManager.ZOMBIE_TANK_TEAM_NAME)) {
                    Random random = oldPlayer.getRandom();
                    Item dropItem = ZOMBIE_CURE_ITEMS[random.nextInt(ZOMBIE_CURE_ITEMS.length)];
                    oldPlayer.dropItem(dropItem, 1);
                }

                // Reapply team effects if player was on the zombie team.
                if (team != null &&
                        (team.getName().equals(TeamManager.ZOMBIE_TEAM_NAME) ||
                                team.getName().equals(TeamManager.ZOMBIE_TANK_TEAM_NAME))) {
                    TeamHandler.switchToTeam(newPlayer, team.getName());
                }
            }
        });
    }
}