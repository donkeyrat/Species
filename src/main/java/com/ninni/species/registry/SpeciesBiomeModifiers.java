package com.ninni.species.registry;

import com.mojang.serialization.MapCodec;
import com.ninni.species.Species;
import com.ninni.species.server.world.gen.biome_modifier.ConditionalSpawnBiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class SpeciesBiomeModifiers {

    public static final DeferredRegister<MapCodec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.BIOME_MODIFIER_SERIALIZERS, Species.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<ConditionalSpawnBiomeModifier>> ADD_SPAWNS_BIOME_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("add_conditional_spawns", () -> ConditionalSpawnBiomeModifier.CODEC);

}
