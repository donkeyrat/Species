package com.ninni.species.registry;

import com.ninni.species.Species;
import com.ninni.species.server.packet.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Species.MOD_ID)
public class SpeciesNetwork {

    @SubscribeEvent
    public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(Species.MOD_ID);
        registrar.playToClient(SendSpringlingPacket.TYPE, SendSpringlingPacket.STREAM_CODEC, SendSpringlingPacket::handle);
        registrar.playToClient(OpenCruncherScreenPacket.TYPE, OpenCruncherScreenPacket.STREAM_CODEC, OpenCruncherScreenPacket::handle);
        registrar.playToClient(PlayGutFeelingSoundPacket.TYPE, PlayGutFeelingSoundPacket.STREAM_CODEC, PlayGutFeelingSoundPacket::handle);
        registrar.playToServer(UpdateSpringlingDataPacket.TYPE, UpdateSpringlingDataPacket.STREAM_CODEC, UpdateSpringlingDataPacket::handle);
        registrar.playToClient(SnatchedPacket.TYPE, SnatchedPacket.STREAM_CODEC, SnatchedPacket::handle);
        registrar.playToClient(TankedPacket.TYPE, TankedPacket.STREAM_CODEC, TankedPacket::handle);
        registrar.playToClient(BlockEntitySyncPacket.TYPE, BlockEntitySyncPacket.STREAM_CODEC, BlockEntitySyncPacket::handle);
        registrar.playToServer(HarpoonInputPacket.TYPE, HarpoonInputPacket.STREAM_CODEC, HarpoonInputPacket::handle);
        registrar.playToClient(HarpoonSyncPacket.TYPE, HarpoonSyncPacket.STREAM_CODEC, HarpoonSyncPacket::handle);
        registrar.playToServer(UpdateBirtdayCakeDataPacket.TYPE, UpdateBirtdayCakeDataPacket.STREAM_CODEC, UpdateBirtdayCakeDataPacket::handle);
    }

}
