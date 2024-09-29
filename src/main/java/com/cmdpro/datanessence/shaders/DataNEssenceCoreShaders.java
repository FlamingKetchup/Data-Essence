package com.cmdpro.datanessence.shaders;

import com.cmdpro.datanessence.DataNEssence;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(value = Dist.CLIENT, modid = DataNEssence.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataNEssenceCoreShaders {
    public static final VertexFormat POSITION_NORMAL = VertexFormat.builder().add("Position", VertexFormatElement.POSITION).add("Normal", VertexFormatElement.NORMAL).build();
    public static ShaderInstance WARPING_POINT;
    public static ShaderInstance getWarpingPoint() {
        return WARPING_POINT;
    }
    @SubscribeEvent
    public static void shaderRegistry(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(DataNEssence.MOD_ID, "warping_point"), DefaultVertexFormat.PARTICLE), shader -> { WARPING_POINT = shader; });
    }
}
