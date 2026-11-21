package com.craftingslab.menu;

import com.craftingslab.registry.ModMenuTypes;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.item.crafting.RecipeType;

/**
 * Furnace menu for the furnace slab.
 */
public class FurnaceSlabMenu extends AbstractFurnaceMenu {
    public FurnaceSlabMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
        this(containerId, playerInventory);
    }

    public FurnaceSlabMenu(int containerId, Inventory playerInventory) {
        super(
                ModMenuTypes.FURNACE_SLAB.get(),
                RecipeType.SMELTING,
                RecipePropertySet.FURNACE_INPUT,
                RecipeBookType.FURNACE,
                containerId,
                playerInventory);
    }

    public FurnaceSlabMenu(int containerId, Inventory playerInventory, Container furnaceContainer, ContainerData furnaceData) {
        super(
                ModMenuTypes.FURNACE_SLAB.get(),
                RecipeType.SMELTING,
                RecipePropertySet.FURNACE_INPUT,
                RecipeBookType.FURNACE,
                containerId,
                playerInventory,
                furnaceContainer,
                furnaceData);
    }
}
