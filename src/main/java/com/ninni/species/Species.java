package com.ninni.species;

import com.mojang.logging.LogUtils;
import com.ninni.species.registry.*;
import com.ninni.species.server.entity.mob.update_2.Cruncher;
import com.ninni.species.server.entity.mob.update_3.Quake;
import com.ninni.species.server.world.poi.SpeciesPointOfInterestTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(Species.MOD_ID)
@EventBusSubscriber(modid = Species.MOD_ID)
public class Species {

	public static final String MOD_ID = "species";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final List<Runnable> CALLBACKS = new ArrayList<>();

	public static ResourceLocation of(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}

	public Species(IEventBus bus) {
		SpeciesBlocks.BLOCKS.register(bus);
		SpeciesBlockEntities.BLOCK_ENTITY_TYPES.register(bus);
		SpeciesBiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register(bus);
		SpeciesCreativeModeTabs.CREATIVE_MODE_TABS.register(bus);
		SpeciesStatusEffects.MOB_EFFECTS.register(bus);
		SpeciesEntityDataSerializers.ENTITY_DATA_SERIALIZERS.register(bus);
		SpeciesEntities.ENTITY_TYPES.register(bus);
		SpeciesFeatures.FEATURES.register(bus);
		SpeciesSoundEvents.SOUND_EVENTS.register(bus);
		SpeciesItems.ITEMS.register(bus);
		SpeciesPotions.POTIONS.register(bus);
		SpeciesStructureTypes.STRUCTURES.register(bus);
		SpeciesStructurePieceTypes.STRUCTURE_PIECE_TYPES.register(bus);
		SpeciesMemoryModuleTypes.MEMORY_MODULE_TYPES.register(bus);
		SpeciesSensorTypes.SENSOR_TYPES.register(bus);
		SpeciesParticles.PARTICLE_TYPES.register(bus);
		SpeciesPointOfInterestTypes.POI_TYPES.register(bus);
		SpeciesTreeDecorators.TREE_DECORATOR_TYPE.register(bus);
		SpeciesVillagerTypes.VILLAGER_TYPES.register(bus);
		SpeciesRecipeSerializers.RECIPE_SERIALIZERS.register(bus);
		SpeciesDataComponents.DATA_COMPONENTS.register(bus);
		SpeciesEnchantmentEffectComponents.ENCHANTMENT_EFFECT_COMPONENTS.register(bus);
		SpeciesMenus.MENUS.register(bus);
		SpeciesCriterion.TRIGGER_TYPES.register(bus);
	}

	@SubscribeEvent
	public static void onShieldBlock(LivingShieldBlockEvent event) {
		if (event.getEntity() instanceof Player player && event.getDamageSource().getEntity() instanceof Cruncher) {
			player.disableShield();
		}
		if (event.getEntity() instanceof Player player && event.getDamageSource().getEntity() instanceof Quake && event.getBlockedDamage() > 40) {
			player.disableShield();
		}
	}

}
