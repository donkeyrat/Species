package com.ninni.species.registry;

import com.ninni.species.Species;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.animal.WolfVariant;
import net.minecraft.world.entity.npc.VillagerType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SpeciesWolfVariants {

    public static final ResourceKey<WolfVariant> CURED_BEWEREAGER = ResourceKey.create(Registries.WOLF_VARIANT, Species.of("cured_bewereager"));

}
