package com.ninni.species.registry;

import com.ninni.species.Species;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;

public class SpeciesStructureKeys {

    public static final ResourceKey<Structure> LIBRA = create("libra");
    public static final ResourceKey<Structure> SPECTRALIBUR_CHAMBER = create("spectralibur_chamber");
    public static final ResourceKey<Structure> WRAPTOR_COOP = create("wraptor_coop");
    public static final ResourceKey<Structure> PALEONTOLOGY_DIG_SITE = create("paleontology_dig_site");

    public static ResourceKey<Structure> create(String name) {
        return ResourceKey.create(Registries.STRUCTURE, Species.of(name));
    }

}
