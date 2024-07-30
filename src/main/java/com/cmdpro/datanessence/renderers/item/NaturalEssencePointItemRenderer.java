package com.cmdpro.datanessence.renderers.item;

import com.cmdpro.datanessence.DataNEssence;
import com.cmdpro.datanessence.item.blockitem.NaturalEssencePointItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class NaturalEssencePointItemRenderer extends GeoItemRenderer<NaturalEssencePointItem> {
    public NaturalEssencePointItemRenderer() {
        super(new Model());
    }
    public static class Model extends GeoModel<NaturalEssencePointItem> {
        @Override
        public ResourceLocation getModelResource(NaturalEssencePointItem object) {
            return new ResourceLocation(DataNEssence.MOD_ID, "geo/essence_point.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(NaturalEssencePointItem object) {
            return new ResourceLocation(DataNEssence.MOD_ID, "textures/block/natural_essence_point.png");
        }

        @Override
        public ResourceLocation getAnimationResource(NaturalEssencePointItem animatable) {
            return new ResourceLocation(DataNEssence.MOD_ID, "animations/essence_point.animation.json");
        }
    }
}