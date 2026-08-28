package com.ninni.species.event;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.EntityEvent;

public class EquipmentChangeEvent extends EntityEvent {

	private final EquipmentSlot slot;
	private final ItemStack stack;

	public EquipmentChangeEvent(LivingEntity entity, EquipmentSlot slot, ItemStack stack) {
		super(entity);
		this.slot = slot;
		this.stack = stack;
	}

	public EquipmentSlot getSlot() {
		return slot;
	}

	public ItemStack getStack() {
		return stack;
	}

	@Override
	public LivingEntity getEntity() {
		return (LivingEntity) super.getEntity();
	}

}
