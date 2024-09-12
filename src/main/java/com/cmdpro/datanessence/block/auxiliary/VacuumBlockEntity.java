package com.cmdpro.datanessence.block.auxiliary;

import com.cmdpro.datanessence.api.essence.EssenceBlockEntity;
import com.cmdpro.datanessence.api.essence.EssenceStorage;
import com.cmdpro.datanessence.api.essence.container.SingleEssenceContainer;
import com.cmdpro.datanessence.registry.BlockEntityRegistry;
import com.cmdpro.datanessence.registry.EssenceTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VacuumBlockEntity extends BlockEntity implements EssenceBlockEntity {

    public static SingleEssenceContainer storage = new SingleEssenceContainer(EssenceTypeRegistry.ESSENCE.get(), 1000);

    private final ItemStackHandler itemHandler = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

    };
    public VacuumBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.VACUUM.get(), pPos, pBlockState);
    }
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider pRegistries) {
        tag.put("EssenceStorage", storage.toNbt());
        tag.put("inventory", itemHandler.serializeNBT(pRegistries));
        super.saveAdditional(tag, pRegistries);
    }
    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        super.loadAdditional(nbt, pRegistries);
        itemHandler.deserializeNBT(pRegistries, nbt.getCompound("inventory"));
        storage = storage.fromNbt(nbt.getCompound("EssenceStorage"));
    }
    public void drops() {
        SimpleContainer inventory = getInv();

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
    public IItemHandler getItemHandler() {
        return lazyItemHandler.get();
    }
    private Lazy<IItemHandler> lazyItemHandler = Lazy.of(() -> itemHandler);
    public SimpleContainer getInv() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        return inventory;
    }
    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, VacuumBlockEntity pBlockEntity) {
        if (!pLevel.isClientSide) {
            if (storage.getEssence(EssenceTypeRegistry.ESSENCE.get()) >= 1) {
                List<ItemEntity> ents = pLevel.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(pPos.getCenter(), 20, 20, 20));
                for (ItemEntity i : ents) {
                    i.setDeltaMovement(pPos.getCenter().subtract(i.position()).normalize().multiply(0.2, 0.2, 0.2));
                    i.hasImpulse = true;
                }
                if (!ents.isEmpty()) {
                    storage.removeEssence(EssenceTypeRegistry.ESSENCE.get(), 1);
                }
                List<ItemEntity> ents2 = pLevel.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(pPos.getCenter(), 3, 3, 3));
                for (ItemEntity i : ents2) {
                    ItemStack copy = i.getItem().copy();
                    if (!copy.isEmpty()) {
                        for (int o = 0; o < pBlockEntity.itemHandler.getSlots(); o++) {
                            ItemStack stack = pBlockEntity.itemHandler.getStackInSlot(o);
                            if (stack.is(copy.getItem()) || stack.isEmpty()) {
                                copy = pBlockEntity.itemHandler.insertItem(o, copy, false);
                            }
                        }
                    }
                    i.setItem(copy);
                }
            }
        }
    }

    @Override
    public EssenceStorage getStorage() {
        return storage;
    }
}
