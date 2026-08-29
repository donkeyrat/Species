package com.ninni.species.registry;

import com.mojang.serialization.Codec;
import com.ninni.species.Species;
import com.ninni.species.server.block.entity.BirtDwellingBlockEntity.Occupant;
import com.ninni.species.server.component.EntityDataComponent;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;

import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class SpeciesDataComponents {

    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Species.MOD_ID);

    public static final Supplier<DataComponentType<List<Occupant>>> BIRTS = DATA_COMPONENTS.registerComponentType("birts", builder -> builder
        .persistent(Occupant.LIST_CODEC)
        .networkSynchronized(Occupant.STREAM_CODEC.apply(ByteBufCodecs.list()))
        .cacheEncoding()
    );

    public static final Supplier<DataComponentType<Integer>> COOLDOWN = DATA_COMPONENTS.registerComponentType("cooldown", builder -> builder
        .persistent(Codec.INT)
        .networkSynchronized(ByteBufCodecs.INT)
        .cacheEncoding()
    );

    public static final Supplier<DataComponentType<Float>> STORED_DAMAGE = DATA_COMPONENTS.registerComponentType("stored_damage", builder -> builder
        .persistent(Codec.FLOAT)
        .networkSynchronized(ByteBufCodecs.FLOAT)
        .cacheEncoding()
    );

    public static final Supplier<DataComponentType<Integer>> SOULS = DATA_COMPONENTS.registerComponentType("souls", builder -> builder
        .persistent(Codec.INT)
        .networkSynchronized(ByteBufCodecs.INT)
        .cacheEncoding()
    );

    public static final Supplier<DataComponentType<Integer>> USING_SOULS = register("using_souls", Codec.INT, ByteBufCodecs.INT);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<EntityDataComponent>> STORED_ENTITY_DATA = register("stored_entity_data",
        EntityDataComponent.CODEC, EntityDataComponent.STREAM_CODEC
    );

    public static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return DATA_COMPONENTS.registerComponentType(name, builder -> builder
            .persistent(codec)
            .networkSynchronized(streamCodec)
            .cacheEncoding()
        );
    }

}