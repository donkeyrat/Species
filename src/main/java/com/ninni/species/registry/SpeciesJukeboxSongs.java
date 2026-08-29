package com.ninni.species.registry;

import com.ninni.species.Species;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.JukeboxSong;

public class SpeciesJukeboxSongs {

    public static ResourceKey<JukeboxSong> DIAL = create("dial");
    public static ResourceKey<JukeboxSong> LAPIDARIAN = create("lapidarian");
    public static ResourceKey<JukeboxSong> SPAWNER = create("spawner");

    public static ResourceKey<JukeboxSong> create(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, Species.of(name));
    }

}