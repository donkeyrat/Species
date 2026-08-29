package com.ninni.species.server.packet;

import com.ninni.species.Species;
import com.ninni.species.access.HarpooningEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class HarpoonSyncPacket implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<HarpoonSyncPacket> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(Species.MOD_ID, "harpoon_sync"));
    public static final StreamCodec<FriendlyByteBuf, HarpoonSyncPacket> STREAM_CODEC = StreamCodec.of(HarpoonSyncPacket::write, HarpoonSyncPacket::read);
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private final int harpoonId;

    public HarpoonSyncPacket(int harpoonId) {
        this.harpoonId = harpoonId;
    }

    public static void write(FriendlyByteBuf buf, HarpoonSyncPacket msg) {
        buf.writeInt(msg.harpoonId);
    }

    public static HarpoonSyncPacket read(FriendlyByteBuf buf) {
        return new HarpoonSyncPacket(buf.readInt());
    }

    public static void handle(HarpoonSyncPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if (player instanceof HarpooningEntity playerAccess) {
                playerAccess.setHarpoonId(packet.harpoonId);
            }
        });
    }
}
