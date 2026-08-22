package com.ninni.species.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ninni.species.Species;
import com.ninni.species.registry.SpeciesDataComponents;
import com.ninni.species.registry.SpeciesItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(value = Dist.CLIENT, modid = Species.MOD_ID)
public class RicoshieldItemRenderer extends BlockEntityWithoutLevelRenderer {

	public static RicoshieldItemRenderer INSTANCE;

	public static class RicoshieldItemExtensions implements IClientItemExtensions {

		@Override
		public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
			return INSTANCE;
		}

	}

	public static final ResourceLocation RICOSHIELD_TEXTURE = Species.of("textures/entity/ricoshield/default.png");
	public static final ResourceLocation RICOSHIELD_TEXTURE_CHARGING = Species.of("textures/entity/ricoshield/charging.png");
	public static final ResourceLocation RICOSHIELD_TEXTURE_CHARGED = Species.of("textures/entity/ricoshield/charged.png");

	public RicoshieldItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet set) {
		super(dispatcher, set);
	}

	@SubscribeEvent
	public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
		event.registerItem(new RicoshieldItemExtensions(), SpeciesItems.RICOSHIELD);
	}

	@SubscribeEvent
	public static void onRegisterReloadListener(RegisterClientReloadListenersEvent event) {
		INSTANCE = new RicoshieldItemRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
		event.registerReloadListener(INSTANCE);
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource source, int light, int overlay) {
		poseStack.pushPose();
		poseStack.scale(1, -1, -1);

		VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(source, RenderType.armorCutoutNoCull(RICOSHIELD_TEXTURE), stack.hasFoil());
		this.shieldModel.handle().render(poseStack, vertexconsumer, light, overlay, FastColor.ARGB32.colorFromFloat(1, 1, 1, 1));
		this.shieldModel.plate().render(poseStack, vertexconsumer, light, overlay, FastColor.ARGB32.colorFromFloat(1, 1, 1, 1));

		float damage = stack.getOrDefault(SpeciesDataComponents.STORED_DAMAGE, 0F);
		if (damage >= 25) {
			VertexConsumer vertexconsumerCharging = ItemRenderer.getArmorFoilBuffer(source, RenderType.armorCutoutNoCull(RICOSHIELD_TEXTURE_CHARGING), false);
			this.shieldModel.plate().render(poseStack, vertexconsumerCharging, light, overlay, FastColor.ARGB32.colorFromFloat(1, 1, 1, 1));
		}
		if (damage >= 35) {
			VertexConsumer vertexconsumerCharged = ItemRenderer.getArmorFoilBuffer(source, RenderType.armorCutoutNoCull(RICOSHIELD_TEXTURE_CHARGED), false);
			this.shieldModel.plate().render(poseStack, vertexconsumerCharged, light, overlay, FastColor.ARGB32.colorFromFloat(1, 1, 1, 1));
		}
//		if (damage > 0) {
//			float opacityCharging = damage >= 25 ? 1 : 0;
//			float opacityCharged = damage >= 35 ? 1 : 0;
//
//			VertexConsumer vertexconsumerCharging = ItemRenderer.getArmorFoilBuffer(source, RenderType.armorCutoutNoCull(RICOSHIELD_TEXTURE_CHARGING), false);
//			this.shieldModel.plate().render(poseStack, vertexconsumerCharging, light, overlay, FastColor.ARGB32.colorFromFloat(opacityCharging, 1, 1, 1));
//
//			VertexConsumer vertexconsumerCharged = ItemRenderer.getArmorFoilBuffer(source, RenderType.armorCutoutNoCull(RICOSHIELD_TEXTURE_CHARGED), false);
//			this.shieldModel.plate().render(poseStack, vertexconsumerCharged, light, overlay, FastColor.ARGB32.colorFromFloat(opacityCharged, 1, 1, 1));
//		}
		poseStack.popPose();

	}

}
