package com.ninni.species.server.item;

import com.ninni.species.Species;
import com.ninni.species.registry.*;
import com.ninni.species.server.entity.mob.update_2.Cruncher;
import com.ninni.species.server.entity.mob.update_3.Quake;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

import java.util.List;

import static net.minecraft.world.entity.EntitySelector.NO_CREATIVE_OR_SPECTATOR;

@EventBusSubscriber(modid = Species.MOD_ID)
public class RicoshieldItem extends ShieldItem {

    public RicoshieldItem(Properties properties) {
        super(properties);
    }

    @SubscribeEvent
    public static void onShieldBlock(LivingShieldBlockEvent event) {
        if (!event.getBlocked()) return;

        LivingEntity entity = event.getEntity();
        ItemStack stack = entity.getUseItem();

        if (!stack.is(SpeciesItems.RICOSHIELD.get())) return;

        float damage = stack.getOrDefault(SpeciesDataComponents.STORED_DAMAGE, 0F);
        float newDamage = Math.min(damage + event.getBlockedDamage(), 40);
        stack.set(SpeciesDataComponents.STORED_DAMAGE, newDamage);
        entity.level().playSound(null, entity.blockPosition(), SpeciesSoundEvents.RICOSHIELD_ABSORB.get(), SoundSource.PLAYERS, 1F, damage * 0.05F);
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity player, int count) {
        float damage = stack.getOrDefault(SpeciesDataComponents.STORED_DAMAGE, 0F);
        if (damage > 0) {
            this.damageTargets(player.level(), player, damage);
            stack.set(SpeciesDataComponents.STORED_DAMAGE, 0F);
        }
        super.onStopUsing(stack, player, count);
    }

    private void damageTargets(Level level, LivingEntity player, float amount) {
        Vec3 pos = player.position();
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(SpeciesParticles.SMALL_KINETIC_ENERGY.get(), pos.x, pos.y + 0.01F, pos.z, 1, 0, 0, 0, 0.5F);
        }
        level.playSound(player, player.blockPosition(), SpeciesSoundEvents.RICOSHIELD_ATTACK.get(), SoundSource.PLAYERS, 1, 1);

        level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(4D), NO_CREATIVE_OR_SPECTATOR).stream()
            .filter(target -> target != player && !(target instanceof TamableAnimal tamableAnimal && tamableAnimal.getOwner() == player))
            .forEach(target -> this.damageTarget(player, target, pos, amount));
    }

    private void damageTarget(LivingEntity player, LivingEntity target, Vec3 pos, float amount) {
        Vec3 enemyPos = target.position().subtract(pos);
        Vec3 normalizedDirection = enemyPos.normalize();

        double knockbackXZ = 0.75F * (1 - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
        double knockbackY = 0.15F * (1 - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));

        target.push(normalizedDirection.x() * knockbackXZ, normalizedDirection.y() * knockbackY, normalizedDirection.z() * knockbackXZ);

        double distanceFromEnemy = target.position().distanceTo(pos);

        float scalingFactor;
        if (distanceFromEnemy <= 1) scalingFactor = 1F;
        else if (distanceFromEnemy <= 2) scalingFactor = 0.8F;
        else if (distanceFromEnemy <= 3) scalingFactor = 0.65F;
        else scalingFactor = 0.5F;

        target.hurt(kinetic(target, player), amount * scalingFactor);
        player.doHurtTarget(target);
    }

    public static DamageSource kinetic(LivingEntity target, LivingEntity attacker) {
        return attacker.damageSources().source(SpeciesDamageTypes.KINETIC, target, attacker);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack material) {
        return material.is(SpeciesItems.BROKEN_LINKS.get()); // TODO replace with tag
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag flag) {
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.species.ricoshield.desc.blocking").withStyle(ChatFormatting.GRAY));
        list.add(CommonComponents.SPACE.copy().append(Component.translatable("item.species.ricoshield.desc.damage").withStyle(style -> style.withColor(0xE21447))));

        super.appendHoverText(stack, context, list, flag);
    }

}
