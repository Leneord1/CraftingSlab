package com.craftingslab;

import com.craftingslab.client.CraftingSlabScreen;
import com.craftingslab.registry.ModMenuTypes;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod(value = CraftingSlabMod.MOD_ID, dist = Dist.CLIENT)
public class CraftingSlabModClient {
    public CraftingSlabModClient(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerScreens);
    }

    private void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.CRAFTING_SLAB.get(), CraftingSlabScreen::new);
    }
}
