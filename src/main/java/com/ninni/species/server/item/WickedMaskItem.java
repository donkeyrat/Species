package com.ninni.species.server.item;

import com.ninni.species.Species;
import com.ninni.species.access.DisguisingEntity;
import com.ninni.species.event.EquipmentChangeEvent;
import com.ninni.species.registry.SpeciesDataComponents;
import com.ninni.species.registry.SpeciesItems;
import com.ninni.species.registry.SpeciesSoundEvents;
import com.ninni.species.registry.SpeciesCriterion;
import com.ninni.species.server.component.EntityDataComponent;
import com.ninni.species.server.item.util.HasImportantInteraction;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = Species.MOD_ID)
public class WickedMaskItem extends Item implements Equipable, HasImportantInteraction {

    public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        protected ItemStack execute(BlockSource blockSource, ItemStack stack) {
        return WickedMaskItem.dispenseMask(blockSource, stack) ? stack : super.execute(blockSource, stack);
        }
    };

    public WickedMaskItem(Properties properties) {
        super(properties);
    }

    public static boolean dispenseMask(BlockSource source, ItemStack stack) {
        BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
        List<LivingEntity> list = source.level().getEntitiesOfClass(LivingEntity.class, new AABB(pos), EntitySelector.NO_SPECTATORS.and(new EntitySelector.MobCanWearArmorEntitySelector(stack)));
        if (list.isEmpty()) return false;

        LivingEntity entity = list.getFirst();
        EquipmentSlot slot = entity.getEquipmentSlotForItem(stack);
        ItemStack splitStack = stack.split(1);
        entity.setItemSlot(slot, splitStack);
        if (entity instanceof Mob) {
            ((Mob)entity).setDropChance(slot, 2);
            ((Mob)entity).setPersistenceRequired();
        }
        return true;
    }

    @SubscribeEvent
    public static void onEquip(EquipmentChangeEvent event) {
        if (event.getSlot() != EquipmentSlot.HEAD) return;

        LivingEntity entity = event.getEntity();
        if (!(entity instanceof DisguisingEntity disguising)) return;

        ItemStack stack = event.getStack();
        EntityDataComponent component = stack.getOrDefault(SpeciesDataComponents.STORED_ENTITY_DATA, EntityDataComponent.EMPTY);

        if (!(stack.getItem() instanceof WickedMaskItem) || component.isEmpty()) {
            disguising.setDisguisedEntity(null);
            return;
        }

        EntityType<?> type = component.type().value();
        if (disguising.getDisguisedEntity() != null && disguising.getDisguisedEntity().getType().equals(type)) return;

        Entity disguiseEntity = type.create(entity.level());
        if (disguiseEntity == null) return;

        disguiseEntity.load(component.data());
        disguising.setDisguisedEntity(disguiseEntity);
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof DisguisingEntity disguising)) return;

        if (disguising.getDisguisedEntity() != null && disguising.getDisguisedEntity().getPose() != entity.getPose()) {
            disguising.getDisguisedEntity().setPose(entity.getPose());
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!player.isSecondaryUseActive()) return this.swapWithEquipmentSlot(this, level, player, hand);
        return super.use(level, player, hand);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        if (entity instanceof Allay allay && !allay.hasItemInHand()) return InteractionResult.SUCCESS;

        if (stack.has(SpeciesDataComponents.STORED_ENTITY_DATA)) return super.interactLivingEntity(stack, player, entity, hand);

        Level level = entity.level();
        EntityDataComponent component = EntityDataComponent.fromEntity(entity);
        stack.set(SpeciesDataComponents.STORED_ENTITY_DATA, component);

        player.setItemInHand(hand, stack);

        if (entity instanceof WitherBoss && player instanceof ServerPlayer serverPlayer) {
            SpeciesCriterion.WICKED_MASK_WITHER.get().trigger(serverPlayer); // TODO replace with generic trigger
        }

        entity.level().playSound(null, player.getX(), player.getY(), player.getZ(), SpeciesSoundEvents.WICKED_MASK_LINK.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag tooltipFlag) {
        EntityDataComponent component = stack.getOrDefault(SpeciesDataComponents.STORED_ENTITY_DATA, EntityDataComponent.EMPTY);
        if (component.isEmpty()) {
            list.add(Component.translatable("item.species.wicked_mask.desc.disguise.1", Component.translatable("key.sneak"), Component.translatable("key.mouse.right")).withStyle(ChatFormatting.GRAY));
            list.add(Component.translatable("item.species.wicked_mask.desc.disguise.2").withStyle(ChatFormatting.GRAY));
        } else {
            EntityType<?> type = component.type().value();
			list.add(type.getDescription().copy().withColor(0xFFCA3D));
		}
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.modifiers.head").withStyle(ChatFormatting.GRAY));
        list.add(CommonComponents.SPACE.copy().append(Component.translatable("item.species.wicked_mask.desc.apply").withColor(0xE72A8B)));
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public Holder<SoundEvent> getEquipSound() {
        return SpeciesSoundEvents.WICKED_MASK_EQUIP;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return super.isFoil(stack) || stack.has(SpeciesDataComponents.STORED_ENTITY_DATA);
    }

}
