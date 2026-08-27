package com.ninni.species.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ninni.species.access.DisguisingEntity;
import com.ninni.species.registry.SpeciesItems;
import com.ninni.species.server.item.CrankbowItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
    private static void getArmPose(AbstractClientPlayer player, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.swinging && stack.getItem() instanceof CrankbowItem && player.isUsingItem() && stack.has(DataComponents.CHARGED_PROJECTILES)) {
            cir.setReturnValue(HumanoidModel.ArmPose.CROSSBOW_HOLD);
        }
    }

    @Inject(method = "renderLeftHand", at = @At("HEAD"), cancellable = true)
    private void renderLeftHand(PoseStack poseStack, MultiBufferSource buffer, int light, AbstractClientPlayer player, CallbackInfo ci) {
        LivingEntity disguise = ((DisguisingEntity)player).getDisguisedEntity();
        if (disguise == null) return;

        ItemStack headItem = player.getItemBySlot(EquipmentSlot.HEAD);
        if (!headItem.is(SpeciesItems.WICKED_MASK.get())) return;

        ci.cancel();
        EntityRenderer<? super LivingEntity> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(disguise);
        if (renderer instanceof HumanoidMobRenderer humanoidRenderer) {
            HumanoidModel model = (HumanoidModel)humanoidRenderer.getModel();
            this.renderHand(poseStack, buffer, light, player, model.leftArm, null, ci);
        }
    }

    @Inject(method = "renderRightHand", at = @At("HEAD"), cancellable = true)
    private void renderRightHand(PoseStack poseStack, MultiBufferSource buffer, int light, AbstractClientPlayer player, CallbackInfo ci) {
        LivingEntity disguise = ((DisguisingEntity)player).getDisguisedEntity();
        if (disguise == null) return;

        ItemStack headItem = player.getItemBySlot(EquipmentSlot.HEAD);
        if (!headItem.is(SpeciesItems.WICKED_MASK.get())) return;

        ci.cancel();
        EntityRenderer<? super LivingEntity> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(disguise);
        if (renderer instanceof HumanoidMobRenderer humanoidRenderer) {
            HumanoidModel model = (HumanoidModel)humanoidRenderer.getModel();
            this.renderHand(poseStack, buffer, light, player, model.rightArm, null, ci);
        }
    }

    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    private void renderHand(PoseStack poseStack, MultiBufferSource buffer, int light, AbstractClientPlayer player, ModelPart rendererArm, ModelPart rendererArmwear, CallbackInfo ci) {
        LivingEntity disguise = ((DisguisingEntity)player).getDisguisedEntity();
        if (disguise == null) return;

        ItemStack headItem = player.getItemBySlot(EquipmentSlot.HEAD);
        if (!headItem.is(SpeciesItems.WICKED_MASK.get())) return;

        ci.cancel();
        EntityRenderer<? super LivingEntity> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(disguise);
        if (renderer instanceof HumanoidMobRenderer humanoidRenderer) {
            EntityModel model = humanoidRenderer.getModel();
            model.attackTime = 0.0F;
            model.setupAnim(disguise, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            rendererArm.xRot = 0.0F;
            rendererArm.render(poseStack, buffer.getBuffer(RenderType.entitySolid(renderer.getTextureLocation(disguise))), light, OverlayTexture.NO_OVERLAY);
            if (rendererArmwear != null) {
                rendererArmwear.xRot = 0.0F;
                rendererArmwear.render(poseStack, buffer.getBuffer(RenderType.entitySolid(renderer.getTextureLocation(disguise))), light, OverlayTexture.NO_OVERLAY);
            }
        }
    }
}
