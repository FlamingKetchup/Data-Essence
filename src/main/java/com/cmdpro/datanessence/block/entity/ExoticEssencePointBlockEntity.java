package com.cmdpro.datanessence.block.entity;

import com.cmdpro.datanessence.api.BaseEssencePointBlockEntity;
import com.cmdpro.datanessence.api.DataNEssenceUtil;
import com.cmdpro.datanessence.api.EssenceContainer;
import com.cmdpro.datanessence.config.DataNEssenceConfig;
import com.cmdpro.datanessence.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.awt.*;

public class ExoticEssencePointBlockEntity extends BaseEssencePointBlockEntity implements GeoBlockEntity {
    public ExoticEssencePointBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.EXOTIC_ESSENCE_POINT.get(), pos, state);
    }

    private AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private <E extends GeoAnimatable> PlayState predicate(AnimationState event) {
        event.getController().setAnimation(RawAnimation.begin().then("animation.essence_point.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }
    @Override
    public void transfer(EssenceContainer other) {
        DataNEssenceUtil.transferExoticEssence(this, other, DataNEssenceConfig.essencePointTransfer);
    }

    @Override
    public Color linkColor() {
        return new Color(0xe7fcf9);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(tag, pRegistries);
        tag.putFloat("exoticEssence", getExoticEssence());
    }
    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        super.loadAdditional(nbt, pRegistries);
        setExoticEssence(nbt.getFloat("exoticEssence"));
    }

    @Override
    public float getMaxExoticEssence() {
        return DataNEssenceConfig.essencePointTransfer;
    }

    @Override
    public void take(EssenceContainer other) {
        DataNEssenceUtil.transferExoticEssence(other, this, DataNEssenceConfig.essencePointTransfer);
    }
}
