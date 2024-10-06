package com.cmdpro.datanessence.block.transmission;

import com.cmdpro.datanessence.api.block.BaseEssencePointBlockEntity;
import com.cmdpro.datanessence.api.essence.EssenceStorage;
import com.cmdpro.datanessence.api.essence.EssenceType;
import com.cmdpro.datanessence.api.essence.container.SingleEssenceContainer;
import com.cmdpro.datanessence.api.misc.ICustomEssencePointBehaviour;
import com.cmdpro.datanessence.config.DataNEssenceConfig;
import com.cmdpro.datanessence.registry.BlockEntityRegistry;
import com.cmdpro.datanessence.registry.EssenceTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class EssencePointBlockEntity extends BaseEssencePointBlockEntity {
    public EssencePointBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.ESSENCE_POINT.get(), pos, state);
        storage = new SingleEssenceContainer(EssenceTypeRegistry.ESSENCE.get(), DataNEssenceConfig.essencePointTransfer);
    }

    @Override
    public Color linkColor() {
        return new Color(EssenceTypeRegistry.ESSENCE.get().getColor());
    }
    @Override
    public void transfer(BlockEntity otherEntity, EssenceStorage other) {
        if (otherEntity instanceof ICustomEssencePointBehaviour behaviour) {
            if (!behaviour.canInsertEssence(other, EssenceTypeRegistry.ESSENCE.get(), DataNEssenceConfig.essencePointTransfer)) {
                return;
            }
        }
        EssenceStorage.transferEssence(storage, other, EssenceTypeRegistry.ESSENCE.get(), DataNEssenceConfig.essencePointTransfer);
    }
    @Override
    public void take(BlockEntity otherEntity, EssenceStorage other) {
        if (otherEntity instanceof ICustomEssencePointBehaviour behaviour) {
            if (!behaviour.canInsertEssence(other, EssenceTypeRegistry.ESSENCE.get(), DataNEssenceConfig.essencePointTransfer)) {
                return;
            }
        }
        EssenceStorage.transferEssence(other, storage, EssenceTypeRegistry.ESSENCE.get(), DataNEssenceConfig.essencePointTransfer);
    }
}
