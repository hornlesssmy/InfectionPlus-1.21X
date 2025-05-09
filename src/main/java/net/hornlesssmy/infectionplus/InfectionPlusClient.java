package net.hornlesssmy.infectionplus;

import net.fabricmc.api.ClientModInitializer;
import net.hornlesssmy.infectionplus.effect.ModEffects;

public class InfectionPlusClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Client-side effect registration
        ModEffects.registerEffects();
    }
}