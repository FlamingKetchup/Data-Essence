package com.cmdpro.datanessence.screen.datatablet.pages;

import com.cmdpro.datanessence.multiblock.Multiblock;
import com.cmdpro.datanessence.multiblock.MultiblockManager;
import com.cmdpro.datanessence.renderers.other.MultiblockRenderer;
import com.cmdpro.datanessence.screen.DataTabletScreen;
import com.cmdpro.datanessence.screen.datatablet.Page;
import com.cmdpro.datanessence.screen.datatablet.PageSerializer;
import com.cmdpro.datanessence.screen.datatablet.pages.serializers.MultiblockPageSerializer;
import com.cmdpro.datanessence.screen.datatablet.pages.serializers.TextPageSerializer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.block.Rotation;

import java.util.List;

public class MultiblockPage extends Page {
    public MultiblockPage(ResourceLocation multiblock) {
        this.multiblock = multiblock;
    }
    public ResourceLocation multiblock;
    @Override
    public void render(DataTabletScreen screen, GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY, int xOffset, int yOffset) {
        Multiblock multiblock = MultiblockManager.multiblocks.get(this.multiblock);
        pGuiGraphics.pose().pushPose();
        int sizeX = multiblock.multiblockLayers[0][0].length();
        int sizeY = multiblock.multiblockLayers.length;
        int sizeZ = multiblock.multiblockLayers[0].length;
        float maxX = 90;
        float maxY = 90;
        float diag = (float) Math.sqrt(sizeX * sizeX + sizeZ * sizeZ);
        float scaleX = maxX / diag;
        float scaleY = maxY / sizeY;
        float scale = -Math.min(scaleX, scaleY);
        pGuiGraphics.pose().translate(xOffset+(DataTabletScreen.imageWidth/2f), yOffset+(DataTabletScreen.imageHeight/2f), 100);
        pGuiGraphics.pose().scale(scale, scale, scale);
        pGuiGraphics.pose().translate(-(float) sizeX / 2, -(float) sizeY / 2, 0);
        float offX = (float) -sizeX / 2f;
        float offZ = (float) -sizeZ / 2f;
        pGuiGraphics.pose().mulPose(Axis.XP.rotationDegrees(-30));
        pGuiGraphics.pose().translate(-offX, 0, -offZ);
        pGuiGraphics.pose().mulPose(Axis.YP.rotationDegrees((((float)screen.ticks+Minecraft.getInstance().getTimer().getRealtimeDeltaTicks())/5f)%360f));
        pGuiGraphics.pose().mulPose(Axis.YP.rotationDegrees(-45));
        pGuiGraphics.pose().translate(offX, 0, offZ);
        pGuiGraphics.pose().translate(-multiblock.offset.getX(), -multiblock.offset.getY(), -multiblock.offset.getZ());
        MultiblockRenderer.renderMultiblock(multiblock, null, pGuiGraphics.pose(), Minecraft.getInstance().getTimer(), Rotation.NONE, Minecraft.getInstance().renderBuffers().bufferSource());
        pGuiGraphics.pose().popPose();
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(0, 0, 200);
        pGuiGraphics.blit(DataTabletScreen.TEXTUREMISC, xOffset+120, yOffset+142, 0, 38, 16, 16);
        pGuiGraphics.pose().popPose();
    }

    @Override
    public boolean onClick(DataTabletScreen screen, double pMouseX, double pMouseY, int pButton, int xOffset, int yOffset) {
        if (pMouseX >= xOffset+120 && pMouseX <= xOffset+120+16) {
            if (pMouseY >= yOffset+142 && pMouseY <= yOffset+142+16) {
                MultiblockRenderer.multiblockPos = null;
                MultiblockRenderer.multiblockRotation = null;
                MultiblockRenderer.multiblock = MultiblockRenderer.multiblock == null ? MultiblockManager.multiblocks.get(multiblock) : null;
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                Minecraft.getInstance().popGuiLayer();
                return true;
            }
        }
        return super.onClick(screen, pMouseX, pMouseY, pButton, xOffset, yOffset);
    }

    @Override
    public PageSerializer getSerializer() {
        return MultiblockPageSerializer.INSTANCE;
    }
}
