package com.ninni.species.mixin;

import com.ninni.species.server.entity.mob.update_3.Bewereager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeleton.class)
public abstract class AbstractSkeletonEntityMixin extends Monster implements RangedAttackMob {

    protected AbstractSkeletonEntityMixin(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void onInitGoals(CallbackInfo ci) {
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Bewereager.class, 6, 1, 1.2F));
    }

}
