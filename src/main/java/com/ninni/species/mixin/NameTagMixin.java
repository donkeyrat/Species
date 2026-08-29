package com.ninni.species.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.NameTagItem;
import org.spongepowered.asm.mixin.Mixin;

@Deprecated
@Mixin(NameTagItem.class)
public abstract class NameTagMixin extends Item {

    public NameTagMixin(Properties properties) {
        super(properties);
    }

}
