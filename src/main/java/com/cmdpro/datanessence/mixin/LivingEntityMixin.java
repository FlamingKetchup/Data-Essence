package com.cmdpro.datanessence.mixin;

import com.cmdpro.datanessence.item.equipment.TraversiteTrudgers;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    /**
     * The Traversite Trudgers prevent movement slowdowns. This actually carries that out, for preventing slipping on ice, slime, and other such blocks.
     */
    @ModifyExpressionValue(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getFriction(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)F"))
    private float datanessence$preventSlipping(float original) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if ( TraversiteTrudgers.areTrudgersEquipped(entity) )
            return 0.6f; // default friction of blocks

        return original;
    }
}
