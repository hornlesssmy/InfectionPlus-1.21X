package net.hornlesssmy.infectionplus.item;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class ModFoodComponents {
    public static final FoodComponent ZOMBIE_CURE = new FoodComponent.Builder()
            .snack().build();

    public static final FoodComponent GHOSTLY_FISH = new FoodComponent.Builder()
            .nutrition(20).saturationModifier(0.9f).alwaysEdible().snack()
            .statusEffect(new StatusEffectInstance(StatusEffects.LUCK, 999999999, 999999999), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 6000), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 2), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 200, 5), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 6000), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 100, 4), 1.0f).build();
}