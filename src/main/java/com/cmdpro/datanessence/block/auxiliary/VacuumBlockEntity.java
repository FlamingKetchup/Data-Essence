package com.cmdpro.datanessence.block.auxiliary;

import com.cmdpro.datanessence.particle.*;
import com.cmdpro.datanessence.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import java.awt.*;
import java.util.List;

public class VacuumBlockEntity extends BlockEntity {
    public VacuumBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.VACUUM.get(), pPos, pBlockState);
    }
    public static void tick(Level world, BlockPos pos, BlockState pState, VacuumBlockEntity pBlockEntity) {
        if (!world.isClientSide) {
            List<ItemEntity> ents = world.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(pos.getCenter(), 20, 20, 20));
            for (ItemEntity i : ents) {
                i.setDeltaMovement(pos.getCenter().subtract(i.position()).normalize().multiply(0.2, 0.2, 0.2));
                i.hasImpulse = true;
            }
            List<ItemEntity> ents2 = world.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(pos.getCenter(), 3, 3, 3));
            IItemHandler itemHandler = world.getCapability(Capabilities.ItemHandler.BLOCK, pos.above(), Direction.DOWN);
            if (itemHandler != null) {
                for (ItemEntity i : ents2) {
                    ItemStack copy = i.getItem().copy();
                    if (!copy.isEmpty()) {
                        for (int o = 0; o < itemHandler.getSlots(); o++) {
                            ItemStack stack = itemHandler.getStackInSlot(o);
                            if (stack.is(copy.getItem()) || stack.isEmpty()) {
                                copy = itemHandler.insertItem(o, copy, false);
                                world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.7f, 0.3f);
                            }
                        }
                    }
                    i.setItem(copy);
                }
            }
        } else {
            Color[] enderColors = new Color[] { new Color(0x005c3c), new Color(0x033328), new Color(0x006653) };
            Color[] redstoneColors = new Color[] { new Color(0xE51F1F), new Color(0x9D0624), new Color(0xF14E32) };

            Vec3 pos1 = pos.getCenter().add(world.random.nextFloat()-0.5f, world.random.nextFloat()-0.5f, world.random.nextFloat()-0.5f);
            Vec3 vel1 = pos.getCenter().subtract(pos1).multiply(0.4f, 0.1f, 0.2f);
            Color color1 = enderColors[world.random.nextIntBetweenInclusive(0, enderColors.length-1)];

            Vec3 pos2 = pos.getCenter().add(world.random.nextFloat()-0.5f, world.random.nextFloat()-0.5f, world.random.nextFloat()-0.5f);
            Vec3 vel2 = pos.getCenter().subtract(pos2).multiply(0.4f, 0.1f, 0.2f);
            Color color2 = enderColors[world.random.nextIntBetweenInclusive(0, enderColors.length-1)];

            Vec3 pos3 = pos.getCenter();
            Vec3 vel3 = pos.getCenter().subtract(pos3).add(0.0f, 0.08f, 0.0f);
            Color color3 = redstoneColors[world.random.nextIntBetweenInclusive(0, redstoneColors.length-1)];

            world.addParticle(new RhombusParticleOptions(color1, false), pos1.x, pos1.y, pos1.z, vel1.x, vel1.y, vel1.z);
            world.addParticle(new SmallCircleParticleOptions(color2, false), pos2.x, pos2.y, pos2.z, vel2.x, vel2.y, vel2.z);
            if (world.random.nextInt() % 3 == 0)
            {
                world.addParticle(new MoteParticleOptions(color3, true), pos3.x, pos3.y, pos3.z, vel3.x, vel3.y, vel3.z);
            }
        }
    }
}
