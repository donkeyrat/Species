package com.ninni.species.server.entity.mob.update_1;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import com.ninni.species.registry.*;
import com.ninni.species.server.LimpetOres;
import com.ninni.species.server.entity.ai.LimpetAi;
import com.ninni.species.server.entity.util.SpeciesPose;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Limpet extends PathfinderMob {

    protected static final ImmutableList<SensorType<? extends Sensor<? super Limpet>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY);
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.IS_PANICKING, MemoryModuleType.AVOID_TARGET);

    protected static final EntityDataAccessor<Integer> SCARED_TICKS = SynchedEntityData.defineId(Limpet.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> HAS_SHELL = SynchedEntityData.defineId(Limpet.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Integer> CRACKED_STAGE = SynchedEntityData.defineId(Limpet.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Holder<LimpetOres>> ORE_DATA = SynchedEntityData.defineId(Limpet.class, SpeciesEntityDataSerializers.LIMPET_ORE_DATA.get());
    protected static final EntityDataAccessor<Integer> MAX_COUNT = SynchedEntityData.defineId(Limpet.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<ItemStack> ORE_ITEM_STACK = SynchedEntityData.defineId(Limpet.class, EntityDataSerializers.ITEM_STACK);
    protected static final EntityDataAccessor<BlockState> ORE_BLOCK_STATE = SynchedEntityData.defineId(Limpet.class, EntityDataSerializers.BLOCK_STATE);

    protected static final UniformInt RETREAT_DURATION = TimeUtil.rangeOfSeconds(5, 20);

    protected static final EntityDimensions SCARED_DIMENSIONS = EntityDimensions.scalable(0.75F, 0.75F);

    public Limpet(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected Brain.Provider<Limpet> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Brain<Limpet> getBrain() {
        return (Brain<Limpet>) super.getBrain();
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return LimpetAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("limpetBrain");
        this.getBrain().tick((ServerLevel)this.level(), this);
        this.level().getProfiler().pop();
        this.level().getProfiler().push("limpetActivityUpdate");
        LimpetAi.updateActivity(this);
        this.level().getProfiler().pop();
        super.customServerAiStep();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SCARED_TICKS, 0);
        builder.define(HAS_SHELL, true);
        builder.define(CRACKED_STAGE, 0);

        Registry<LimpetOres> registry = this.registryAccess().registryOrThrow(SpeciesRegistries.LIMPET_ORES);
        builder.define(ORE_DATA, registry.getHolder(SpeciesLimpetOreData.SHELL).or(registry::getAny).orElseThrow());

        builder.define(MAX_COUNT, 0);
        builder.define(ORE_ITEM_STACK, Items.BONE_MEAL.getDefaultInstance());
        builder.define(ORE_BLOCK_STATE, Blocks.STONE.defaultBlockState());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("ScaredTicks", this.getScaredTicks());
        nbt.putBoolean("HasShell", this.hasShell());
        nbt.putInt("CrackedStage", this.getCrackedStage());

        this.getOreData().unwrapKey().ifPresent(key ->
            nbt.putString("ore_data", key.location().toString())
        );

        nbt.putInt("MaxCount", this.getMaxCount());
        nbt.put("OreItemStack", this.getOreItemStack().save(registryAccess()));
        nbt.put("OreBlockState", NbtUtils.writeBlockState(this.getOreBlockState()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setScaredTicks(nbt.getInt("ScaredTicks"));
        this.setHasShell(nbt.getBoolean("HasShell"));
        this.setCrackedStage(nbt.getInt("CrackedStage"));

        Optional.ofNullable(ResourceLocation.tryParse(nbt.getString("ore_data")))
            .map(location -> ResourceKey.create(SpeciesRegistries.LIMPET_ORES, location))
            .flatMap(key -> this.registryAccess().registryOrThrow(SpeciesRegistries.LIMPET_ORES).getHolder(key))
            .ifPresent(this::setOreData);

        if (nbt.contains("MaxCount")) this.setMaxCount(nbt.getInt("MaxCount"));
        if (nbt.contains("OreItemStack")) this.setOreItemStack(ItemStack.parseOptional(registryAccess(), nbt.getCompound("OreItemStack")));
        if (nbt.contains("OreBlockState")) this.setOreBlockState(NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), nbt.getCompound("OreBlockState")));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType type, @Nullable SpawnGroupData group) {
        this.setRandomOre();
        return super.finalizeSpawn(level, difficulty, type, group);
    }

    public void setRandomOre() {
        Level level = this.level();
        Holder<Biome> biome = level.getBiome(this.blockPosition());
        int yLevel = this.blockPosition().getY();
        int maxYLevel = level.getMaxBuildHeight();
        int minYLevel = level.getMinBuildHeight();

        Predicate<LimpetOres> filter = data -> data.location().isPresent()
                && data.location().get().matchesBiome(biome)
                && yLevel <= data.maxSpawnHeight().orElse(maxYLevel)
                && yLevel > data.minSpawnHeight().orElse(minYLevel);

        Holder<LimpetOres> data = getRandomVariant(level, filter);
        if (data != null) this.setOreData(data);
    }

    public static Holder.Reference<LimpetOres> getRandomVariant(Level level, Predicate<LimpetOres> filter) {
        List<Holder.Reference<LimpetOres>> entries = LimpetOres.getAllData(level).holders()
            .filter(holder -> filter.test(holder.value()))
            .toList();
        if (entries.isEmpty()) return null;

        int totalWeight = entries.stream().map(Holder.Reference::value).mapToInt(LimpetOres::spawnWeight).sum();
        if (totalWeight <= 0) return null;

        int randomWeight = level.getRandom().nextInt(totalWeight);

        int cumulativeWeight = 0;
        for (Holder.Reference<LimpetOres> entry : entries) {
            cumulativeWeight += entry.value().spawnWeight();
            if (randomWeight < cumulativeWeight) return entry;
        }

        return null;
    }

    public ItemStack getOreItemStack() {
        return this.entityData.get(ORE_ITEM_STACK);
    }

    public void setOreItemStack(ItemStack stack) {
        this.entityData.set(ORE_ITEM_STACK, stack);
    }

    public BlockState getOreBlockState() {
        return this.entityData.get(ORE_BLOCK_STATE);
    }

    public void setOreBlockState(BlockState state) {
        this.entityData.set(ORE_BLOCK_STATE, state);
    }

    public int getMaxCount() {
        return this.entityData.get(MAX_COUNT);
    }

    public void setMaxCount(int maxCount) {
        this.entityData.set(MAX_COUNT, maxCount);
    }

    public boolean hasShell() {
        return this.entityData.get(HAS_SHELL);
    }

    public void setHasShell(boolean hasShell) {
        this.entityData.set(HAS_SHELL, hasShell);
    }

    public int getCrackedStage() {
        return this.entityData.get(CRACKED_STAGE);
    }

    public void setCrackedStage(int crackedStage) {
        this.entityData.set(CRACKED_STAGE, crackedStage);
    }

    public Holder<LimpetOres> getOreData() {
        return this.entityData.get(ORE_DATA);
    }

    public void setOreData(Holder<LimpetOres> holder) {
        this.entityData.set(ORE_DATA, holder);

        LimpetOres data = holder.value();
        this.setMaxCount(data.maxCount().orElse(0));

        ItemStack stack = data.item() != null ? data.item().getDefaultInstance() : Items.BONE_MEAL.getDefaultInstance();
        this.setOreItemStack(stack);

        this.setOreBlockState(data.block().defaultBlockState());
    }

    public int getScaredTicks() {
        return this.entityData.get(SCARED_TICKS);
    }

    public void setScaredTicks(int scaredTicks) {
        this.entityData.set(SCARED_TICKS, scaredTicks);
    }

    public boolean isScared() {
        return this.getScaredTicks() > 0;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.isScared()) this.setPose(SpeciesPose.SCARED.get());
        else this.setPose(Pose.STANDING);

        if (this.level().isClientSide) return;

        if (!this.getBrain().hasMemoryValue(MemoryModuleType.AVOID_TARGET)) {
            this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(4D), this::isValidEntity).forEach(player -> this.setScaredTicks(100));
        }

        if (this.isScared()) {
            int scaredTicks = this.getBrain().hasMemoryValue(MemoryModuleType.AVOID_TARGET) ? 0 : this.getScaredTicks() - 1;
            this.getNavigation().stop();
            this.setScaredTicks(scaredTicks);
        }
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return pose == SpeciesPose.SCARED.get() ? SCARED_DIMENSIONS.scale(this.getScale()) : super.getDefaultDimensions(pose);
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isScared();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);

        if (this.getCrackedStage() > 0 && this.hasShell() && stack.getItem() == this.getOreItemStack().getItem() && !this.getBrain().hasMemoryValue(MemoryModuleType.AVOID_TARGET)) {
            this.setCrackedStage(this.getCrackedStage() - 1);
            this.playSound(this.getOreBlockState().getSoundType().getPlaceSound(), 1, 1);
            if (!player.getAbilities().instabuild) stack.shrink(1);
            this.setPersistenceRequired();
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, interactionHand);
    }

    @Override
    public boolean isPushable() {
        return !this.isScared();
    }

    public boolean isValidEntity(Player player) {
        Optional<ItemStack> stack = this.getStackInHand(player);
        return this.hasShell()
            && !player.isSpectator()
            && player.isAlive()
            && !player.getAbilities().instabuild
            && !player.isShiftKeyDown()
            || (this.hasShell() && stack.isPresent());
    }

    public boolean isValidEntityHoldingPickaxe(Player player) {
        return this.hasShell() && this.getStackInHand(player).isPresent();
    }

    public Optional<ItemStack> getStackInHand(Player player) {
        return player.getMainHandItem().isCorrectToolForDrops(this.getOreBlockState()) ? Optional.of(player.getMainHandItem()) : Optional.empty();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof LivingEntity && amount < 12 && !this.level().isClientSide && this.hasShell()) {
            if (source.getDirectEntity() instanceof Projectile projectile) projectile.setDeltaMovement(new Vec3(1, 1, 0));
            this.playSound(SpeciesSoundEvents.LIMPET_DEFLECT.get(), 1, 1);
            if (!this.getBrain().hasMemoryValue(MemoryModuleType.AVOID_TARGET)) this.setScaredTicks(300);
            return false;
        }

        this.tryBreaking(this.level(), source);
        return super.hurt(source, amount);
    }

    public void tryBreaking(Level level, DamageSource source) {
        boolean canBreak = this.hasShell();

        ItemStack stack = source.getWeaponItem();
        if (stack != null) canBreak = canBreak && stack.isCorrectToolForDrops(this.getOreBlockState());

        Entity entity = source.getEntity();
        if (entity instanceof Player player) canBreak = canBreak && !player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem());

        if (!canBreak) return;

        boolean hasOre = this.getOreData().getKey() != SpeciesLimpetOreData.SHELL;
        if (hasOre) this.spawnBreakingParticles();

        if (this.getCrackedStage() < 3) {
            if (entity instanceof LivingEntity livingEntity) {
                this.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, livingEntity, RETREAT_DURATION.sample(this.level().random));
            }
            this.setCrackedStage(this.getCrackedStage() + 1);
            this.playSound(this.getOreBlockState().getSoundType().getBreakSound(), 1, (float) this.getCrackedStage() * 0.3f + 0.5f);
            this.playSound(SpeciesSoundEvents.LIMPET_BREAK.get(), 0.6f, this.getCrackedStage() + 1);
            this.setScaredTicks(0);

            if (entity instanceof Player player && !player.isCreative()) {
                player.getCooldowns().addCooldown(stack.getItem(), 80);
            }

            return;
        }

        if (this.getMaxCount() > 0) {
            float multiplier = 1 + stack.getEnchantmentLevel(level.registryAccess().holderOrThrow(Enchantments.FORTUNE)) * 0.15F;
            int count = (int) ((this.getMaxCount() / 2F + random.nextInt(this.getMaxCount() / 2)) * multiplier);

            if (hasOre) for (int i = 0; i < count; i++) {
                this.spawnAtLocation(this.getOreItemStack().getItem(), 1);
            }
        }

        this.playSound(this.getOreBlockState().getSoundType().getBreakSound(), 1, (float) this.getCrackedStage() * 0.3F + 1);
        this.playSound(SpeciesSoundEvents.LIMPET_BREAK.get(), 0.6F, this.getCrackedStage() + 1.5F);
        this.setCrackedStage(0);
        if (stack != null && EnchantmentHelper.hasTag(stack, SpeciesTags.Enchantments.PREVENT_LIMPET_ORE_DROPS)) {
            if (!hasOre) this.setHasShell(false);
            if (entity instanceof ServerPlayer serverPlayer) SpeciesCriterion.SILK_TOUCH_BREAK_LIMPET.get().trigger(serverPlayer);
            return;
        } else {
            this.setHasShell(false);
            this.setScaredTicks(0);
        }
        if (entity instanceof ServerPlayer serverPlayer) SpeciesCriterion.BREAK_LIMPET.get().trigger(serverPlayer);
    }

    public void spawnBreakingParticles() {
        for (int i = 0; i < 40; ++i) this.level().addParticle(
            new ItemParticleOption(ParticleTypes.ITEM, this.getOreItemStack()),
            this.getX(), this.getY() + this.getBbHeight(), this.getZ(),
            ((double)this.random.nextFloat() - 0.5) * 0.5,
            ((double)this.random.nextFloat() - 0.5) * 0.8,
            ((double)this.random.nextFloat() - 0.5) * 0.5
        );
    }

    @Override
    public void travel(Vec3 vec3) {
        if (!this.getBrain().hasMemoryValue(MemoryModuleType.AVOID_TARGET) && this.isScared()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0, 1, 0));
            vec3 = vec3.multiply(0, 1, 0);
        }
        super.travel(vec3);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SpeciesSoundEvents.LIMPET_IDLE.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SpeciesSoundEvents.LIMPET_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SpeciesSoundEvents.LIMPET_HURT.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SpeciesSoundEvents.LIMPET_STEP.get(), 0.15F, 1);
    }

    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return -level.getPathfindingCostFromLightLevels(pos);
    }

    public static AttributeSupplier.Builder createLimpetAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 10)
            .add(Attributes.KNOCKBACK_RESISTANCE, 1)
            .add(Attributes.MOVEMENT_SPEED, 0.25F)
            .add(Attributes.STEP_HEIGHT, 1);
    }

    public static boolean canSpawn(EntityType<? extends PathfinderMob> type, ServerLevelAccessor level, MobSpawnType spawn, BlockPos pos, RandomSource random) {
        return level.getBrightness(LightLayer.BLOCK, pos) == 0
            && level.getBrightness(LightLayer.SKY, pos) == 0
            && level.getBlockState(pos.below()).is(SpeciesTags.Blocks.LIMPET_SPAWNABLE_ON)
            && level.getBlockState(pos.below()).isValidSpawn(level, pos, type);
    }

}
