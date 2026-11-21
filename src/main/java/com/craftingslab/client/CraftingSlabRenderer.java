package com.craftingslab.client;

import com.craftingslab.blockentity.CraftingSlabBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jspecify.annotations.Nullable;

/**
 * Renders crafting-grid items flat on the slab surface.
 */
@OnlyIn(Dist.CLIENT)
public class CraftingSlabRenderer implements BlockEntityRenderer<CraftingSlabBlockEntity, CraftingSlabRenderState> {
    private static final float ITEM_SCALE = 0.28F;
    private static final float GRID_START = 0.22F;
    private static final float GRID_STEP = 0.28F;

    private final ItemModelResolver itemModelResolver;

    public CraftingSlabRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public CraftingSlabRenderState createRenderState() {
        return new CraftingSlabRenderState();
    }

    @Override
    public void extractRenderState(
            CraftingSlabBlockEntity blockEntity,
            CraftingSlabRenderState renderState,
            float partialTick,
            Vec3 cameraPos,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPos, breakProgress);
        renderState.slabType = blockEntity.getBlockState().getValue(SlabBlock.TYPE);
        renderState.items.clear();

        ItemStackHandler handler = blockEntity.getItems();
        int seed = (int) blockEntity.getBlockPos().asLong();
        for (int i = 0; i < CraftingSlabBlockEntity.GRID_SIZE; i++) {
            ItemStack stack = handler.getStackInSlot(i);
            ItemStackRenderState itemState = new ItemStackRenderState();
            if (!stack.isEmpty()) {
                this.itemModelResolver.updateForTopItem(
                        itemState, stack, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, seed + i);
            }
            renderState.items.add(itemState);
        }
    }

    @Override
    public void submit(
            CraftingSlabRenderState renderState,
            PoseStack poseStack,
            SubmitNodeCollector nodeCollector,
            CameraRenderState camera) {
        float surfaceY = renderState.slabType == SlabType.BOTTOM ? 0.5F : 1.0F;

        for (int i = 0; i < renderState.items.size(); i++) {
            ItemStackRenderState itemState = renderState.items.get(i);
            if (itemState.isEmpty()) {
                continue;
            }

            int row = i / 3;
            int col = i % 3;
            float x = GRID_START + col * GRID_STEP;
            float z = GRID_START + row * GRID_STEP;

            poseStack.pushPose();
            poseStack.translate(x, surfaceY + 0.02F, z);
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
            itemState.submit(poseStack, nodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }
}
