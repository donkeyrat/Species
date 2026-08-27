package com.ninni.species.client.renderer.entity;

import com.ninni.species.Species;
import com.ninni.species.client.model.mob.update_1.LimpetModel;
import com.ninni.species.client.renderer.entity.feature.LimpetBreakingLayer;
import com.ninni.species.registry.SpeciesEntityModelLayers;
import com.ninni.species.server.entity.mob.update_1.Limpet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LimpetRenderer extends MobRenderer<Limpet, LimpetModel<Limpet>> {

    public static final ResourceLocation TEXTURE_PATH = Species.of("textures/entity/limpet/");

    public LimpetRenderer(EntityRendererProvider.Context context) {
        super(context, new LimpetModel<>(context.bakeLayer(SpeciesEntityModelLayers.LIMPET)), 0.5f);
        // TODO limpet ore layer? issue is sometimes base texture changes based on ore (eg. uranium)
        this.addLayer(new LimpetBreakingLayer(this, new LimpetModel<>(context.bakeLayer(SpeciesEntityModelLayers.LIMPET))));
    }

    @Override
    protected boolean isShaking(Limpet entity) {
        if (!entity.level().getEntitiesOfClass(Player.class, entity.getBoundingBox().inflate(4D), entity::isValidEntityHoldingPickaxe).isEmpty()) return true;
        return super.isShaking(entity);
    }

    @Override
    public ResourceLocation getTextureLocation(Limpet entity) {
        boolean isGary = entity.getName().getString().equalsIgnoreCase("gary");
        ResourceLocation ore = entity.getOreData().value().getPath(entity.getOreData());

        if (entity.hasShell() && ore != null) {
            return ore.withPath(path -> "textures/entity/limpet/ores/" + (isGary ? "gary/" : "") + path + ".png");
        }
        return TEXTURE_PATH.withSuffix(isGary ? "gary/default.png" : "default.png");
    }

}
