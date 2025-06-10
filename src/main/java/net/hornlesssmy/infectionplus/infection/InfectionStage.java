package net.hornlesssmy.infectionplus.infection;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;

/**
 * Enum representing the 5 stages of zombie infection.
 * Each stage lasts 1.6 in-game hours (96,000 ticks)
 */
public enum InfectionStage {
    STAGE_1(1, new RegistryEntry[]{StatusEffects.HUNGER}, new int[]{0}),
    STAGE_2(2, new RegistryEntry[]{StatusEffects.HUNGER, StatusEffects.SLOWNESS}, new int[]{0, 0}),
    STAGE_3(3, new RegistryEntry[]{StatusEffects.HUNGER, StatusEffects.SLOWNESS}, new int[]{1, 0}),
    STAGE_4(4, new RegistryEntry[]{StatusEffects.HUNGER, StatusEffects.SLOWNESS}, new int[]{1, 1}),
    STAGE_5(5, new RegistryEntry[]{StatusEffects.HUNGER, StatusEffects.SLOWNESS, StatusEffects.WEAKNESS}, new int[]{1, 1, 0});

    public static final int STAGE_DURATION_TICKS = 96000; // 1.6 in-game hours

    private final int stageNumber;
    private final RegistryEntry<StatusEffect>[] effects;
    private final int[] amplifiers;

    InfectionStage(int stageNumber, RegistryEntry<StatusEffect>[] effects, int[] amplifiers) {
        this.stageNumber = stageNumber;
        this.effects = effects;
        this.amplifiers = amplifiers;
    }

    public int getStageNumber() {
        return stageNumber;
    }

    public RegistryEntry<StatusEffect>[] getEffects() {
        return effects;
    }

    public int[] getAmplifiers() {
        return amplifiers;
    }

    public InfectionStage getNextStage() {
        return switch (this) {
            case STAGE_1 -> STAGE_2;
            case STAGE_2 -> STAGE_3;
            case STAGE_3 -> STAGE_4;
            case STAGE_4 -> STAGE_5;
            case STAGE_5 -> null; // Final stage
            default -> null;
        };
    }

    public static InfectionStage getFirstStage() {
        return STAGE_1;
    }
}