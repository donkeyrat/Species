package com.ninni.species.access;

import net.minecraft.world.level.block.state.BlockState;

public interface AccessibleFallingBlockEntity {

    BlockState getBlockState();
    void setBlockState(BlockState state);

}
