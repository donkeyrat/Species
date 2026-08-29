package com.ninni.species.registry;

import com.ninni.species.server.entity.mob.update_3.DeflectorDummy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class SpeciesDispenserBehaviour {

	static final DispenseItemBehavior DISPENSE_BUCKET_BEHAVIOUR = new DefaultDispenseItemBehavior() {

		private final DefaultDispenseItemBehavior defaultBehaviour = new DefaultDispenseItemBehavior();

		public ItemStack execute(BlockSource source, ItemStack stack) {
			DispensibleContainerItem item = (DispensibleContainerItem) stack.getItem();
			Level level = source.level();
			BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
			if (item.emptyContents(null, level, pos, null, stack)) {
				item.checkExtraContent(null, level, stack, pos);
				return new ItemStack(Items.BUCKET);
			}
			return this.defaultBehaviour.dispense(source, stack);
		}

	};


	static final DispenseItemBehavior DISPENSE_DEFLECTOR_DUMMY_BEHAVIOUR = new DefaultDispenseItemBehavior() {

		public ItemStack execute(BlockSource source, ItemStack stack) {
			Direction direction = source.state().getValue(DispenserBlock.FACING);
			BlockPos pos = source.pos().relative(direction);
			ServerLevel level = source.level();
			DeflectorDummy dummy = SpeciesEntities.DEFLECTOR_DUMMY.get().spawn(level, stack, null, pos, MobSpawnType.DISPENSER, false, false);
			if (dummy != null) stack.shrink(1);

			return stack;
		}

	};


}
