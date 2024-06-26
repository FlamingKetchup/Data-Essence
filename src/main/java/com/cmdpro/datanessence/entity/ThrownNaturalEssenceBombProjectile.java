package com.cmdpro.datanessence.entity;

import com.cmdpro.datanessence.init.EntityInit;
import com.cmdpro.datanessence.init.ItemInit;
import com.cmdpro.datanessence.misc.LunarEssenceBombExplosion;
import com.cmdpro.datanessence.misc.NaturalEssenceBombExplosion;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownNaturalEssenceBombProjectile extends ThrowableItemProjectile {
    public ThrownNaturalEssenceBombProjectile(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public ThrownNaturalEssenceBombProjectile(LivingEntity pShooter, Level pLevel) {
        super(EntityInit.NATURAL_ESSENCE_BOMB.get(), pShooter, pLevel);
    }
    @Override
    protected Item getDefaultItem() {
        return ItemInit.NATURAL_ESSENCE_BOMB.get();
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!level().isClientSide) {
            Explosion.BlockInteraction explosion$blockinteraction1 = level().getGameRules().getBoolean(GameRules.RULE_BLOCK_EXPLOSION_DROP_DECAY) ? Explosion.BlockInteraction.DESTROY_WITH_DECAY : Explosion.BlockInteraction.DESTROY;

            Explosion.BlockInteraction explosion$blockinteraction = explosion$blockinteraction1;
            Explosion explosion = new NaturalEssenceBombExplosion(level(), this, null, null, pResult.getLocation().x, pResult.getLocation().y, pResult.getLocation().z, 5, false, explosion$blockinteraction);
            if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(level(), explosion)) return;
            explosion.explode();
            explosion.finalizeExplosion(true);
            for (ServerPlayer serverplayer : ((ServerLevel) level()).players()) {
                if (serverplayer.distanceToSqr(pResult.getLocation().x, pResult.getLocation().y, pResult.getLocation().z) < 4096.0D) {
                    serverplayer.connection.send(new ClientboundExplodePacket(pResult.getLocation().x, pResult.getLocation().y, pResult.getLocation().z, 5, explosion.getToBlow(), explosion.getHitPlayers().get(serverplayer)));
                }
            }
            remove(RemovalReason.KILLED);
        }
    }
}
