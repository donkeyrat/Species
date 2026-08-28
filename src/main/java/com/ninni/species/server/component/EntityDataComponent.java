package com.ninni.species.server.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.Arrays;
import java.util.List;

public record EntityDataComponent(Holder<EntityType<?>> type, CompoundTag data) {

	public static final EntityDataComponent EMPTY = new EntityDataComponent(null, null);

	static final List<String> IGNORED_TAGS = Arrays.asList(
		"Air", "ArmorDropChances", "ArmorItems", "Brain", "CanPickUpLoot", "DeathTime", "FallDistance", "FallFlying",
		"Fire", "HandDropChances", "HandItems", "HurtByTimestamp", "HurtTime", "LeftHanded", "Motion", "NoGravity",
		"OnGround", "PortalCooldown", "Pos", "Rotation", "SleepingX", "SleepingY", "SleepingZ", "CannotEnterHiveTicks",
		"TicksSincePollination", "CropsGrownSincePollination", "hive_pos", "Passengers", "leash", "UUID"
	);

	public static final Codec<EntityDataComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		BuiltInRegistries.ENTITY_TYPE.holderByNameCodec().fieldOf("type").forGetter(EntityDataComponent::type),
		CompoundTag.CODEC.fieldOf("data").forGetter(EntityDataComponent::data)
	).apply(instance, EntityDataComponent::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, EntityDataComponent> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.holderRegistry(Registries.ENTITY_TYPE), EntityDataComponent::type,
		ByteBufCodecs.COMPOUND_TAG, EntityDataComponent::data,
		EntityDataComponent::new
	);

	public static EntityDataComponent fromEntity(Entity entity) {
		CompoundTag tag = entity.saveWithoutId(new CompoundTag());
		IGNORED_TAGS.forEach(tag::remove);
		return new EntityDataComponent(entity.getType().builtInRegistryHolder(), tag);
	}

	public boolean isEmpty() {
		return this == EMPTY;
	}

}
