package com.ninni.species.mixin;

import com.ninni.species.access.BewereagerableEntity;
import com.ninni.species.server.entity.ai.goal.TransformDuringFullMoonGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Wolf.class)
public abstract class WolfMixin extends TamableAnimal implements NeutralMob, BewereagerableEntity {

    @Shadow @Final private static EntityDataAccessor<Integer> DATA_COLLAR_COLOR;

    @Unique private static final EntityDataAccessor<Boolean> DATA_IS_BEWEREAGER = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.BOOLEAN);
    @Unique private static final EntityDataAccessor<Boolean> DATA_IS_CURED = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.BOOLEAN);

    protected WolfMixin(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    @Inject(method = "finalizeSpawn", at = @At("HEAD"))
    public void finalizeSpawn(ServerLevelAccessor l, DifficultyInstance d, MobSpawnType s, SpawnGroupData g, CallbackInfoReturnable<SpawnGroupData> cir) {
        if (this.getRandom().nextInt(10) == 0) this.setBewereager(true); // TODO replace with gamerule?
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        this.goalSelector.addGoal(1, new TransformDuringFullMoonGoal((Wolf)(Object)this));
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(DATA_IS_BEWEREAGER, false);
        builder.define(DATA_IS_CURED, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        tag.putBoolean("is_bewereager", this.isBewereager());
        tag.putBoolean("is_cured", this.isCured());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        this.setBewereager(compoundTag.getBoolean("is_bewereager"));
        this.setCured(compoundTag.getBoolean("is_cured"));
    }

    @Override
    public boolean isBewereager() {
        return this.entityData.get(DATA_IS_BEWEREAGER);
    }

    @Override
    public void setBewereager(boolean isBewereager) {
        this.entityData.set(DATA_IS_BEWEREAGER, isBewereager);
    }

    @Override
    public boolean isCured() {
        return this.entityData.get(DATA_IS_CURED);
    }

    @Override
    public void setCured(boolean setCured) {
        this.entityData.set(DATA_IS_CURED, setCured);
    }

    @Override
    public void setCollarColor(DyeColor color) {
        this.entityData.set(DATA_COLLAR_COLOR, color.getId());
    }

}
