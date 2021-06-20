package net.minecraft.world.gen.treedecorator;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.VineBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.Feature;

public class TrunkVineTreeDecorator extends TreeDecorator {
   public static final Codec<TrunkVineTreeDecorator> CODEC;
   public static final TrunkVineTreeDecorator INSTANCE = new TrunkVineTreeDecorator();

   protected TreeDecoratorType<?> getDecoratorType() {
      return TreeDecoratorType.TRUNK_VINE;
   }

   public void func_225576_a_(ISeedReader world, Random rand, List<BlockPos> p_225576_3_, List<BlockPos> p_225576_4_, Set<BlockPos> p_225576_5_, MutableBoundingBox p_225576_6_) {
      p_225576_3_.forEach((p_236880_5_) -> {
         if (rand.nextInt(3) > 0) {
            BlockPos blockpos = p_236880_5_.west();
            if (Feature.isAirAt(world, blockpos)) {
               this.func_227424_a_(world, blockpos, VineBlock.EAST, p_225576_5_, p_225576_6_);
            }
         }

         if (rand.nextInt(3) > 0) {
            BlockPos blockpos1 = p_236880_5_.east();
            if (Feature.isAirAt(world, blockpos1)) {
               this.func_227424_a_(world, blockpos1, VineBlock.WEST, p_225576_5_, p_225576_6_);
            }
         }

         if (rand.nextInt(3) > 0) {
            BlockPos blockpos2 = p_236880_5_.north();
            if (Feature.isAirAt(world, blockpos2)) {
               this.func_227424_a_(world, blockpos2, VineBlock.SOUTH, p_225576_5_, p_225576_6_);
            }
         }

         if (rand.nextInt(3) > 0) {
            BlockPos blockpos3 = p_236880_5_.south();
            if (Feature.isAirAt(world, blockpos3)) {
               this.func_227424_a_(world, blockpos3, VineBlock.NORTH, p_225576_5_, p_225576_6_);
            }
         }

      });
   }

   static {
      CODEC = Codec.unit(() -> {
         return INSTANCE;
      });
   }
}