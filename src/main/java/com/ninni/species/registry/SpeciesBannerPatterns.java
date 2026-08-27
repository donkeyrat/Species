package com.ninni.species.registry;

import com.ninni.species.Species;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BannerPattern;

public class SpeciesBannerPatterns {

    public static final ResourceKey<BannerPattern> VILLAGER = create("villager");

    public static ResourceKey<BannerPattern> create(String name) {
        return ResourceKey.create(Registries.BANNER_PATTERN, Species.of(name));
    }

}
