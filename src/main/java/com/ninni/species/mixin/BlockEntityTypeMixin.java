package com.ninni.species.mixin;

import com.ninni.species.registry.SpeciesBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {

    @Inject(method = "isValid", at = @At("RETURN"), cancellable = true)
    private void S$isValid(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (BlockEntityType.BRUSHABLE_BLOCK.equals(this) && state.is(SpeciesBlocks.RED_SUSPICIOUS_SAND.get())) {
            cir.setReturnValue(true);
        }
    }

}