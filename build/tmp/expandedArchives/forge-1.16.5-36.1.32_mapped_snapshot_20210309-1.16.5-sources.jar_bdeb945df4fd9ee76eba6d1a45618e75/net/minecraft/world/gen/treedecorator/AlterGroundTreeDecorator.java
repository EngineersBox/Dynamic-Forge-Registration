package net.minecraft.world.gen.treedecorator;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.feature.Feature;

public class AlterGroundTreeDecorator extends TreeDecorator {
   public static final Codec<AlterGroundTreeDecorator> CODEC = BlockStateProvider.CODEC.fieldOf("provider").xmap(AlterGroundTreeDecorator::new, (p_236862_0_) -> {
      return p_236862_0_.provider;
   }).codec();
   private final BlockStateProvider provider;

   public AlterGroundTreeDecorator(BlockStateProvider provider) {
      this.provider = provider;
   }

   protected TreeDecoratorType<?> getDecoratorType() {
      return TreeDecoratorType.ALTER_GROUND;
   }

   public void func_225576_a_(ISeedReader world, Random rand, List<BlockPos> p_225576_3_, List<BlockPos> p_225576_4_, Set<BlockPos> p_225576_5_, MutableBoundingBox p_225576_6_) {
      int i = p_225576_3_.get(0).getY();
      p_225576_3_.stream().filter((pos) -> {
         return pos.getY() == i;
      }).forEach((pos) -> {
         this.func_227413_a_(world, rand, pos.west().north());
         this.func_227413_a_(world, rand, pos.east(2).north());
         this.func_227413_a_(world, rand, pos.west().south(2));
         this.func_227413_a_(world, rand, pos.east(2).south(2));

         for(int j = 0; j < 5; ++j) {
            int k = rand.nextInt(64);
            int l = k % 8;
            int i1 = k / 8;
            if (l == 0 || l == 7 || i1 == 0 || i1 == 7) {
               this.func_227413_a_(world, rand, pos.add(-3 + l, 0, -3 + i1));
            }
         }

      });
   }

   private void func_227413_a_(IWorldGenerationReader p_227413_1_, Random p_227413_2_, BlockPos p_227413_3_) {
      for(int i = -2; i <= 2; ++i) {
         for(int j = -2; j <= 2; ++j) {
            if (Math.abs(i) != 2 || Math.abs(j) != 2) {
               this.func_227414_b_(p_227413_1_, p_227413_2_, p_227413_3_.add(i, 0, j));
            }
         }
      }

   }

   private void func_227414_b_(IWorldGenerationReader p_227414_1_, Random p_227414_2_, BlockPos p_227414_3_) {
      for(int i = 2; i >= -3; --i) {
         BlockPos blockpos = p_227414_3_.up(i);
         if (Feature.isDirtAt(p_227414_1_, blockpos)) {
            p_227414_1_.setBlockState(blockpos, this.provider.getBlockState(p_227414_2_, p_227414_3_), 19);
            break;
         }

         if (!Feature.isAirAt(p_227414_1_, blockpos) && i < 0) {
            break;
         }
      }

   }
}