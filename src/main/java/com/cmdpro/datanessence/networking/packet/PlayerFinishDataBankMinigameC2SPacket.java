package com.cmdpro.datanessence.networking.packet;

import com.cmdpro.datanessence.DataNEssence;
import com.cmdpro.datanessence.api.DataNEssenceUtil;
import com.cmdpro.datanessence.networking.Message;
import com.cmdpro.datanessence.registry.AttachmentTypeRegistry;
import com.cmdpro.datanessence.screen.databank.DataBankEntries;
import com.cmdpro.datanessence.screen.databank.DataBankEntry;
import com.cmdpro.datanessence.screen.datatablet.Entries;
import com.cmdpro.datanessence.screen.datatablet.Entry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public record PlayerFinishDataBankMinigameC2SPacket(ResourceLocation entry) implements Message {

    public static PlayerFinishDataBankMinigameC2SPacket read(RegistryFriendlyByteBuf buf) {
        ResourceLocation entry = buf.readResourceLocation();
        return new PlayerFinishDataBankMinigameC2SPacket(entry);
    }

    @Override
    public void handleServer(MinecraftServer server, ServerPlayer player) {
        DataBankEntry entry2 = DataBankEntries.entries.get(entry);
        if (entry2 != null) {
            if (entry2.tier <= player.getData(AttachmentTypeRegistry.TIER)) {
                Entry entry3 = Entries.entries.get(entry2.entry);
                DataNEssenceUtil.DataTabletUtil.unlockEntry(player, entry2.entry, entry3.incomplete);
            }
        }
    }

    public static void write(RegistryFriendlyByteBuf buf, PlayerFinishDataBankMinigameC2SPacket obj) {
        buf.writeResourceLocation(obj.entry);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static final Type<PlayerFinishDataBankMinigameC2SPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(DataNEssence.MOD_ID, "player_finish_data_bank_minigame"));
}