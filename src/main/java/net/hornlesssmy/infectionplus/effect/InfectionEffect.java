package net.hornlesssmy.infectionplus.effect;

import net.hornlesssmy.infectionplus.InfectionPlus;
import net.hornlesssmy.infectionplus.event.InfectionEffectHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class InfectionEffect extends StatusEffect {
    // Registry entries for each infection stage
    public static final RegistryEntry<StatusEffect> INFECTION_1 = register("infection_1");
    public static final RegistryEntry<StatusEffect> INFECTION_2 = register("infection_2");
    public static final RegistryEntry<StatusEffect> INFECTION_3 = register("infection_3");
    public static final RegistryEntry<StatusEffect> INFECTION_4 = register("infection_4");
    public static final RegistryEntry<StatusEffect> INFECTION_5 = register("infection_5");

    public InfectionEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    private static RegistryEntry<StatusEffect> register(String name) {
        return Registry.registerReference(
                Registries.STATUS_EFFECT,
                Identifier.of(InfectionPlus.MOD_ID, name),
                new InfectionEffect(StatusEffectCategory.HARMFUL, getColorForStage(name))
        );
    }

    private static int getColorForStage(String name) {
        return switch (name) {
            case "infection_1" -> 0x4B5320;
            case "infection_2" -> 0x5E6B1F;
            case "infection_3" -> 0x728C1E;
            case "infection_4" -> 0x8AAD1D;
            case "infection_5" -> 0xA3CF1C;
            default -> 0xFFFFFF;
        };
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof ServerPlayerEntity player) {
            Team team = player.getScoreboardTeam();
            if (team != null && team.getName().equals(InfectionPlus.ZOMBIE_TEAM_NAME)) {
                player.removeStatusEffect(INFECTION_1);
                return false;
            }

            player.addStatusEffect(new StatusEffectInstance(
                    getCurrentInfection(),
                    InfectionEffectHandler.INFECTION_DURATION,
                    amplifier,
                    false,
                    false
            ));

            applyStageEffects(player);
        }
        return false;
    }

    private RegistryEntry<StatusEffect> getCurrentInfection() {
        if (this == INFECTION_1.value()) return INFECTION_1;
        if (this == INFECTION_2.value()) return INFECTION_2;
        if (this == INFECTION_3.value()) return INFECTION_3;
        if (this == INFECTION_4.value()) return INFECTION_4;
        return INFECTION_5;
    }

    private void applyStageEffects(ServerPlayerEntity player) {
        if (this == INFECTION_1.value()) {
            addHunger(player, 0);
        }
        else if (this == INFECTION_2.value()) {
            addHunger(player, 0);
            addSlowness(player, 0);
        }
        else if (this == INFECTION_3.value()) {
            addHunger(player, 1);
            addSlowness(player, 0);
        }
        else if (this == INFECTION_4.value()) {
            addHunger(player, 1);
            addSlowness(player, 1);
        }
        else if (this == INFECTION_5.value()) {
            addHunger(player, 2);
            addSlowness(player, 1);
        }
    }

    private void addHunger(ServerPlayerEntity player, int amplifier) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 40, amplifier, false, false));
    }

    private void addSlowness(ServerPlayerEntity player, int amplifier) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, amplifier, false, false));
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}