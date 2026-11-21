package com.craftingslab.registry;

import com.craftingslab.CraftingSlabMod;
import com.craftingslab.menu.CraftingSlabMenu;
import com.craftingslab.menu.FurnaceSlabMenu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, CraftingSlabMod.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<CraftingSlabMenu>> CRAFTING_SLAB =
            MENU_TYPES.register("crafting_slab", () -> IMenuTypeExtension.create(CraftingSlabMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<FurnaceSlabMenu>> FURNACE_SLAB =
            MENU_TYPES.register("furnace_slab", () -> IMenuTypeExtension.create(FurnaceSlabMenu::new));

    private ModMenuTypes() {}
}
