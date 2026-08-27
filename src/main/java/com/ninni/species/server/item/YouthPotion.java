package com.ninni.species.server.item;

import com.ninni.species.registry.SpeciesParticles;
import com.ninni.species.registry.SpeciesSoundEvents;
import com.ninni.species.registry.SpeciesTags;
import com.ninni.species.registry.SpeciesCriterion;
import com.ninni.species.server.entity.mob.update_2.Springling;
import com.ninni.species.server.item.util.HasImportantInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class YouthPotion extends Item implements HasImportantInteraction {

    public YouthPotion(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        if (!(entity instanceof AgeableMob ageable) || entity.getType().is(SpeciesTags.EntityTypes.ALWAYS_ADULT)) {
            return super.interactLivingEntity(stack, player, entity, hand);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        stack.consume(1, player);
        if (!player.hasInfiniteMaterials() && stack.isEmpty()) {
            player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
        }

        if (!ageable.isBaby()) {
            ageable.setBaby(true);
            if (player instanceof ServerPlayer serverPlayer) SpeciesCriterion.TURN_MOB_INTO_BABY.get().trigger(serverPlayer);
            ageable.playSound(SpeciesSoundEvents.YOUTH_POTION_BABY.get(), 1, 1);
            if (entity.level() instanceof ServerLevel serverLevel) {
                double vx = entity.getRandom().nextGaussian() * 0.02D;
                double vy = entity.getRandom().nextGaussian() * 0.02D;
                double vz = entity.getRandom().nextGaussian() * 0.02D;
                serverLevel.sendParticles(SpeciesParticles.YOUTH_POTION.get(), entity.getRandomX(1), entity.getRandomY() + 0.5D, entity.getRandomZ(1), 0, vx, vy, vz, 1);
            }
        }
        if (ageable instanceof Springling springling) {
            springling.setExtendedAmount(0);
            springling.setTame(false, false);
            springling.setOwnerUUID(null);
        }

        return InteractionResult.SUCCESS;
    }

}