package com.cmdpro.datanessence.block.generation;

import com.cmdpro.datanessence.DataNEssence;
import com.cmdpro.datanessence.api.essence.EssenceBlockEntity;
import com.cmdpro.datanessence.api.essence.EssenceStorage;
import com.cmdpro.datanessence.api.essence.container.SingleEssenceContainer;
import com.cmdpro.datanessence.registry.BlockEntityRegistry;
import com.cmdpro.datanessence.registry.EssenceTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class EssenceLeechBlockEntity extends BlockEntity implements EssenceBlockEntity {
    public static SingleEssenceContainer storage = new SingleEssenceContainer(EssenceTypeRegistry.ESSENCE.get(), 1000);
    @Override
    public EssenceStorage getStorage() {
        return storage;
    }
    public int cooldown;

    public EssenceLeechBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.ESSENCE_LEECH.get(), pos, state);
    }
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(tag, pRegistries);
        tag.putInt("cooldown", cooldown);
        tag.put("EssenceStorage", storage.toNbt());
    }
    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        super.loadAdditional(nbt, pRegistries);
        storage = storage.fromNbt(nbt.getCompound("EssenceStorage"));
        cooldown = nbt.getInt("cooldown");
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, EssenceLeechBlockEntity pBlockEntity) {
        if (!pLevel.isClientSide) {
            if (pBlockEntity.getStorage().getEssence(EssenceTypeRegistry.ESSENCE.get()) < pBlockEntity.getStorage().getMaxEssence() && pBlockEntity.cooldown <= 0) {
                for (LivingEntity i : pLevel.getEntitiesOfClass(LivingEntity.class, AABB.ofSize(pPos.getCenter().add(0, 2, 0), 3, 3, 3))) {
                    if (i.isAlive()) {
                        i.hurt(i.damageSources().source(DataNEssence.essenceSiphoned), 5);
                        if (!i.isAlive()) {
                            pBlockEntity.getStorage().addEssence(EssenceTypeRegistry.ESSENCE.get(), 20);
                        }
                    }
                }
                pBlockEntity.cooldown = 10;
            } else {
                pBlockEntity.cooldown -= 1;
            }
        }
    }
}
