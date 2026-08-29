package com.ninni.species.server;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ninni.species.registry.SpeciesRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public record LimpetOres(
	Optional<ResourceLocation> path, Item item, Block block, Optional<Integer> maxCount,
	Optional<BiomeLocation> location, int spawnWeight, Optional<Integer> maxSpawnHeight,
	Optional<Integer> minSpawnHeight
) {

	public record BiomeLocation(Either<ResourceKey<Biome>, TagKey<Biome>> value) {

		public static final Codec<BiomeLocation> CODEC = Codec.either(
			ResourceKey.codec(Registries.BIOME),
			TagKey.hashedCodec(Registries.BIOME)
		).xmap(BiomeLocation::new, BiomeLocation::value);

		public boolean matchesBiome(Holder<Biome> biome) {
			return this.value.map(biome::is, biome::is);
		}

	}

	public static final Codec<LimpetOres> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		ResourceLocation.CODEC.optionalFieldOf("path").forGetter(LimpetOres::path),
		BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(LimpetOres::item),
		BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(LimpetOres::block),
		Codec.INT.optionalFieldOf("max_count").forGetter(LimpetOres::maxCount),
		BiomeLocation.CODEC.optionalFieldOf("location").forGetter(LimpetOres::location),
		ExtraCodecs.POSITIVE_INT.optionalFieldOf("spawn_weight", 1).forGetter(LimpetOres::spawnWeight),
		Codec.INT.optionalFieldOf("max_spawn_height").forGetter(LimpetOres::maxSpawnHeight),
		Codec.INT.optionalFieldOf("min_spawn_height").forGetter(LimpetOres::minSpawnHeight)
	).apply(instance, LimpetOres::new));

	public static Registry<LimpetOres> getAllData(Level level) {
		return level.registryAccess().registry(SpeciesRegistries.LIMPET_ORES).orElseThrow();
	}

	public ResourceLocation getPath(Holder<LimpetOres> holder) {
		return holder.value().path.orElse(holder.getKey().location());
	}

}
