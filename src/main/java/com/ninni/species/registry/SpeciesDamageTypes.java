package com.ninni.species.registry;

import com.ninni.species.Species;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class SpeciesDamageTypes {

    public static final ResourceKey<DamageType> CRUNCH = create("crunch");
    public static final ResourceKey<DamageType> TORN = create("torn");
    public static final ResourceKey<DamageType> KINETIC = create("kinetic");
    public static final ResourceKey<DamageType> CRANKTRAP = create("cranktrap");

    public static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Species.of(name));
    }
    
}