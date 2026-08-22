package com.ninni.species.registry;

import net.minecraft.world.level.block.SkullBlock;
import org.jetbrains.annotations.NotNull;

public enum SpeciesSkullTypes implements SkullBlock.Type  {

	GHOUL("ghoul"),
	WICKED("wicked"),
	QUAKE("quake"),
	BEWEREAGER("bewereager");

	private final String name;

	SpeciesSkullTypes(String name) {
		this.name = name;
		SkullBlock.Type.TYPES.put(name, this);
	}

	@Override
	public @NotNull String getSerializedName() {
		return this.name;
	}

}
