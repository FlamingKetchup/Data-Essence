package com.cmdpro.datanessence.block.entity;

import com.cmdpro.datanessence.api.EssenceContainer;
import com.cmdpro.datanessence.block.BaseEssencePoint;
import com.cmdpro.datanessence.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class BaseEssencePointBlockEntity extends EssenceContainer {
    public BlockPos link;

    public BaseEssencePointBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putBoolean("hasLink", link != null);
        if (link != null) {
            pTag.putInt("linkX", link.getX());
            pTag.putInt("linkY", link.getY());
            pTag.putInt("linkZ", link.getZ());
        }
    }
    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, BaseEssencePointBlockEntity pBlockEntity) {
        if (!pLevel.isClientSide()) {
            if (pLevel.getBlockEntity(pPos.relative(pBlockEntity.getDirection().getOpposite())) instanceof EssenceContainer ent) {
                if (pBlockEntity.link == null) {
                    pBlockEntity.transfer(ent);
                } else {
                    pBlockEntity.take(ent);
                }
            }
            if (pBlockEntity.link != null) {
                if (pLevel.getBlockEntity(pBlockEntity.link) instanceof EssenceContainer ent) {
                    if (ent.getMaxEssence() > 0 && pBlockEntity.getMaxEssence() > 0) {
                        pBlockEntity.transfer(ent);
                    }
                }
            }
        }
    }
    public abstract void transfer(EssenceContainer other);
    public abstract void take(EssenceContainer other);
    public Direction getDirection() {
        if (getBlockState().getValue(BaseEssencePoint.FACE).equals(AttachFace.CEILING)) {
            return Direction.DOWN;
        }
        if (getBlockState().getValue(BaseEssencePoint.FACE).equals(AttachFace.WALL)) {
            return getBlockState().getValue(BaseEssencePoint.FACING);
        }
        return Direction.UP;
    }
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        if (tag.getBoolean("hasLink")) {
            link = new BlockPos(tag.getInt("linkX"), tag.getInt("linkY"), tag.getInt("linkZ"));
        }
    }
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("hasLink", link != null);
        if (link != null) {
            tag.putInt("linkX", link.getX());
            tag.putInt("linkY", link.getY());
            tag.putInt("linkZ", link.getZ());
        }
        return tag;
    }
    public void updateBlock() {
        BlockState blockState = level.getBlockState(this.getBlockPos());
        this.level.sendBlockUpdated(this.getBlockPos(), blockState, blockState, 3);
        this.setChanged();
    }
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.getBoolean("hasLink")) {
            link = new BlockPos(pTag.getInt("linkX"), pTag.getInt("linkY"), pTag.getInt("linkZ"));
        }
    }

}
