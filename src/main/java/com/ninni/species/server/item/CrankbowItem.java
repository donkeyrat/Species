package com.ninni.species.server.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ninni.species.access.ImmunityFrameIgnoringEntity;
import com.ninni.species.registry.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CrankbowItem extends ProjectileWeaponItem {

    public static final int BASE_CAPACITY = 128;
    public static final int BASE_MINIMUM_SPEED = 30;
    public static final int BASE_MAXIMUM_SPEED = 7;
    public static final float BASE_SPARING_CHANCE = 0;

    public CrankbowItem(Item.Properties properties) {
        super(properties);
    }

    public static float getPullProperty(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        if (level == null || entity == null) return -1;
        if (!stack.has(SpeciesDataComponents.USING)) return -1;

        int cooldown = stack.getOrDefault(SpeciesDataComponents.COOLDOWN, 0);
        int maxCooldown = CrankbowItem.getShootingCooldown(entity, stack, level.getRandom(), level);
        float progress = 1f - ((float) cooldown / Math.max(1, maxCooldown));

        if (progress < 0.05F) return 0;
        else if (progress < 0.20F) return 0.15F;
        else if (progress < 0.35F) return 0.3F;
        else if (progress < 0.5F) return 0.4F;
        else return 0.6F;
    }

    public static ChargedProjectiles getProjectiles(LivingEntity entity, ItemStack stack) {
        ChargedProjectiles projectiles = stack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
        if (projectiles.isEmpty() && entity instanceof Player player && player.isCreative()) {
            return ChargedProjectiles.of(new ItemStack(Items.ARROW));
        }
        return projectiles;
    }

    public static boolean canStartShooting(LivingEntity entity, ItemStack stack) {
        if (entity instanceof Player player && player.isCreative()) return true;
        return !getProjectiles(entity, stack).isEmpty();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!canStartShooting(player, stack)) return InteractionResultHolder.fail(stack);

        stack.set(SpeciesDataComponents.SHOTS_FIRED, 0);
        stack.remove(SpeciesDataComponents.COOLDOWN);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int time) {
        if (stack.getDamageValue() >= stack.getMaxDamage()) {
            if (entity instanceof Player player) player.stopUsingItem();
            return;
        }

        if (stack.has(SpeciesDataComponents.COOLDOWN)) {
            int cooldown = stack.get(SpeciesDataComponents.COOLDOWN);
            if (cooldown > 0) stack.set(SpeciesDataComponents.COOLDOWN, cooldown - 1);
        }

        var chargedProjectiles = getProjectiles(entity, stack);
        if (!chargedProjectiles.isEmpty()) {
            PullingSounds sounds = getPullingSounds(stack);
            if (!stack.has(SpeciesDataComponents.COOLDOWN)) {
                entity.playSound(sounds.pull.value());
                stack.set(SpeciesDataComponents.COOLDOWN, getShootingCooldown(entity, stack, level.getRandom(), level));
                stack.set(SpeciesDataComponents.SHOTS_FIRED, 0);
            } else {
                if (stack.get(SpeciesDataComponents.COOLDOWN) == 0) {
                    int shotsFired = stack.getOrDefault(SpeciesDataComponents.SHOTS_FIRED, 0);
                    entity.playSound(sounds.pull.value());

                    if (shotsFired % 5 == 0 && shotsFired != 0 && shotsFired <= 30) {
                        entity.playSound(SpeciesSoundEvents.CRANKBOW_SPEED.get(), 0.5F, shotsFired / 20f + 0.5F);
                    }

                    if (level instanceof ServerLevel serverLevel) {
                        shoot(serverLevel, entity, entity.getUsedItemHand(), stack, chargedProjectiles.getItems(), 1f, 1f, false, null);
                    }
                    stack.set(SpeciesDataComponents.COOLDOWN, getShootingCooldown(entity, stack, level.getRandom(), level));
                    if (shotsFired < 40) stack.set(SpeciesDataComponents.SHOTS_FIRED, shotsFired + 1);
                }
            }
            if (!level.isClientSide) stack.set(SpeciesDataComponents.USING, true);
        } else {
            if (entity instanceof Player player) {
                stack.set(SpeciesDataComponents.SHOTS_FIRED, 0);
                stack.remove(SpeciesDataComponents.COOLDOWN);
                player.stopUsingItem();
            }
        }
    }

    public static PullingSounds getPullingSounds(ItemStack stack) {
        return EnchantmentHelper.pickHighestLevel(stack, SpeciesEnchantmentEffectComponents.CRANKBOW_PULLING_SOUNDS.get()).orElse(PullingSounds.DEFAULT);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        int shots = stack.getOrDefault(SpeciesDataComponents.SHOTS_FIRED, 0);
        if (entity instanceof Player player) player.getCooldowns().addCooldown(this, shots * 8);

        if (shots > 7) entity.playSound(SpeciesSoundEvents.CRANKBOW_STOP.get());
        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < shots / 2; i++) {
                serverLevel.sendParticles(
                    SpeciesParticles.BEWEREAGER_SLOW.get(),
                    entity.getX() + entity.getRandom().nextGaussian() * 0.5,
                    entity.getY(1F) + entity.getRandom().nextFloat(),
                    entity.getZ() + entity.getRandom().nextGaussian() * 0.5,
                    1, 0.3, 0.3, 0.3, 1.0D
                );
            }
        }
        stack.set(SpeciesDataComponents.SHOTS_FIRED, 0);
        stack.remove(SpeciesDataComponents.COOLDOWN);
        stack.remove(SpeciesDataComponents.USING);
    }

    protected void shoot(ServerLevel level, LivingEntity entity, InteractionHand hand, ItemStack weapon, List<ItemStack> projectileItems, float originalVelocity, float inaccuracy, boolean isCrit, @Nullable LivingEntity target) {
        ChargedProjectiles projectiles = getProjectiles(entity, weapon);
        if (projectiles.isEmpty()) return;

        int shots = weapon.getOrDefault(SpeciesDataComponents.SHOTS_FIRED, 0);
        ItemStack projectileStack = projectiles.getItems().getFirst();
        float v = shots / 20f;
        float velocity = v + 1.15f;

        float spread = (2 + (getMaxSpeed(weapon, level.getRandom()) - getShootingCooldown(entity, weapon, level.getRandom(), level)) / (float) getMaxSpeed(weapon, level.getRandom())) / 2f;
        for (int i = 0; i < spread * 10; i++) {
            level.sendParticles(
                SpeciesParticles.BEWEREAGER_SPEED.get(),
                entity.getRandomX(0.35D),
                entity.getY(0.35D) + entity.getRandom().nextFloat(),
                entity.getRandomZ(0.35D),
                1, 0.3, 0.3, 0.3, 1.0D
            );
        }

        if (EnchantmentHelper.has(weapon, SpeciesEnchantmentEffectComponents.CRANKBOW_SCATTERSHOT.get())) {
            if (shots <= 10) {
                float z = (v * -15) + 10;
                shootProjectile(level, entity, hand, weapon, projectileStack, 1.0F, velocity, 1.0F, z, true);
                shootProjectile(level, entity, hand, weapon, projectileStack, 1.0F, velocity, 1.0F, -z, true);
            }

            if (shots <= 5) {
                float z = (v * -40) + 20;
                shootProjectile(level, entity, hand, weapon, projectileStack, 1.0F, velocity,1.0F, z, true);
                shootProjectile(level, entity, hand, weapon, projectileStack, 1.0F, velocity,1.0F, -z, true);
            }
        }
        shootProjectile(level, entity, hand, weapon, projectileStack, 1.0F, velocity,1.0F, 0, false);
    }

    @Override
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
        projectile.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() + angle, 0.0F, velocity, inaccuracy);
    }

    public static int getShootingCooldown(LivingEntity entity, ItemStack stack, RandomSource random, Level level) {
        int shots = stack.getOrDefault(SpeciesDataComponents.SHOTS_FIRED, 0);
        int enchantLevel = stack.getEnchantmentLevel(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(SpeciesEnchantments.QUICK_CRANK));
        int i = enchantLevel == 0 ? 3 : Math.max(1, 4 - enchantLevel);

        int cooldown = getMinSpeed(stack, random);
        if (shots == 0) cooldown = getMinSpeed(stack, random);
        else if (shots <= 1) cooldown -= i;
        else if (shots <= 5) cooldown -= 2 * i;
        else if (shots <= 10) cooldown -= 5 * i;
        else if (shots <= 20) cooldown -= 10 * i;
        else if (shots <= 30) cooldown -= 20 * i;
        else cooldown -= 30 * i;

        return Math.clamp(cooldown, getMaxSpeed(stack, random), 40);
    }

    public static int getMinSpeed(ItemStack stack, RandomSource random) {
        return modifyCrankbowMinimumSpeed(stack, random, BASE_MINIMUM_SPEED);
    }

    public static int modifyCrankbowMinimumSpeed(ItemStack stack, RandomSource random, float value) {
        MutableFloat result = new MutableFloat(value);
        EnchantmentHelper.runIterationOnItem(stack, (holder, level) ->
            holder.value().modifyUnfilteredValue(SpeciesEnchantmentEffectComponents.CRANKBOW_MINIMUM_SPEED.get(), random, level, result)
        );
        return (int) Math.max(0, Math.ceil(result.floatValue()));
    }

    public static int getMaxSpeed(ItemStack stack, RandomSource random) {
        return modifyCrankbowMaximumSpeed(stack, random, BASE_MAXIMUM_SPEED);
    }

    public static int modifyCrankbowMaximumSpeed(ItemStack stack, RandomSource random, float value) {
        MutableFloat result = new MutableFloat(value);
        EnchantmentHelper.runIterationOnItem(stack, (holder, level) ->
            holder.value().modifyUnfilteredValue(SpeciesEnchantmentEffectComponents.CRANKBOW_MAXIMUM_SPEED.get(), random, level, result)
        );
        return (int) Math.max(0, Math.ceil(result.floatValue()));
    }

    public static int getMaxWeight(LivingEntity entity, ItemStack stack) {
        return modifyCrankbowCapacity(stack, entity.getRandom(), BASE_CAPACITY);
    }

    public static int getMaxWeight(RandomSource random, ItemStack stack) {
        return modifyCrankbowCapacity(stack, random, BASE_CAPACITY);
    }

    public static int modifyCrankbowCapacity(ItemStack stack, RandomSource random, float value) {
        MutableFloat result = new MutableFloat(value);
        EnchantmentHelper.runIterationOnItem(stack, (holder, level) ->
            holder.value().modifyUnfilteredValue(SpeciesEnchantmentEffectComponents.CRANKBOW_CAPACITY.get(), random, level, result)
        );
        return (int) Math.max(0, Math.ceil(result.floatValue()));
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    private static void shootProjectile(Level level, LivingEntity livingEntity, InteractionHand hand, ItemStack stack, ItemStack stack1, float pitch, float x, float y, float z, boolean fromScattershot) {
        if (level.isClientSide) return;

        ArrowItem arrowitem = (ArrowItem)(stack1.getItem() instanceof ArrowItem ? stack1.getItem() : Items.ARROW);
        AbstractArrow abstractarrow = arrowitem.createArrow(level, stack1, livingEntity, stack);
        if (abstractarrow instanceof ImmunityFrameIgnoringEntity access) access.setIgnoreImmunityFrames(true);

        Vec3 vec31 = livingEntity.getUpVector(1.0F);
        Quaternionf quaternionf = (new Quaternionf()).setAngleAxis((z * 0.017453292F), vec31.x, vec31.y, vec31.z);
        Vec3 vec3 = livingEntity.getViewVector(1.0F);
        Vector3f vector3f = vec3.toVector3f().rotate(quaternionf);
        abstractarrow.shoot(vector3f.x(), vector3f.y(), vector3f.z(), x, y);

        if (livingEntity instanceof Player player) {
            boolean flag1 = player.getAbilities().instabuild || arrowitem.isInfinite(stack1, stack, player) || fromScattershot;
            if (flag1 || player.getAbilities().instabuild && (stack1.is(Items.SPECTRAL_ARROW) || stack1.is(Items.TIPPED_ARROW))) {
                abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }

            if (!flag1 && !player.getAbilities().instabuild) {
                float sparingChance = getSparingChance(stack, level.getRandom());
                if (sparingChance > 0 && level.getRandom().nextFloat() < sparingChance) {
                    abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),  SpeciesSoundEvents.CRANKBOW_SHOOT_SPARING.get(), SoundSource.PLAYERS, 1.0F, pitch);
                } else {
                    removeOneItem(stack);
                    level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SpeciesSoundEvents.CRANKBOW_SHOOT.get(), SoundSource.PLAYERS, 1.0F, pitch);
                }
            } else {
                if (!fromScattershot) level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SpeciesSoundEvents.CRANKBOW_SHOOT.get(), SoundSource.PLAYERS, 1.0F, pitch);
            }
        }

        stack.hurtAndBreak(1, livingEntity, livingEntity.getEquipmentSlotForItem(livingEntity.getItemInHand(hand)));
        level.addFreshEntity(abstractarrow);
    }

    public static float getSparingChance(ItemStack stack, RandomSource random) {
        return modifyCrankbowSparingChance(stack, random, BASE_SPARING_CHANCE);
    }

    public static float modifyCrankbowSparingChance(ItemStack stack, RandomSource random, float value) {
        MutableFloat result = new MutableFloat(value);
        EnchantmentHelper.runIterationOnItem(stack, (holder, level) ->
            holder.value().modifyUnfilteredValue(SpeciesEnchantmentEffectComponents.CRANKBOW_SPARING_CHANCE.get(), random, level, result)
        );
        return (float) Math.max(0, Math.ceil(result.floatValue()));
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (stack.getCount() != 1) return false;
        if (action != ClickAction.SECONDARY) return false;

		ItemStack slotStack = slot.getItem();
		if (slotStack.isEmpty()) {
			this.playRemoveOneSound(player);
			removeOne(stack).ifPresent(rStack ->
				add(player, stack, slot.safeInsert(rStack))
			);
		} else if (slotStack.getItem().canFitInsideContainerItems() && getAllSupportedProjectiles().test(slotStack)) {
			int i = (getMaxWeight(player, stack) - getContentWeight(stack));
			int j = add(player, stack, slot.safeTake(slotStack.getCount(), i, player));
			if (j > 0) this.playInsertSound(player);
		}

		return true;
	}

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (stack.getCount() != 1) return false;
        if (action != ClickAction.SECONDARY) return false;
        if (!slot.allowModification(player)) return false;

        if (other.isEmpty()) {
            removeOne(stack).ifPresent(rStack -> {
                this.playRemoveOneSound(player);
                access.set(rStack);
            });
        } else if (getAllSupportedProjectiles().test(other)) {
            int i = add(player, stack, other);
            if (i > 0) {
                this.playInsertSound(player);
                other.shrink(i);
            }
        }

        return true;
    }

    public static int add(LivingEntity entity, ItemStack weapon, ItemStack stack) {
        if (stack.isEmpty() || !stack.getItem().canFitInsideContainerItems()) return 0;

        ChargedProjectiles projectiles = weapon.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
        List<ItemStack> items = new ArrayList<>(projectiles.getItems());

        int i = getContentWeight(weapon);
        int k = Math.min(stack.getCount(), getMaxWeight(entity, weapon) - i);

        if (k == 0) return 0;

        Optional<ItemStack> optional = getMatchingItem(stack, items);
        if (optional.isPresent()) {
            ItemStack itemStack = optional.get();

            if (itemStack.getCount() + k > 64) {
                int i2 = itemStack.getCount() + k;
                itemStack.setCount(64);
                items.remove(itemStack);
                items.addFirst(itemStack);

                ItemStack copiedItemStack = stack.copyWithCount(k);
                copiedItemStack.setCount(i2-64);
                items.addFirst(copiedItemStack);
            } else {
                itemStack.grow(k);
                items.remove(itemStack);
                items.addFirst(itemStack);
            }
        } else {
            ItemStack copiedItemStack = stack.copyWithCount(k);
            items.addFirst(copiedItemStack);
        }
        weapon.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(items));
        return k;
    }

    private static Optional<ItemStack> getMatchingItem(ItemStack stack, List<ItemStack> items) {
        if (stack.is(Items.BUNDLE)) return Optional.empty();
        return items.stream().filter(iStack -> ItemStack.isSameItemSameComponents(iStack, stack)).findFirst();
    }

    public static int getContentWeight(ItemStack stack) {
        return CrankbowItem.getContents(stack).mapToInt(ItemStack::getCount).sum();
    }

    private static Optional<ItemStack> removeOne(ItemStack stack) {
        ChargedProjectiles projectiles = stack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
        if (projectiles.isEmpty()) return Optional.empty();
        if (projectiles.getItems().isEmpty()) return Optional.empty();

        ArrayList<ItemStack> items = new ArrayList<>(projectiles.getItems());
        ItemStack firstStack = items.getFirst();
        items.removeFirst();

        stack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(items));
        return Optional.of(firstStack);
    }

    private static Optional<ItemStack> removeOneItem(ItemStack stack) {
        ChargedProjectiles projectiles = stack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
        if (projectiles.isEmpty()) return Optional.empty();
        if (projectiles.getItems().isEmpty()) return Optional.empty();

        ArrayList<ItemStack> items = new ArrayList<>(projectiles.getItems());
        ItemStack firstStack = items.getFirst();

        if (firstStack.getCount() == 1) {
            if (items.size() == 1) items.clear();
            else items.removeFirst();

            stack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(items));
            return Optional.empty();
        }

        ItemStack reducedStack = firstStack.copyWithCount(firstStack.getCount() - 1);
        items.set(0, reducedStack);
        stack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(items));
        return Optional.of(reducedStack);
    }

    private static Stream<ItemStack> getContents(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY).getItems().stream();
    }

    @Override
    public void onDestroyed(ItemEntity entity) {
        ItemUtils.onContainerDestroyed(entity, CrankbowItem.getContents(entity.getItem()).toList());
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SpeciesSoundEvents.CRANKBOW_REMOVE_ARROW.get(), 0.8f, 0.8f + entity.level().getRandom().nextFloat() * 0.4f);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SpeciesSoundEvents.CRANKBOW_LOAD_ARROW.get(), 0.8f, 0.8f + entity.level().getRandom().nextFloat() * 0.4f);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        NonNullList<ItemStack> list = NonNullList.create();
        CrankbowItem.getContents(stack).forEach(list::add);
        return Optional.of(new BundleTooltip(new BundleContents(list)));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag tooltipFlag) {
        Level level = context.level();
        if (level == null) return;

        list.add(Component.translatable("item.species.crankbow.fullness", CrankbowItem.getContentWeight(stack), CrankbowItem.getMaxWeight(level.getRandom(), stack)).withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("item.species.crankbow.desc").withStyle(Style.EMPTY.withColor(0x723548)));
        super.appendHoverText(stack, context, list, tooltipFlag);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack material) {
        return material.is(SpeciesItems.WEREFANG.get());
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean changed) {
        return changed;
    }

    public record PullingSounds(Holder<SoundEvent> pull) {

        public static final PullingSounds DEFAULT = new PullingSounds(SpeciesSoundEvents.CRANKBOW_PULL);

        public static final Codec<PullingSounds> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SoundEvent.CODEC.optionalFieldOf("pull", SpeciesSoundEvents.CRANKBOW_PULL).forGetter(PullingSounds::pull)
        ).apply(instance, PullingSounds::new));

    }

}
