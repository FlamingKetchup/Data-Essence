package com.cmdpro.datanessence.registry;

import com.cmdpro.datanessence.DataNEssence;
import com.cmdpro.datanessence.api.*;
import com.cmdpro.datanessence.item.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, DataNEssence.MOD_ID);

    // Tools
    public static final Supplier<Item> MAGIC_WRENCH = register("magic_wrench", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> DATA_DRIVE = register("data_drive", () -> new DataDrive(new Item.Properties()));

    // Essence Shards
    public static final Supplier<Item> ESSENCE_SHARD = register("essence_shard", () -> new EssenceShard(new Item.Properties(), 100, 0, 0, 0));

    // Wires
    public static final Supplier<Item> ESSENCE_WIRE = register("essence_wire", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> LUNAR_ESSENCE_WIRE = register("lunar_essence_wire", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> NATURAL_ESSENCE_WIRE = register("natural_essence_wire", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> EXOTIC_ESSENCE_WIRE = register("exotic_essence_wire", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> ITEM_WIRE = register("item_wire", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> FLUID_WIRE = register("fluid_wire", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> DATA_TABLET = register("data_tablet", () -> new DataTablet(new Item.Properties().stacksTo(1)));

    // BlockItems
    public static final Supplier<Item> FABRICATOR_ITEM = register("fabricator", () -> new FabricatorItem(BlockRegistry.FABRICATOR.get(), new Item.Properties()));
    public static final Supplier<Item> ESSENCE_POINT_ITEM = register("essence_point", () -> new EssencePointItem(BlockRegistry.ESSENCE_POINT.get(), new Item.Properties()));
    public static final Supplier<Item> LUNAR_ESSENCE_POINT_ITEM = register("lunar_essence_point", () -> new LunarEssencePointItem(BlockRegistry.LUNAR_ESSENCE_POINT.get(), new Item.Properties()));
    public static final Supplier<Item> NATURAL_ESSENCE_POINT_ITEM = register("natural_essence_point", () -> new NaturalEssencePointItem(BlockRegistry.NATURAL_ESSENCE_POINT.get(), new Item.Properties()));
    public static final Supplier<Item> EXOTIC_ESSENCE_POINT_ITEM = register("exotic_essence_point", () -> new ExoticEssencePointItem(BlockRegistry.EXOTIC_ESSENCE_POINT.get(), new Item.Properties()));
    public static final Supplier<Item> ITEM_POINT_ITEM = register("item_point", () -> new ItemPointItem(BlockRegistry.ITEM_POINT.get(), new Item.Properties()));
    public static final Supplier<Item> FLUID_POINT_ITEM = register("fluid_point", () -> new FluidPointItem(BlockRegistry.FLUID_POINT.get(), new Item.Properties()));
    public static final Supplier<Item> INFUSER_ITEM = register("infuser", () -> new InfuserItem(BlockRegistry.INFUSER.get(), new Item.Properties()));

    // Bombs
    public static final Supplier<Item> ESSENCE_BOMB = register("essence_bomb", () -> new EssenceBombItem(new Item.Properties()));
    public static final Supplier<Item> LUNAR_ESSENCE_BOMB = register("lunar_essence_bomb", () -> new LunarEssenceBombItem(new Item.Properties()));
    public static final Supplier<Item> NATURAL_ESSENCE_BOMB = register("natural_essence_bomb", () -> new NaturalEssenceBombItem(new Item.Properties()));
    public static final Supplier<Item> EXOTIC_ESSENCE_BOMB = register("exotic_essence_bomb", () -> new ExoticEssenceBombItem(new Item.Properties()));

    // Crafting Components
    public static final Supplier<Item> CONDUCTANCE_ROD = register("conductance_rod", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CAPACITANCE_PANEL = register("capacitance_panel", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> LOGICAL_MATRIX = register("logical_matrix", () -> new Item(new Item.Properties()));

    // Misc
    public static final Supplier<Item> MAGITECH_8_BALL = register("magitech_8_ball", () -> new Magitech8Ball(new Item.Properties().stacksTo(1)));

    private static <T extends Item> Supplier<T> register(final String name, final Supplier<T> item) {
        return ITEMS.register(name, item);
    }
}
