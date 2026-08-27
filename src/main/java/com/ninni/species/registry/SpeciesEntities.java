package com.ninni.species.registry;

import com.ninni.species.Species;
import com.ninni.species.server.entity.mob.update_1.*;
import com.ninni.species.server.entity.mob.update_2.*;
import com.ninni.species.server.entity.mob.update_3.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SpeciesEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Species.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<Wraptor>> WRAPTOR = register("wraptor",
        EntityType.Builder.of(Wraptor::new, MobCategory.MONSTER)
            .sized(0.8F, 2.2F)
            .clientTrackingRange(8)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<Deepfish>> DEEPFISH = register("deepfish",
        EntityType.Builder.of(Deepfish::new, MobCategory.UNDERGROUND_WATER_CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<Stackatick>> STACKATICK = register("stackatick",
        EntityType.Builder.of(Stackatick::new, MobCategory.CREATURE)
            .sized(1, 1.05F)
            .clientTrackingRange(10)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<Birt>> BIRT = register("birt",
        EntityType.Builder.of(Birt::new, MobCategory.CREATURE)
            .sized(0.6F, 0.6F)
            .clientTrackingRange(10)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<BirtEgg>> BIRT_EGG = register("birt_egg",
        EntityType.Builder.<BirtEgg>of(BirtEgg::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<Limpet>> LIMPET = register("limpet",
        EntityType.Builder.of(Limpet::new, MobCategory.MONSTER)
            .sized(0.75F, 1.25F)
            .clientTrackingRange(10)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<Treeper>> TREEPER = register("treeper",
        EntityType.Builder.of(Treeper::new, MobCategory.CREATURE)
            .sized(1.9F, 7)
            .clientTrackingRange(10)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<Trooper>> TROOPER = register("trooper",
        EntityType.Builder.of(Trooper::new, MobCategory.CREATURE)
            .sized(0.7F, 1.2F)
            .clientTrackingRange(10)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<Goober>> GOOBER = register("goober",
        EntityType.Builder.of(Goober::new, MobCategory.CREATURE)
            .sized(1.5F, 2.2F)
            .clientTrackingRange(10)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<Cruncher>> CRUNCHER = register("cruncher",
        EntityType.Builder.of(Cruncher::new, MobCategory.CREATURE)
            .sized(2.6F, 4.2F)
            .clientTrackingRange(10)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<Mammutilation>> MAMMUTILATION = register("mammutilation",
        EntityType.Builder.of(Mammutilation::new, MobCategory.CREATURE)
            .sized(2.6F, 3.8F)
            .clientTrackingRange(10)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<Springling>> SPRINGLING = register("springling",
        EntityType.Builder.of(Springling::new, MobCategory.CREATURE)
            .sized(0.8F, 1.3F)
            .clientTrackingRange(10)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<CruncherPellet>> CRUNCHER_PELLET = register("cruncher_pellet",
        EntityType.Builder.<CruncherPellet>of(CruncherPellet::new, MobCategory.MISC)
            .sized(0.98F, 0.98F)
            .clientTrackingRange(10)
            .updateInterval(20)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<GooberGoo>> GOOBER_GOO = register("goober_goo",
        EntityType.Builder.<GooberGoo>of(GooberGoo::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<Ghoul>> GHOUL = register("ghoul",
        EntityType.Builder.of(Ghoul::new, MobCategory.MONSTER)
            .sized(0.8F, 1.5F)
            .clientTrackingRange(30)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<Quake>> QUAKE = register("quake",
        EntityType.Builder.of(Quake::new, MobCategory.MONSTER)
            .sized(1.8F, 2.5F)
            .clientTrackingRange(10)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<DeflectorDummy>> DEFLECTOR_DUMMY = register("deflector_dummy",
        EntityType.Builder.of(DeflectorDummy::new, MobCategory.MISC)
            .sized(0.8F, 1.9F)
            .clientTrackingRange(10)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<Bewereager>> BEWEREAGER = register("bewereager",
        EntityType.Builder.of(Bewereager::new, MobCategory.MONSTER)
            .sized(0.8F, 1.8F)
            .clientTrackingRange(10)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<Spectre>> SPECTRE = register("spectre",
        EntityType.Builder.of(Spectre::new, MobCategory.MONSTER)
            .sized(0.6F, 1.5F)
            .clientTrackingRange(10)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<Wicked>> WICKED = register("wicked",
        EntityType.Builder.of(Wicked::new, MobCategory.MONSTER)
            .sized(0.7F, 1.5F)
            .clientTrackingRange(10)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<WickedFireball>> WICKED_FIREBALL = register("wicked_fireball",
        EntityType.Builder.<WickedFireball>of(WickedFireball::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<WickedSwapperProjectile>> WICKED_SWAPPER = register("wicked_swapper",
        EntityType.Builder.<WickedSwapperProjectile>of(WickedSwapperProjectile::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<LeafHanger>> LEAF_HANGER = register("leaf_hanger",
        EntityType.Builder.of(LeafHanger::new, MobCategory.WATER_CREATURE)
            .sized(0.7F, 1.5F)
            .clientTrackingRange(10)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<CliffHanger>> CLIFF_HANGER = register("cliff_hanger",
        EntityType.Builder.of(CliffHanger::new, MobCategory.MONSTER)
            .sized(0.7F, 1.5F)
            .clientTrackingRange(10)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<Coil>> COIL = register("coil",
        EntityType.Builder.<Coil>of(Coil::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(256)
            .updateInterval(1)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<Harpoon>> HARPOON = register("harpoon",
        EntityType.Builder.<Harpoon>of(Harpoon::new, MobCategory.MISC)
            .sized(0.25f, 0.25f)
            .clientTrackingRange(64)
            .updateInterval(1)
            .setShouldReceiveVelocityUpdates(true)
    );

    public static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        return ENTITY_TYPES.register(name, location -> builder.build(location.toString()));
    }

}
