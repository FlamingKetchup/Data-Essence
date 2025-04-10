package com.cmdpro.datanessence.recipe;


import com.cmdpro.datanessence.DataNEssence;
import com.cmdpro.datanessence.registry.RecipeRegistry;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.CommonHooks;

import java.util.*;

public class ShapedFabricationRecipe implements IFabricationRecipe {
    private final ResourceLocation entry;
    private final boolean allowIncomplete, showIncompleteInEMI;
    private final Map<ResourceLocation, Float> essenceCost;
    final ShapedRecipePattern pattern;
    final ItemStack result;

    public ShapedFabricationRecipe(ShapedRecipePattern pPattern, ItemStack pResult, ResourceLocation entry, boolean allowIncomplete, boolean showIncompleteInEMI, Map<ResourceLocation, Float> essenceCost) {
        this.entry = entry;
        this.allowIncomplete = allowIncomplete;
        this.showIncompleteInEMI = showIncompleteInEMI;
        this.essenceCost = essenceCost;
        this.pattern = pPattern;
        this.result = pResult;
    }

    @Override
    public boolean allowIncomplete() {
        return allowIncomplete;
    }

    @Override
    public boolean showIncompleteInEMI() {
        return showIncompleteInEMI;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistryAccess) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.pattern.ingredients();
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth >= this.pattern.width() && pHeight >= this.pattern.height();
    }

    @Override
    public boolean matches(CraftingInput pInv, Level pLevel) {
        return this.pattern.matches(pInv);
    }

    public ItemStack assemble(CraftingInput pContainer, HolderLookup.Provider pRegistryAccess) {
        return this.getResultItem(pRegistryAccess).copy();
    }

    public int getWidth() {
        return this.pattern.width();
    }

    public int getHeight() {
        return this.pattern.height();
    }

    @Override
    public boolean isIncomplete() {
        NonNullList<Ingredient> nonnulllist = this.getIngredients();
        return nonnulllist.isEmpty() || nonnulllist.stream().filter(p_151277_ -> !p_151277_.isEmpty()).anyMatch(Ingredient::hasNoItems);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.FABRICATIONCRAFTING.get();
    }

    @Override
    public ResourceLocation getEntry() {
        return entry;
    }



    @Override
    public Map<ResourceLocation, Float> getEssenceCost() {
        return essenceCost;
    }

    public static class Serializer implements RecipeSerializer<ShapedFabricationRecipe> {
        public static final MapCodec<ShapedFabricationRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ShapedRecipePattern.MAP_CODEC.forGetter(p_311733_ -> p_311733_.pattern),
                ItemStack.CODEC.fieldOf("result").forGetter(p_311730_ -> p_311730_.result),
                ResourceLocation.CODEC.fieldOf("entry").forGetter((r) -> r.entry),
                Codec.BOOL.optionalFieldOf("allow_incomplete", false).forGetter((r) -> r.allowIncomplete),
                Codec.BOOL.optionalFieldOf("show_incomplete_in_emi", false).forGetter((r) -> r.showIncompleteInEMI),
                Codec.unboundedMap(ResourceLocation.CODEC, Codec.FLOAT).fieldOf("essenceCost").forGetter(r -> r.essenceCost)
        ).apply(instance, ShapedFabricationRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ShapedFabricationRecipe> STREAM_CODEC = StreamCodec.of(
                (buf, obj) -> {
                    ShapedRecipePattern.STREAM_CODEC.encode(buf, obj.pattern);
                    ItemStack.STREAM_CODEC.encode(buf, obj.result);
                    buf.writeResourceLocation(obj.entry);
                    buf.writeBoolean(obj.allowIncomplete);
                    buf.writeBoolean(obj.showIncompleteInEMI);
                    buf.writeMap(obj.essenceCost, FriendlyByteBuf::writeResourceLocation, FriendlyByteBuf::writeFloat);
                },
                (buf) -> {
                    ShapedRecipePattern shapedrecipepattern = ShapedRecipePattern.STREAM_CODEC.decode(buf);
                    ItemStack itemstack = ItemStack.STREAM_CODEC.decode(buf);
                    ResourceLocation entry = buf.readResourceLocation();
                    boolean allowIncomplete = buf.readBoolean();
                    boolean showIncompleteInEMI = buf.readBoolean();
                    Map<ResourceLocation, Float> essenceCost = buf.readMap(FriendlyByteBuf::readResourceLocation, FriendlyByteBuf::readFloat);
                    return new ShapedFabricationRecipe(shapedrecipepattern, itemstack, entry, allowIncomplete, showIncompleteInEMI, essenceCost);
                }
        );
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public MapCodec<ShapedFabricationRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ShapedFabricationRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}