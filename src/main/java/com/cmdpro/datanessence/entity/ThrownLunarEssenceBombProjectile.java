package com.cmdpro.datanessence.entity;

import com.cmdpro.datanessence.init.EntityInit;
import com.cmdpro.datanessence.init.ItemInit;
import com.cmdpro.datanessence.misc.LunarEssenceBombExplosion;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class ThrownLunarEssenceBombProjectile extends ThrowableItemProjectile {
    public ThrownLunarEssenceBombProjectile(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public ThrownLunarEssenceBombProjectile(LivingEntity pShooter, Level pLevel) {
        super(EntityInit.LUNAR_ESSENCE_BOMB.get(), pShooter, pLevel);
    }
    @Override
    protected Item getDefaultItem() {
        return ItemInit.LUNAR_ESSENCE_BOMB.get();
    }
    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!level().isClientSide) {
            Explosion.BlockInteraction explosion$blockinteraction1 = Explosion.BlockInteraction.KEEP;

            Explosion.BlockInteraction explosion$blockinteraction = explosion$blockinteraction1;
            Explosion explosion = new LunarEssenceBombExplosion(level(), this, null, null, pResult.getLocation().x, pResult.getLocation().y, pResult.getLocation().z, 5, false, explosion$blockinteraction);
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
