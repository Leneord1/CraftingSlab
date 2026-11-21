package com.craftingslab.compat.jei;

import com.craftingslab.CraftingSlabMod;
import com.craftingslab.menu.CraftingSlabMenu;
import com.craftingslab.registry.ModBlocks;
import com.craftingslab.registry.ModMenuTypes;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class CraftingSlabJeiPlugin implements IModPlugin {
    private static final Identifier PLUGIN_ID = Identifier.fromNamespaceAndPath(CraftingSlabMod.MOD_ID, "jei_plugin");

    @Override
    public Identifier getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(
                CraftingSlabMenu.class,
                ModMenuTypes.CRAFTING_SLAB.get(),
                RecipeTypes.CRAFTING,
                CraftingSlabMenu.CRAFT_SLOT_START,
                CraftingSlabMenu.CRAFT_SLOT_COUNT,
                CraftingSlabMenu.INV_SLOT_START,
                36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CRAFTING_SLAB_ITEM.get()), RecipeTypes.CRAFTING);
    }
}
