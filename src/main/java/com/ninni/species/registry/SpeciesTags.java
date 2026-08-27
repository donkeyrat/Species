package com.ninni.species.registry;

import com.ninni.species.Species;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class SpeciesTags {

    public interface Items {

        TagKey<Item> WRAPTOR_BREED_ITEMS = create("wraptor_breed_items");
        TagKey<Item> STACKATICK_BREED_ITEMS = create("stackatick_breed_items");
        TagKey<Item> STACKATICK_TEMPT_ITEMS = create("stackatick_tempt_items");
        TagKey<Item> STACKATICK_TAME_ITEMS = create("stackatick_tame_items");
        TagKey<Item> GOOBER_BREED_ITEMS = create("goober_breed_items");
        TagKey<Item> SPRINGLING_BREED_ITEMS = create("springling_breed_items");
        TagKey<Item> SPRINGLING_TAMING_ITEMS = create("springling_taming_items");
        TagKey<Item> CRUNCHER_EATS = create("cruncher_eats");
        TagKey<Item> BURNS_TREEPER = create("burns_treeper");
        TagKey<Item> EXTINGUISHES_TREEPER = create("extinguishes_treeper");

        static TagKey<Item> create(String name) {
            return TagKey.create(Registries.ITEM, Species.of(name));
        }

    }

    public interface Blocks {

        TagKey<Block> STACKATICK_IS_COMFY_ON = create("stackatick_is_comfy_on");
        TagKey<Block> WRAPTOR_NESTING_BLOCKS = create("wraptor_nesting_blocks");
        TagKey<Block> LIMPET_SPAWNABLE_ON = create("limpet_spawnable_on");
        TagKey<Block> TREEPER_SPAWNABLE_ON = create("treeper_spawnable_on");
        TagKey<Block> PETRIFIED_EGG_HATCH = create("petrified_egg_hatch");
        TagKey<Block> PETRIFIED_EGG_HATCH_BOOST = create("petrified_egg_hatch_boost");
        TagKey<Block> MAMMUTILATION_REMNANT_INVALID_BLOCKS = create("mammutilation_remnant_invalid_blocks");
        TagKey<Block> MAMMUTILATION_BODY_BLOCKS = create("mammutilation_body_blocks");
        TagKey<Block> CLIFF_HANGER_SPAWNABLE_ON = create("cliff_hanger_spawnable_on");
        TagKey<Block> COILABLE = create("coilable");

        static TagKey<Block> create(String name) {
            return TagKey.create(Registries.BLOCK, Species.of(name));
        }

    }

    public interface Biomes {

        TagKey<Biome> WRAPTOR_COOP_HAS_STRUCTURE = create("wraptor_coop_has_structure");
        TagKey<Biome> STACKATICK_SPAWNS = create("stackatick_spawns");
        TagKey<Biome> BIRT_TREE_SPAWNS_IN = create("birt_tree_spawns_in");
        TagKey<Biome> MAMMUTILATION_REMNANT_SPAWNS_IN = create("mammutilation_remnant_spawns_in");
        TagKey<Biome> LIMPET_SPAWNS = create("limpet_spawns");
        TagKey<Biome> WITHOUT_LIMPET_SPAWNS = create("without_limpet_spawns");
        TagKey<Biome> TREEPER_SPAWNS = create("treeper_spawns");
        TagKey<Biome> LIBRA_HAS_STRUCTURE = create("libra_has_structure");
        TagKey<Biome> SPECTRALIBUR_CHAMBER_HAS_STRUCTURE = create("spectralibur_chamber_has_structure");
        TagKey<Biome> LEAF_HANGER_SPAWNS = create("leaf_hanger_spawns");
        TagKey<Biome> LEAF_HANGER_HAS_DRIPLEAF = create("leaf_hanger_has_dripleaf");

        static TagKey<Biome> create(String name) {
            return TagKey.create(Registries.BIOME, Species.of(name));
        }

    }

    public interface PointsOfInterest {

        TagKey<PoiType> BIRT_HOME = create("birt_home");

        static TagKey<PoiType> create(String name) {
            return TagKey.create(Registries.POINT_OF_INTEREST_TYPE, Species.of(name));
        }

    }

    public interface EntityTypes {

        TagKey<EntityType<?>> ALWAYS_ADULT = create("always_adult");
        TagKey<EntityType<?>> CANT_BE_DAMAGED_BY_DUMMY = create("cant_be_damaged_by_dummy");
        TagKey<EntityType<?>> CANT_BE_TARGETED_BY_GHOUL = create("cant_be_targeted_by_ghoul");
        TagKey<EntityType<?>> ATTACKED_BY_BEWEREAGER = create("attacked_by_bewereager");
        TagKey<EntityType<?>> SOULLESS = create("soulless");
        TagKey<EntityType<?>> CANT_BE_HAUNTED = create("cant_be_haunted");
        TagKey<EntityType<?>> CAN_BE_HAUNTED_EXTRAS = create("can_be_haunted_extras");
        TagKey<EntityType<?>> CLIFF_HANGER_PREY = create("cliff_hanger_prey");
        TagKey<EntityType<?>> LEAF_HANGER_PREY = create("leaf_hanger_prey");
        TagKey<EntityType<?>> PREHISTORIC = create("prehistoric");

        static TagKey<EntityType<?>> create(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, Species.of(name));
        }

    }

    public interface Enchantments {

        TagKey<Enchantment> PREVENT_BIRT_SPAWNS_WHEN_MINING = create("prevent_birt_spawns_when_mining");
        TagKey<Enchantment> PREVENT_LIMPET_ORE_DROPS = create("prevent_limpet_ore_drops");

        static TagKey<Enchantment> create(String name) {
            return TagKey.create(Registries.ENCHANTMENT, Species.of(name));
        }

    }

}
