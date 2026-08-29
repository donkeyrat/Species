package com.ninni.species.access;

import net.minecraft.world.inventory.AbstractContainerMenu;

public interface ContainerCountingEntity {

    int getContainerCounter();
    void getNextContainerCounter();

    void doInitMenu(AbstractContainerMenu menu);

}
