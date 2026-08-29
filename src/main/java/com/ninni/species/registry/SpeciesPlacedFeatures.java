package com.ninni.species.registry;

import com.ninni.species.Species;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

public class SpeciesPlacedFeatures {

    public static final ResourceKey<PlacedFeature> BIRTED_BIRCH_TREE_CHECKED = create("birted_birch");
    public static final ResourceKey<PlacedFeature> BIRTED_BIRCH_TREES = create("birted_birch_trees");
    public static final ResourceKey<PlacedFeature> MAMMUTILATION_REMNANT = create("mammutilation_remnant");

    public static ResourceKey<PlacedFeature> create(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Species.of(name));
    }

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> lookup = context.lookup(Registries.CONFIGURED_FEATURE);

        PlacementUtils.register(context, BIRTED_BIRCH_TREE_CHECKED,
            lookup.getOrThrow(SpeciesConfiguredFeatures.BIRTED_BIRCH),
            PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
        );
        PlacementUtils.register(context, BIRTED_BIRCH_TREES,
            lookup.getOrThrow(SpeciesConfiguredFeatures.BIRTED_BIRCH_TREE_FILTERED),
            VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(50))
        );
        PlacementUtils.register(context, MAMMUTILATION_REMNANT,
            lookup.getOrThrow(SpeciesConfiguredFeatures.MAMMUTILATION_REMNANT),
            RarityFilter.onAverageOnceEvery(5),
            InSquarePlacement.spread(),
            HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(20), VerticalAnchor.absolute(200)),
            BiomeFilter.biome()
        );
    }

}
