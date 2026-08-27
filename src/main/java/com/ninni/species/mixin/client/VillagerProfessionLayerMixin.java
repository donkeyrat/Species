package com.ninni.species.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ninni.species.registry.SpeciesVillagerTypes;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.VillagerHeadModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(VillagerProfessionLayer.class)
public abstract class VillagerProfessionLayerMixin<T extends LivingEntity & VillagerDataHolder, M extends EntityModel<T> & VillagerHeadModel> extends RenderLayer<T, M> {

    @Shadow protected abstract ResourceLocation getResourceLocation(String folder, ResourceLocation location);
    @Shadow @Final private static Int2ObjectMap<ResourceLocation> LEVEL_LOCATIONS;

    public VillagerProfessionLayerMixin(RenderLayerParent<T, M> p_117346_) {
        super(p_117346_);
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    private void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T villager, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        VillagerData data = villager.getVillagerData();
        VillagerType type = data.getType();
        VillagerProfession profession = data.getProfession();
        if (villager.isInvisible()) return;
        if (type != SpeciesVillagerTypes.CURED_BEWEREAGER.get()) return;

        ci.cancel();
        M model = this.getParentModel();
        model.hatVisible(true);
        ResourceLocation typeLocation = this.getResourceLocation("type", BuiltInRegistries.VILLAGER_TYPE.getKey(type));
        renderColoredCutoutModel(model, typeLocation, poseStack, bufferSource, packedLight, villager, -1);
        model.hatVisible(true);
        if (villager.isBaby()) return;

        ResourceLocation professionLocation = this.getResourceLocation("profession", BuiltInRegistries.VILLAGER_PROFESSION.getKey(profession));
        renderColoredCutoutModel(model, professionLocation, poseStack, bufferSource, packedLight, villager, -1);
        if (profession == VillagerProfession.NITWIT) return;

        ResourceLocation professionLevelLocation = this.getResourceLocation("profession_level", LEVEL_LOCATIONS.get(Mth.clamp(data.getLevel(), 1, LEVEL_LOCATIONS.size())));
        renderColoredCutoutModel(model, professionLevelLocation, poseStack, bufferSource, packedLight, villager, -1);
    }
}
