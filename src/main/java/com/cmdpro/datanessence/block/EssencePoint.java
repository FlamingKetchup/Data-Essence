package com.cmdpro.datanessence.block;

import com.cmdpro.datanessence.api.BaseEssencePoint;
import com.cmdpro.datanessence.block.entity.EssencePointBlockEntity;
import com.cmdpro.datanessence.registry.BlockEntityRegistry;
import com.cmdpro.datanessence.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class EssencePoint extends BaseEssencePoint {

    public EssencePoint(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Item getRequiredWire() {
        return ItemRegistry.ESSENCE_WIRE.get();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new EssencePointBlockEntity(pPos, pState);
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, BlockEntityRegistry.ESSENCE_POINT.get(),
                EssencePointBlockEntity::tick);
    }
}
