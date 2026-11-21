package com.craftingslab;

import org.slf4j.Logger;

import com.craftingslab.registry.ModBlockEntities;
import com.craftingslab.registry.ModBlocks;
import com.craftingslab.registry.ModMenuTypes;
import com.mojang.logging.LogUtils;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(CraftingSlabMod.MOD_ID)
public class CraftingSlabMod {
    public static final String MOD_ID = "craftingslab";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CraftingSlabMod(IEventBus modEventBus, ModContainer modContainer) {
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlocks.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        modEventBus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModBlocks.CRAFTING_SLAB_ITEM);
            event.accept(ModBlocks.FURNACE_SLAB_ITEM);
        }
    }
}
