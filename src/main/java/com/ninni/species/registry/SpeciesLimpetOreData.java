package com.ninni.species.registry;

import com.ninni.species.server.LimpetOres;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class SpeciesLimpetOreData {

	public static final ResourceKey<LimpetOres> SHELL = create("shell");

	public static ResourceKey<LimpetOres> create(String name) {
		return ResourceKey.create(SpeciesRegistries.LIMPET_ORES, ResourceLocation.withDefaultNamespace(name));
	}

}
