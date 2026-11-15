package com.craftingslab.block;

import com.craftingslab.blockentity.CraftingSlabBlockEntity;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class CraftingSlabBlock extends SlabBlock implements EntityBlock {
    public static final MapCodec<CraftingSlabBlock> CODEC = simpleCodec(CraftingSlabBlock::new);

    public CraftingSlabBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends SlabBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CraftingSlabBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CraftingSlabBlockEntity craftingSlab) {
                player.openMenu(craftingSlab, buf -> buf.writeBlockPos(pos));
                player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
