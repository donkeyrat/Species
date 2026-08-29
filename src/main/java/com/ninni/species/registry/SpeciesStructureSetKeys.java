package com.ninni.species.registry;

import com.ninni.species.Species;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.StructureSet;

public class SpeciesStructureSetKeys {

    public static final ResourceKey<StructureSet> WRAPTOR_COOPS = create("wraptor_coops");

    public static ResourceKey<StructureSet> create(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, Species.of(name));
    }

}
