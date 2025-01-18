package com.cmdpro.datanessence.block.processing;

import com.cmdpro.datanessence.api.essence.EssenceBlockEntity;
import com.cmdpro.datanessence.api.essence.EssenceStorage;
import com.cmdpro.datanessence.api.essence.container.SingleEssenceContainer;
import com.cmdpro.datanessence.api.util.BufferUtil;
import com.cmdpro.datanessence.recipe.EntropicProcessingRecipe;
import com.cmdpro.datanessence.recipe.MeltingRecipe;
import com.cmdpro.datanessence.registry.BlockEntityRegistry;
import com.cmdpro.datanessence.registry.DamageTypeRegistry;
import com.cmdpro.datanessence.registry.EssenceTypeRegistry;
import com.cmdpro.datanessence.registry.RecipeRegistry;
import com.cmdpro.datanessence.screen.EntropicProcessorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MelterBlockEntity extends BlockEntity implements MenuProvider, EssenceBlockEntity {
    public SingleEssenceContainer storage = new SingleEssenceContainer(EssenceTypeRegistry.ESSENCE.get(), 1000);
    @Override
    public EssenceStorage getStorage() {
        return storage;
    }

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private final FluidTank outputFluidHandler = new FluidTank(2000);
    private final FluidTank fuelFluidHandler = new FluidTank(2000);

    public void drops() {
        SimpleContainer inventory = getInv();

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
    private Lazy<IItemHandler> lazyItemHandler = Lazy.of(() -> itemHandler);
    private Lazy<IFluidHandler> lazyOutputHandler = Lazy.of(() -> outputFluidHandler);
    private Lazy<IFluidHandler> lazyFuelHandler = Lazy.of(() -> fuelFluidHandler);

    public IItemHandler getItemHandler() {
        return lazyItemHandler.get();
    }
    public IFluidHandler getOutputHandler() {
        return lazyOutputHandler.get();
    }
    public IFluidHandler getFuelHandler() {
        return lazyFuelHandler.get();
    }
    public MelterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.MELTER.get(), pos, state);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider pRegistries){
        CompoundTag tag = pkt.getTag();
        storage.fromNbt(tag.getCompound("EssenceStorage"));
        workTime = tag.getInt("workTime");
        maxTime = tag.getInt("maxTime");
        tag.put("outputFluidHandler", outputFluidHandler.writeToNBT(pRegistries, new CompoundTag()));
        tag.put("fuelFluidHandler", fuelFluidHandler.writeToNBT(pRegistries, new CompoundTag()));
    }
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag tag = new CompoundTag();
        tag.put("EssenceStorage", storage.toNbt());
        tag.putInt("workTime", workTime);
        tag.putInt("maxTime", recipe == null ? -1 : recipe.getTime());
        outputFluidHandler.readFromNBT(pRegistries, tag.getCompound("outputFluidHandler"));
        fuelFluidHandler.readFromNBT(pRegistries, tag.getCompound("fuelFluidHandler"));
        return tag;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider pRegistries) {
        tag.put("input", itemHandler.serializeNBT(pRegistries));
        tag.put("outputFluidHandler", outputFluidHandler.writeToNBT(pRegistries, new CompoundTag()));
        tag.put("EssenceStorage", storage.toNbt());
        tag.putInt("workTime", workTime);
        tag.put("fuelFluidHandler", fuelFluidHandler.writeToNBT(pRegistries, new CompoundTag()));
        super.saveAdditional(tag, pRegistries);
    }
    public float maxTime;
    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        super.loadAdditional(nbt, pRegistries);
        itemHandler.deserializeNBT(pRegistries, nbt.getCompound("input"));
        outputFluidHandler.readFromNBT(pRegistries, nbt.getCompound("outputFluidHandler"));
        fuelFluidHandler.readFromNBT(pRegistries, nbt.getCompound("fuelFluidHandler"));
        storage.fromNbt(nbt.getCompound("EssenceStorage"));
        workTime = nbt.getInt("workTime");
    }
    public SimpleContainer getInv() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        return inventory;
    }
    public RecipeInput getCraftingInv() {
        RecipeInput inventory = new SingleRecipeInput(itemHandler.getStackInSlot(0));
        return inventory;
    }
    public int workTime;
    public MeltingRecipe recipe;
    public float essenceCost;
    public int drainCooldown;
    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, MelterBlockEntity pBlockEntity) {
        if (!pLevel.isClientSide()) {
            BufferUtil.getEssenceFromBuffersBelow(pBlockEntity, EssenceTypeRegistry.ESSENCE.get());
            BufferUtil.getItemsFromBuffersBelow(pBlockEntity);
            BufferUtil.getFluidsFromBuffersBelow(pBlockEntity);
            boolean resetWorkTime = true;
            if (pBlockEntity.getStorage().getEssence(EssenceTypeRegistry.ESSENCE.get()) >= 1) {
                Optional<RecipeHolder<MeltingRecipe>> recipe = pLevel.getRecipeManager().getRecipeFor(RecipeRegistry.MELTING_TYPE.get(), pBlockEntity.getCraftingInv(), pLevel);
                if (recipe.isPresent()) {
                    if (!recipe.get().value().equals(pBlockEntity.recipe)) {
                        pBlockEntity.workTime = 0;
                    }
                    pBlockEntity.recipe = recipe.get().value();
                    FluidStack assembled = recipe.get().value().getOutput().copy();
                    if (pBlockEntity.getFuelHandler().drain(1, IFluidHandler.FluidAction.SIMULATE).getAmount() >= 1 && pBlockEntity.outputFluidHandler.fill(assembled, IFluidHandler.FluidAction.SIMULATE) >= assembled.getAmount()) {
                        resetWorkTime = false;
                        pBlockEntity.workTime++;
                        pBlockEntity.drainCooldown++;
                        if (pBlockEntity.drainCooldown >= 5) {
                            pBlockEntity.drainCooldown = 0;
                            pBlockEntity.getFuelHandler().drain(1, IFluidHandler.FluidAction.EXECUTE);
                        }
                        pBlockEntity.getStorage().removeEssence(EssenceTypeRegistry.ESSENCE.get(), 1);
                        if (pBlockEntity.workTime >= recipe.get().value().getTime()) {
                            pBlockEntity.outputFluidHandler.fill(assembled, IFluidHandler.FluidAction.EXECUTE);
                            pBlockEntity.itemHandler.extractItem(0, 1, false);
                            pBlockEntity.workTime = 0;
                        }
                    }
                } else {
                    pBlockEntity.recipe = null;
                    pBlockEntity.drainCooldown = 0;
                }
            } else {
                pBlockEntity.recipe = null;
                pBlockEntity.drainCooldown = 0;
            }
            if (resetWorkTime) {
                pBlockEntity.workTime = -1;
            }
            pBlockEntity.updateBlock();
        }
    }
    protected void updateBlock() {
        BlockState blockState = level.getBlockState(this.getBlockPos());
        this.level.sendBlockUpdated(this.getBlockPos(), blockState, blockState, 3);
        this.setChanged();
    }
    @Override
    public Component getDisplayName() {
        return Component.empty();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new EntropicProcessorMenu(pContainerId, pInventory, this);
    }
}
