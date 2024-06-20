package com.cmdpro.datanessence.shaders;

import com.cmdpro.datanessence.DataNEssence;
import com.cmdpro.datanessence.shaders.system.ShaderHolder;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.ELEMENT_NORMAL;
import static com.mojang.blaze3d.vertex.DefaultVertexFormat.ELEMENT_POSITION;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = DataNEssence.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataNEssenceCoreShaders implements ResourceManagerReloadListener {
    public static List<ShaderHolder> SHADERS = new ArrayList<>();

    public static final VertexFormat POSITION_NORMAL = new VertexFormat(ImmutableMap.<String, VertexFormatElement>builder().put("Position", ELEMENT_POSITION).put("Normal", ELEMENT_NORMAL).build());
    public static ShaderHolder WARPINGPOINT = createShader("warping_point", POSITION_NORMAL);
    @Override
    public void onResourceManagerReload(ResourceManager pResourceManager) {

    }

    public static ShaderHolder createShader(String name, VertexFormat shaderFormat) {
        ShaderHolder shaderHolder = new ShaderHolder(new ResourceLocation(DataNEssence.MOD_ID, name), shaderFormat);
        SHADERS.add(shaderHolder);
        return shaderHolder;
    }

    @SubscribeEvent
    public static void shaderRegistry(RegisterShadersEvent event) throws IOException {
        ResourceProvider resourceProvider = event.getResourceProvider();

        for (ShaderHolder shader : SHADERS) {
            event.registerShader(shader.createInstance(resourceProvider), s -> {});
        }

    }
}
