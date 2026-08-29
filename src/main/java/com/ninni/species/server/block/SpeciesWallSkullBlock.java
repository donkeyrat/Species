package com.ninni.species.server.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.ninni.species.registry.SpeciesBlocks;
import com.ninni.species.registry.SpeciesSkullTypes;
import com.ninni.species.server.block.entity.SpeciesSkullBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class SpeciesWallSkullBlock extends WallSkullBlock {

	private static final Map<Direction, VoxelShape> GHOUL_AABBS = Maps.newEnumMap(
		ImmutableMap.of(
			Direction.NORTH, Block.box(2, 4, 8, 14, 11, 16),
			Direction.SOUTH, Block.box(2, 4, 0, 14, 11, 8),
			Direction.EAST, Block.box(0, 4, 2, 8, 11, 14),
			Direction.WEST, Block.box(8, 4, 2, 16, 11, 14)
		)
	);
	private static final Map<Direction, VoxelShape> WICKED_AABBS = Maps.newEnumMap(
		ImmutableMap.of(
			Direction.NORTH, Block.box(4, 4, 8, 12, 10, 16),
			Direction.SOUTH, Block.box(4, 4, 0, 12, 10, 8),
			Direction.EAST, Block.box(0, 4, 4, 8, 10, 12),
			Direction.WEST, Block.box(8, 4, 4, 16, 10, 12)
		)
	);
	private static final Map<Direction, VoxelShape> QUAKE_AABBS = Maps.newEnumMap(
		ImmutableMap.of(
			Direction.NORTH, Block.box(1.5F, 4, 6, 14.5F, 11, 16),
			Direction.SOUTH, Block.box(1.5F, 4, 0, 14.5F, 11, 10),
			Direction.EAST, Block.box(0, 4, 1.5F, 10, 11, 14.5F),
			Direction.WEST, Block.box(6, 4, 1.5F, 16, 11, 14.5F)
		)
	);
	private static final Map<Direction, VoxelShape> BEWEREAGER_AABBS = Maps.newEnumMap(
		ImmutableMap.of(
			Direction.NORTH, Block.box(3, 3, 6, 13, 13, 16),
			Direction.SOUTH, Block.box(3, 3, 0, 13, 13, 10),
			Direction.EAST, Block.box(0, 3, 3, 10, 13, 13),
			Direction.WEST, Block.box(6, 3, 3, 16, 13, 13)
		)
	);

	public SpeciesWallSkullBlock(SkullBlock.Type type, Properties properties) {
		super(type, properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SpeciesSkullBlockEntity(pos, state);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter g, BlockPos p, CollisionContext c) {
		Direction facing = state.getValue(FACING);
		return switch (this.getType()) {
			case SpeciesSkullTypes.WICKED -> WICKED_AABBS.get(facing);
			case SpeciesSkullTypes.GHOUL -> GHOUL_AABBS.get(facing);
			case SpeciesSkullTypes.BEWEREAGER -> BEWEREAGER_AABBS.get(facing);
			default -> QUAKE_AABBS.get(facing);
		};
	}

}
