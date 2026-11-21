package com.craftingslab.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Client render state for crafting-slab surface items.
 */
@OnlyIn(Dist.CLIENT)
public class CraftingSlabRenderState extends BlockEntityRenderState {
    public final List<ItemStackRenderState> items = new ArrayList<>(9);
    public SlabType slabType = SlabType.BOTTOM;
}
