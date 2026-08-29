package com.ninni.species.server.packet;

import com.ninni.species.Species;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class BlockEntitySyncPacket implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<BlockEntitySyncPacket> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(Species.MOD_ID, "block_entity_sync"));
    public static final StreamCodec<FriendlyByteBuf, BlockEntitySyncPacket> STREAM_CODEC = StreamCodec.of(BlockEntitySyncPacket::write, BlockEntitySyncPacket::read);
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    final BlockPos pos;
    final CompoundTag tag;

    public BlockEntitySyncPacket(BlockPos pos, CompoundTag tag) {
        this.pos = pos;
        this.tag = tag;
    }

    public static void write(FriendlyByteBuf buffer, BlockEntitySyncPacket object) {
        buffer.writeBlockPos(object.pos);
        buffer.writeNbt(object.tag);
    }

    public static BlockEntitySyncPacket read(FriendlyByteBuf buffer) {
        return new BlockEntitySyncPacket(buffer.readBlockPos(), buffer.readNbt());
    }

    public static void handle(BlockEntitySyncPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = context.player().level();
            BlockEntity entity = level.getBlockEntity(packet.pos);
            if (entity != null) {
                entity.loadCustomOnly(packet.tag, level.registryAccess());
                entity.setChanged();
            }
        });
    }

}