package com.cmdpro.datanessence.networking;

import com.cmdpro.datanessence.DataNEssence;
import com.cmdpro.datanessence.networking.packet.c2s.PlayerChangeDriveData;
import com.cmdpro.datanessence.networking.packet.c2s.PlayerFinishDataBankMinigame;
import com.cmdpro.datanessence.networking.packet.c2s.PlayerSetItemHandlerLocked;
import com.cmdpro.datanessence.networking.packet.s2c.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = DataNEssence.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModMessages {
    public class Handler {
        public static <T extends CustomPacketPayload> void handle(T message, IPayloadContext ctx) {
            if (message instanceof Message msg) {
                if (ctx.flow().getReceptionSide() == LogicalSide.SERVER) {
                    ctx.enqueueWork(() -> {
                        Server.handle(msg, ctx);
                    });
                } else {
                    ctx.enqueueWork(() -> {
                        Client.handle(msg, ctx);
                    });
                }
            }
        }
        public class Client {
            public static <T extends Message> void handle(T message, IPayloadContext ctx) {
                message.handleClient(Minecraft.getInstance(), Minecraft.getInstance().player);
            }
        }
        public class Server {
            public static <T extends Message> void handle(T message, IPayloadContext ctx) {
                message.handleServer(ctx.player().getServer(), (ServerPlayer)ctx.player());
            }
        }
        public abstract interface Reader<T extends Message> {
            public abstract T read(RegistryFriendlyByteBuf buf);
        }
        public abstract interface Writer<T extends Message> {
            public abstract void write(RegistryFriendlyByteBuf buf, T message);
        }
    }
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(DataNEssence.MOD_ID)
                .versioned("1.0");

        //S2C
        registrar.playToClient(UnlockEntry.TYPE, getNetworkCodec(UnlockEntry::read, UnlockEntry::write), Handler::handle);
        registrar.playToClient(UnlockedEntrySync.TYPE, getNetworkCodec(UnlockedEntrySync::read, UnlockedEntrySync::write), Handler::handle);
        registrar.playToClient(PlayerTierSync.TYPE, getNetworkCodec(PlayerTierSync::read, PlayerTierSync::write), Handler::handle);
        registrar.playToClient(PlayerDataSync.TYPE, getNetworkCodec(PlayerDataSync::read, PlayerDataSync::write), Handler::handle);
        registrar.playToClient(DataBankEntrySync.TYPE, getNetworkCodec(DataBankEntrySync::read, DataBankEntrySync::write), Handler::handle);
        registrar.playToClient(ComputerDataSync.TYPE, getNetworkCodec(ComputerDataSync::read, ComputerDataSync::write), Handler::handle);
        registrar.playToClient(EntrySync.TYPE, getNetworkCodec(EntrySync::read, EntrySync::write), Handler::handle);
        registrar.playToClient(DragonPartsSync.TYPE, getNetworkCodec(DragonPartsSync::read, DragonPartsSync::write), Handler::handle);
        registrar.playToClient(PlayBufferTransferParticle.TYPE, getNetworkCodec(PlayBufferTransferParticle::read, PlayBufferTransferParticle::write), Handler::handle);
        //C2S
        registrar.playToServer(PlayerFinishDataBankMinigame.TYPE, getNetworkCodec(PlayerFinishDataBankMinigame::read, PlayerFinishDataBankMinigame::write), Handler::handle);
        registrar.playToServer(PlayerChangeDriveData.TYPE, getNetworkCodec(PlayerChangeDriveData::read, PlayerChangeDriveData::write), Handler::handle);
        registrar.playToServer(PlayerSetItemHandlerLocked.TYPE, getNetworkCodec(PlayerSetItemHandlerLocked::read, PlayerSetItemHandlerLocked::write), Handler::handle);
    }

    public static <T extends Message> StreamCodec<RegistryFriendlyByteBuf, T> getNetworkCodec(Handler.Reader<T> reader, Handler.Writer<T> writer) {
        return StreamCodec.of(writer::write, reader::read);
    }

    public static <T extends Message> void sendToServer(T message) {
        PacketDistributor.sendToServer(message);
    }

    public static <T extends Message> void sendToPlayer(T message, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, message);
    }
    public static <T extends Message> void sendToPlayersTrackingEntityAndSelf(T message, ServerPlayer player) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, message);
    }
}
