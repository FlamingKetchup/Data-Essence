package com.cmdpro.datanessence.item.blockitem;

import com.cmdpro.datanessence.renderers.item.IndustrialPlantSiphonItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class IndustrialPlantSiphonItem extends BlockItem {
    public IndustrialPlantSiphonItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    public static IClientItemExtensions extensions() {
        return new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    renderer = new IndustrialPlantSiphonItemRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
                }
                return renderer;
            }
        };
    }
}
