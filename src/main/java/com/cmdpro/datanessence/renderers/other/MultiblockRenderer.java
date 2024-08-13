package com.cmdpro.datanessence.renderers.other;

import com.cmdpro.datanessence.DataNEssence;
import com.cmdpro.datanessence.mixins.client.BufferSourceMixin;
import com.cmdpro.datanessence.multiblock.Multiblock;
import com.cmdpro.datanessence.shaders.DataNEssenceCoreShaders;
import com.cmdpro.datanessence.shaders.DataNEssenceRenderTypes;
import com.eliotlash.mclib.math.functions.limit.Min;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MultiblockRenderer {
    public static Rotation multiblockRotation;
    public static BlockPos multiblockPos;
    public static Multiblock multiblock;

    public static MultiBufferSource.BufferSource buffers;
    private static Map<BlockPos, BlockEntity> blockEntityCache = new Object2ObjectOpenHashMap<>();
    private static Set<BlockEntity> erroredBlockEntities = Collections.newSetFromMap(new WeakHashMap<>());
    public static void renderBlock(BlockState block, BlockPos pos, PoseStack stack, DeltaTracker partialTick) {
        if (buffers == null) {
            buffers = initBuffers(Minecraft.getInstance().renderBuffers().bufferSource());
        }
        renderBlock(block, pos, stack, partialTick, buffers);
    }
    public static void renderCurrentMultiblock(PoseStack stack, DeltaTracker partialTick) {
        if (multiblock != null) {
            if (multiblockPos == null) {
                if (Minecraft.getInstance().hitResult instanceof BlockHitResult result) {
                    renderMultiblock(multiblock, result.getBlockPos().relative(result.getDirection()), stack, partialTick, getRotation());
                }
            } else {
                renderMultiblock(multiblock, multiblockPos, stack, partialTick, multiblockRotation);
            }
        }
    }

    @NotNull
    public static Rotation getRotation() {
        Rotation rot = Rotation.NONE;
        if (Minecraft.getInstance().player.getDirection().equals(Direction.EAST)) {
            rot = Rotation.CLOCKWISE_90;
        }
        if (Minecraft.getInstance().player.getDirection().equals(Direction.SOUTH)) {
            rot = Rotation.CLOCKWISE_180;
        }
        if (Minecraft.getInstance().player.getDirection().equals(Direction.WEST)) {
            rot = Rotation.COUNTERCLOCKWISE_90;
        }
        return rot;
    }
    public static void renderMultiblock(Multiblock multiblock, BlockPos pos, PoseStack stack, DeltaTracker partialTick) {
        renderMultiblock(multiblock, pos, stack, partialTick, Rotation.NONE);
    }
    public static void renderMultiblock(Multiblock multiblock, BlockPos pos, PoseStack stack, DeltaTracker partialTick, Rotation rotation) {
        if (buffers == null) {
            buffers = initBuffers(Minecraft.getInstance().renderBuffers().bufferSource());
        }
        renderMultiblock(multiblock, pos, stack, partialTick, rotation, buffers);
    }
    public static void renderMultiblock(Multiblock multiblock, BlockPos pos, PoseStack stack, DeltaTracker partialTick, Rotation rotation, MultiBufferSource.BufferSource bufferSource) {
        BlockPos blockpos = pos;
        if (blockpos == null) {
            blockpos = new BlockPos(0, 0, 0);
        }
        for (List<Multiblock.StateAndPos> i : multiblock.getStates()) {
            for (Multiblock.StateAndPos o : i) {
                boolean stateMatches = false;
                if (pos != null) {
                    stateMatches = true;
                    BlockState state = Minecraft.getInstance().level.getBlockState(o.offset.offset(blockpos));
                    if (state.is(o.state.getBlock())) {
                        for (Property<?> p : o.state.getProperties()) {
                            if (state.hasProperty(p)) {
                                if (!state.getValue(p).equals(o.state.getValue(p))) {
                                    stateMatches = false;
                                    break;
                                }
                            } else {
                                stateMatches = false;
                                break;
                            }
                        }
                    } else {
                        stateMatches = false;
                    }
                }
                if (!stateMatches) {
                    renderBlock(o.state, o.offset.rotate(rotation).offset(blockpos), stack, partialTick, bufferSource);
                }
            }
        }
        bufferSource.endBatch();
    }
    public static void renderBlock(BlockState block, BlockPos pos, PoseStack stack, DeltaTracker partialTick, MultiBufferSource.BufferSource bufferSource) {
        stack.pushPose();
        stack.translate(pos.getX(), pos.getY(), pos.getZ());
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        FluidState fluidState = block.getFluidState();
        if (!fluidState.isEmpty()) {
            RenderType layer = ItemBlockRenderTypes.getRenderLayer(fluidState);
            VertexConsumer buffer = bufferSource.getBuffer(layer);
            blockRenderer.renderLiquid(pos, Minecraft.getInstance().level, buffer, block, fluidState);
        }
        if (block.getRenderShape() != RenderShape.INVISIBLE) {
            BakedModel model = blockRenderer.getBlockModel(block);
            for (RenderType i : model.getRenderTypes(block, Minecraft.getInstance().level.random, ModelData.EMPTY)) {
                VertexConsumer hologramConsumer = bufferSource.getBuffer(i);
                blockRenderer.renderBatched(block, pos, Minecraft.getInstance().level, stack, hologramConsumer, false, Minecraft.getInstance().level.random, ModelData.EMPTY, i);
            }
        }
        if (block.getBlock() instanceof EntityBlock entityBlock) {
            var be = blockEntityCache.computeIfAbsent(pos.immutable(), p -> entityBlock.newBlockEntity(p, block));
            if (be != null && !erroredBlockEntities.contains(be)) {
                be.setLevel(Minecraft.getInstance().level);

                // fake cached state in case the renderer checks it as we don't want to query the actual world
                be.setBlockState(block);


                try {
                    BlockEntityRenderer<BlockEntity> renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(be);
                    if (renderer != null) {
                        renderer.render(be, partialTick.getGameTimeDeltaTicks(), stack, buffers, 0xF000F0, OverlayTexture.NO_OVERLAY);
                    }
                } catch (Exception e) {
                    erroredBlockEntities.add(be);
                    DataNEssence.LOGGER.error("Error rendering block entity", e);
                }
            }
        }
        stack.popPose();
    }
    private static MultiBufferSource.BufferSource initBuffers(MultiBufferSource.BufferSource original) {
        BufferSourceMixin mixin = (BufferSourceMixin)original;
        var fallback = mixin.getSharedBuffer();
        var layerBuffers = mixin.getFixedBuffers();
        SequencedMap<RenderType, ByteBufferBuilder> remapped = new Object2ObjectLinkedOpenHashMap<>();
        for (Map.Entry<RenderType, ByteBufferBuilder> e : layerBuffers.entrySet()) {
            remapped.put(HologramRenderType.remap(e.getKey()), e.getValue());
        }
        return new HologramBuffers(fallback, remapped);
    }
    private static class HologramBuffers extends MultiBufferSource.BufferSource {
        protected HologramBuffers(ByteBufferBuilder fallback, SequencedMap<RenderType, ByteBufferBuilder> layerBuffers) {
            super(fallback, layerBuffers);
        }

        @Override
        public VertexConsumer getBuffer(RenderType type) {
            return super.getBuffer(HologramRenderType.remap(type));
        }
    }

    private static class HologramRenderType extends RenderType {
        private static final Map<RenderType, RenderType> remappedTypes = new IdentityHashMap<>();

        private HologramRenderType(RenderType original) {
            super(String.format("%s_%s_hologram", original.toString(), DataNEssence.MOD_ID), original.format(), original.mode(), original.bufferSize(), original.affectsCrumbling(), true, () -> {
                original.setupRenderState();

                RenderSystem.disableDepthTest();
                RenderSystem.enableBlend();
                RenderSystem.setShaderColor(1f, 1f, 1f, 0.5f);
            }, () -> {
                RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
                RenderSystem.disableBlend();
                RenderSystem.enableDepthTest();

                original.clearRenderState();
            });
        }

        public static RenderType remap(RenderType in) {
            if (in instanceof HologramRenderType) {
                return in;
            } else {
                return remappedTypes.computeIfAbsent(in, HologramRenderType::new);
            }
        }
    }
}
