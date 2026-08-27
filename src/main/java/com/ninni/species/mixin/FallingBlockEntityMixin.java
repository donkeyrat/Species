package com.ninni.species.mixin;

import com.ninni.species.access.AccessibleFallingBlockEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FallingBlockEntity.class)
public class FallingBlockEntityMixin implements AccessibleFallingBlockEntity {

    @Shadow private BlockState blockState;

    @Override
    public BlockState getBlockState() {
        return this.blockState;
    }

    @Override
    public void setBlockState(BlockState state) {
        this.blockState = state;
    }

}
