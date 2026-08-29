package com.ninni.species.registry;

import com.ninni.species.Species;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class SpeciesLootTables {

    public static ResourceKey<LootTable> WRAPTOR_COOP_CHEST = create("chests/wraptor_coop_chest");
    public static ResourceKey<LootTable> LIBRA_CHEST = create("chests/libra_chest");
    public static ResourceKey<LootTable> PALEONTOLOGY_DIG_SITE_COMMON = create("archaeology/paleontology_dig_site/common");
    public static ResourceKey<LootTable> PALEONTOLOGY_DIG_SITE_RARE = create("archaeology/paleontology_dig_site/rare");
    public static ResourceKey<LootTable> PALEONTOLOGY_DIG_SITE_EPIC = create("archaeology/paleontology_dig_site/epic");

    public static ResourceKey<LootTable> create(String name) {
        return ResourceKey.create(Registries.LOOT_TABLE, Species.of(name));
    }

}
