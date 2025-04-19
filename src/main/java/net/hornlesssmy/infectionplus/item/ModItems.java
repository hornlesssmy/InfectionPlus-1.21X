package net.hornlesssmy.infectionplus.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.hornlesssmy.infectionplus.InfectionPlus;
import net.hornlesssmy.infectionplus.item.custom.GhostlyFishItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item HUMAN_HUNTER_EASTER_EGG = registerItem("human_hunter_easter_egg", new Item(new Item.Settings()));
    public static final Item CROWN_OF_UNNATURAL_LIFE_EASTER_EGG = registerItem("crown_of_unnatural_life_easter_egg", new Item(new Item.Settings()));
    public static final Item LETHAL_STEROIDS = registerItem("lethal_steroids_easter_egg", new Item(new Item.Settings()));
    public static final Item SHADOW_CROSSER_EASTER_EGG = registerItem("shadow_crosser_easter_egg", new Item(new Item.Settings()));
    public static final Item MENY_BOMB_EASTER_EGG = registerItem("meny_bomb_easter_egg", new Item(new Item.Settings()));
    public static final Item DANIELS_DARKNESS_EASTER_EGG = registerItem("daniels_darkness_easter_egg", new Item(new Item.Settings()));
    public static final Item MARIOS_BABY_OIL_EASTER_EGG = registerItem("marios_baby_oil_easter_egg", new Item(new Item.Settings()));
    public static final Item MASONS_WOLFPACK_EASTER_EGG = registerItem("masons_wolfpack_easter_egg", new Item(new Item.Settings()));
    public static final Item JESSES_REVENGE_EASTER_EGG = registerItem("jesses_revenge_easter_egg", new Item(new Item.Settings()));
    public static final Item ETHANS_EVERYDAY_EASTER_EGG = registerItem("ethans_everyday_easter_egg", new Item(new Item.Settings()));
    public static final Item GHOSTLY_FISH = registerItem("ghostly_fish", new GhostlyFishItem(new Item.Settings().food(ModFoodComponents.GHOSTLY_FISH)));
    public static final Item ZOMBIE_CURE = registerItem("zombie_cure", new Item(new Item.Settings().food(ModFoodComponents.ZOMBIE_CURE)));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(InfectionPlus.MOD_ID, name), item);
    }

    public static void registerModItems() {
        InfectionPlus.LOGGER.info("Registering Mod Items for " + InfectionPlus.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.add(LETHAL_STEROIDS);
            fabricItemGroupEntries.add(SHADOW_CROSSER_EASTER_EGG);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.add(MENY_BOMB_EASTER_EGG);
            fabricItemGroupEntries.add(CROWN_OF_UNNATURAL_LIFE_EASTER_EGG);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(fabricItemGroupEntries -> fabricItemGroupEntries.add(MASONS_WOLFPACK_EASTER_EGG));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.add(HUMAN_HUNTER_EASTER_EGG);
            fabricItemGroupEntries.add(DANIELS_DARKNESS_EASTER_EGG);
            fabricItemGroupEntries.add(MARIOS_BABY_OIL_EASTER_EGG);
            fabricItemGroupEntries.add(JESSES_REVENGE_EASTER_EGG);
            fabricItemGroupEntries.add(ETHANS_EVERYDAY_EASTER_EGG);
        });
    }
}