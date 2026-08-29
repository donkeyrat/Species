package com.ninni.species.registry;

import com.mojang.datafixers.types.Type;
import com.ninni.species.Species;
import com.ninni.species.server.block.entity.*;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SpeciesBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Species.MOD_ID);

    //Wave 1
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BirtDwellingBlockEntity>> BIRT_DWELLING = register("birt_dwelling", () -> BlockEntityType.Builder.of(BirtDwellingBlockEntity::new, SpeciesBlocks.BIRT_DWELLING.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BirtdayCakeBlockEntity>> BIRTDAY_CAKE = register("birtday_cake", () -> BlockEntityType.Builder.of(BirtdayCakeBlockEntity::new, SpeciesBlocks.BIRTDAY_CAKE.get()));

    //Wave 2
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CruncherPelletBlockEntity>> CRUNCHER_PELLET = register("cruncher_pellet", () -> BlockEntityType.Builder.of(CruncherPelletBlockEntity::new, SpeciesBlocks.CRUNCHER_PELLET.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CruncherEggBlockEntity>> CRUNCHER_EGG = register("cruncher_egg", () -> BlockEntityType.Builder.of(CruncherEggBlockEntity::new, SpeciesBlocks.CRUNCHER_EGG.get()));

    //Wave 3
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SpeciesSkullBlockEntity>> SKULL = register("skull", () -> BlockEntityType.Builder.of(SpeciesSkullBlockEntity::new, SpeciesBlocks.GHOUL_HEAD.get(), SpeciesBlocks.GHOUL_WALL_HEAD.get(), SpeciesBlocks.WICKED_CANDLE.get(), SpeciesBlocks.WICKED_WALL_CANDLE.get(), SpeciesBlocks.QUAKE_HEAD.get(), SpeciesBlocks.QUAKE_WALL_HEAD.get(), SpeciesBlocks.BEWEREAGER_HEAD.get(), SpeciesBlocks.BEWEREAGER_WALL_HEAD.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SpectraliburBlockEntity>> SPECTRALIBUR = register("spectralibur", () -> BlockEntityType.Builder.of(SpectraliburBlockEntity::new, SpeciesBlocks.SPECTRALIBUR.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SpeclightBlockEntity>> SPECLIGHT = register("speclight", () -> BlockEntityType.Builder.of(SpeclightBlockEntity::new, SpeciesBlocks.SPECLIGHT.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ChaindelierBlockEntity>> CHAINDELIER = register("chaindelier", () -> BlockEntityType.Builder.of(ChaindelierBlockEntity::new, SpeciesBlocks.CHAINDELIER.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HopelightBlockEntity>> HOPELIGHT = register("hopelight", () -> BlockEntityType.Builder.of(HopelightBlockEntity::new, SpeciesBlocks.HOPELIGHT.get()));

    public static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String name, Supplier<BlockEntityType.Builder<T>> builder) {
        Type<?> type = Util.fetchChoiceType(References.BLOCK_ENTITY, name);
        return BLOCK_ENTITY_TYPES.register(name, () -> builder.get().build(type));
    }

}
