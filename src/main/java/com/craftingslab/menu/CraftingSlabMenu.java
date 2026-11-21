package com.craftingslab.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.craftingslab.blockentity.CraftingSlabBlockEntity;
import com.craftingslab.registry.ModBlocks;
import com.craftingslab.registry.ModMenuTypes;
import com.jomlom.nearbycrafting.platform.Services;
import com.jomlom.recipebookaccess.api.RecipeBookInventoryProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractCraftingMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jspecify.annotations.Nullable;

/**
 * Persistent crafting-slab menu with Nearby Crafting recipe-book autofill.
 */
public class CraftingSlabMenu extends AbstractCraftingMenu implements RecipeBookInventoryProvider {
    public static final int RESULT_SLOT = 0;
    public static final int CRAFT_SLOT_START = 1;
    public static final int CRAFT_SLOT_COUNT = 9;
    public static final int CRAFT_SLOT_END = 10;
    public static final int INV_SLOT_START = 10;
    public static final int INV_SLOT_END = 37;
    public static final int USE_ROW_SLOT_START = 37;
    public static final int USE_ROW_SLOT_END = 46;

    private final ContainerLevelAccess access;
    private final Player player;
    private final CraftingSlabBlockEntity blockEntity;
    private boolean placingRecipe;
    private boolean syncingFromBlockEntity;

    public CraftingSlabMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
        this(containerId, playerInventory, getBlockEntity(playerInventory, data.readBlockPos()));
    }

    public CraftingSlabMenu(int containerId, Inventory playerInventory, CraftingSlabBlockEntity blockEntity) {
        super(ModMenuTypes.CRAFTING_SLAB.get(), containerId, 3, 3);
        this.blockEntity = blockEntity;
        this.player = playerInventory.player;
        this.access = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
        this.addResultSlot(this.player, 124, 35);
        this.addCraftingGridSlots(30, 17);
        this.addStandardInventorySlots(playerInventory, 8, 84);
        this.loadFromBlockEntity();
        this.slotsChanged(this.craftSlots);
    }

    private static CraftingSlabBlockEntity getBlockEntity(Inventory inventory, BlockPos pos) {
        BlockEntity blockEntity = inventory.player.level().getBlockEntity(pos);
        if (blockEntity instanceof CraftingSlabBlockEntity craftingSlab) {
            return craftingSlab;
        }
        // Client fallback before sync; keep a local handler so the menu can open.
        return new CraftingSlabBlockEntity(pos, ModBlocks.CRAFTING_SLAB.get().defaultBlockState());
    }

    private void loadFromBlockEntity() {
        this.syncingFromBlockEntity = true;
        ItemStackHandler items = this.blockEntity.getItems();
        for (int i = 0; i < CRAFT_SLOT_COUNT; i++) {
            this.craftSlots.setItem(i, items.getStackInSlot(i).copy());
        }
        this.syncingFromBlockEntity = false;
    }

    private void syncToBlockEntity() {
        if (this.syncingFromBlockEntity) {
            return;
        }
        ItemStackHandler items = this.blockEntity.getItems();
        for (int i = 0; i < CRAFT_SLOT_COUNT; i++) {
            items.setStackInSlot(i, this.craftSlots.getItem(i).copy());
        }
    }

    protected static void slotChangedCraftingGrid(
            AbstractCraftingMenu menu,
            ServerLevel level,
            Player player,
            CraftingContainer craftSlots,
            ResultContainer resultSlots,
            @Nullable RecipeHolder<CraftingRecipe> recipe) {
        CraftingInput input = craftSlots.asCraftInput();
        ServerPlayer serverPlayer = (ServerPlayer) player;
        ItemStack result = ItemStack.EMPTY;
        Optional<RecipeHolder<CraftingRecipe>> optional = level.getServer()
                .getRecipeManager()
                .getRecipeFor(RecipeType.CRAFTING, input, level, recipe);
        if (optional.isPresent()) {
            RecipeHolder<CraftingRecipe> holder = optional.get();
            CraftingRecipe craftingRecipe = holder.value();
            if (resultSlots.setRecipeUsed(serverPlayer, holder)) {
                ItemStack assembled = craftingRecipe.assemble(input, level.registryAccess());
                if (assembled.isItemEnabled(level.enabledFeatures())) {
                    result = assembled;
                }
            }
        }
        resultSlots.setItem(0, result);
        menu.setRemoteSlot(0, result);
        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, result));
    }

    @Override
    public void slotsChanged(Container container) {
        this.syncToBlockEntity();
        if (!this.placingRecipe) {
            this.access.execute((level, pos) -> {
                if (level instanceof ServerLevel serverLevel) {
                    slotChangedCraftingGrid(this, serverLevel, this.player, this.craftSlots, this.resultSlots, null);
                }
            });
        }
    }

    @Override
    public void beginPlacingRecipe() {
        this.placingRecipe = true;
    }

    @Override
    public void finishPlacingRecipe(ServerLevel level, RecipeHolder<CraftingRecipe> recipe) {
        this.placingRecipe = false;
        this.syncToBlockEntity();
        slotChangedCraftingGrid(this, level, this.player, this.craftSlots, this.resultSlots, recipe);
    }

    @Override
    public void removed(Player player) {
        // Keep matrix items in the block entity; do not clear or eject them.
        this.syncToBlockEntity();
        super.removed(player);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ModBlocks.CRAFTING_SLAB.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();
            if (index == RESULT_SLOT) {
                slotStack.getItem().onCraftedBy(slotStack, player);
                if (!this.moveItemStackTo(slotStack, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, itemstack);
            } else if (index >= INV_SLOT_START && index < USE_ROW_SLOT_END) {
                if (!this.moveItemStackTo(slotStack, CRAFT_SLOT_START, CRAFT_SLOT_END, false)) {
                    if (index < INV_SLOT_END) {
                        if (!this.moveItemStackTo(slotStack, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(slotStack, INV_SLOT_START, INV_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(slotStack, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
            if (index == RESULT_SLOT) {
                player.drop(slotStack, false);
            }
        }
        return itemstack;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public Slot getResultSlot() {
        return this.slots.get(RESULT_SLOT);
    }

    @Override
    public List<Slot> getInputGridSlots() {
        return this.slots.subList(CRAFT_SLOT_START, CRAFT_SLOT_END);
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    @Override
    protected Player owner() {
        return this.player;
    }

    @Override
    public List<Container> getInventoriesForAutofill() {
        if (!Services.CONFIG.craftingTableCanReach()) {
            return List.of(this.player.getInventory());
        }

        List<Container> inventories = new ArrayList<>();
        this.access.execute((level, origin) -> {
            int reach = Services.CONFIG.craftingTableReach();
            BlockPos.betweenClosedStream(origin.offset(-reach, -reach, -reach), origin.offset(reach, reach, reach))
                    .forEach(pos -> {
                        if (pos.equals(origin)) {
                            return;
                        }
                        BlockEntity nearby = level.getBlockEntity(pos);
                        if (nearby instanceof Container container) {
                            Identifier blockId = BuiltInRegistries.BLOCK.getKey(nearby.getBlockState().getBlock());
                            if (Services.CONFIG.isContainerBlockEnabled(blockId.getNamespace(), blockId.toString())) {
                                inventories.add(container);
                            }
                        }
                    });
            inventories.add(this.player.getInventory());
        });
        return inventories;
    }

    @Override
    public boolean persistentInventory() {
        // Slab stores grid contents in the block entity across GUI closes.
        return true;
    }

    public CraftingSlabBlockEntity getBlockEntity() {
        return blockEntity;
    }
}
