package com.ninni.species.server;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ninni.species.registry.SpeciesRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public record CruncherHunting(EntityType<?> entityType, ItemStack item, int minTries, int maxTries) {

	public static final Codec<CruncherHunting> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(CruncherHunting::entityType),
		ItemStack.CODEC.fieldOf("item").forGetter(CruncherHunting::item),
		Codec.INT.fieldOf("min_tries").forGetter(CruncherHunting::minTries),
		Codec.INT.fieldOf("max_tries").forGetter(CruncherHunting::maxTries)
	).apply(instance, CruncherHunting::new));

	public static final Codec<Holder<CruncherHunting>> HOLDER_CODEC = RegistryFileCodec.create(SpeciesRegistries.CRUNCHER_HUNTING, CODEC);

	public static Registry<CruncherHunting> getData(Level level) {
		return level.registryAccess().registry(SpeciesRegistries.CRUNCHER_HUNTING).orElseThrow();
	}

}
