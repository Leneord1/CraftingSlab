package com.craftingslab.blockentity;

import com.craftingslab.menu.FurnaceSlabMenu;
import com.craftingslab.registry.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Furnace block entity backed by vanilla smelting recipes.
 */
public class FurnaceSlabBlockEntity extends AbstractFurnaceBlockEntity {
    private static final Component DEFAULT_NAME = Component.translatable("container.furnace");

    public FurnaceSlabBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FURNACE_SLAB.get(), pos, state, RecipeType.SMELTING);
    }

    @Override
    protected Component getDefaultName() {
        return DEFAULT_NAME;
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return new FurnaceSlabMenu(id, player, this, this.dataAccess);
    }
}
