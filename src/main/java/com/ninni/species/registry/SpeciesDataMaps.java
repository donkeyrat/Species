package com.ninni.species.registry;

import com.mojang.serialization.Codec;
import com.ninni.species.Species;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Species.MOD_ID)
public class SpeciesDataMaps {

	@SubscribeEvent
	public static void registerDataMaps(RegisterDataMapTypesEvent event) {
		event.register(Blocks.GOOBER_GOO_CONVERSION);
		event.register(EntityTypes.UPSIDE_DOWN_CONVERSION);
	}

	public interface Blocks {

		DataMapType<Block, Block> GOOBER_GOO_CONVERSION = create("goober_goo_conversion", BuiltInRegistries.BLOCK.byNameCodec());

		static <T> DataMapType<Block, T> create(String name, Codec<T> codec) {
			return DataMapType.builder(Species.of(name), Registries.BLOCK, codec).synced(codec, true).build();
		}

	}

	public interface EntityTypes {

		DataMapType<EntityType<?>, EntityType<?>> UPSIDE_DOWN_CONVERSION = create("upside_down_conversion", BuiltInRegistries.ENTITY_TYPE.byNameCodec());

		static <T> DataMapType<EntityType<?>, T> create(String name, Codec<T> codec) {
			return DataMapType.builder(Species.of(name), Registries.ENTITY_TYPE, codec).synced(codec, true).build();
		}

	}

}
