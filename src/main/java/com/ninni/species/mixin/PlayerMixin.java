package com.ninni.species.mixin;

import com.ninni.species.access.HarpooningEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements HarpooningEntity {

    @Unique private int harpoonId;

    protected PlayerMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public int getHarpoonId() {
        return this.harpoonId;
    }

    @Override
    public void setHarpoonId(int id) {
        this.harpoonId = id;
    }

}
