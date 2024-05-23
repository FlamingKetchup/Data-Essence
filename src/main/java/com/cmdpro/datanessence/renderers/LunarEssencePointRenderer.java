package com.cmdpro.datanessence.renderers;

import com.cmdpro.datanessence.DataNEssence;
import com.cmdpro.datanessence.block.entity.EssencePointBlockEntity;
import com.cmdpro.datanessence.block.entity.LunarEssencePointBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;


public class LunarEssencePointRenderer extends BaseEssencePointRenderer<LunarEssencePointBlockEntity> {
    EntityRenderDispatcher renderDispatcher;

    public LunarEssencePointRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super(new Model(new ResourceLocation(DataNEssence.MOD_ID, "textures/block/lunaressencepoint.png")));
        renderDispatcher = rendererProvider.getEntityRenderer();
    }
}