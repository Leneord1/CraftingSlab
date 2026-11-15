package com.craftingslab.registry;

import java.util.Set;

import com.craftingslab.CraftingSlabMod;
import com.craftingslab.blockentity.CraftingSlabBlockEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CraftingSlabMod.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CraftingSlabBlockEntity>> CRAFTING_SLAB =
            BLOCK_ENTITIES.register("crafting_slab", () -> new BlockEntityType<>(
                    CraftingSlabBlockEntity::new,
                    Set.of(ModBlocks.CRAFTING_SLAB.get())));

    private ModBlockEntities() {}
}
