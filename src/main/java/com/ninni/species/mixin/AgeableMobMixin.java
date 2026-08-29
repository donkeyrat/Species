package com.ninni.species.mixin;

import com.ninni.species.registry.SpeciesItems;
import com.ninni.species.registry.SpeciesParticles;
import com.ninni.species.registry.SpeciesSoundEvents;
import com.ninni.species.registry.SpeciesTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AgeableMob.class)
public abstract class AgeableMobMixin extends PathfinderMob {

    @Shadow public abstract boolean isBaby();

    @Unique private boolean drankYouthPotion;

    protected AgeableMobMixin(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    private void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        compoundTag.putBoolean("drank_youth_potion", this.drankYouthPotion);
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    private void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        this.drankYouthPotion = compoundTag.getBoolean("drank_youth_potion");
    }

    @Inject(at = @At("HEAD"), method = "aiStep", cancellable = true)
    private void aiStep(CallbackInfo ci) {
        if (!this.drankYouthPotion) return;

        ci.cancel();
        super.aiStep();
    }

    // TODO replace with item action probably
    @Override
    public InteractionResult interactAt(Player player, Vec3 vec3, InteractionHand interactionHand) {
        if (this.getType().is(SpeciesTags.EntityTypes.ALWAYS_ADULT)) return super.interactAt(player, vec3, interactionHand);
        if (player.getItemInHand(interactionHand).is(SpeciesItems.YOUTH_POTION.get()) && this.isBaby() && !this.drankYouthPotion) {
            this.drankYouthPotion = true;
            this.playSound(SpeciesSoundEvents.YOUTH_POTION_STUMPED.get(), 1, 1);
            if (this.level() instanceof ServerLevel serverLevel) {
                double d = this.getRandom().nextGaussian() * 0.02;
                double e = this.getRandom().nextGaussian() * 0.02;
                double f = this.getRandom().nextGaussian() * 0.02;
                serverLevel.sendParticles(SpeciesParticles.YOUTH_POTION.get(), this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 0, d, e, f, 1);
            }
            return InteractionResult.SUCCESS;
        }
        if (this.drankYouthPotion && player.getItemInHand(interactionHand).is(Items.MILK_BUCKET)) {
            this.drankYouthPotion = false;
            this.playSound(SoundEvents.GENERIC_DRINK, 1, 1);
            return InteractionResult.SUCCESS;
        }
        return super.interactAt(player, vec3, interactionHand);
    }

}