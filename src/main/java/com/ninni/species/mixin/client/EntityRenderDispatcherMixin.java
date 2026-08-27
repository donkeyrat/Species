package com.ninni.species.mixin.client;

import com.ninni.species.mixin_util.EntityRenderDispatcherAccess;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin implements EntityRenderDispatcherAccess {

    @Unique private boolean isRenderingInventoryEntity = false;

    @Override
    public boolean getRenderingInventoryEntity() {
        return this.isRenderingInventoryEntity;
    }

    @Override
    public void setIsRenderingInventoryEntity(boolean rendering) {
        this.isRenderingInventoryEntity = rendering;
    }

}
