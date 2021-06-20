package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;

public class HugeFungusFeature extends Feature<HugeFungusConfig> {
   public HugeFungusFeature(Codec<HugeFungusConfig> codec) {
      super(codec);
   }

   public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, HugeFungusConfig config) {
      Block block = config.validBaseBlock.getBlock();
      BlockPos blockpos = null;
      Block block1 = reader.getBlockState(pos.down()).getBlock();
      if (block1 == block) {
         blockpos = pos;
      }

      if (blockpos == null) {
         return false;
      } else {
         int i = MathHelper.nextInt(rand, 4, 13);
         if (rand.nextInt(12) == 0) {
            i *= 2;
         }

         if (!config.planted) {
            int j = generator.getMaxBuildHeight();
            if (blockpos.getY() + i + 1 >= j) {
               return false;
            }
         }

         boolean flag = !config.planted && rand.nextFloat() < 0.06F;
         reader.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
         this.generateStems(reader, rand, config, blockpos, i, flag);
         this.generateFungusHat(reader, rand, config, blockpos, i, flag);
         return true;
      }
   }

   private static boolean isPlantable(IWorld world, BlockPos pos, boolean p_236315_2_) {
      return world.hasBlockState(pos, (p_236320_1_) -> {
         Material material = p_236320_1_.getMaterial();
         return p_236320_1_.getMaterial().isReplaceable() || p_236315_2_ && material == Material.PLANTS;
      });
   }

   private void generateStems(IWorld world, Random rand, HugeFungusConfig config, BlockPos pos, int p_236317_5_, boolean p_236317_6_) {
      BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
      BlockState blockstate = config.stemState;
      int i = p_236317_6_ ? 1 : 0;

      for(int j = -i; j <= i; ++j) {
         for(int k = -i; k <= i; ++k) {
            boolean flag = p_236317_6_ && MathHelper.abs(j) == i && MathHelper.abs(k) == i;

            for(int l = 0; l < p_236317_5_; ++l) {
               blockpos$mutable.setAndOffset(pos, j, l, k);
               if (isPlantable(world, blockpos$mutable, true)) {
                  if (config.planted) {
                     if (!world.getBlockState(blockpos$mutable.down()).isAir()) {
                        world.destroyBlock(blockpos$mutable, true);
                     }

                     world.setBlockState(blockpos$mutable, blockstate, 3);
                  } else if (flag) {
                     if (rand.nextFloat() < 0.1F) {
                        this.setBlockState(world, blockpos$mutable, blockstate);
                     }
                  } else {
                     this.setBlockState(world, blockpos$mutable, blockstate);
                  }
               }
            }
         }
      }

   }

   private void generateFungusHat(IWorld world, Random rand, HugeFungusConfig config, BlockPos pos, int p_236321_5_, boolean p_236321_6_) {
      BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
      boolean flag = config.hatState.matchesBlock(Blocks.NETHER_WART_BLOCK);
      int i = Math.min(rand.nextInt(1 + p_236321_5_ / 3) + 5, p_236321_5_);
      int j = p_236321_5_ - i;

      for(int k = j; k <= p_236321_5_; ++k) {
         int l = k < p_236321_5_ - rand.nextInt(3) ? 2 : 1;
         if (i > 8 && k < j + 4) {
            l = 3;
         }

         if (p_236321_6_) {
            ++l;
         }

         for(int i1 = -l; i1 <= l; ++i1) {
            for(int j1 = -l; j1 <= l; ++j1) {
               boolean flag1 = i1 == -l || i1 == l;
               boolean flag2 = j1 == -l || j1 == l;
               boolean flag3 = !flag1 && !flag2 && k != p_236321_5_;
               boolean flag4 = flag1 && flag2;
               boolean flag5 = k < j + 3;
               blockpos$mutable.setAndOffset(pos, i1, k, j1);
               if (isPlantable(world, blockpos$mutable, false)) {
                  if (config.planted && !world.getBlockState(blockpos$mutable.down()).isAir()) {
                     world.destroyBlock(blockpos$mutable, true);
                  }

                  if (flag5) {
                     if (!flag3) {
                        this.generateHatWithVines(world, rand, blockpos$mutable, config.hatState, flag);
                     }
                  } else if (flag3) {
                     this.generateFungus(world, rand, config, blockpos$mutable, 0.1F, 0.2F, flag ? 0.1F : 0.0F);
                  } else if (flag4) {
                     this.generateFungus(world, rand, config, blockpos$mutable, 0.01F, 0.7F, flag ? 0.083F : 0.0F);
                  } else {
                     this.generateFungus(world, rand, config, blockpos$mutable, 5.0E-4F, 0.98F, flag ? 0.07F : 0.0F);
                  }
               }
            }
         }
      }

   }

   private void generateFungus(IWorld world, Random rand, HugeFungusConfig config, BlockPos.Mutable pos, float p_236316_5_, float p_236316_6_, float p_236316_7_) {
      if (rand.nextFloat() < p_236316_5_) {
         this.setBlockState(world, pos, config.decorState);
      } else if (rand.nextFloat() < p_236316_6_) {
         this.setBlockState(world, pos, config.hatState);
         if (rand.nextFloat() < p_236316_7_) {
            placeWeepingVine(pos, world, rand);
         }
      }

   }

   private void generateHatWithVines(IWorld world, Random rand, BlockPos pos, BlockState state, boolean isNetherWart) {
      if (world.getBlockState(pos.down()).matchesBlock(state.getBlock())) {
         this.setBlockState(world, pos, state);
      } else if ((double)rand.nextFloat() < 0.15D) {
         this.setBlockState(world, pos, state);
         if (isNetherWart && rand.nextInt(11) == 0) {
            placeWeepingVine(pos, world, rand);
         }
      }

   }

   private static void placeWeepingVine(BlockPos pos, IWorld world, Random rand) {
      BlockPos.Mutable blockpos$mutable = pos.toMutable().move(Direction.DOWN);
      if (world.isAirBlock(blockpos$mutable)) {
         int i = MathHelper.nextInt(rand, 1, 5);
         if (rand.nextInt(7) == 0) {
            i *= 2;
         }

         int j = 23;
         int k = 25;
         WeepingVineFeature.func_236427_a_(world, rand, blockpos$mutable, i, 23, 25);
      }
   }
}