package com.ninni.species.mixin;

import com.ninni.species.access.DisguisingEntity;
import com.ninni.species.access.TankingAndSnatchingEntity;
import com.ninni.species.event.EquipmentChangeEvent;
import com.ninni.species.registry.*;
import com.ninni.species.server.entity.util.CustomDeathParticles;
import com.ninni.species.server.packet.SnatchedPacket;
import com.ninni.species.server.packet.TankedPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements TankingAndSnatchingEntity, DisguisingEntity {

    @Shadow public abstract boolean hasEffect(Holder<MobEffect> effect);
    @Shadow @Nullable public abstract MobEffectInstance getEffect(Holder<MobEffect> effect);

    @OnlyIn(Dist.CLIENT) @Unique private boolean snatched;
    @OnlyIn(Dist.CLIENT) @Unique private boolean tanked;

    @Unique private final ItemStack[] previousEquipment = new ItemStack[EquipmentSlot.values().length];
    @Unique private Entity disguisedEntity;

    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = self.getItemBySlot(slot);
            ItemStack previousStack = previousEquipment[slot.ordinal()];
            if (previousStack == null) previousStack = ItemStack.EMPTY;
            if (!ItemStack.matches(stack, previousStack)) {
                NeoForge.EVENT_BUS.post(new EquipmentChangeEvent(self, slot, stack));
                previousEquipment[slot.ordinal()] = stack.copy();
            }
        }
    }

    @Inject(method = "tickEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;updateGlowingStatus()V", ordinal = 0))
    private void onStatusEffectChange(CallbackInfo ci) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(this, new SnatchedPacket(this.getId(), this.hasEffect(SpeciesStatusEffects.SNATCHED)));
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(this, new TankedPacket(this.getId(), this.hasEffect(SpeciesStatusEffects.TANKED)));
    }

    @Inject(method = "tickEffects", at = @At("HEAD"))
    public void applySpeciesEffects(CallbackInfo ci) {
        Level level = this.level();

        MobEffectInstance instance = this.getEffect(SpeciesStatusEffects.GUT_FEELING);
        if (instance != null) {
            if (instance.getDuration() < 20 * 60 * 5) {
                if (this.getRandom().nextInt(200) == 0) this.playSound(SpeciesSoundEvents.GUT_FEELING_ROAR.get(), 0.2f, 0);
            } else {
                if (this.getRandom().nextInt(800) == 0) this.playSound(SpeciesSoundEvents.GUT_FEELING_ROAR.get(), 0.2f, 0);
            }
        }

        if (this.hasEffect(SpeciesStatusEffects.BIRTD) && level instanceof ServerLevel world) {
            if (this.tickCount % 10 == 1) {
                world.sendParticles(SpeciesParticles.BIRTD.get(), this.getX(), this.getEyeY() + 0.5F, this.getZ() - 0.5, 1,0, 0, 0, 0);
            }
        }
    }

    @Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
    public void applyBirtd(CallbackInfo ci) {
        if (this.hasEffect(SpeciesStatusEffects.BIRTD) || this.hasEffect(SpeciesStatusEffects.STUCK)) ci.cancel();
    }

    @Inject(method = "makePoofParticles", at = @At("HEAD"), cancellable = true)
    public void S$makePoofParticles(CallbackInfo ci) {
        if (this instanceof CustomDeathParticles customDeathParticles) {
            ci.cancel();
            customDeathParticles.makeDeathParticles();
        }
    }

    @Inject(method = "isPushable", at = @At("HEAD"), cancellable = true)
    public void S$isPushable(CallbackInfoReturnable<Boolean> cir) {
        if (this.hasEffect(SpeciesStatusEffects.BIRTD) || this.hasEffect(SpeciesStatusEffects.STUCK)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getVisibilityPercent", at = @At("RETURN"), cancellable = true)
    private void onGetVisibilityPercent(@Nullable Entity observer, CallbackInfoReturnable<Double> cir) {
        LivingEntity self = (LivingEntity)(Object)this;
        double visibility = cir.getReturnValue();

        if (observer == null) return;

        ItemStack headItem = self.getItemBySlot(EquipmentSlot.HEAD);
        EntityType<?> observerType = observer.getType();

        if (observerType == SpeciesEntities.GHOUL.get() && headItem.is(SpeciesItems.GHOUL_HEAD.get()) ||
                observerType == SpeciesEntities.WICKED.get() && headItem.is(SpeciesItems.WICKED_CANDLE.get()) ||
                observerType == SpeciesEntities.BEWEREAGER.get() && headItem.is(SpeciesItems.BEWEREAGER_HEAD.get()) ||
                observerType == SpeciesEntities.QUAKE.get() && headItem.is(SpeciesItems.QUAKE_HEAD.get())) {
            visibility *= 0.5D;
            cir.setReturnValue(visibility);
        }
    }

    @Override
    public boolean hasTanked() {
        return this.tanked;
    }

    @Override
    public void setTanked(boolean tanked) {
        this.tanked = tanked;
    }

    @Override
    public boolean hasSnatched() {
        return this.snatched;
    }

    @Override
    public void setSnatched(boolean snatched) {
        this.snatched = snatched;
    }

    @Override
    public Entity getDisguisedEntity() {
        return this.disguisedEntity;
    }

    @Override
    public void setDisguisedEntity(Entity disguisedEntity) {
        this.disguisedEntity = disguisedEntity;
    }

}
