package com.ninni.species.server.item;

import com.ninni.species.registry.SpeciesDataComponents;
import com.ninni.species.registry.SpeciesParticles;
import com.ninni.species.registry.SpeciesSoundEvents;
import com.ninni.species.server.item.util.HasImportantInteraction;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.List;

public class PetEffectItem extends Item implements HasImportantInteraction {

    public PetEffectItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        boolean canApply = entity instanceof OwnableEntity ownableEntity
            && ownableEntity.getOwner() != null
            && ownableEntity.getOwner().is(player)
            && !player.getCooldowns().isOnCooldown(stack.getItem());

        if (!canApply) return super.interactLivingEntity(stack, player, entity, hand);

        PotionContents contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        contents.forEachEffect(effect -> {
            if (effect.getEffect().value().isInstantenous()) {
                effect.getEffect().value().applyInstantenousEffect(player, player, entity, effect.getAmplifier(), 1);
            } else {
                entity.addEffect(effect);
            }
        });

        player.getCooldowns().addCooldown(stack.getItem(), stack.getOrDefault(SpeciesDataComponents.COOLDOWN, 0));
        stack.consume(1, player);

        if (player.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(SpeciesParticles.WICKED_FLAME.get(),
                entity.getX(), entity.getY(0.6F), entity.getZ(), 20,
                0.3F, 0.3F, 0.3F, 1
            );
        }
        entity.playSound(SpeciesSoundEvents.WICKED_TREAT_APPLY.get());

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag flag) {
        list.add(Component.translatable("item.species.pet_effect.desc").withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        list.add(CommonComponents.EMPTY);

        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        if (contents != null) contents.addPotionTooltip(list::add, 1, context.tickRate());
    }
}
