package com.ninni.species.server.world.gen.biome_modifier;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.EitherCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ninni.species.registry.SpeciesBiomeModifiers;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.MobSpawnSettingsBuilder;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

import java.util.List;
import java.util.function.Function;

public record ConditionalSpawnBiomeModifier(HolderSet<Biome> biomes, HolderSet<Biome> filtered, List<MobSpawnSettings.SpawnerData> spawners) implements BiomeModifier {

    public static final MapCodec<ConditionalSpawnBiomeModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Biome.LIST_CODEC.fieldOf("biomes").forGetter(ConditionalSpawnBiomeModifier::biomes),
        Biome.LIST_CODEC.fieldOf("filtered").forGetter(ConditionalSpawnBiomeModifier::filtered),
        new EitherCodec<>(MobSpawnSettings.SpawnerData.CODEC.listOf(), MobSpawnSettings.SpawnerData.CODEC).xmap(
            either -> either.map(Function.identity(), List::of),
            list -> list.size() == 1 ? Either.right(list.getFirst()) : Either.left(list)
        ).fieldOf("spawners").forGetter(ConditionalSpawnBiomeModifier::spawners)
    ).apply(instance, ConditionalSpawnBiomeModifier::new));

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return SpeciesBiomeModifiers.ADD_SPAWNS_BIOME_MODIFIER_TYPE.get();
    }

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD && this.biomes.contains(biome) && !this.filtered.contains(biome)) {
            MobSpawnSettingsBuilder spawns = builder.getMobSpawnSettings();
            for (MobSpawnSettings.SpawnerData spawner : this.spawners) {
                EntityType<?> type = spawner.type;
                spawns.addSpawn(type.getCategory(), spawner);
            }
        }
    }

}
