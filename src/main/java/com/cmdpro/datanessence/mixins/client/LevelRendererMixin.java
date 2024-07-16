package com.cmdpro.datanessence.mixins.client;

import com.cmdpro.datanessence.api.ClientDataNEssenceUtil;
import com.cmdpro.datanessence.shaders.system.ShaderInstance;
import com.cmdpro.datanessence.shaders.system.ShaderManager;
import com.google.common.collect.Queues;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Inject(method = "renderLevel", at = @At(value = "HEAD"), remap = false)
    private void renderLevel(float pPartialTick,
                             long pNanoTime,
                             boolean pRenderBlockOutline,
                             Camera pCamera,
                             GameRenderer pGameRenderer,
                             LightTexture pLightTexture,
                             Matrix4f pFrustumMatrix,
                             Matrix4f pProjectionMatrix, CallbackInfo ci) {
        Matrix4fStack stack = RenderSystem.getModelViewStack();
        stack.pushMatrix();
        if (Minecraft.getInstance().options.bobView().get()) {
            unbobView(stack, pPartialTick);
        }
        stack.mul(pFrustumMatrix);
        ClientDataNEssenceUtil.viewStackMatrix = new Matrix4f(stack);
        stack.popMatrix();
    }
    private void unbobView(Matrix4fStack pPoseStack, float pPartialTicks) {
        if (Minecraft.getInstance().getCameraEntity() instanceof Player) {
            Player player = (Player)Minecraft.getInstance().getCameraEntity();
            float f = player.walkDist - player.walkDistO;
            float f1 = -(player.walkDist + f * pPartialTicks);
            float f2 = Mth.lerp(pPartialTicks, player.oBob, player.bob);
            pPoseStack.rotate(Axis.XP.rotationDegrees(Math.abs(Mth.cos(f1 * (float)Math.PI - 0.2F) * f2) * 5.0F).invert());
            pPoseStack.rotate(Axis.ZP.rotationDegrees(Mth.sin(f1 * (float)Math.PI) * f2 * 3.0F).invert());
            pPoseStack.translate(-(Mth.sin(f1 * (float)Math.PI) * f2 * 0.5F), -(-Math.abs(Mth.cos(f1 * (float)Math.PI) * f2)), 0.0F);
        }
    }
}