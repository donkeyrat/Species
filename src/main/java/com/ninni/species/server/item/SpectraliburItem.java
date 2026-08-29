package com.ninni.species.server.item;

import com.ninni.species.Species;
import com.ninni.species.registry.*;
import com.ninni.species.server.entity.mob.update_3.Spectre;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Species.MOD_ID)
public class SpectraliburItem extends Item {

    public static final Style STYLE = Style.EMPTY.withColor(0x44B4D1);

    public static final List<List<EntityType<?>>> SPECTRES = List.of(
        List.of()
    );
    
    public SpectraliburItem(Properties properties) {
        super(properties.component(DataComponents.TOOL, createToolProperties()));
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingDamageEvent.Pre event) {
        DamageSource source = event.getSource();

        ItemStack stack = source.getWeaponItem();
        if (stack == null || stack.isEmpty()) return;

        LivingEntity entity = event.getEntity();
        Level level = entity.level();
        float amount = event.getOriginalDamage();
        if (stack.getItem() instanceof SpectraliburItem spectralibur) spectralibur.storeSouls(level, entity, stack, amount);
    }

    public static float getSoulsProperty(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        return stack.getOrDefault(SpeciesDataComponents.SOULS, 0);
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
            .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 8F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
            .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.4F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
            .build();
    }

    public static Tool createToolProperties() {
        return new Tool(List.of(Tool.Rule.minesAndDrops(List.of(Blocks.COBWEB), 15.0F), Tool.Rule.overrideSpeed(BlockTags.SWORD_EFFICIENT, 1.5F)), 1.0F, 2);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getMainHandItem();
        int souls = stack.getOrDefault(SpeciesDataComponents.SOULS, 0);
        if (souls > 0) {
            player.startUsingItem(hand);
            player.playSound(SpeciesSoundEvents.SPECTRALIBUR_START_CHARGING.get(), 1,1);
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int time) {
        super.onUseTick(level, entity, stack, time);
        if (time >= stack.getUseDuration(entity) || time % 10 != 0) return;

        int souls = stack.getOrDefault(SpeciesDataComponents.SOULS, 0);
        if (souls <= 0) return;

        int usingSouls = stack.getOrDefault(SpeciesDataComponents.USING_SOULS, 0);
        stack.set(SpeciesDataComponents.SOULS, Math.max(souls - 1, 0));
        stack.set(SpeciesDataComponents.USING_SOULS, usingSouls + 1);

        entity.playSound(SpeciesSoundEvents.SPECTRALIBUR_USE_SOUL.get(), 1,0.75F + usingSouls * 0.1F);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeUsed) {
        this.spawnSpectres(stack, level, entity);
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        super.onStopUsing(stack, entity, count);
        this.spawnSpectres(stack, entity.level(), entity);
    }

    public void spawnSpectres(ItemStack stack, Level level, LivingEntity entity) {
        int usingSouls = stack.getOrDefault(SpeciesDataComponents.USING_SOULS, 0);
        if (usingSouls <= 0) return;

        if (level instanceof ServerLevel serverLevel && entity instanceof Player player) {
            if (usingSouls == 1 || usingSouls == 3) {
                Spectre.spawnSpectre(serverLevel, player, player.getOnPos().above(2), Spectre.Type.SPECTRE, true);
            }
            if (usingSouls == 2 || usingSouls == 3 || usingSouls == 4) {
                Spectre.spawnSpectre(serverLevel, player, player.getOnPos().above(2), Spectre.Type.JOUSTING_SPECTRE, true);
            }
            if (usingSouls == 4) {
                Spectre.spawnSpectre(serverLevel, player, player.getOnPos().above(2), Spectre.Type.JOUSTING_SPECTRE, true);
            }
            if (usingSouls == 5) {
                Spectre.spawnSpectre(serverLevel, player, player.getOnPos().above(2), Spectre.Type.HULKING_SPECTRE, true);
            }
            Vec3 pos = entity.position();
            serverLevel.sendParticles(SpeciesParticles.SPECTRALIBUR.get(), pos.x,pos.y + 0.01F, pos.z, 1,0, 0, 0, 0);
        }

        if (entity instanceof ServerPlayer serverPlayer) {
            SpeciesCriterion.SUMMON_SPECTRE.get().trigger(serverPlayer);
        }

        entity.playSound(SpeciesSoundEvents.SPECTRALIBUR_RELEASE_SPECTRE.get(), 1,1);
        stack.set(SpeciesDataComponents.USING_SOULS, 0);
    }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        int souls = stack.getOrDefault(SpeciesDataComponents.SOULS, 0);
        if (souls <= 0) return 0;
        return souls * 10 + 10;
    }

    public void storeSouls(Level level, LivingEntity entity, ItemStack stack, float amount) {
        if (amount <= entity.getHealth()) return;

        int souls = stack.getOrDefault(SpeciesDataComponents.SOULS, 0);
        if (souls >= 5) return;

        stack.set(SpeciesDataComponents.SOULS, souls + 1);
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SpeciesSoundEvents.SPECTRALIBUR_COLLECT_SOUL.get(), SoundSource.PLAYERS, 1, 1);
            serverLevel.sendParticles(SpeciesParticles.COLLECTED_SOUL.get(), entity.getX(), entity.getY() + 0.2F, entity.getZ(), 1, 0,0,0, 0);
        }
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility ability) {
        return ItemAbilities.DEFAULT_SWORD_ACTIONS.contains(ability);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.is(Blocks.COBWEB)) return 15;
        else return state.is(BlockTags.SWORD_EFFICIENT) ? 1.5F : 1;
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity livingEntity) {
        return true;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.TOOT_HORN;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, list, tooltipFlag);
        
        int souls = stack.getOrDefault(SpeciesDataComponents.SOULS, 0);
        if (souls <= 0) {
            list.add(Component.translatable("item.species.spectralibur.desc").withStyle(STYLE));
            return;
        }

        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.species.spectralibur.desc.release").withStyle(ChatFormatting.GRAY));

        if (souls > 1) {
            list.add(CommonComponents.SPACE.copy().append(Component.translatable("item.species.spectralibur.desc.spectre.2", souls).withStyle(STYLE)));
        } else {
            list.add(CommonComponents.SPACE.copy().append(Component.translatable("item.species.spectralibur.desc.spectre.1", souls).withStyle(STYLE)));
        }

        if (souls / 2 > 0) {
            if (souls >= 4) list.add(CommonComponents.SPACE.copy().append(Component.translatable("item.species.spectralibur.desc.jousting_spectre.2", souls / 2).withStyle(STYLE)));
            else list.add(CommonComponents.SPACE.copy().append(Component.translatable("item.species.spectralibur.desc.jousting_spectre.1", souls / 2).withStyle(STYLE)));
        }
        if (souls / 5 > 0) {
            if (souls >= 10) list.add(CommonComponents.SPACE.copy().append(Component.translatable("item.species.spectralibur.desc.hulking_spectre.2", souls / 5).withStyle(STYLE)));
            else list.add(CommonComponents.SPACE.copy().append(Component.translatable("item.species.spectralibur.desc.hulking_spectre.1", souls / 5).withStyle(STYLE)));
        }
            
    }
}
