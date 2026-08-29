package com.ninni.species.registry;

import com.mojang.serialization.Codec;
import com.ninni.species.Species;
import com.ninni.species.server.item.CrankbowItem;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Unit;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class SpeciesEnchantmentEffectComponents {

	public static final DeferredRegister<DataComponentType<?>> ENCHANTMENT_EFFECT_COMPONENTS = DeferredRegister.create(BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, Species.MOD_ID);

	public static final Supplier<DataComponentType<EnchantmentValueEffect>> CRANKBOW_CAPACITY = register("crankbow_capacity", EnchantmentValueEffect.CODEC);
	public static final Supplier<DataComponentType<EnchantmentValueEffect>> CRANKBOW_MINIMUM_SPEED = register("crankbow_minimum_speed", EnchantmentValueEffect.CODEC);
	public static final Supplier<DataComponentType<EnchantmentValueEffect>> CRANKBOW_MAXIMUM_SPEED = register("crankbow_maximum_speed", EnchantmentValueEffect.CODEC);
	public static final Supplier<DataComponentType<EnchantmentValueEffect>> CRANKBOW_SPARING_CHANCE = register("crankbow_sparing_chance", EnchantmentValueEffect.CODEC);
	public static final Supplier<DataComponentType<Unit>> CRANKBOW_SCATTERSHOT = register("crankbow_scattershot", Unit.CODEC);
	public static final Supplier<DataComponentType<List<CrankbowItem.PullingSounds>>> CRANKBOW_PULLING_SOUNDS = register("crankbow_pulling_sounds", CrankbowItem.PullingSounds.CODEC.listOf());

	public static <T> Supplier<DataComponentType<T>> register(String name, Codec<T> codec) {
		return register(name, builder -> builder.persistent(codec));
	}

	public static <T> Supplier<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> operator) {
		return ENCHANTMENT_EFFECT_COMPONENTS.register(name, () -> operator.apply(DataComponentType.builder()).build());
	}

}
