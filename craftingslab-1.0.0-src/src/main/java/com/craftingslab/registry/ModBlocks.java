package com.craftingslab.registry;

import com.craftingslab.CraftingSlabMod;
import com.craftingslab.block.CraftingSlabBlock;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CraftingSlabMod.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CraftingSlabMod.MOD_ID);

    public static final DeferredBlock<CraftingSlabBlock> CRAFTING_SLAB = BLOCKS.registerBlock(
            "crafting_slab",
            CraftingSlabBlock::new,
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CRAFTING_TABLE)
                    .mapColor(MapColor.WOOD)
                    .strength(2.5F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava());

    public static final DeferredItem<BlockItem> CRAFTING_SLAB_ITEM = ITEMS.registerSimpleBlockItem("crafting_slab", CRAFTING_SLAB);

    private ModBlocks() {}
}
