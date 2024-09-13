package com.cmdpro.datanessence.screen;

import com.cmdpro.datanessence.DataNEssence;
import com.cmdpro.datanessence.api.DataNEssenceRegistries;
import com.cmdpro.datanessence.api.util.client.ClientEssenceBarUtil;
import com.cmdpro.datanessence.moddata.ClientPlayerData;
import com.cmdpro.datanessence.registry.EssenceTypeRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class EntropicProcessorScreen extends AbstractContainerScreen<EntropicProcessorMenu> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DataNEssence.MOD_ID, "textures/gui/entropic_processor.png");
    public EntropicProcessorScreen(EntropicProcessorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }
    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        pGuiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        if (menu.blockEntity.workTime >= 0) {
            pGuiGraphics.blit(TEXTURE, x + 82, y + 33, 188, 0, (int) Math.ceil(22f * ((float)menu.blockEntity.workTime / menu.blockEntity.recipe.getTime())), 17);
        }
        ClientEssenceBarUtil.drawEssenceBar(pGuiGraphics, x+8, y+17, EssenceTypeRegistry.ESSENCE.get(), menu.blockEntity.getStorage().getEssence(EssenceTypeRegistry.ESSENCE.get()), menu.blockEntity.getStorage().getMaxEssence());
        if (ClientPlayerData.getUnlockedEssences().getOrDefault(DataNEssenceRegistries.ESSENCE_TYPE_REGISTRY.getKey(EssenceTypeRegistry.ESSENCE.get()), false)) {
            pGuiGraphics.blit(TEXTURE, x+7, y+6, 177, 0, 9, 9);
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        List<FormattedCharSequence> component = new ArrayList<>();
        Component essence = ClientEssenceBarUtil.getEssenceBarTooltip(pMouseX, pMouseY, x+8, y+17, EssenceTypeRegistry.ESSENCE.get(), menu.blockEntity.getStorage().getEssence(EssenceTypeRegistry.ESSENCE.get()));
        if (essence != null) {
            component.clear();
            component.add(essence.getVisualOrderText());
        }
        if (component != null) {
            pGuiGraphics.renderTooltip(font, component, pMouseX, pMouseY);
        }
    }
}
