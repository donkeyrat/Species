package com.ninni.species.server.entity.mob.update_2;

import com.ninni.species.registry.SpeciesDataMaps;
import com.ninni.species.registry.SpeciesEntities;
import com.ninni.species.registry.SpeciesItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class GooberGoo extends ThrowableItemProjectile {

    public GooberGoo(EntityType<? extends GooberGoo> type, Level world) {
        super(type, world);
    }

    public GooberGoo(Level world, double x, double y, double z) {
        super(SpeciesEntities.GOOBER_GOO.get(), x, y, z, world);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (hitResult.getType() != HitResult.Type.ENTITY) this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        Level level = this.level();
        if (level.isClientSide) return;

        BlockPos pos = result.getBlockPos();

        int xRadius = UniformInt.of(4, 7).sample(this.random);
        int yRange = 2;
        int zRadius = UniformInt.of(4, 7).sample(this.random);

        for (int x = -xRadius; x <= xRadius; x++) {
            for (int y = -yRange; y <= yRange; y++) {
                for (int z = -zRadius; z <= zRadius; z++) {
                    BlockPos placePos = BlockPos.containing(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    int distance = Math.max(1, Math.round(Mth.sqrt((float) pos.distSqr(placePos))));
                    if (x * x + z * z > xRadius * zRadius && this.random.nextInt(distance) != 0) continue;

                    BlockState state = level.getBlockState(placePos);
                    Block newBlock = state.getBlock().builtInRegistryHolder().getData(SpeciesDataMaps.Blocks.GOOBER_GOO_CONVERSION);
                    if (newBlock == null) continue;

                    BlockState aboveState = level.getBlockState(placePos.above());
                    if (aboveState.isAir() || aboveState.canBeReplaced()) {
                        this.replaceBlock(level, placePos, state, newBlock.defaultBlockState());
                    }
                }
            }
        }
    }

    public void replaceBlock(Level level, BlockPos pos, BlockState state, BlockState newState) {
        if (level.getRandom().nextFloat() < 0.35F) {
            level.levelEvent(2005, pos, 0);
        }
        if (state.getBlock() instanceof DoublePlantBlock && state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
            BlockState aboveState = level.getBlockState(pos.above());
            if (aboveState.getBlock() instanceof DoublePlantBlock && aboveState.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
                level.removeBlock(pos.above(), false);
            }
            DoublePlantBlock.placeAt(level, newState, pos, 2);
        } else {
            level.setBlock(pos, newState, 2);
        }
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        double xS = packet.getXa();
        double yS = packet.getYa();
        double zS = packet.getZa();
        for (int i = 0; i < 7; ++i) {
            double g = 0.4F + 0.1F * (double) i;
            this.level().addParticle(ParticleTypes.SPIT, this.getX(), this.getY(), this.getZ(), xS * g, yS, zS * g);
        }
        this.setDeltaMovement(xS, yS, zS);
    }

    @Override
    protected Item getDefaultItem() {
        return SpeciesItems.PETRIFIED_EGG.get();
    }

}
