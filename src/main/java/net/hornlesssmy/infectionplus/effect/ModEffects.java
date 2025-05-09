package net.hornlesssmy.infectionplus.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEffects {
    public static StatusEffect INFECTION_1;
    public static StatusEffect INFECTION_2;
    public static StatusEffect INFECTION_3;
    public static StatusEffect INFECTION_4;
    public static StatusEffect INFECTION_5;

    public static void registerEffects() {
        INFECTION_1 = registerEffect("infection_1", new InfectionEffect(StatusEffectCategory.HARMFUL, 0x8B0000));
        INFECTION_2 = registerEffect("infection_2", new InfectionEffect(StatusEffectCategory.HARMFUL, 0xA52A2A));
        INFECTION_3 = registerEffect("infection_3", new InfectionEffect(StatusEffectCategory.HARMFUL, 0xB22222));
        INFECTION_4 = registerEffect("infection_4", new InfectionEffect(StatusEffectCategory.HARMFUL, 0xDC143C));
        INFECTION_5 = registerEffect("infection_5", new InfectionEffect(StatusEffectCategory.HARMFUL, 0xFF0000));
    }

    private static StatusEffect registerEffect(String path, StatusEffect effect) {
        return Registry.register(Registries.STATUS_EFFECT, Identifier.of("infectionplus", path), effect);
    }
}