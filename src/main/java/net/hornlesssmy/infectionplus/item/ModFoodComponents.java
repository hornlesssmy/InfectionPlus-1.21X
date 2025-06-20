package net.hornlesssmy.infectionplus.item;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class ModFoodComponents {
    public static final FoodComponent ZOMBIE_CURE = new FoodComponent.Builder()
            .alwaysEdible().snack()
            .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 60, 1), 1.0f)
            .build();

    public static final FoodComponent GHOSTLY_FISH = new FoodComponent.Builder()
            .nutrition(50).saturationModifier(5.0f).alwaysEdible().snack()
            .statusEffect(new StatusEffectInstance(StatusEffects.LUCK, 999999999, 999999999, false, false), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 6000, 0, false, false), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 3, false, false), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 999999999, 5, false, false), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 6000, 0, false, false), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 6000, 9, false, false), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 6000, 2, false, false), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 120, 5, false, false), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.HERO_OF_THE_VILLAGE, 999999999, 999999999, false, false), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 100, 3, false, false), 1.0f)
            .build();
}