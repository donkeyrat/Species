package com.ninni.species.registry;

import com.ninni.species.server.world.gen.structure.LibraStructure;
import com.ninni.species.server.world.gen.structure.PaleontologyDigSiteStructure;
import com.ninni.species.server.world.gen.structure.SpectraliburChamberStructure;
import com.ninni.species.server.world.gen.structure.WraptorCoopStructure;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

import java.util.Map;

public class SpeciesStructures {

    public static void bootstrap(BootstrapContext<Structure> context) {
        HolderGetter<Biome> lookup = context.lookup(Registries.BIOME);
        context.register(SpeciesStructureKeys.WRAPTOR_COOP,
            new WraptorCoopStructure(structure(lookup.getOrThrow(SpeciesTags.Biomes.WRAPTOR_COOP_HAS_STRUCTURE), TerrainAdjustment.BEARD_BOX))
        );
        context.register(SpeciesStructureKeys.PALEONTOLOGY_DIG_SITE,
            new PaleontologyDigSiteStructure(structure(lookup.getOrThrow(BiomeTags.HAS_MINESHAFT_MESA), TerrainAdjustment.BEARD_BOX))
        );
        context.register(SpeciesStructureKeys.LIBRA,
            new LibraStructure(structure(lookup.getOrThrow(SpeciesTags.Biomes.LIBRA_HAS_STRUCTURE), TerrainAdjustment.BEARD_BOX))
        );
        context.register(SpeciesStructureKeys.SPECTRALIBUR_CHAMBER,
            new SpectraliburChamberStructure(structure(lookup.getOrThrow(SpeciesTags.Biomes.SPECTRALIBUR_CHAMBER_HAS_STRUCTURE), TerrainAdjustment.BEARD_BOX))
        );
    }

    protected static Structure.StructureSettings structure(HolderSet<Biome> holderSet, Map<MobCategory, StructureSpawnOverride> map, GenerationStep.Decoration decoration, TerrainAdjustment terrainAdjustment) {
        return new Structure.StructureSettings(holderSet, map, decoration, terrainAdjustment);
    }

    protected static Structure.StructureSettings structure(HolderSet<Biome> holderSet, TerrainAdjustment terrainAdjustment) {
        return structure(holderSet, Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, terrainAdjustment);
    }

}
