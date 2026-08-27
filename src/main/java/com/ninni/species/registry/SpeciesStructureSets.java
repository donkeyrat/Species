package com.ninni.species.registry;

import com.ninni.species.Species;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

public class SpeciesStructureSets {

    public static final ResourceKey<StructureSet> WRAPTOR_COOPS = create("wraptor_coops");
    public static final ResourceKey<StructureSet> PALEONTOLOGY_DIG_SITE = create("paleontology_dig_site");
    public static final ResourceKey<StructureSet> LIBRA = create("libra");
    public static final ResourceKey<StructureSet> SPECTRALIBUR_CHAMBER = create("spectralibur_chamber");

    public static ResourceKey<StructureSet> create(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, Species.of(name));
    }

    public static void bootstrap(BootstrapContext<StructureSet> context) {
        HolderGetter<Structure> lookup = context.lookup(Registries.STRUCTURE);
        context.register(WRAPTOR_COOPS, new StructureSet(
            lookup.getOrThrow(SpeciesStructureKeys.WRAPTOR_COOP),
            new RandomSpreadStructurePlacement(64, 8, RandomSpreadType.LINEAR, 867700449))
        );
        context.register(PALEONTOLOGY_DIG_SITE, new StructureSet(
            lookup.getOrThrow(SpeciesStructureKeys.PALEONTOLOGY_DIG_SITE),
            new RandomSpreadStructurePlacement(8, 5, RandomSpreadType.LINEAR, 867700449))
        );
        context.register(LIBRA, new StructureSet(
            lookup.getOrThrow(SpeciesStructureKeys.LIBRA),
            new RandomSpreadStructurePlacement(6, 5, RandomSpreadType.LINEAR, 867700449))
        );
        context.register(SPECTRALIBUR_CHAMBER, new StructureSet(
            lookup.getOrThrow(SpeciesStructureKeys.SPECTRALIBUR_CHAMBER),
            new RandomSpreadStructurePlacement(6, 5, RandomSpreadType.LINEAR, 867700449))
        );
    }

}
