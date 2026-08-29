package com.ninni.species.server.item;

import com.ninni.species.registry.SpeciesSoundEvents;
import com.ninni.species.server.entity.mob.update_3.WickedSwapperProjectile;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class WickedSwapperItem extends Item {

    public WickedSwapperItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level  level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        Vec3 pos = player.position();
        level.playSound(null, pos.x, pos.y, pos.z, SpeciesSoundEvents.WICKED_SWAPPER_THROW.get(), SoundSource.PLAYERS, 1, 1);
        player.awardStat(Stats.ITEM_USED.get(this));
        player.getCooldowns().addCooldown(this, 20);

        if (!level.isClientSide) {
            WickedSwapperProjectile projectile = new WickedSwapperProjectile(level, player);
            projectile.setPos(pos.x, player.getEyeY() - 0.1F, pos.z);
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2, 1);
            level.addFreshEntity(projectile);
        }
        stack.consume(1, player);

        return InteractionResultHolder.success(stack);
    }
}
