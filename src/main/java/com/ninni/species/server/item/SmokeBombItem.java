package com.ninni.species.server.item;

import com.ninni.species.registry.SpeciesDataComponents;
import com.ninni.species.registry.SpeciesParticles;
import com.ninni.species.registry.SpeciesSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SmokeBombItem extends Item {

    public SmokeBombItem(Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) return;
        int chargeTime = this.getUseDuration(stack, player) - timeLeft;
        if (chargeTime < 10) {
            player.getCooldowns().addCooldown(this, 20);
            return;
        }

        Vec3 pos = player.position();
        level.playSound(null, pos.x, pos.y, pos.z, SpeciesSoundEvents.SMOKE_BOMB_USE.get(), SoundSource.PLAYERS, 1, 1);
        player.awardStat(Stats.ITEM_USED.get(this));

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(SpeciesParticles.POOF.get(), pos.x, pos.y + 0.01, pos.z, 1, 0, 0, 0, 0.5F);
            serverLevel.sendParticles(ParticleTypes.POOF, pos.x, pos.y + 1, pos.z, 100, 0, 0, 0, 0.15F);
        }

        PotionContents contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        contents.forEachEffect(effect -> {
            if (effect.getEffect().value().isInstantenous()) {
                effect.getEffect().value().applyInstantenousEffect(player, player, player, effect.getAmplifier(), 1);
            } else {
                entity.addEffect(effect);
            }
        });

        player.getCooldowns().addCooldown(stack.getItem(), stack.getOrDefault(SpeciesDataComponents.COOLDOWN, 0));
        stack.consume(1, player);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        Vec3 pos = player.position();
        level.playSound(null, pos.x, pos.y, pos.z, SpeciesSoundEvents.SMOKE_BOMB_CHARGE.get(), SoundSource.PLAYERS, 1, 1);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag flag) {
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        if (contents != null) contents.addPotionTooltip(list::add, 1, context.tickRate());
    }

}
