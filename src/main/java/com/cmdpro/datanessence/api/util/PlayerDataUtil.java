package com.cmdpro.datanessence.api.util;

import com.cmdpro.databank.DatabankUtils;
import com.cmdpro.datanessence.api.node.block.BaseCapabilityPointBlockEntity;
import com.cmdpro.datanessence.api.node.block.BaseEssencePointBlockEntity;
import com.cmdpro.datanessence.networking.ModMessages;
import com.cmdpro.datanessence.networking.packet.PlayerDataSyncS2CPacket;
import com.cmdpro.datanessence.networking.packet.PlayerTierSyncS2CPacket;
import com.cmdpro.datanessence.networking.packet.UnlockEntryS2CPacket;
import com.cmdpro.datanessence.networking.packet.UnlockedEntrySyncS2CPacket;
import com.cmdpro.datanessence.registry.AttachmentTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.awt.*;
import java.util.Map;
import java.util.Optional;

public class PlayerDataUtil {
    public static void updateData(ServerPlayer player) {
        Color linkColor = new Color(0, 0, 0, 0);
        Optional<BlockEntity> linkFromEntity = player.getData(AttachmentTypeRegistry.LINK_FROM);
        BlockPos linkFrom = null;
        if (linkFromEntity.isPresent()) {
            linkFrom = linkFromEntity.get().getBlockPos();
            if (linkFromEntity.get() instanceof BaseEssencePointBlockEntity linkFrom2) {
                linkColor = linkFrom2.linkColor();
            } else if (linkFromEntity.get() instanceof BaseCapabilityPointBlockEntity linkFrom2) {
                linkColor = linkFrom2.linkColor();
            }
        }
        ModMessages.sendToPlayer(new PlayerDataSyncS2CPacket(PlayerDataUtil.getUnlockedEssences(player), linkFrom, linkColor), (player));
    }

    public static void updateUnlockedEntries(ServerPlayer player) {
        ModMessages.sendToPlayer(new UnlockedEntrySyncS2CPacket(player.getData(AttachmentTypeRegistry.UNLOCKED), player.getData(AttachmentTypeRegistry.INCOMPLETE)), (player));
        DatabankUtils.updateHiddenBlocks(player);
    }

    public static void unlockEntry(ServerPlayer player, ResourceLocation entry, boolean incomplete) {
        ModMessages.sendToPlayer(new UnlockEntryS2CPacket(entry, incomplete), (player));
        DatabankUtils.updateHiddenBlocks(player);
    }

    public static void sendTier(ServerPlayer player, boolean showIndicator) {
        ModMessages.sendToPlayer(new PlayerTierSyncS2CPacket(player.getData(AttachmentTypeRegistry.TIER), showIndicator), player);
    }

    public static Map<ResourceLocation, Boolean> getUnlockedEssences(ServerPlayer player) {
        return player.getData(AttachmentTypeRegistry.UNLOCKED_ESSENCES);
    }
}
