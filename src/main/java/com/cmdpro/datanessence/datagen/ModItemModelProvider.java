package com.cmdpro.datanessence.datagen;

import com.cmdpro.datanessence.DataNEssence;
import com.cmdpro.datanessence.registry.BlockRegistry;
import com.cmdpro.datanessence.registry.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;

public class ModItemModelProvider extends ItemModelProvider {
    private static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DataNEssence.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ItemRegistry.DATA_TABLET);
        simpleItem(ItemRegistry.DATA_DRIVE);
        simpleItem(ItemRegistry.ESSENCE_SHARD);
        simpleItemWithSubdirectory(ItemRegistry.ESSENCE_WIRE, "wires");
        simpleItemWithSubdirectory(ItemRegistry.LUNAR_ESSENCE_WIRE, "wires");
        simpleItemWithSubdirectory(ItemRegistry.NATURAL_ESSENCE_WIRE, "wires");
        simpleItemWithSubdirectory(ItemRegistry.EXOTIC_ESSENCE_WIRE, "wires");
        simpleItemWithSubdirectory(ItemRegistry.ITEM_WIRE, "wires");
        simpleItemWithSubdirectory(ItemRegistry.FLUID_WIRE, "wires");

        handheldItem(ItemRegistry.MAGIC_WRENCH);

        evenSimplerBlockItem(BlockRegistry.FABRICATOR);
        evenSimplerBlockItem(BlockRegistry.ESSENCE_POINT);
        evenSimplerBlockItem(BlockRegistry.LUNAR_ESSENCE_POINT);
        evenSimplerBlockItem(BlockRegistry.NATURAL_ESSENCE_POINT);
        evenSimplerBlockItem(BlockRegistry.EXOTIC_ESSENCE_POINT);
        evenSimplerBlockItem(BlockRegistry.ITEM_POINT);
        evenSimplerBlockItem(BlockRegistry.FLUID_POINT);
        evenSimplerBlockItem(BlockRegistry.ESSENCE_CRYSTAL);
        evenSimplerBlockItem(BlockRegistry.INFUSER);

        evenSimplerBlockItem(BlockRegistry.DECO_ESSENCE_BUFFER);
        evenSimplerBlockItem(BlockRegistry.DECO_ITEM_BUFFER);
        evenSimplerBlockItem(BlockRegistry.DECO_FLUID_BUFFER);

        evenSimplerBlockItem(BlockRegistry.ANCIENT_ROCK_COLUMN);
        evenSimplerBlockItem(BlockRegistry.ANCIENT_ROCK_BRICKS);
        evenSimplerBlockItem(BlockRegistry.ANCIENT_ROCK_TILES);
        evenSimplerBlockItem(BlockRegistry.ENERGIZED_ANCIENT_ROCK_COLUMN);
        evenSimplerBlockItem(BlockRegistry.ANCIENT_LANTERN);

        evenSimplerBlockItem(BlockRegistry.POLISHED_OBSIDIAN_COLUMN);

        simpleItem(ItemRegistry.CONDUCTANCE_ROD);
        simpleItem(ItemRegistry.CAPACITANCE_PANEL);
        simpleItem(ItemRegistry.LOGICAL_MATRIX);

        simpleItemWithSubdirectory(ItemRegistry.ESSENCE_BOMB, "bombs");
        simpleItemWithSubdirectory(ItemRegistry.LUNAR_ESSENCE_BOMB, "bombs");
        simpleItemWithSubdirectory(ItemRegistry.NATURAL_ESSENCE_BOMB, "bombs");
        simpleItemWithSubdirectory(ItemRegistry.EXOTIC_ESSENCE_BOMB, "bombs");

    }
    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(DataNEssence.MOD_ID,"item/" + item.getId().getPath()));
    }
    private ItemModelBuilder simpleItemWithSubdirectory(RegistryObject<Item> item, String subdirectory) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(DataNEssence.MOD_ID,"item/" + subdirectory + "/" + item.getId().getPath()));
    }
    private ItemModelBuilder flatBlockItemWithTexture(RegistryObject<Block> item, ResourceLocation texture) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                texture);
    }

    public void evenSimplerBlockItem(RegistryObject<Block> block) {
        this.withExistingParent(DataNEssence.MOD_ID + ":" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
    }
    public void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/wall_inventory"))
                .texture("wall",  new ResourceLocation(DataNEssence.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(DataNEssence.MOD_ID,"item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(DataNEssence.MOD_ID,"item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItemBlockTexture(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(DataNEssence.MOD_ID,"block/" + item.getId().getPath()));
    }
}
