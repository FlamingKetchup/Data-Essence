package com.cmdpro.datanessence.screen.datatablet.pages.crafting.types;

import com.cmdpro.datanessence.init.RecipeInit;
import com.cmdpro.datanessence.recipe.IFabricationRecipe;
import com.cmdpro.datanessence.recipe.ShapedFabricationRecipe;
import com.cmdpro.datanessence.recipe.ShapelessFabricationRecipe;
import com.cmdpro.datanessence.screen.DataTabletScreen;
import com.cmdpro.datanessence.screen.datatablet.pages.CraftingPage;
import com.cmdpro.datanessence.screen.datatablet.pages.crafting.CraftingType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;

public class FabricatorType extends CraftingType {
    @Override
    public void render(CraftingPage page, DataTabletScreen screen, GuiGraphics pGuiGraphics, int xOffset, int x, int yOffset, int y, Recipe recipe, int pMouseX, int pMouseY) {
        if (recipe instanceof IFabricationRecipe recipe2) {
            pGuiGraphics.blit(DataTabletScreen.TEXTURECRAFTING, xOffset + x, yOffset + y, 10, 196, 123, 60);
            page.renderIngredientWithTooltip(screen, pGuiGraphics, Ingredient.of(Items.CRAFTING_TABLE), xOffset + x + 98, yOffset + y + 43, pMouseX, pMouseY);
            if (recipe2.getEssenceCost() > 0) {
                pGuiGraphics.fill(x+xOffset+5, y+yOffset+28-(int)Math.ceil(22f*(recipe2.getEssenceCost()/1000f)), x+xOffset+8, y+yOffset+28, 0xFFFF00FF);
                if (pMouseX >= x+xOffset+5 && pMouseY >= y+yOffset+(28-22)) {
                    if (pMouseX <= x+xOffset+8 && pMouseY <= y+yOffset+28) {
                        page.showTooltip = true;
                        page.tooltipToShow.clear();
                        page.tooltipToShow.add(Component.translatable("item.datanessence.datatablet.pagetype.crafting.fabricator.essence", recipe2.getEssenceCost()).getVisualOrderText());
                    }
                }
            }
            if (recipe2.getLunarEssenceCost() > 0) {
                pGuiGraphics.fill(x+xOffset+13, y+yOffset+28-(int)Math.ceil(22f*(recipe2.getLunarEssenceCost()/1000f)), x+xOffset+16, y+yOffset+28, 0xFFAAAAFF);
                if (pMouseX >= x+xOffset+13 && pMouseY >= y+yOffset+(28-22)) {
                    if (pMouseX <= x+xOffset+16 && pMouseY <= y+yOffset+28) {
                        page.showTooltip = true;
                        page.tooltipToShow.clear();
                        page.tooltipToShow.add(Component.translatable("item.datanessence.datatablet.pagetype.crafting.fabricator.lunaressence", recipe2.getLunarEssenceCost()).getVisualOrderText());
                    }
                }
            }
            if (recipe2.getNaturalEssenceCost() > 0) {
                pGuiGraphics.fill(x+xOffset+5, y+yOffset+54-(int)Math.ceil(22f*(recipe2.getNaturalEssenceCost()/1000f)), x+xOffset+8, y+yOffset+54, 0xFF00FF00);
                if (pMouseX >= x+xOffset+5 && pMouseY >= y+yOffset+(54-22)) {
                    if (pMouseX <= x+xOffset+8 && pMouseY <= y+yOffset+54) {
                        page.showTooltip = true;
                        page.tooltipToShow.clear();
                        page.tooltipToShow.add(Component.translatable("item.datanessence.datatablet.pagetype.crafting.fabricator.naturalessence", recipe2.getNaturalEssenceCost()).getVisualOrderText());
                    }
                }
            }
            if (recipe2.getExoticEssenceCost() > 0) {
                pGuiGraphics.fill(x+xOffset+13, y+yOffset+54-(int)Math.ceil(22f*(recipe2.getExoticEssenceCost()/1000f)), x+xOffset+16, y+yOffset+54, 0xFFFFFFFF);
                if (pMouseX >= x+xOffset+13 && pMouseY >= y+yOffset+(54-22)) {
                    if (pMouseX <= x+xOffset+16 && pMouseY <= y+yOffset+54) {
                        page.showTooltip = true;
                        page.tooltipToShow.clear();
                        page.tooltipToShow.add(Component.translatable("item.datanessence.datatablet.pagetype.crafting.fabricator.exoticessence", recipe2.getExoticEssenceCost()).getVisualOrderText());
                    }
                }
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
        return recipe.getType().equals(RecipeInit.FABRICATIONCRAFTING.get());
    }
}
