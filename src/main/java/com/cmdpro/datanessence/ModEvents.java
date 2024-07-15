package com.cmdpro.datanessence;

import com.cmdpro.datanessence.api.BaseCapabilityPoint;
import com.cmdpro.datanessence.api.BaseEssencePoint;
import com.cmdpro.datanessence.api.DataNEssenceUtil;
import com.cmdpro.datanessence.block.TraversiteRoad;
import com.cmdpro.datanessence.computers.ComputerTypeManager;
import com.cmdpro.datanessence.hiddenblocks.HiddenBlocksManager;
import com.cmdpro.datanessence.networking.ModMessages;
import com.cmdpro.datanessence.networking.packet.EntrySyncS2CPacket;
import com.cmdpro.datanessence.networking.packet.HiddenBlockSyncS2CPacket;
import com.cmdpro.datanessence.registry.AttachmentTypeRegistry;
import com.cmdpro.datanessence.screen.databank.DataBankEntryManager;
import com.cmdpro.datanessence.screen.databank.DataBankTypeManager;
import com.cmdpro.datanessence.screen.datatablet.DataTabManager;
import com.cmdpro.datanessence.screen.datatablet.Entries;
import com.cmdpro.datanessence.screen.datatablet.EntryManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Optional;

@EventBusSubscriber(modid = DataNEssence.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onLivingEntityTick(EntityTickEvent event) {
        if (event.getEntity() instanceof LivingEntity ent) {
            if (event.getEntity().getBlockStateOn().getBlock() instanceof TraversiteRoad road) {
                if (ent.getAttribute(Attributes.MOVEMENT_SPEED).getModifier(TraversiteRoad.TRAVERSITE_ROAD_SPEED_UUID) == null) {
                    ent.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(new AttributeModifier(TraversiteRoad.TRAVERSITE_ROAD_SPEED_UUID, "Traversite Road Speed", road.boost, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                } else {
                    if (ent.getAttribute(Attributes.MOVEMENT_SPEED).getModifier(TraversiteRoad.TRAVERSITE_ROAD_SPEED_UUID).amount() != road.boost) {
                        ent.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(TraversiteRoad.TRAVERSITE_ROAD_SPEED_UUID);
                        ent.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(new AttributeModifier(TraversiteRoad.TRAVERSITE_ROAD_SPEED_UUID, "Traversite Road Speed", road.boost, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                    }
                }
            } else {
                ent.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(TraversiteRoad.TRAVERSITE_ROAD_SPEED_UUID);
            }
        }
    }
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent event) {
        if (!event.getEntity().level().isClientSide) {
            Optional<BlockEntity> ent = event.getEntity().getData(AttachmentTypeRegistry.LINK_FROM);
            if (ent.isPresent()) {
                if (ent.get().getBlockPos().getCenter().distanceTo(event.getEntity().getBoundingBox().getCenter()) > 20) {
                    event.getEntity().setData(AttachmentTypeRegistry.LINK_FROM, Optional.empty());
                    DataNEssenceUtil.PlayerDataUtil.updateData((ServerPlayer)event.getEntity());
                } else {
                    if (ent.get().getBlockState().getBlock() instanceof BaseEssencePoint block) {
                        if (!event.getEntity().isHolding(block.getRequiredWire())) {
                            event.getEntity().setData(AttachmentTypeRegistry.LINK_FROM, Optional.empty());
                            DataNEssenceUtil.PlayerDataUtil.updateData((ServerPlayer) event.getEntity());
                        }
                    }
                    if (ent.get().getBlockState().getBlock() instanceof BaseCapabilityPoint block) {
                        if (!event.getEntity().isHolding(block.getRequiredWire())) {
                            event.getEntity().setData(AttachmentTypeRegistry.LINK_FROM, Optional.empty());
                            DataNEssenceUtil.PlayerDataUtil.updateData((ServerPlayer) event.getEntity());
                        }
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public static void addReloadListenerEvent(AddReloadListenerEvent event) {
        event.addListener(EntryManager.getOrCreateInstance());
        event.addListener(DataTabManager.getOrCreateInstance());
        event.addListener(DataBankEntryManager.getOrCreateInstance());
        event.addListener(DataBankTypeManager.getOrCreateInstance());
        event.addListener(HiddenBlocksManager.getOrCreateInstance());
        event.addListener(ComputerTypeManager.getOrCreateInstance());
    }
    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        if (event.getPlayer() == null) {
            for (ServerPlayer player : event.getPlayerList().getPlayers()) {
                syncToPlayer(player);
            }
        } else {
            syncToPlayer(event.getPlayer());
        }
    }
    protected static void syncToPlayer(ServerPlayer player) {
        ModMessages.sendToPlayer(new EntrySyncS2CPacket(Entries.entries, Entries.tabs), player);
        ModMessages.sendToPlayer(new HiddenBlockSyncS2CPacket(HiddenBlocksManager.blocks), player);
    }
    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if(!event.getLevel().isClientSide()) {
            if(event.getEntity() instanceof ServerPlayer player) {
                DataNEssenceUtil.PlayerDataUtil.updateData(player);
                DataNEssenceUtil.PlayerDataUtil.updateUnlockedEntries(player);
                DataNEssenceUtil.PlayerDataUtil.sendTier(player, false);
            }
        }
    }
}
