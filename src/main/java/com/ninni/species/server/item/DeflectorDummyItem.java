package com.ninni.species.server.item;

import com.ninni.species.registry.SpeciesEntities;
import com.ninni.species.registry.SpeciesSoundEvents;
import com.ninni.species.server.entity.mob.update_3.DeflectorDummy;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class DeflectorDummyItem extends Item {

    public DeflectorDummyItem(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(UseOnContext context) {
        Direction direction = context.getClickedFace();
        if (direction == Direction.DOWN) return InteractionResult.FAIL;

        Level level = context.getLevel();
        BlockPlaceContext blockContext = new BlockPlaceContext(context);
        BlockPos pos = blockContext.getClickedPos();
        ItemStack stack = context.getItemInHand();
        Vec3 center = Vec3.atBottomCenterOf(pos);
        AABB box = SpeciesEntities.DEFLECTOR_DUMMY.get().getDimensions().makeBoundingBox(center.x, center.y, center.z);
        if (!level.noCollision(null, box)) return InteractionResult.FAIL;
        if (!level.getEntities(null, box).isEmpty()) return InteractionResult.FAIL;

        if (level instanceof ServerLevel serverlevel) {
            Consumer<DeflectorDummy> consumer = EntityType.createDefaultStackConfig(serverlevel, stack, context.getPlayer());
            DeflectorDummy dummy = SpeciesEntities.DEFLECTOR_DUMMY.get().create(serverlevel, consumer, pos, MobSpawnType.SPAWN_EGG, true, true);
            if (dummy == null) return InteractionResult.FAIL;

            float yRot = (float)Mth.floor((Mth.wrapDegrees(context.getRotation() - 180) + 22.5F) / 45) * 45;
            dummy.moveTo(dummy.getX(), dummy.getY(), dummy.getZ(), yRot, 0);
            serverlevel.addFreshEntityWithPassengers(dummy);
            level.playSound(null, dummy.getX(), dummy.getY(), dummy.getZ(), SpeciesSoundEvents.DEFLECTOR_DUMMY_PLACE.get(), SoundSource.BLOCKS, 0.75F, 0.8F);
            dummy.gameEvent(GameEvent.ENTITY_PLACE, context.getPlayer());
        }

        stack.consume(1, context.getPlayer());
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.species.deflector_dummy.desc.powered").withStyle(ChatFormatting.GRAY));
        list.add(CommonComponents.SPACE.copy().append(Component.translatable("item.species.deflector_dummy.desc.damage").withColor(0xE21447)));
    }

}