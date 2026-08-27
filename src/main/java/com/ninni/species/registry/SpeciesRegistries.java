package com.ninni.species.registry;

import com.ninni.species.Species;
import com.ninni.species.server.CruncherHunting;
import com.ninni.species.server.LimpetOres;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(modid = Species.MOD_ID)
public class SpeciesRegistries {

	public static final ResourceKey<Registry<CruncherHunting>> CRUNCHER_HUNTING = ResourceKey.createRegistryKey(Species.of("cruncher_hunting"));
	public static final ResourceKey<Registry<LimpetOres>> LIMPET_ORES = ResourceKey.createRegistryKey(Species.of("limpet_ores"));

	@SubscribeEvent
	public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
		event.dataPackRegistry(CRUNCHER_HUNTING, CruncherHunting.CODEC, CruncherHunting.CODEC);
		event.dataPackRegistry(LIMPET_ORES, LimpetOres.CODEC, LimpetOres.CODEC);
	}

}
