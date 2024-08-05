package com.cmdpro.datanessence.block;

import com.cmdpro.datanessence.DataNEssence;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.UUID;

public class TraversiteRoad extends Block {
    public static final ResourceLocation TRAVERSITE_ROAD_SPEED_UUID = ResourceLocation.fromNamespaceAndPath(DataNEssence.MOD_ID, "traversite_road_speed");
    public float boost;
    public TraversiteRoad(Properties pProperties, float boost) {
        super(pProperties);
        this.boost = boost;
    }
}
