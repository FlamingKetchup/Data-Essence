package com.cmdpro.datanessence.screen.datatablet.pages.crafting.types;

import com.cmdpro.datanessence.api.ClientDataNEssenceUtil;
import com.cmdpro.datanessence.registry.ItemRegistry;
import com.cmdpro.datanessence.registry.RecipeRegistry;
import com.cmdpro.datanessence.moddata.ClientPlayerData;
import com.cmdpro.datanessence.recipe.IFabricationRecipe;
import com.cmdpro.datanessence.recipe.ShapedFabricationRecipe;
import com.cmdpro.datanessence.recipe.ShapelessFabricationRecipe;
import com.cmdpro.datanessence.screen.DataTabletScreen;
import com.cmdpro.datanessence.screen.datatablet.pages.CraftingPage;
import com.cmdpro.datanessence.screen.datatablet.pages.crafting.CraftingType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.*;

public class FabricatorType extends CraftingType {
    @Override
    public void render(CraftingPage page, DataTabletScreen screen, GuiGraphics pGuiGraphics, int xOffset, int x, int yOffset, int y, Recipe recipe, int pMouseX, int pMouseY) {
        if (recipe instanceof IFabricationRecipe recipe2) {
            pGuiGraphics.blit(DataTabletScreen.TEXTURECRAFTING, xOffset + x, yOffset + y, 10, 196, 123, 60);
            page.renderIngredientWithTooltip(screen, pGuiGraphics, Ingredient.of(ItemRegistry.FABRICATOR_ITEM.get()), xOffset + x + 98, yOffset + y + 43, pMouseX, pMouseY);

            ClientDataNEssenceUtil.EssenceBarRendering.drawEssenceBarTiny(pGuiGraphics, 5, 28-22, 0, recipe2.getEssenceCost(), 1000);
            ClientDataNEssenceUtil.EssenceBarRendering.drawEssenceBarTiny(pGuiGraphics, 13, 28-22, 1, recipe2.getLunarEssenceCost(), 1000);
            ClientDataNEssenceUtil.EssenceBarRendering.drawEssenceBarTiny(pGuiGraphics, 5, 54-22, 2, recipe2.getNaturalEssenceCost(), 1000);
            ClientDataNEssenceUtil.EssenceBarRendering.drawEssenceBarTiny(pGuiGraphics, 13, 54-22, 3, recipe2.getExoticEssenceCost(), 1000);

            Component essence = ClientDataNEssenceUtil.EssenceBarRendering.getEssenceBarTooltipTiny(pMouseX, pMouseY, 5, 28-22, 0, recipe2.getEssenceCost());
            if (essence != null) {
                page.tooltipToShow.clear();
                page.tooltipToShow.add(essence.getVisualOrderText());
            }
            Component lunarEssence = ClientDataNEssenceUtil.EssenceBarRendering.getEssenceBarTooltipTiny(pMouseX, pMouseY, 13, 28-22, 1, recipe2.getLunarEssenceCost());
            if (lunarEssence != null) {
                page.tooltipToShow.clear();
                page.tooltipToShow.add(lunarEssence.getVisualOrderText());
            }
            Component naturalEssence = ClientDataNEssenceUtil.EssenceBarRendering.getEssenceBarTooltipTiny(pMouseX, pMouseY, 5, 54-22, 2, recipe2.getNaturalEssenceCost());
            if (naturalEssence != null) {
                page.tooltipToShow.clear();
                page.tooltipToShow.add(naturalEssence.getVisualOrderText());
            }
            Component exoticEssence = ClientDataNEssenceUtil.EssenceBarRendering.getEssenceBarTooltipTiny(pMouseX, pMouseY, 13, 54-22, 3, recipe2.getExoticEssenceCost());
            if (exoticEssence != null) {
                page.tooltipToShow.clear();
                page.tooltipToShow.add(exoticEssence.getVisualOrderText());
            }

            page.renderItemWithTooltip(pGuiGraphics, recipe.getResultItem(RegistryAccess.EMPTY), xOffset + x + 98, yOffset + y + 22, pMouseX, pMouseY);
            if (recipe2 instanceof ShapelessFabricationRecipe) {
                pGuiGraphics.blit(DataTabletScreen.TEXTURECRAFTING, xOffset + x + 93, yOffset + y + 4, 242, 185, 14, 11);
            }
            int x2 = 1;
            int y2 = 1;
            int p = 0;
            int wrap = 3;
            if (recipe2 instanceof ShapedFabricationRecipe shaped) {
                wrap = shaped.getWidth();
            }
            for (Ingredient o : recipe2.getIngredients()) {
                page.renderIngredientWithTooltip(screen, pGuiGraphics, o, xOffset + x + 20 + x2, yOffset + y + 4 + y2, pMouseX, pMouseY);
                x2 += 17;
                p++;
                if (p >= wrap) {
                    p = 0;
                    x2 = 1;
                    y2 += 17;
                }
            }
        }
    }

    @Override
    public boolean isRecipeType(Recipe recipe) {
        return recipe.getType().equals(RecipeRegistry.FABRICATIONCRAFTING.get());
    }
}
