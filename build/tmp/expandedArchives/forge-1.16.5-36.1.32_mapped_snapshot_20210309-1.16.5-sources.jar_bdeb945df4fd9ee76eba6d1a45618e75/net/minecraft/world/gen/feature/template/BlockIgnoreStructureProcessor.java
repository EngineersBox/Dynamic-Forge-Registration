package net.minecraft.world.gen.feature.template;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class BlockIgnoreStructureProcessor extends StructureProcessor {
   public static final Codec<BlockIgnoreStructureProcessor> CODEC = BlockState.CODEC.xmap(AbstractBlock.AbstractBlockState::getBlock, Block::getDefaultState).listOf().fieldOf("blocks").xmap(BlockIgnoreStructureProcessor::new, (processor) -> {
      return processor.blocks;
   }).codec();
   public static final BlockIgnoreStructureProcessor STRUCTURE_BLOCK = new BlockIgnoreStructureProcessor(ImmutableList.of(Blocks.STRUCTURE_BLOCK));
   public static final BlockIgnoreStructureProcessor AIR = new BlockIgnoreStructureProcessor(ImmutableList.of(Blocks.AIR));
   public static final BlockIgnoreStructureProcessor AIR_AND_STRUCTURE_BLOCK = new BlockIgnoreStructureProcessor(ImmutableList.of(Blocks.AIR, Blocks.STRUCTURE_BLOCK));
   private final ImmutableList<Block> blocks;

   public BlockIgnoreStructureProcessor(List<Block> blocks) {
      this.blocks = ImmutableList.copyOf(blocks);
   }

   @Nullable
   public Template.BlockInfo func_230386_a_(IWorldReader world, BlockPos p_230386_2_, BlockPos p_230386_3_, Template.BlockInfo p_230386_4_, Template.BlockInfo p_230386_5_, PlacementSettings settings) {
      return this.blocks.contains(p_230386_5_.state.getBlock()) ? null : p_230386_5_;
   }

   protected IStructureProcessorType<?> getType() {
      return IStructureProcessorType.BLOCK_IGNORE;
   }
}