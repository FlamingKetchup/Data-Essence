package com.cmdpro.datanessence.screen.datatablet.pages.crafting.types;

import com.cmdpro.datanessence.registry.ItemRegistry;
import com.cmdpro.datanessence.moddata.ClientPlayerData;
import com.cmdpro.datanessence.recipe.InfusionRecipe;
import com.cmdpro.datanessence.registry.RecipeRegistry;
import com.cmdpro.datanessence.screen.DataTabletScreen;
import com.cmdpro.datanessence.screen.datatablet.pages.CraftingPage;
import com.cmdpro.datanessence.screen.datatablet.pages.crafting.CraftingType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

public class InfuserType extends CraftingType {
    @Override
    public void render(CraftingPage page, DataTabletScreen screen, GuiGraphics pGuiGraphics, int xOffset, int x, int yOffset, int y, Recipe recipe, int pMouseX, int pMouseY) {
        if (recipe instanceof InfusionRecipe recipe2) {
            pGuiGraphics.blit(DataTabletScreen.TEXTURECRAFTING, xOffset + x, yOffset + y, 10, 136, 123, 60);
            page.renderIngredientWithTooltip(screen, pGuiGraphics, Ingredient.of(ItemRegistry.INFUSER_ITEM.get()), xOffset + x + 58, yOffset + y + 4, pMouseX, pMouseY);
            if (recipe2.getEssenceCost() > 0) {
                pGuiGraphics.blit(DataTabletScreen.TEXTURECRAFTING, x+xOffset+5, y+yOffset+28-(int)Math.ceil(22f*(recipe2.getEssenceCost()/1000f)), 6, 224-(int)Math.ceil(22f*(recipe2.getEssenceCost()/1000f)), 3, (int)Math.ceil(22f*(recipe2.getEssenceCost()/1000f)));
                if (pMouseX >= x+xOffset+5 && pMouseY >= y+yOffset+(28-22)) {
                    if (pMouseX <= x+xOffset+8 && pMouseY <= y+yOffset+28) {
                        page.showTooltip = true;
                        page.tooltipToShow.clear();
                        if (ClientPlayerData.getUnlockedEssences()[0]) {
                            page.tooltipToShow.add(Component.translatable("item.datanessence.data_tablet.page_type.crafting.fabricator.essence", recipe2.getEssenceCost()).getVisualOrderText());
                        } else {
                            page.tooltipToShow.add(Component.translatable("item.datanessence.data_tablet.page_type.crafting.fabricator.unknown", recipe2.getEssenceCost()).getVisualOrderText());
                        }
                    }
                }
            }
            if (recipe2.getLunarEssenceCost() > 0) {
                pGuiGraphics.blit(DataTabletScreen.TEXTURECRAFTING, x+xOffset+13, y+yOffset+28-(int)Math.ceil(22f*(recipe2.getLunarEssenceCost()/1000f)), 1, 224-(int)Math.ceil(22f*(recipe2.getLunarEssenceCost()/1000f)), 3, (int)Math.ceil(22f*(recipe2.getLunarEssenceCost()/1000f)));
                if (pMouseX >= x+xOffset+13 && pMouseY >= y+yOffset+(28-22)) {
                    if (pMouseX <= x+xOffset+16 && pMouseY <= y+yOffset+28) {
                        page.showTooltip = true;
                        page.tooltipToShow.clear();
                        if (ClientPlayerData.getUnlockedEssences()[1]) {
                            page.tooltipToShow.add(Component.translatable("item.datanessence.data_tablet.page_type.crafting.fabricator.lunar_essence", recipe2.getLunarEssenceCost()).getVisualOrderText());
                        } else {
                            page.tooltipToShow.add(Component.translatable("item.datanessence.data_tablet.page_type.crafting.fabricator.unknown", recipe2.getLunarEssenceCost()).getVisualOrderText());
                        }
                    }
                }
            }
            if (recipe2.getNaturalEssenceCost() > 0) {
                pGuiGraphics.blit(DataTabletScreen.TEXTURECRAFTING, x+xOffset+5, y+yOffset+54-(int)Math.ceil(22f*(recipe2.getNaturalEssenceCost()/1000f)), 6, 248-(int)Math.ceil(22f*(recipe2.getNaturalEssenceCost()/1000f)), 3, (int)Math.ceil(22f*(recipe2.getNaturalEssenceCost()/1000f)));
                if (pMouseX >= x+xOffset+5 && pMouseY >= y+yOffset+(54-22)) {
                    if (pMouseX <= x+xOffset+8 && pMouseY <= y+yOffset+54) {
                        page.showTooltip = true;
                        page.tooltipToShow.clear();
                        if (ClientPlayerData.getUnlockedEssences()[2]) {
                            page.tooltipToShow.add(Component.translatable("item.datanessence.data_tablet.page_type.crafting.fabricator.natural_essence", recipe2.getNaturalEssenceCost()).getVisualOrderText());
                        } else {
                            page.tooltipToShow.add(Component.translatable("item.datanessence.data_tablet.page_type.crafting.fabricator.unknown", recipe2.getNaturalEssenceCost()).getVisualOrderText());
                        }
                    }
                }
            }
            if (recipe2.getExoticEssenceCost() > 0) {
                pGuiGraphics.blit(DataTabletScreen.TEXTURECRAFTING, x+xOffset+13, y+yOffset+54-(int)Math.ceil(22f*(recipe2.getExoticEssenceCost()/1000f)), 1, 248-(int)Math.ceil(22f*(recipe2.getExoticEssenceCost()/1000f)), 3, (int)Math.ceil(22f*(recipe2.getExoticEssenceCost()/1000f)));
                if (pMouseX >= x+xOffset+13 && pMouseY >= y+yOffset+(54-22)) {
                    if (pMouseX <= x+xOffset+16 && pMouseY <= y+yOffset+54) {
                        page.showTooltip = true;
                        page.tooltipToShow.clear();
                        if (ClientPlayerData.getUnlockedEssences()[3]) {
                            page.tooltipToShow.add(Component.translatable("item.datanessence.data_tablet.page_type.crafting.fabricator.exotic_essence", recipe2.getExoticEssenceCost()).getVisualOrderText());
                        } else {
                            page.tooltipToShow.add(Component.translatable("item.datanessence.data_tablet.page_type.crafting.fabricator.unknown", recipe2.getExoticEssenceCost()).getVisualOrderText());
                        }
                    }
                }
            }
            page.renderItemWithTooltip(pGuiGraphics, recipe.getResultItem(RegistryAccess.EMPTY), xOffset + x + 82, yOffset + y + 22, pMouseX, pMouseY);
            page.renderIngredientWithTooltip(screen, pGuiGraphics, recipe2.getIngredients().get(0), xOffset + x + 38, yOffset + y + 22, pMouseX, pMouseY);
        }
    }

    @Override
    public boolean isRecipeType(Recipe recipe) {
        return recipe.getType().equals(RecipeRegistry.INFUSION_TYPE.get());
    }
}
