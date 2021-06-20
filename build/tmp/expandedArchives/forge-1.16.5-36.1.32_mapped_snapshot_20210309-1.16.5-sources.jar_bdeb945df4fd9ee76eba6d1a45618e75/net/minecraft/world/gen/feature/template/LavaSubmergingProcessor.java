package net.minecraft.world.gen.feature.template;

import com.mojang.serialization.Codec;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class LavaSubmergingProcessor extends StructureProcessor {
   public static final Codec<LavaSubmergingProcessor> CODEC;
   public static final LavaSubmergingProcessor INSTANCE = new LavaSubmergingProcessor();

   @Nullable
   public Template.BlockInfo func_230386_a_(IWorldReader world, BlockPos p_230386_2_, BlockPos p_230386_3_, Template.BlockInfo p_230386_4_, Template.BlockInfo p_230386_5_, PlacementSettings settings) {
      BlockPos blockpos = p_230386_5_.pos;
      boolean flag = world.getBlockState(blockpos).matchesBlock(Blocks.LAVA);
      return flag && !Block.isOpaque(p_230386_5_.state.getShape(world, blockpos)) ? new Template.BlockInfo(blockpos, Blocks.LAVA.getDefaultState(), p_230386_5_.nbt) : p_230386_5_;
   }

   protected IStructureProcessorType<?> getType() {
      return IStructureProcessorType.LAVA_SUBMERGED_BLOCK;
   }

   static {
      CODEC = Codec.unit(() -> {
         return INSTANCE;
      });
   }
}