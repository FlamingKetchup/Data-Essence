package com.cmdpro.datanessence.screen.datatablet;

import com.cmdpro.datanessence.api.DataNEssenceRegistries;
import com.cmdpro.datanessence.api.datatablet.Page;
import com.cmdpro.datanessence.api.datatablet.PageSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.*;
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
import java.util.function.Function;

public class EntrySerializer {
    public Entry read(ResourceLocation entryId, JsonObject json) {
        Entry entry = CODEC.codec().parse(JsonOps.INSTANCE, json).getOrThrow();
        entry.id = entryId;
        return entry;
    }
    public static final Codec<Page> PAGE_CODEC = DataNEssenceRegistries.PAGE_TYPE_REGISTRY.byNameCodec().dispatch(Page::getSerializer, pageSerializer -> pageSerializer.getCodec());
    public static final StreamCodec<RegistryFriendlyByteBuf, Page> PAGE_STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {
        pBuffer.writeResourceLocation(DataNEssenceRegistries.PAGE_TYPE_REGISTRY.getKey(pValue.getSerializer()));
        pValue.getSerializer().getStreamCodec().encode(pBuffer, pValue);
    }, pBuffer -> {
        ResourceLocation type = pBuffer.readResourceLocation();
        PageSerializer pageSerializer = DataNEssenceRegistries.PAGE_TYPE_REGISTRY.get(type);
        Page page = (Page)pageSerializer.getStreamCodec().decode(pBuffer);
        return page;
    });
    public static final MapCodec<Entry> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            ItemStack.CODEC.fieldOf("icon").forGetter((entry) -> entry.icon),
            Codec.INT.fieldOf("x").forGetter((entry) -> entry.x),
            Codec.INT.fieldOf("y").forGetter((entry) -> entry.y),
            ComponentSerialization.CODEC.fieldOf("name").forGetter((entry) -> entry.name),
            ComponentSerialization.CODEC.optionalFieldOf("flavor", Component.empty()).forGetter((entry -> entry.flavor)),
            ResourceLocation.CODEC.listOf().optionalFieldOf("parents", new ArrayList<>()).forGetter((entry) -> Arrays.stream(entry.parents).toList()),
            PAGE_CODEC.listOf().fieldOf("pages").forGetter((entry) -> List.of(entry.pages)),
            Codec.BOOL.optionalFieldOf("critical", false).forGetter((entry) -> entry.critical),
            ResourceLocation.CODEC.fieldOf("tab").forGetter((entry) -> entry.tab),
            Codec.BOOL.optionalFieldOf("incomplete", false).forGetter((entry) -> entry.incomplete),
            PAGE_CODEC.listOf().optionalFieldOf("incompletePages", new ArrayList<>()).forGetter((entry) -> Arrays.stream(entry.incompletePages).toList()),
            ResourceLocation.CODEC.optionalFieldOf("completionAdvancement", ResourceLocation.fromNamespaceAndPath("", "")).forGetter((entry) -> entry.completionAdvancement)
    ).apply(instance, (icon, x, y, name, flavor, parents, pages, critical, tab, incomplete, incompletePages, completionAdvancement) -> new Entry(null, tab, icon, x, y, pages.toArray(new Page[0]), parents.toArray(new ResourceLocation[0]), name, flavor, critical, incomplete, incompletePages.toArray(new Page[0]), completionAdvancement)));
    public static final StreamCodec<RegistryFriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {
        pBuffer.writeResourceLocation(pValue.id);
        pBuffer.writeInt(pValue.x);
        pBuffer.writeInt(pValue.y);
        ItemStack.STREAM_CODEC.encode(pBuffer, pValue.icon);
        ComponentSerialization.STREAM_CODEC.encode(pBuffer, pValue.name);
        ComponentSerialization.STREAM_CODEC.encode(pBuffer, pValue.flavor);
        pBuffer.writeCollection(Arrays.stream(pValue.pages).toList(), (pBuffer1, pValue1) -> PAGE_STREAM_CODEC.encode((RegistryFriendlyByteBuf)pBuffer1, pValue1));
        List<ResourceLocation> parents = new ArrayList<>();
        for (Entry i : pValue.getParentEntries()) {
            parents.add(i.id);
        }
        pBuffer.writeCollection(parents, FriendlyByteBuf::writeResourceLocation);
        pBuffer.writeBoolean(pValue.critical);
        pBuffer.writeResourceLocation(pValue.tab);
        pBuffer.writeBoolean(pValue.incomplete);
        pBuffer.writeCollection(Arrays.stream(pValue.incompletePages).toList(), (pBuffer1, pValue1) -> PAGE_STREAM_CODEC.encode((RegistryFriendlyByteBuf)pBuffer1, pValue1));
        pBuffer.writeResourceLocation(pValue.completionAdvancement);
    }, pBuffer -> {
        ResourceLocation id = pBuffer.readResourceLocation();
        int x = pBuffer.readInt();
        int y = pBuffer.readInt();
        ItemStack icon = ItemStack.STREAM_CODEC.decode(pBuffer);
        Component name = ComponentSerialization.STREAM_CODEC.decode(pBuffer);
        Component flavor = ComponentSerialization.STREAM_CODEC.decode(pBuffer);
        Page[] pages = pBuffer.readList((pBuffer1) -> PAGE_STREAM_CODEC.decode((RegistryFriendlyByteBuf)pBuffer1)).toArray(new Page[0]);
        List<ResourceLocation> parents = pBuffer.readList(FriendlyByteBuf::readResourceLocation);
        boolean critical = pBuffer.readBoolean();
        ResourceLocation tab = pBuffer.readResourceLocation();
        boolean incomplete = pBuffer.readBoolean();
        Page[] incompletePages = pBuffer.readList((pBuffer1) -> PAGE_STREAM_CODEC.decode((RegistryFriendlyByteBuf)pBuffer1)).toArray(new Page[0]);
        ResourceLocation completionAdvancement = pBuffer.readResourceLocation();
        return new Entry(id, tab, icon, x, y, pages, parents.toArray(new ResourceLocation[0]), name, flavor, critical, incomplete, incompletePages, completionAdvancement);
    });
}