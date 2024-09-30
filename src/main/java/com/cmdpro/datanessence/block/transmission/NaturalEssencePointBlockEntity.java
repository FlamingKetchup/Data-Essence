package com.cmdpro.datanessence.block.transmission;

import com.cmdpro.datanessence.api.block.BaseEssencePointBlockEntity;
import com.cmdpro.datanessence.api.essence.EssenceStorage;
import com.cmdpro.datanessence.api.essence.EssenceType;
import com.cmdpro.datanessence.api.essence.container.SingleEssenceContainer;
import com.cmdpro.datanessence.config.DataNEssenceConfig;
import com.cmdpro.datanessence.registry.BlockEntityRegistry;
import com.cmdpro.datanessence.registry.EssenceTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class NaturalEssencePointBlockEntity extends BaseEssencePointBlockEntity {
    public NaturalEssencePointBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.NATURAL_ESSENCE_POINT.get(), pos, state);
        storage = new SingleEssenceContainer(EssenceTypeRegistry.NATURAL_ESSENCE.get(), DataNEssenceConfig.essencePointTransfer);
    }
    @Override
    public Color linkColor() {
        return new Color(EssenceTypeRegistry.NATURAL_ESSENCE.get().getColor());
    }

    @Override
    public void transfer(EssenceStorage other) {
        EssenceStorage.transferEssence(storage, other, EssenceTypeRegistry.NATURAL_ESSENCE.get(), DataNEssenceConfig.essencePointTransfer);
    }
    @Override
    public void take(EssenceStorage other) {
        EssenceStorage.transferEssence(other, storage, EssenceTypeRegistry.NATURAL_ESSENCE.get(), DataNEssenceConfig.essencePointTransfer);
    }
}
