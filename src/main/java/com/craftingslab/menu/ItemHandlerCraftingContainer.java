package com.craftingslab.menu;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

/**
 * CraftingContainer adapter over a NeoForge ItemStackHandler.
 */
public class ItemHandlerCraftingContainer implements CraftingContainer {
    private final AbstractContainerMenu menu;
    private final ItemStackHandler handler;
    private final int width;
    private final int height;

    public ItemHandlerCraftingContainer(AbstractContainerMenu menu, ItemStackHandler handler, int width, int height) {
        this.menu = menu;
        this.handler = handler;
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public List<ItemStack> getItems() {
        List<ItemStack> list = new ArrayList<>(handler.getSlots());
        for (int i = 0; i < handler.getSlots(); i++) {
            list.add(handler.getStackInSlot(i));
        }
        return list;
    }

    @Override
    public int getContainerSize() {
        return handler.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < handler.getSlots(); i++) {
            if (!handler.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return handler.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = handler.extractItem(slot, amount, false);
        if (!stack.isEmpty()) {
            menu.slotsChanged(this);
        }
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = handler.getStackInSlot(slot);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        handler.setStackInSlot(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        handler.setStackInSlot(slot, stack);
        menu.slotsChanged(this);
    }

    @Override
    public void setChanged() {
        menu.slotsChanged(this);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < handler.getSlots(); i++) {
            handler.setStackInSlot(i, ItemStack.EMPTY);
        }
        menu.slotsChanged(this);
    }

    @Override
    public void fillStackedContents(StackedItemContents contents) {
        for (int i = 0; i < handler.getSlots(); i++) {
            contents.accountSimpleStack(handler.getStackInSlot(i));
        }
    }
}
