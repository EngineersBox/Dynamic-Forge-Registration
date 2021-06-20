package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DeadCoralWallFanBlock;
import net.minecraft.block.SeaPickleBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;

public abstract class CoralFeature extends Feature<NoFeatureConfig> {
   public CoralFeature(Codec<NoFeatureConfig> codec) {
      super(codec);
   }

   public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
      BlockState blockstate = BlockTags.CORAL_BLOCKS.getRandomElement(rand).getDefaultState();
      return this.func_204623_a(reader, rand, pos, blockstate);
   }

   protected abstract boolean func_204623_a(IWorld world, Random rand, BlockPos pos, BlockState state);

   protected boolean func_204624_b(IWorld world, Random rand, BlockPos pos, BlockState state) {
      BlockPos blockpos = pos.up();
      BlockState blockstate = world.getBlockState(pos);
      if ((blockstate.matchesBlock(Blocks.WATER) || blockstate.isIn(BlockTags.CORALS)) && world.getBlockState(blockpos).matchesBlock(Blocks.WATER)) {
         world.setBlockState(pos, state, 3);
         if (rand.nextFloat() < 0.25F) {
            world.setBlockState(blockpos, BlockTags.CORALS.getRandomElement(rand).getDefaultState(), 2);
         } else if (rand.nextFloat() < 0.05F) {
            world.setBlockState(blockpos, Blocks.SEA_PICKLE.getDefaultState().with(SeaPickleBlock.PICKLES, Integer.valueOf(rand.nextInt(4) + 1)), 2);
         }

         for(Direction direction : Direction.Plane.HORIZONTAL) {
            if (rand.nextFloat() < 0.2F) {
               BlockPos blockpos1 = pos.offset(direction);
               if (world.getBlockState(blockpos1).matchesBlock(Blocks.WATER)) {
                  BlockState blockstate1 = BlockTags.WALL_CORALS.getRandomElement(rand).getDefaultState().with(DeadCoralWallFanBlock.FACING, direction);
                  world.setBlockState(blockpos1, blockstate1, 2);
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }
}