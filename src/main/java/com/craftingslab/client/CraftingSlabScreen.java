package com.craftingslab.client;

import com.craftingslab.menu.CraftingSlabMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.recipebook.CraftingRecipeBookComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Recipe-book crafting screen for the crafting slab.
 */
@OnlyIn(Dist.CLIENT)
public class CraftingSlabScreen extends AbstractRecipeBookScreen<CraftingSlabMenu> {
    private static final Identifier CRAFTING_TABLE_LOCATION =
            Identifier.withDefaultNamespace("textures/gui/container/crafting_table.png");

    public CraftingSlabScreen(CraftingSlabMenu menu, Inventory playerInventory, Component title) {
        super(menu, new CraftingRecipeBookComponent(menu), playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 29;
    }

    @Override
    protected ScreenPosition getRecipeBookButtonPosition() {
        return new ScreenPosition(this.leftPos + 5, this.height / 2 - 49);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, CRAFTING_TABLE_LOCATION, x, y, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }
}
