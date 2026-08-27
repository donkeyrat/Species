package com.ninni.species.registry;

import com.ninni.species.Species;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface SpeciesEntityModelLayers {

    ModelLayerLocation WRAPTOR = registerMain("wraptor");
    ModelLayerLocation DEEPFISH = registerMain("deepfish");
    ModelLayerLocation STACKATICK = registerMain("stackatick");
    ModelLayerLocation BIRT = registerMain("birt");
    ModelLayerLocation LIMPET = registerMain("limpet");
    ModelLayerLocation TREEPER = registerMain("treeper");
    ModelLayerLocation TROOPER = registerMain("trooper");
    ModelLayerLocation GOOBER = registerMain("goober");
    ModelLayerLocation GOOBER_GOO = registerMain("goober_goo");
    ModelLayerLocation CRUNCHER = registerMain("cruncher");
    ModelLayerLocation MAMMUTILATION = registerMain("mammutilation");
    ModelLayerLocation SPRINGLING = registerMain("springling");
    ModelLayerLocation GHOUL = registerMain("ghoul");
    ModelLayerLocation GHOUL_HEAD = registerMain("ghoul_head");
    ModelLayerLocation QUAKE = registerMain("quake");
    ModelLayerLocation QUAKE_HEAD = registerMain("quake_head");
    ModelLayerLocation DEFLECTOR_DUMMY = registerMain("deflector_dummy");
    ModelLayerLocation BEWEREAGER = registerMain("bewereager");
    ModelLayerLocation SPECTRE = registerMain("spectre");
    ModelLayerLocation SABLE_SPECTRE = registerMain("sable_spectre");
    ModelLayerLocation JOUSTING_SPECTRE = registerMain("jousting_spectre");
    ModelLayerLocation WICKED = registerMain("wicked");
    ModelLayerLocation WICKED_FIREBALL = registerMain("wicked_fireball");
    ModelLayerLocation WICKED_CANDLE = registerMain("wicked_candle");
    ModelLayerLocation BEWEREAGER_HEAD = registerMain("bewereager_head");
    ModelLayerLocation LEAF_HANGER = registerMain("leaf_hanger");
    ModelLayerLocation CLIFF_HANGER = registerMain("cliff_hanger");
    ModelLayerLocation COIL_KNOT = registerMain("coil_knot");
    ModelLayerLocation COIL = registerMain("coil");

    private static ModelLayerLocation registerMain(String name) {
        return register(name, "main");
    }

    private static ModelLayerLocation register(String name, String layer) {
        return new ModelLayerLocation(Species.of(name), layer);
    }

}
