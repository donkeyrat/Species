package com.ninni.species.server.block;

import com.ninni.species.registry.SpeciesBlocks;
import com.ninni.species.registry.SpeciesSkullTypes;
import com.ninni.species.server.block.entity.SpeciesSkullBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SpeciesSkullBlock extends SkullBlock {

	protected static final VoxelShape GHOUL_SHAPE = Block.box(2, 0, 2, 14, 7, 14);
	protected static final VoxelShape WICKED_SHAPE = Block.box(4, 0, 4, 12, 6, 12);
	protected static final VoxelShape QUAKE_SHAPE = Block.box(1.5F, 0, 1.5F, 14.5F, 7, 14.5F);
	protected static final VoxelShape BEWEREAGER_SHAPE = Block.box(3, 0, 3, 13, 10, 13);

	public SpeciesSkullBlock(Type type, Properties properties) {
		super(type, properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SpeciesSkullBlockEntity(pos, state);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter g, BlockPos p, CollisionContext c) {
		return switch (this.getType()) {
			case SpeciesSkullTypes.WICKED -> WICKED_SHAPE;
			case SpeciesSkullTypes.GHOUL -> GHOUL_SHAPE;
			case SpeciesSkullTypes.BEWEREAGER -> BEWEREAGER_SHAPE;
			default -> QUAKE_SHAPE;
		};
	}

}
