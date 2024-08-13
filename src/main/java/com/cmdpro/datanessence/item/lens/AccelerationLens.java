package com.cmdpro.datanessence.item.lens;

import com.cmdpro.datanessence.api.item.ILaserEmitterModule;
import com.cmdpro.datanessence.block.auxiliary.LaserEmitter;
import com.cmdpro.datanessence.block.auxiliary.LaserEmitterBlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.joml.Vector3f;

public class AccelerationLens extends Item implements ILaserEmitterModule {
    public AccelerationLens(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void applyToMob(LaserEmitterBlockEntity ent, LivingEntity entity) {
        if (entity instanceof Player player) {
            player.hurtMarked = true;
        }
        Vector3f vec = ent.getBlockState().getValue(LaserEmitter.FACING).step();
        entity.push(vec.x, vec.y, vec.z);
    }
}
