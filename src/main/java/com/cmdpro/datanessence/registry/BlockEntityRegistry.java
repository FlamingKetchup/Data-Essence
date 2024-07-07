package com.cmdpro.datanessence.registry;

import com.cmdpro.datanessence.DataNEssence;
import com.cmdpro.datanessence.block.entity.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, DataNEssence.MOD_ID);
    public static final Supplier<BlockEntityType<FabricatorBlockEntity>> FABRICATOR =
            register("fabricator", () ->
                    BlockEntityType.Builder.of(FabricatorBlockEntity::new,
                            BlockRegistry.FABRICATOR.get()).build(null));
    public static final Supplier<BlockEntityType<InfuserBlockEntity>> INFUSER =
            register("infuser", () ->
                    BlockEntityType.Builder.of(InfuserBlockEntity::new,
                            BlockRegistry.INFUSER.get()).build(null));
    public static final Supplier<BlockEntityType<EssenceBurnerBlockEntity>> ESSENCE_BURNER =
            register("essence_burner", () ->
                    BlockEntityType.Builder.of(EssenceBurnerBlockEntity::new,
                            BlockRegistry.ESSENCE_BURNER.get()).build(null));
    public static final Supplier<BlockEntityType<EssencePointBlockEntity>> ESSENCE_POINT =
            register("essence_point", () ->
                    BlockEntityType.Builder.of(EssencePointBlockEntity::new,
                            BlockRegistry.ESSENCE_POINT.get()).build(null));
    public static final Supplier<BlockEntityType<LunarEssencePointBlockEntity>> LUNAR_ESSENCE_POINT =
            register("lunar_essence_point", () ->
                    BlockEntityType.Builder.of(LunarEssencePointBlockEntity::new,
                            BlockRegistry.LUNAR_ESSENCE_POINT.get()).build(null));
    public static final Supplier<BlockEntityType<NaturalEssencePointBlockEntity>> NATURAL_ESSENCE_POINT =
            register("natural_essence_point", () ->
                    BlockEntityType.Builder.of(NaturalEssencePointBlockEntity::new,
                            BlockRegistry.NATURAL_ESSENCE_POINT.get()).build(null));
    public static final Supplier<BlockEntityType<ExoticEssencePointBlockEntity>> EXOTIC_ESSENCE_POINT =
            register("exotic_essence_point", () ->
                    BlockEntityType.Builder.of(ExoticEssencePointBlockEntity::new,
                            BlockRegistry.EXOTIC_ESSENCE_POINT.get()).build(null));
    public static final Supplier<BlockEntityType<ItemPointBlockEntity>> ITEM_POINT =
            register("item_point", () ->
                    BlockEntityType.Builder.of(ItemPointBlockEntity::new,
                            BlockRegistry.ITEM_POINT.get()).build(null));
    public static final Supplier<BlockEntityType<FluidPointBlockEntity>> FLUID_POINT =
            register("fluid_point", () ->
                    BlockEntityType.Builder.of(FluidPointBlockEntity::new,
                            BlockRegistry.FLUID_POINT.get()).build(null));
    public static final Supplier<BlockEntityType<EssenceBufferBlockEntity>> ESSENCE_BUFFER =
            register("essence_buffer", () ->
                    BlockEntityType.Builder.of(EssenceBufferBlockEntity::new,
                            BlockRegistry.ESSENCE_BUFFER.get()).build(null));
    public static final Supplier<BlockEntityType<ItemBufferBlockEntity>> ITEM_BUFFER =
            register("item_buffer", () ->
                    BlockEntityType.Builder.of(ItemBufferBlockEntity::new,
                            BlockRegistry.ITEM_BUFFER.get()).build(null));
    public static final Supplier<BlockEntityType<FluidBufferBlockEntity>> FLUID_BUFFER =
            register("fluid_buffer", () ->
                    BlockEntityType.Builder.of(FluidBufferBlockEntity::new,
                            BlockRegistry.FLUID_BUFFER.get()).build(null));
    public static final Supplier<BlockEntityType<DataBankBlockEntity>> DATA_BANK =
            register("data_bank", () ->
                    BlockEntityType.Builder.of(DataBankBlockEntity::new,
                            BlockRegistry.DATA_BANK.get()).build(null));
    public static final Supplier<BlockEntityType<EssenceBatteryBlockEntity>> ESSENCE_BATTERY =
            register("essence_battery", () ->
                    BlockEntityType.Builder.of(EssenceBatteryBlockEntity::new,
                            BlockRegistry.ESSENCE_BATTERY.get()).build(null));
    public static final Supplier<BlockEntityType<LunarEssenceBatteryBlockEntity>> LUNAR_ESSENCE_BATTERY =
            register("lunar_essence_battery", () ->
                    BlockEntityType.Builder.of(LunarEssenceBatteryBlockEntity::new,
                            BlockRegistry.LUNAR_ESSENCE_BATTERY.get()).build(null));
    public static final Supplier<BlockEntityType<NaturalEssenceBatteryBlockEntity>> NATURAL_ESSENCE_BATTERY =
            register("natural_essence_battery", () ->
                    BlockEntityType.Builder.of(NaturalEssenceBatteryBlockEntity::new,
                            BlockRegistry.NATURAL_ESSENCE_BATTERY.get()).build(null));
    public static final Supplier<BlockEntityType<ExoticEssenceBatteryBlockEntity>> EXOTIC_ESSENCE_BATTERY =
            register("exotic_essence_battery", () ->
                    BlockEntityType.Builder.of(ExoticEssenceBatteryBlockEntity::new,
                            BlockRegistry.EXOTIC_ESSENCE_BATTERY.get()).build(null));


    private static <T extends BlockEntityType<?>> Supplier<T> register(final String name, final Supplier<T> blockentity) {
        return BLOCK_ENTITIES.register(name, blockentity);
    }
}
