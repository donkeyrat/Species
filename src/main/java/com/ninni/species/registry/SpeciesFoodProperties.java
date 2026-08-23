package com.ninni.species.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class SpeciesFoodProperties {

	public static final FoodProperties CRACKED_WRAPTOR_EGG = new FoodProperties.Builder()
		.nutrition(5).saturationModifier(0.7F)
		.effect(() -> new MobEffectInstance(SpeciesStatusEffects.WITHER_RESISTANCE, 20 * 90, 0), 1)
		.build();

	public static final FoodProperties GHOUL_TONGUE = new FoodProperties.Builder()
		.nutrition(2).saturationModifier(0.2f)
		.effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 20 * 10, 0), 1)
		.effect(() -> new MobEffectInstance(MobEffects.HUNGER, 20 * 10, 1), 1)
		.build();

	public static final FoodProperties MONSTER_MEAL = new FoodProperties.Builder()
		.nutrition(3).saturationModifier(1.2F).alwaysEdible()
		.build();

	public static final FoodProperties WICKED_DOPE = new FoodProperties.Builder()
		.nutrition(3).saturationModifier(2).alwaysEdible()
		.build();

}
