package com.ninni.species.server.events;

import com.ninni.species.Species;
import com.ninni.species.registry.SpeciesParticles;
import com.ninni.species.registry.SpeciesSoundEvents;
import com.ninni.species.registry.SpeciesStatusEffects;
import com.ninni.species.registry.SpeciesTags;
import com.ninni.species.server.item.SpectraliburItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

@EventBusSubscriber(modid = Species.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onLivingTick(LivingBreatheEvent event) {
        LivingEntity livingEntity = event.getEntity();
        Level level = livingEntity.level();
        if (!level.isClientSide) {
            if (livingEntity.hasEffect(SpeciesStatusEffects.BLOODLUST)) {
                BlockPos blockpos = BlockPos.containing(livingEntity.getX(), livingEntity.getEyeY(), livingEntity.getZ());
                float f = livingEntity.getLightLevelDependentMagicValue();
                if (f > 0.5F && level.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && !livingEntity.isInWaterOrBubble() && level.canSeeSky(blockpos) && level.isDay()) {
                    level.playSound(null, livingEntity, SpeciesSoundEvents.BLOODLUST_REMOVED.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    livingEntity.removeEffect(SpeciesStatusEffects.BLOODLUST);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMobEventApplied(MobEffectEvent.Applicable event) {
        LivingEntity livingEntity = event.getEntity();
        MobEffectInstance mobEffectInstance = event.getEffectInstance();
        if (livingEntity.hasEffect(SpeciesStatusEffects.WITHER_RESISTANCE) && mobEffectInstance.getEffect() == MobEffects.WITHER) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingDamageEvent.Pre event) {
        LivingEntity attacked = event.getEntity();
        DamageSource source = event.getSource();
        float amount = event.getOriginalDamage();


        //Replenish hunger when killing a mob with the Bloodlust effect
        if (source.getEntity() instanceof Player player && player.hasEffect(SpeciesStatusEffects.BLOODLUST)) {
            if (amount > attacked.getHealth()) {
                attacked.level().playSound(null, attacked.getX(), attacked.getY(), attacked.getZ(), SpeciesSoundEvents.BLOODLUST_FEED.get(), attacked.getSoundSource(), 1, 1);
                player.getFoodData().eat((int) (attacked.getMaxHealth() / 5), ((attacked.getMaxHealth() / 5F) * 0.1F));
            }
        }

        //Making mob explode when having the Combustion effect
        if (attacked.hasEffect(SpeciesStatusEffects.COMBUSTION) && amount > attacked.getHealth()) {
            int amplifier = attacked.getEffect(SpeciesStatusEffects.COMBUSTION).getAmplifier();
            attacked.level().explode(attacked, attacked.getX(), attacked.getY(0.0625D), attacked.getZ(), amplifier, Level.ExplosionInteraction.MOB);
            attacked.level().getEntitiesOfClass(LivingEntity.class, attacked.getBoundingBox().inflate(2), (livingEntity) -> livingEntity.isAlive() && !livingEntity.is(attacked)).forEach(livingEntity -> livingEntity.hurt(attacked.level().damageSources().mobAttack(attacked), 6));
            attacked.removeEffect(SpeciesStatusEffects.COMBUSTION);
        }

        //Spectralibur
        if (source.getEntity() instanceof Player player && player.getMainHandItem().getItem() instanceof SpectraliburItem && !(attacked.getType().is(SpeciesTags.EntityTypes.SOULLESS))) {
            if (amount > attacked.getHealth()) {

                //Storing souls in Spectralibur
                var tag = player.getMainHandItem().getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                if (!(tag.contains("Souls") && tag.getInt("Souls") == 5)) {
                    CompoundTag newTag = tag.copy();
                    newTag.putInt("Souls", Math.min ((tag.getInt("Souls") + 1), 5));
                    if (player.level() instanceof ServerLevel serverLevel) {
                        serverLevel.playSound(null, attacked.getX(), attacked.getY(), attacked.getZ(), SpeciesSoundEvents.SPECTRALIBUR_COLLECT_SOUL.get(), SoundSource.PLAYERS, 1, 1);
                        serverLevel.sendParticles(SpeciesParticles.COLLECTED_SOUL.get(), attacked.getX(), attacked.getY() + 0.2F, attacked.getZ(), 1, 0,0,0, 0);
                    }
                    CustomData.set(DataComponents.CUSTOM_DATA, player.getMainHandItem(), newTag);
                }
            }
        }

    }

}
