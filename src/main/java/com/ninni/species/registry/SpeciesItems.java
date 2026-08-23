package com.ninni.species.registry;

import com.ninni.species.Species;
import com.ninni.species.SpeciesDevelopers;
import com.ninni.species.server.item.*;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Optional;

public class SpeciesItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Species.MOD_ID);

    public static final DeferredItem<Item> LOGO = ITEMS.registerItem("logo", Item::new);

    //UPDATE 1
    public static final DeferredItem<Item> WRAPTOR_SPAWN_EGG = ITEMS.registerItem("wraptor_spawn_egg", properties ->
        new SpeciesSpawnEggItem(SpeciesEntities.WRAPTOR.get(), 0xBC2765, 0x44A19D, SpeciesDevelopers.SpeciesDeveloperNames.NOON, properties)
    );
    public static final DeferredItem<BlockItem> WRAPTOR_EGG = ITEMS.registerSimpleBlockItem(SpeciesBlocks.WRAPTOR_EGG);
    public static final DeferredItem<Item> CRACKED_WRAPTOR_EGG = ITEMS.registerItem("cracked_wraptor_egg",
        CrakedWraptorEggItem::new,
        new Item.Properties().food(SpeciesFoodProperties.CRACKED_WRAPTOR_EGG)
    );

    public static final DeferredItem<Item> DEEPFISH_SPAWN_EGG = ITEMS.registerItem("deepfish_spawn_egg", properties ->
        new SpeciesSpawnEggItem(SpeciesEntities.DEEPFISH.get(), 0x5A5A5A, 0xED98BD, SpeciesDevelopers.SpeciesDeveloperNames.BORNULHU, properties)
    );
    public static final DeferredItem<Item> DEEPFISH_BUCKET = ITEMS.registerItem("deepfish_bucket", properties ->
        new MobBucketItem(SpeciesEntities.DEEPFISH.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, properties),
        new Item.Properties().stacksTo(1)
    );

    public static final DeferredItem<Item> STACKATICK_SPAWN_EGG = ITEMS.registerItem("stackatick_spawn_egg", properties ->
        new SpeciesSpawnEggItem(SpeciesEntities.STACKATICK.get(), 0x83493B, 0x1F1F21, SpeciesDevelopers.SpeciesDeveloperNames.NINNI, properties)
    );

    public static final DeferredItem<Item> BIRT_SPAWN_EGG = ITEMS.registerItem("birt_spawn_egg", properties ->
        new SpeciesSpawnEggItem(SpeciesEntities.BIRT.get(), 0x4DD1E1, 0xD87247, SpeciesDevelopers.SpeciesDeveloperNames.REDA, properties)
    );
    public static final DeferredItem<Item> BIRT_EGG = ITEMS.registerItem("birt_egg", BirtEggItem::new,
        new Item.Properties().stacksTo(16)
    );
    public static final DeferredItem<BlockItem> BIRT_DWELLING = ITEMS.registerSimpleBlockItem(SpeciesBlocks.BIRT_DWELLING);
    public static final DeferredItem<BlockItem> BIRTDAY_CAKE = ITEMS.registerSimpleBlockItem(SpeciesBlocks.BIRTDAY_CAKE,
        new Item.Properties().stacksTo(1)
    );
    public static final DeferredItem<Item> BIRTDAY_CAKE_SLICE = ITEMS.registerItem("birtday_cake_slice", BirtdayCakeSliceItem::new, new Item.Properties().food(new FoodProperties.Builder().alwaysEdible().nutrition(4).saturationModifier(0.6f).effect(() -> new MobEffectInstance(SpeciesStatusEffects.BIRTD, 20 * 10, 0), 1).build()));
    public static final DeferredItem<Item> MUSIC_DISC_DIAL = ITEMS.registerItem("music_disc_dial", Item::new, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath("species", "dial"))));

    public static final DeferredItem<Item> LIMPET_SPAWN_EGG = ITEMS.registerItem("limpet_spawn_egg",properties ->
        new SpeciesSpawnEggItem(SpeciesEntities.LIMPET.get(), 0xA5C1D2, 0xFBF236, SpeciesDevelopers.SpeciesDeveloperNames.GLADOS, properties)
    );

    //UPDATE 2
    public static final DeferredItem<BlockItem> RED_SUSPICIOUS_SAND = ITEMS.registerSimpleBlockItem(SpeciesBlocks.RED_SUSPICIOUS_SAND);
    public static final DeferredItem<Item> MUSIC_DISC_LAPIDARIAN = ITEMS.registerItem("music_disc_lapidarian", Item::new,
        new Item.Properties()
            .rarity(Rarity.RARE).stacksTo(1)
            .jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG, Species.of("lapidarian")))
    );

    public static final DeferredItem<BlockItem> BONE_BARK = ITEMS.registerSimpleBlockItem(SpeciesBlocks.BONE_BARK);
    public static final DeferredItem<BlockItem> BONE_VERTEBRA = ITEMS.registerSimpleBlockItem(SpeciesBlocks.BONE_VERTEBRA);
    public static final DeferredItem<BlockItem> BONE_SPIKE = ITEMS.registerSimpleBlockItem(SpeciesBlocks.BONE_SPIKE);

    public static final DeferredItem<Item> TREEPER_SPAWN_EGG = ITEMS.registerItem("treeper_spawn_egg", properties ->
        new SpeciesSpawnEggItem(SpeciesEntities.TREEPER.get(), 0x402E1B, 0x32992D, SpeciesDevelopers.SpeciesDeveloperNames.NINNI, properties)
    );
    public static final DeferredItem<Item> ANCIENT_PINECONE = ITEMS.registerItem("ancient_pinecone",
        properties -> new ItemNameBlockItem(SpeciesBlocks.TROOPER.get(), properties)
    );
    public static final DeferredItem<Item> TROOPER_SPAWN_EGG = ITEMS.registerItem("trooper_spawn_egg",
        properties -> new SpeciesSpawnEggItem(SpeciesEntities.TROOPER.get(), 0x6f5535, 0x32992D, SpeciesDevelopers.SpeciesDeveloperNames.NINNI, properties)
    );

    public static final DeferredItem<Item> GOOBER_SPAWN_EGG = ITEMS.registerItem("goober_spawn_egg", properties ->
        new SpeciesSpawnEggItem(SpeciesEntities.GOOBER.get(), 0x49674E, 0x49674E, SpeciesDevelopers.SpeciesDeveloperNames.BORNULHU, properties)
    );
    public static final DeferredItem<BlockItem> PETRIFIED_EGG = ITEMS.registerSimpleBlockItem(SpeciesBlocks.PETRIFIED_EGG);
    public static final DeferredItem<BlockItem> ALPHACENE_MOSS_BLOCK = ITEMS.registerSimpleBlockItem(SpeciesBlocks.ALPHACENE_MOSS_BLOCK);
    public static final DeferredItem<BlockItem> ALPHACENE_MOSS_CARPET = ITEMS.registerSimpleBlockItem(SpeciesBlocks.ALPHACENE_MOSS_CARPET);
    public static final DeferredItem<BlockItem> ALPHACENE_GRASS_BLOCK = ITEMS.registerSimpleBlockItem(SpeciesBlocks.ALPHACENE_GRASS_BLOCK);
    public static final DeferredItem<BlockItem> ALPHACENE_GRASS = ITEMS.registerSimpleBlockItem(SpeciesBlocks.ALPHACENE_GRASS);
    public static final DeferredItem<Item> ALPHACENE_TALL_GRASS = ITEMS.registerItem("alphacene_tall_grass",
        properties -> new DoubleHighBlockItem(SpeciesBlocks.ALPHACENE_TALL_GRASS.get(), properties)
    );
    public static final DeferredItem<BlockItem> ALPHACENE_MUSHROOM = ITEMS.registerSimpleBlockItem(SpeciesBlocks.ALPHACENE_MUSHROOM);
    public static final DeferredItem<BlockItem> ALPHACENE_MUSHROOM_BLOCK = ITEMS.registerSimpleBlockItem(SpeciesBlocks.ALPHACENE_MUSHROOM_BLOCK);
    public static final DeferredItem<BlockItem> ALPHACENE_MUSHROOM_GROWTH = ITEMS.registerSimpleBlockItem(SpeciesBlocks.ALPHACENE_MUSHROOM_GROWTH);

    public static final DeferredItem<Item> CRUNCHER_SPAWN_EGG = ITEMS.registerItem("cruncher_spawn_egg", properties -> new SpeciesSpawnEggItem(SpeciesEntities.CRUNCHER.get(), 0x5522B6, 0x99032B, SpeciesDevelopers.SpeciesDeveloperNames.NOON, properties));
    public static final DeferredItem<Item> CRUNCHER_EGG = ITEMS.registerItem("cruncher_egg",
        properties -> new DoubleHighBlockItem(SpeciesBlocks.CRUNCHER_EGG.get(), properties)
    );
    public static final DeferredItem<BlockItem> CRUNCHER_PELLET = ITEMS.registerSimpleBlockItem(SpeciesBlocks.CRUNCHER_PELLET);

    public static final DeferredItem<Item> MAMMUTILATION_SPAWN_EGG = ITEMS.registerItem("mammutilation_spawn_egg",
        properties -> new SpeciesSpawnEggItem(SpeciesEntities.MAMMUTILATION.get(), 0x472418, 0xDE5D34, SpeciesDevelopers.SpeciesDeveloperNames.REDA, properties)
    );
    public static final DeferredItem<BlockItem> FROZEN_MEAT = ITEMS.registerSimpleBlockItem(SpeciesBlocks.FROZEN_MEAT);
    public static final DeferredItem<BlockItem> FROZEN_HAIR = ITEMS.registerSimpleBlockItem(SpeciesBlocks.FROZEN_HAIR);
    public static final DeferredItem<Item> ICHOR_BOTTLE = ITEMS.registerItem("ichor_bottle",
        properties -> new IchorBottle(SpeciesBlocks.ICHOR.get(), properties),
        new Item.Properties().stacksTo(16)
    );
    public static final DeferredItem<Item> YOUTH_POTION = ITEMS.registerItem("youth_potion", YouthPotion::new,
        new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)
    );

    public static final DeferredItem<Item> SPRINGLING_SPAWN_EGG = ITEMS.registerItem("springling_spawn_egg",
        properties -> new SpeciesSpawnEggItem(SpeciesEntities.SPRINGLING.get(), 0x413D70, 0xE7663A, SpeciesDevelopers.SpeciesDeveloperNames.GLADOS, properties)
    );
    public static final DeferredItem<Item> SPRINGLING_EGG = ITEMS.registerItem("springling_egg",
        properties -> new DoubleHighBlockItem(SpeciesBlocks.SPRINGLING_EGG.get(), properties)
    );

    //UPDATE 3
    public static final DeferredItem<Item> GHOUL_SPAWN_EGG = ITEMS.registerItem("ghoul_spawn_egg",
        properties -> new SpeciesSpawnEggItem(SpeciesEntities.GHOUL.get(), 0xA3908C, 0xBAA3A0, SpeciesDevelopers.SpeciesDeveloperNames.BORNULHU, properties)
    );
    public static final DeferredItem<Item> GHOUL_TONGUE = ITEMS.registerItem("ghoul_tongue", Item::new,
        new Item.Properties().food(SpeciesFoodProperties.GHOUL_TONGUE)
    );
    public static final DeferredItem<Item> GHOUL_HEAD = ITEMS.registerItem("ghoul_head",
        properties -> new StandingAndWallBlockItem(SpeciesBlocks.GHOUL_HEAD.get(), SpeciesBlocks.GHOUL_WALL_HEAD.get(), properties, Direction.DOWN),
        new Item.Properties().rarity(Rarity.UNCOMMON)
    );

    public static final DeferredItem<Item> QUAKE_SPAWN_EGG = ITEMS.registerItem("quake_spawn_egg",
        properties -> new SpeciesSpawnEggItem(SpeciesEntities.QUAKE.get(), 0x454646, 0xB77541, SpeciesDevelopers.SpeciesDeveloperNames.NINNI, properties)
    );
    public static final DeferredItem<BlockItem> KINETIC_CORE = ITEMS.registerSimpleBlockItem(SpeciesBlocks.KINETIC_CORE,
        new Item.Properties().rarity(Rarity.UNCOMMON)
    );
    public static final DeferredItem<Item> DEFLECTOR_DUMMY = ITEMS.registerItem("deflector_dummy", DeflectorDummyItem::new,
        new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(16)
    );
    public static final DeferredItem<Item> RICOSHIELD = ITEMS.registerItem("ricoshield", RicoshieldItem::new,
        new Item.Properties().rarity(Rarity.UNCOMMON).durability(528)
    );
    public static final DeferredItem<Item> QUAKE_HEAD = ITEMS.registerItem("quake_head",
        properties -> new StandingAndWallBlockItem(SpeciesBlocks.QUAKE_HEAD.get(), SpeciesBlocks.QUAKE_WALL_HEAD.get(), properties, Direction.DOWN),
        new Item.Properties().rarity(Rarity.UNCOMMON)
    );
    public static final DeferredItem<Item> MUSIC_DISK_SPAWNER = ITEMS.registerItem("music_disk_spawner", Item::new,
        new Item.Properties()
            .rarity(Rarity.RARE)
            .stacksTo(1)
            .jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG, Species.of("spawner")))
    );

    public static final DeferredItem<Item> SPECTRE_SPAWN_EGG = ITEMS.registerItem("spectre_spawn_egg",
        properties -> new SpeciesSpawnEggItem(SpeciesEntities.SPECTRE.get(), 0x182C39, 0x35f8ff, SpeciesDevelopers.SpeciesDeveloperNames.REDA, properties)
    );
    public static final DeferredItem<Item> BROKEN_LINKS = ITEMS.registerItem("broken_links", Item::new);
    public static final DeferredItem<Item> SPECLIGHT = ITEMS.registerItem("speclight",
        properties -> new SpectreLightBlockItem(SpeciesBlocks.SPECLIGHT.get(), properties),
        new Item.Properties().component(DataComponents.DYED_COLOR, new DyedItemColor(0x7CF2F5, false))
    );
    public static final DeferredItem<BlockItem> CHAINDELIER = ITEMS.registerSimpleBlockItem(SpeciesBlocks.CHAINDELIER);
    public static final DeferredItem<Item> HOPELIGHT = ITEMS.registerItem("hopelight",
        properties -> new SpectreLightBlockItem(SpeciesBlocks.HOPELIGHT.get(), properties),
        new Item.Properties().component(DataComponents.DYED_COLOR, new DyedItemColor(0x7CF2F5, false))
    );
    public static final DeferredItem<Item> SPECTRALIBUR = ITEMS.registerItem("spectralibur", SpectraliburItem::new,
        new Item.Properties().stacksTo(1).rarity(Rarity.RARE).attributes(SpectraliburItem.createAttributes())
    );
    public static final DeferredItem<BlockItem> SPECTRALIBUR_PEDESTAL = ITEMS.registerSimpleBlockItem(SpeciesBlocks.SPECTRALIBUR_PEDESTAL,
        new Item.Properties().rarity(Rarity.RARE)
    );

    public static final DeferredItem<Item> WICKED_SPAWN_EGG = ITEMS.registerItem("wicked_spawn_egg",
        properties -> new SpeciesSpawnEggItem(SpeciesEntities.WICKED.get(), 0x435AA3, 0xDF77A0, SpeciesDevelopers.SpeciesDeveloperNames.GLADOS, properties)
    );
    public static final DeferredItem<Item> WICKED_WAX = ITEMS.registerItem("wicked_wax", Item::new);
    public static final DeferredItem<Item> WICKED_SWAPPER = ITEMS.registerItem("wicked_swapper", WickedSwapperItem::new,
        new Item.Properties().stacksTo(16)
    );
    public static final DeferredItem<Item> MONSTER_MEAL = ITEMS.registerItem("monster_meal", MonsterMealitem::new,
        new Item.Properties().food(SpeciesFoodProperties.MONSTER_MEAL)
    );
    public static final DeferredItem<Item> SMOKE_BOMB = ITEMS.registerItem("smoke_bomb", SmokeBombItem::new,
        new Item.Properties().stacksTo(16)
    );
    public static final DeferredItem<Item> WICKED_DOPE = ITEMS.registerItem("wicked_dope", WickedDopeItem::new,
        new Item.Properties().food(SpeciesFoodProperties.WICKED_DOPE)
    );
    public static final DeferredItem<Item> WICKED_MASK = ITEMS.registerItem("wicked_mask", WickedMaskItem::new,
        new Item.Properties().stacksTo(1)
    );
    public static final DeferredItem<Item> WICKED_TREAT = ITEMS.register("wicked_treat",
        () -> new PetEffectItem(new Item.Properties().component(DataComponents.POTION_CONTENTS, new PotionContents(
            Optional.empty(),
            Optional.empty(),
            List.of(
                new MobEffectInstance(SpeciesStatusEffects.SNATCHED, 45 * 20, 1),
                new MobEffectInstance(SpeciesStatusEffects.IRON_WILL, 45 * 20, 0),
                new MobEffectInstance(MobEffects.REGENERATION, 45 * 20, 0),
                new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 20, 0)
            )
        )).component(SpeciesDataComponents.COOLDOWN, 45 * 20))
    );
    public static final DeferredItem<Item> WICKED_CANDLE = ITEMS.registerItem("wicked_candle",
        properties -> new StandingAndWallBlockItem(SpeciesBlocks.WICKED_CANDLE.get(), SpeciesBlocks.WICKED_WALL_CANDLE.get(), properties, Direction.DOWN),
        new Item.Properties().rarity(Rarity.UNCOMMON)
    );

    public static final DeferredItem<Item> BEWEREAGER_SPAWN_EGG = ITEMS.registerItem("bewereager_spawn_egg",
        properties -> new SpeciesSpawnEggItem(SpeciesEntities.BEWEREAGER.get(), 0x8D383F, 0x5D4B4E, SpeciesDevelopers.SpeciesDeveloperNames.NOON, properties)
    );
    public static final DeferredItem<Item> WEREFANG = ITEMS.registerItem("werefang", Item::new);
    public static final DeferredItem<Item> CRANKBOW = ITEMS.registerItem("crankbow", CrankbowItem::new,
        new Item.Properties().stacksTo(1).durability(865)
    );
    public static final DeferredItem<BlockItem> CRANKTRAP = ITEMS.registerSimpleBlockItem(SpeciesBlocks.CRANKTRAP);
    public static final DeferredItem<Item> BEWEREAGER_HEAD = ITEMS.registerItem("bewereager_head",
        properties -> new StandingAndWallBlockItem(SpeciesBlocks.BEWEREAGER_HEAD.get(), SpeciesBlocks.BEWEREAGER_WALL_HEAD.get(), properties, Direction.DOWN),
        new Item.Properties().rarity(Rarity.UNCOMMON)
    );

    public static final DeferredItem<Item> LEAF_HANGER_SPAWN_EGG = ITEMS.registerItem("leaf_hanger_spawn_egg",
        properties -> new SpeciesSpawnEggItem(SpeciesEntities.LEAF_HANGER.get(), 0x43994E, 0x5C4A45, SpeciesDevelopers.SpeciesDeveloperNames.YAPETTO, properties)
    );
    public static final DeferredItem<Item> CLIFF_HANGER_SPAWN_EGG = ITEMS.registerItem("cliff_hanger_spawn_egg",
        properties -> new SpeciesSpawnEggItem(SpeciesEntities.CLIFF_HANGER.value(), 0x8B7648, 0x48484B, SpeciesDevelopers.SpeciesDeveloperNames.YAPETTO, properties)
    );
    public static final DeferredItem<Item> COIL = ITEMS.registerItem("coil", CoilItem::new);
    public static final DeferredItem<Item> HARPOON = ITEMS.registerItem("harpoon", HarpoonItem::new,
        new Item.Properties().stacksTo(1).durability(128)
    );

}
