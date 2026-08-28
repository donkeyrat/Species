package com.ninni.species.mixin;

import com.ninni.species.event.EquipmentChangeEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStand.class)
public class ArmorStandMixin {

	@Inject(method = "setItemSlot", at = @At("HEAD"))
	private void onSetItemSlot(EquipmentSlot slot, ItemStack stack, CallbackInfo ci) {
		NeoForge.EVENT_BUS.post(new EquipmentChangeEvent((LivingEntity)(Object)this, slot, stack));
	}

}
