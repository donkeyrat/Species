package com.ninni.species;

import com.ninni.species.client.events.ClientEvents;
import com.ninni.species.registry.SpeciesItems;
import com.ninni.species.server.item.*;
import net.minecraft.client.renderer.item.ItemProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Species.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Species.MOD_ID, value = Dist.CLIENT)
public class SpeciesClient {

	public SpeciesClient(IEventBus bus, ModContainer container) {
		container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		bus.addListener(ClientEvents::onRegisterSpectatorShaders);
		bus.addListener(ClientEvents::registerKeys);
		bus.addListener(ClientEvents::registerEntityLayers);
		bus.addListener(ClientEvents::registerEntityRenderers);
		bus.addListener(ClientEvents::registerBlockEntityRenderers);
		bus.addListener(ClientEvents::registerParticleTypes);
		bus.addListener(ClientEvents::registerItemColors);
		bus.addListener(ClientEvents::registerCreativeModeTab);
		bus.addListener(ClientEvents::registerSkullModels);
		SpeciesDevelopers.setDeveloperUuids();
	}

	@SubscribeEvent
	public static void register(FMLClientSetupEvent event) {
		ItemProperties.register(SpeciesItems.CRANKBOW.get(), Species.of("pull"), CrankbowItem::getPullProperty);
		ItemProperties.register(SpeciesItems.RICOSHIELD.get(), Species.of("blocking"), RicoshieldItem::getBlockingProperty);
		ItemProperties.register(SpeciesItems.SPECTRALIBUR.get(), Species.of("souls"), SpectraliburItem::getSoulsProperty);
		ItemProperties.register(SpeciesItems.COIL.get(), Species.of("placing"), CoilItem::getPlacingProperty);
		ItemProperties.register(SpeciesItems.HARPOON.get(), Species.of("using"), HarpoonItem::getUsingProperty);
	}

}
