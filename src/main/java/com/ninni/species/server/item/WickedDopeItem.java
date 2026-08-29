package com.ninni.species.server.item;

import com.ninni.species.registry.SpeciesSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WickedDopeItem extends Item {

    public WickedDopeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!player.getActiveEffects().isEmpty()) return super.use(level, player, hand);

        ItemStack stack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SpeciesSoundEvents.WICKED_DOPE_FAIL.get(), player.getSoundSource(), 1.0F, 1.0F);
        player.getCooldowns().addCooldown(this.asItem(), 60);
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player && !player.getActiveEffects().isEmpty()) {
            if (!player.isCreative()) player.getCooldowns().addCooldown(this, 20 * 60 * 2);
            player.getActiveEffects().forEach(instance -> player.addEffect(new MobEffectInstance(instance.getEffect(), instance.getDuration(), instance.getAmplifier() + 1)));
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SpeciesSoundEvents.WICKED_DOPE_BOOST.get(), entity.getSoundSource(), 1, 1);
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag flag) {
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.species.whenEaten").withStyle(ChatFormatting.DARK_PURPLE));
        list.add(CommonComponents.SPACE.copy().append(Component.translatable("item.species.wicked_dope.desc.effect").withColor(0xe72a8b)));
    }

    @Override
    public SoundEvent getEatingSound() {
        return SpeciesSoundEvents.WICKED_WAX_EAT.get();
    }

}
