package com.cmdpro.datanessence.screen.databank;

import com.cmdpro.datanessence.api.DataNEssenceRegistries;
import com.cmdpro.datanessence.api.databank.MinigameCreator;
import com.cmdpro.datanessence.api.databank.MinigameSerializer;
import com.cmdpro.datanessence.api.datatablet.Page;
import com.cmdpro.datanessence.api.datatablet.PageSerializer;
import com.cmdpro.datanessence.screen.datatablet.Entry;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataBankEntrySerializer {
    public DataBankEntry read(ResourceLocation entryId, JsonObject json) {
        DataBankEntry entry = CODEC.codec().parse(JsonOps.INSTANCE, json).getOrThrow();
        entry.id = entryId;
        return entry;
    }
    public static final Codec<MinigameCreator> MINIGAME_CODEC = DataNEssenceRegistries.MINIGAME_TYPE_REGISTRY.byNameCodec().dispatch(MinigameCreator::getSerializer, minigameSerializer -> minigameSerializer.getCodec());
    public static final StreamCodec<RegistryFriendlyByteBuf, MinigameCreator> MINIGAME_STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {
        pBuffer.writeResourceLocation(DataNEssenceRegistries.MINIGAME_TYPE_REGISTRY.getKey(pValue.getSerializer()));
        pValue.getSerializer().getStreamCodec().encode(pBuffer, pValue);
    }, pBuffer -> {
        ResourceLocation type = pBuffer.readResourceLocation();
        MinigameSerializer minigameSerializer = DataNEssenceRegistries.MINIGAME_TYPE_REGISTRY.get(type);
        MinigameCreator page = (MinigameCreator)minigameSerializer.getStreamCodec().decode(pBuffer);
        return page;
    });
    public static final MapCodec<DataBankEntry> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            ItemStack.CODEC.fieldOf("icon").forGetter((entry) -> entry.icon),
            Codec.INT.fieldOf("tier").forGetter((entry) -> entry.tier),
            ComponentSerialization.CODEC.fieldOf("name").forGetter((entry) -> entry.name),
            MINIGAME_CODEC.listOf().fieldOf("minigames").forGetter((entry) -> List.of(entry.minigames)),
            ResourceLocation.CODEC.fieldOf("entry").forGetter((entry) -> entry.entry)
    ).apply(instance, (icon, tier, name, minigames, entry) -> new DataBankEntry(null, icon, tier, minigames.toArray(new MinigameCreator[0]), name, entry)));
    public static final StreamCodec<RegistryFriendlyByteBuf, DataBankEntry> STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {
        pBuffer.writeResourceLocation(pValue.id);
        pBuffer.writeWithCodec(NbtOps.INSTANCE, ItemStack.CODEC, pValue.icon);
        pBuffer.writeWithCodec(NbtOps.INSTANCE, ComponentSerialization.CODEC, pValue.name);
        pBuffer.writeInt(pValue.tier);
        pBuffer.writeCollection(Arrays.stream(pValue.minigames).toList(), (pBuffer1, pValue1) -> MINIGAME_STREAM_CODEC.encode((RegistryFriendlyByteBuf)pBuffer1, pValue1));
        pBuffer.writeResourceLocation(pValue.entry);
    }, pBuffer -> {
        ResourceLocation id = pBuffer.readResourceLocation();
        ItemStack icon = pBuffer.readWithCodecTrusted(NbtOps.INSTANCE, ItemStack.CODEC);
        Component name = pBuffer.readWithCodecTrusted(NbtOps.INSTANCE, ComponentSerialization.CODEC);
        int tier = pBuffer.readInt();
        MinigameCreator[] minigames = pBuffer.readList((pBuffer1) -> MINIGAME_STREAM_CODEC.decode((RegistryFriendlyByteBuf)pBuffer1)).toArray(new MinigameCreator[0]);
        ResourceLocation entry2 = pBuffer.readResourceLocation();
        DataBankEntry entry = new DataBankEntry(id, icon, tier, minigames, name, entry2);
        return entry;
    });
}