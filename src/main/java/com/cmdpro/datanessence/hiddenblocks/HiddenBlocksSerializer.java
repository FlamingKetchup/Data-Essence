package com.cmdpro.datanessence.hiddenblocks;

import com.cmdpro.datanessence.DataNEssence;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HiddenBlocksSerializer {
    public HiddenBlock read(ResourceLocation entryId, JsonObject json) {
        if (!json.has("entry")) {
            throw new JsonSyntaxException("Element entry missing in hidden block JSON for " + entryId.toString());
        }
        if (!json.has("hiddenAs")) {
            throw new JsonSyntaxException("Element hiddenAs missing in hidden block JSON for " + entryId.toString());
        }
        if (!json.has("originalBlock")) {
            throw new JsonSyntaxException("Element originalBlock missing in hidden block JSON for " + entryId.toString());
        }
        ResourceLocation entry = ResourceLocation.tryParse(json.get("entry").getAsString());
        BlockState hiddenAs = null;
        Block originalBlock = BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(json.get("originalBlock").getAsString()));
        try {
            hiddenAs = BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), json.get("hiddenAs").getAsString(), false).blockState();
        } catch (Exception e) {
            DataNEssence.LOGGER.error(e.getMessage());
        }
        boolean completionRequired = GsonHelper.getAsBoolean(json, "completionRequired", true);
        return new HiddenBlock(entry, originalBlock, hiddenAs, completionRequired);
    }
    @Nonnull
    public static HiddenBlock fromNetwork(FriendlyByteBuf buf) {
        ResourceLocation entry = buf.readResourceLocation();
        BlockState hiddenAs = null;
        String originalBlockString = buf.readUtf();
        String hiddenAsString = buf.readUtf();
        Block originalBlock = BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(originalBlockString));
        try {
            hiddenAs = BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), hiddenAsString, false).blockState();
        } catch (Exception e) {
            DataNEssence.LOGGER.error(e.getMessage());
        }
        boolean completionRequired = buf.readBoolean();
        return new HiddenBlock(entry, originalBlock, hiddenAs, completionRequired);
    }
    public static void toNetwork(FriendlyByteBuf buf, HiddenBlock block) {
        buf.writeResourceLocation(block.entry);
        buf.writeResourceLocation(BuiltInRegistries.BLOCK.getKey(block.originalBlock));
        buf.writeUtf(BlockStateParser.serialize(block.hiddenAs));
        buf.writeBoolean(block.completionRequired);
    }
}