package net.hornlesssmy.infectionplus.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.hornlesssmy.infectionplus.InfectionPlus;

public class ModEffects {
    public static final RegistryEntry<StatusEffect> INFECTION = Registry.registerReference(
            Registries.STATUS_EFFECT,
            Identifier.of(InfectionPlus.MOD_ID, "infection"),
            new InfectionEffect(StatusEffectCategory.HARMFUL, 0x4a412a)
    );

    public static void registerEffects() {
        InfectionPlus.LOGGER.info("Registering Mod Effects for " + InfectionPlus.MOD_ID);
    }
}