package com.ninni.species.access;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

public interface DisguisingEntity {

	EntityType<?> getDisguisedEntityType();
	void setDisguisedEntityType(EntityType<?> type);

	LivingEntity getDisguisedEntity();
	void setDisguisedEntity(LivingEntity entity);

}
