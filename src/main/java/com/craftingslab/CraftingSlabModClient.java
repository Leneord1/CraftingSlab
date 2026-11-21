package com.craftingslab;

import com.craftingslab.client.CraftingSlabRenderer;
import com.craftingslab.client.CraftingSlabScreen;
import com.craftingslab.client.FurnaceSlabScreen;
import com.craftingslab.registry.ModBlockEntities;
import com.craftingslab.registry.ModMenuTypes;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod(value = CraftingSlabMod.MOD_ID, dist = Dist.CLIENT)
public class CraftingSlabModClient {
    public CraftingSlabModClient(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerScreens);
        modEventBus.addListener(this::registerRenderers);
    }

    private void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.CRAFTING_SLAB.get(), CraftingSlabScreen::new);
        event.register(ModMenuTypes.FURNACE_SLAB.get(), FurnaceSlabScreen::new);
    }

    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.CRAFTING_SLAB.get(), CraftingSlabRenderer::new);
    }
}
