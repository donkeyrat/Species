package com.ninni.species.mixin.crankbow;

import com.ninni.species.access.CrankingEntity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements CrankingEntity {

	@Unique private int shotsFired;

	@Override
	public int getShotsFired() {
		return this.shotsFired;
	}

	@Override
	public void setShotsFired(int value) {
		this.shotsFired = value;
	}

}
