package com.ninni.species.access;

import net.minecraft.world.item.DyeColor;

public interface BewereagerableEntity {

    boolean isBewereager();
    void setBewereager(boolean bewereager);

    boolean isCured();
    void setCured(boolean cured);

    void setCollarColor(DyeColor color);

}
