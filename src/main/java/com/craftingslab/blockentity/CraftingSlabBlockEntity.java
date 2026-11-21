package com.craftingslab.blockentity;

import org.slf4j.Logger;

import com.craftingslab.menu.CraftingSlabMenu;
import com.craftingslab.registry.ModBlockEntities;
import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jspecify.annotations.Nullable;

/**
 * Stores the persistent 3x3 crafting grid and syncs it for surface rendering.
 */
public class CraftingSlabBlockEntity extends BlockEntity implements MenuProvider {
    public static final int GRID_SIZE = 9;
    private static final Component TITLE = Component.translatable("container.crafting");
    private static final Logger LOGGER = LogUtils.getLogger();

    private final ItemStackHandler items = new ItemStackHandler(GRID_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide()) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    };

    public CraftingSlabBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRAFTING_SLAB.get(), pos, state);
    }

    public ItemStackHandler getItems() {
        return items;
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if (this.level != null) {
            for (int i = 0; i < items.getSlots(); i++) {
                ItemStack stack = items.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    Containers.dropItemStack(this.level, pos.getX(), pos.getY(), pos.getZ(), stack);
                }
            }
        }
        super.preRemoveSideEffects(pos, state);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        items.serialize(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        items.deserialize(input);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        try (ProblemReporter.ScopedCollector collector = new ProblemReporter.ScopedCollector(this.problemPath(), LOGGER)) {
            TagValueOutput output = TagValueOutput.createWithContext(collector, registries);
            this.saveAdditional(output);
            return output.buildResult();
        }
    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new CraftingSlabMenu(containerId, playerInventory, this);
    }
}
