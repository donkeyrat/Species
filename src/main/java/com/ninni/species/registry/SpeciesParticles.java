package com.ninni.species.registry;

import com.ninni.species.Species;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SpeciesParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, Species.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SNORING = register("snoring", false);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BIRTD = register("birtd", false);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DRIPPING_PELLET_DRIP = register("dripping_pellet_drip", false);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FALLING_PELLET_DRIP = register("falling_pellet_drip", false);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LANDING_PELLET_DRIP = register("landing_pellet_drip", false);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FOOD = register("food", false);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ASCENDING_DUST = register("ascending_dust", false);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> TREEPER_LEAF = register("treeper_leaf", false);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ICHOR = register("ichor", false);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> YOUTH_POTION = register("youth_potion", false);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GHOUL_SEARCHING = register("ghoul_searching", true);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GHOUL_SEARCHING2 = register("ghoul_searching2", true);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> KINETIC_ENERGY = register("kinetic_energy", true);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SMALL_KINETIC_ENERGY = register("small_kinetic_energy", true);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> WICKED_FLAME = register("wicked_flame", true);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> WICKED_EMBER = register("wicked_ember", false);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> POOF = register("poof", true);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPECTRALIBUR = register("spectralibur", true);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPECTRALIBUR_INVERTED = register("spectralibur_inverted", true);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPECTRALIBUR_RELEASED = register("spectralibur_released", true);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPECTRE_SMOKE = register("spectre_smoke", true);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ASCENDING_SPECTRE_SMOKE = register("ascending_spectre_smoke", true);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPECTRE_POP = register("spectre_pop", true);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BROKEN_LINK = register("broken_link", true);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> COLLECTED_SOUL = register("collected_soul", true);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BEWEREAGER_HOWL = register("bewereager_howl", true);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BEWEREAGER_SPEED = register("bewereager_speed", true);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BEWEREAGER_SLOW = register("bewereager_slow", true);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DRIPPING_HANGER_SALIVA = register("dripping_hanger_saliva", false);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FALLING_HANGER_SALIVA = register("falling_hanger_saliva", false);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LANDING_HANGER_SALIVA = register("landing_hanger_saliva", false);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HANGER_CRIT = register("hanger_crit", true);

    public static DeferredHolder<ParticleType<?>, SimpleParticleType> register(String name, boolean overrideLimiter) {
        return register(name, () -> new SimpleParticleType(overrideLimiter));
    }

    public static <T extends ParticleType<?>> DeferredHolder<ParticleType<?>, T> register(String name, Supplier<T> supplier) {
        return PARTICLE_TYPES.register(name, supplier);
    }

}