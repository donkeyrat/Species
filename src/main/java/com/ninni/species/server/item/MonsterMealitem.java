package com.ninni.species.server.item;

import com.ninni.species.registry.SpeciesSoundEvents;
import com.ninni.species.registry.SpeciesStatusEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MonsterMealitem extends Item {

    public MonsterMealitem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        List<MobEffectInstance> effects = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).customEffects();
        MobEffectInstance effect = effects.get(level.getRandom().nextInt(effects.size()));
        if (effect.getEffect().value().isInstantenous()) {
            effect.getEffect().value().applyInstantenousEffect(entity, entity, entity, effect.getAmplifier(), 1);
        } else {
            entity.addEffect(effect);
        }

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SpeciesSoundEvents.MONSTER_MEAL_APPLY.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.species.whenEaten").withStyle(ChatFormatting.DARK_PURPLE));
        list.add(CommonComponents.SPACE.copy().append(Component.translatable("item.species.monster_meal.desc.effect").withStyle(Style.EMPTY.withColor(0xe72a8b))));
    }

    @Override
    public SoundEvent getEatingSound() {
        return SpeciesSoundEvents.WICKED_WAX_EAT.get();
    }

}
