package com.ninni.species.registry;

import com.ninni.species.Species;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.decoration.PaintingVariant;

public class SpeciesPaintingVariants {

    public static final ResourceKey<PaintingVariant> THE_COMPOSITION = ResourceKey.create(Registries.PAINTING_VARIANT, Species.of("the_composition"));

}
