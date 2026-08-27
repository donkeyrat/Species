package com.ninni.species.server.block.entity;

import com.mojang.serialization.Dynamic;
import com.ninni.species.Species;
import com.ninni.species.registry.SpeciesBlockEntities;
import com.ninni.species.server.CruncherHunting;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CruncherPelletBlockEntity extends BlockEntity {

    @Nullable private Holder<CruncherHunting> data = null;

    public CruncherPelletBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpeciesBlockEntities.CRUNCHER_PELLET.get(), blockPos, blockState);
    }

    @Override
    public void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        if (compoundTag.contains("PelletData", 10)) {
            CruncherHunting.HOLDER_CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, compoundTag.getCompound("PelletData"))).resultOrPartial(Species.LOGGER::error).ifPresent(this::setPelletData);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        if (this.getPelletData() != null) {
            CruncherHunting.HOLDER_CODEC.encodeStart(NbtOps.INSTANCE, this.getPelletData()).resultOrPartial(Species.LOGGER::error).ifPresent(tag -> compoundTag.put("PelletData", tag));
        }
    }

    public Holder<CruncherHunting> getPelletData() {
        return this.data;
    }

    public void setPelletData(Holder<CruncherHunting> data) {
        this.data = data;
    }

    public void unpackLootTable(Player player) {
        if (this.level == null || this.level.isClientSide || this.level.getServer() == null) return;

        Holder<CruncherHunting> pelletData = this.getPelletData();

        if (pelletData == null) return;

        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.GENERATE_LOOT.trigger(serverPlayer, pelletData.value().entityType().getDefaultLootTable());
        }

        int count = UniformInt.of(pelletData.value().minTries(), pelletData.value().maxTries()).sample(player.getRandom());

        for (int i = 0; i < count; i++) {
            ObjectArrayList<ItemStack> randomDrops = this.getRandomDrops(player);
            randomDrops.forEach(this::spawnItem);
        }

        this.setChanged();
    }

    private void spawnItem(ItemStack itemStack) {
        Block.popResource(this.level, this.worldPosition, itemStack);
    }

    public ObjectArrayList<ItemStack> getRandomDrops(Player player) {
        Holder<CruncherHunting> data = this.getPelletData();
        var key = data.value().entityType().getDefaultLootTable();
        LootTable lootTable = this.level.getServer().reloadableRegistries().getLootTable(key);
        Entity entity = data.value().entityType().create(this.level);

        DamageSource damageSource;

        if (entity instanceof LivingEntity livingEntity) {
            damageSource = this.level.damageSources().mobAttack(livingEntity);
        } else {
            damageSource = this.level.damageSources().fellOutOfWorld();
        }

        LootParams.Builder builder = new LootParams.Builder((ServerLevel)this.level)
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.worldPosition))
                .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, player)
                .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, player)
                .withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player)
                .withLuck(player.getLuck());

        LootParams lootParams = builder.create(LootContextParamSets.ENTITY);

        return lootTable.getRandomItems(lootParams);
    }

}