package com.ninni.species.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ninni.species.access.DisguisingEntity;
import com.ninni.species.registry.SpeciesItems;
import com.ninni.species.server.item.CrankbowItem;
import com.ninni.species.server.item.SpectraliburItem;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

    @Shadow protected abstract void renderPlayerArm(PoseStack p_109347_, MultiBufferSource p_109348_, int p_109349_, float p_109350_, float p_109351_, HumanoidArm p_109352_);
    @Shadow protected abstract void applyItemArmTransform(PoseStack p_109383_, HumanoidArm p_109384_, float p_109385_);
    @Shadow protected abstract void applyItemArmAttackTransform(PoseStack p_109336_, HumanoidArm p_109337_, float p_109338_);
    @Shadow public abstract void renderItem(LivingEntity p_270072_, ItemStack p_270793_, ItemDisplayContext p_270837_, boolean p_270203_, PoseStack p_270974_, MultiBufferSource p_270686_, int p_270103_);

    @Inject(at = @At("TAIL"), method = "renderArmWithItem")
    private void renderDisguisedArms(AbstractClientPlayer player, float v, float v1, InteractionHand hand, float v2, ItemStack stack, float v3, PoseStack poseStack, MultiBufferSource bufferSource, int i, CallbackInfo ci) {
        LivingEntity disguise = ((DisguisingEntity)player).getDisguisedEntity();
        boolean isMainHand = hand == InteractionHand.MAIN_HAND;
        HumanoidArm arm = isMainHand ? player.getMainArm().getOpposite() : player.getMainArm();
        if (player.isScoping()) return;
        if (!player.getItemBySlot(EquipmentSlot.HEAD).is(SpeciesItems.WICKED_MASK.get())) return;
        if (disguise == null) return;
        if (!(disguise instanceof Zombie)) return; // TODO is this necessary?

        poseStack.pushPose();
        if (player.getOffhandItem().isEmpty() && !player.getMainHandItem().is(Items.FILLED_MAP)) {
            if (!(player.getMainHandItem().getItem() instanceof CrossbowItem && CrossbowItem.isCharged(player.getMainHandItem()))) {
                if (isMainHand && !player.isInvisible()) {
                    this.renderPlayerArm(poseStack, bufferSource, i, v3, v2, arm);
                }
            }
        }
    }


    @Inject(at = @At("HEAD"), method = "evaluateWhichHandsToRender", cancellable = true)
    private static void evaluateWhichHandsToRender(LocalPlayer player, CallbackInfoReturnable<ItemInHandRenderer.HandRenderSelection> cir) {
        ItemStack stack = player.getUseItem();
        if (stack.getItem() instanceof CrankbowItem && player.isUsingItem() && stack.has(DataComponents.CHARGED_PROJECTILES)) {
            cir.setReturnValue(player.getUsedItemHand() == InteractionHand.MAIN_HAND ? ItemInHandRenderer.HandRenderSelection.RENDER_MAIN_HAND_ONLY : ItemInHandRenderer.HandRenderSelection.RENDER_OFF_HAND_ONLY);
        }
    }

    @Inject(at = @At("HEAD"), method = "selectionUsingItemWhileHoldingBowLike", cancellable = true)
    private static void selectionUsingItemWhileHoldingBowLike(LocalPlayer localPlayer, CallbackInfoReturnable<ItemInHandRenderer.HandRenderSelection> cir) {
        ItemStack stack = localPlayer.getUseItem();
        InteractionHand hand = localPlayer.getUsedItemHand();
        if (stack.getItem() instanceof CrankbowItem) {
            cir.setReturnValue(ItemInHandRenderer.HandRenderSelection.onlyForHand(hand));
        }
    }

    @Inject(at = @At("HEAD"), method = "renderArmWithItem", cancellable = true)
    private void renderCrankBowItem(AbstractClientPlayer player, float v, float v1, InteractionHand hand, float v2, ItemStack stack, float v3, PoseStack poseStack, MultiBufferSource bufferSource, int i, CallbackInfo ci) {
        boolean isMainHand = hand == InteractionHand.MAIN_HAND;
        HumanoidArm arm = isMainHand ? player.getMainArm().getOpposite() : player.getMainArm();
        if (!(stack.getItem() instanceof CrankbowItem) && !(stack.getItem() instanceof SpectraliburItem)) return;
        if (player.getUseItem() != stack) return;

        ci.cancel();
        poseStack.pushPose();
        boolean isRightHand = arm == HumanoidArm.RIGHT;
        int i2 = isRightHand ? 1 : -1;
        float f12 = -0.4F * Mth.sin(Mth.sqrt(v2) * 3.1415927F);
        float f7 = 0.2F * Mth.sin(Mth.sqrt(v2) * 6.2831855F);
        float f11 = -0.2F * Mth.sin(v2 * 3.1415927F);
        poseStack.translate((float)i2 * f12, f7, f11);
        this.applyItemArmTransform(poseStack, arm, v3);
        this.applyItemArmAttackTransform(poseStack, arm, v2);
        if (v2 < 0.001F) {
            if (stack.getItem() instanceof CrankbowItem) {
                poseStack.translate((float) i2 * -0.641864F, 0.0F, 0.0F);
                poseStack.mulPose(Axis.YP.rotationDegrees((float) i2 * 10.0F));
            }
            if (stack.getItem() instanceof SpectraliburItem && isMainHand) {
                poseStack.translate((float) i2 * -1F, 0.15F, -0.05F);
                poseStack.mulPose(Axis.YP.rotationDegrees((float) i2 * 100.0F));
            }
        }
        this.renderItem(player, stack, isRightHand ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, !isRightHand, poseStack, bufferSource, i);
        poseStack.popPose();
    }
}
