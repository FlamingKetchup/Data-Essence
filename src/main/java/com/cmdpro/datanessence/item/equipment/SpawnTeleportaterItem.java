package com.cmdpro.datanessence.item.equipment;

import com.cmdpro.datanessence.api.CommonVariables;
import com.cmdpro.datanessence.registry.MobEffectRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.apache.commons.lang3.RandomUtils;
import org.joml.Math;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class SpawnTeleportaterItem extends Item {
    public SpawnTeleportaterItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getUseDuration(ItemStack pStack, LivingEntity p_344979_) {
        return 60;
    }
    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        pPlayer.startUsingItem(pHand);
        return InteractionResultHolder.consume(itemstack);
    }
    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
        if (pLevel.isClientSide) {
            for (int i = 0; i < 10; i++) {
                Vec3 pos = pLivingEntity.getBoundingBox().getCenter();
                Vec3 posWithOffset = pos.offsetRandom(pLivingEntity.getRandom(), 3);
                Vec3 dir = pos.subtract(posWithOffset).multiply(0.05, 0.05, 0.05);
                pLevel.addParticle(ParticleTypes.DRAGON_BREATH, posWithOffset.x, posWithOffset.y, posWithOffset.z, dir.x, dir.y, dir.z);
            }
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide) {
            if (pLivingEntity instanceof Player player) {
                DimensionTransition dimensiontransition = findRespawnPositionAndUseSpawnBlock((ServerPlayer) player, DimensionTransition.DO_NOTHING);
                if (player.level().dimension().equals(dimensiontransition.newLevel().dimension())) {
                    if (player.position().distanceTo(dimensiontransition.pos()) <= 10000) {
                        pLevel.playSound(null, pLivingEntity.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS);
                        pLevel.playSound(null, pLivingEntity.blockPosition(), SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS);
                        Vec3 vec3 = dimensiontransition.pos();
                        player.teleportTo(vec3.x, vec3.y, vec3.z);
                        pLevel.playSound(null, BlockPos.containing(vec3), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS);
                        pLevel.playSound(null, BlockPos.containing(vec3), SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS);
                        pStack.consume(1, player);
                    } else {
                        player.sendSystemMessage(Component.translatable("item.datanessence.spawn_teleporter.too_far"));
                    }
                } else {
                    player.sendSystemMessage(Component.translatable("item.datanessence.spawn_teleporter.wrong_dimension"));
                }
            }
        }
        return pStack;
    }
    private DimensionTransition findRespawnPositionAndUseSpawnBlock(ServerPlayer player, DimensionTransition.PostDimensionTransition pPostDimensionTransition) {
        BlockPos blockpos = player.getRespawnPosition();
        float f = player.getRespawnAngle();
        ServerLevel serverlevel = player.getServer().getLevel(player.getRespawnDimension());
        if (serverlevel != null && blockpos != null) {
            Optional<ServerPlayer.RespawnPosAngle> optional = findRespawnAndUseSpawnBlock(serverlevel, blockpos, f);
            if (optional.isPresent()) {
                ServerPlayer.RespawnPosAngle serverplayer$respawnposangle = optional.get();
                return new DimensionTransition(
                        serverlevel, serverplayer$respawnposangle.position(), Vec3.ZERO, serverplayer$respawnposangle.yaw(), 0.0F, pPostDimensionTransition
                );
            } else {
                return DimensionTransition.missingRespawnBlock(player.server.overworld(), player, pPostDimensionTransition);
            }
        } else {
            return new DimensionTransition(player.server.overworld(), player, pPostDimensionTransition);
        }
    }
    private static Optional<ServerPlayer.RespawnPosAngle> findRespawnAndUseSpawnBlock(ServerLevel pLevel, BlockPos pPos, float pAngle) {
        BlockState blockstate = pLevel.getBlockState(pPos);
        Block block = blockstate.getBlock();
        if (block instanceof RespawnAnchorBlock
                && (blockstate.getValue(RespawnAnchorBlock.CHARGE) > 0)
                && RespawnAnchorBlock.canSetSpawn(pLevel)) {
            Optional<Vec3> optional = RespawnAnchorBlock.findStandUpPosition(EntityType.PLAYER, pLevel, pPos);
            return optional.map(p_348139_ -> ServerPlayer.RespawnPosAngle.of(p_348139_, pPos));
        } else if (block instanceof BedBlock && BedBlock.canSetSpawn(pLevel)) {
            return BedBlock.findStandUpPosition(EntityType.PLAYER, pLevel, pPos, blockstate.getValue(BedBlock.FACING), pAngle)
                    .map(p_348148_ -> ServerPlayer.RespawnPosAngle.of(p_348148_, pPos));
        } else {
            return blockstate.getRespawnPosition(EntityType.PLAYER, pLevel, pPos, pAngle);
        }
    }
}
