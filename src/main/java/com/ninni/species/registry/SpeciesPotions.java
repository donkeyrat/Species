package com.ninni.species.registry;

import com.ninni.species.Species;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SpeciesPotions {

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(BuiltInRegistries.POTION, Species.MOD_ID);

    public static final DeferredHolder<Potion, Potion> BLOODLUST = POTIONS.register("bloodlust", location ->
        new Potion(location.getPath(), new MobEffectInstance(SpeciesStatusEffects.BLOODLUST, 20 * 60 * 60))
    );

}
