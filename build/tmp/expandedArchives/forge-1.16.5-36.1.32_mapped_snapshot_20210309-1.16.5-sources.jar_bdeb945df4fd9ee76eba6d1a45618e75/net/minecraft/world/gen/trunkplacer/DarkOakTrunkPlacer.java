package net.minecraft.world.gen.trunkplacer;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;

public class DarkOakTrunkPlacer extends AbstractTrunkPlacer {
   public static final Codec<DarkOakTrunkPlacer> CODEC = RecordCodecBuilder.create((builderInstance) -> {
      return getAbstractTrunkCodec(builderInstance).apply(builderInstance, DarkOakTrunkPlacer::new);
   });

   public DarkOakTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
      super(baseHeight, heightRandA, heightRandB);
   }

   protected TrunkPlacerType<?> getPlacerType() {
      return TrunkPlacerType.DARK_OAK_TRUNK_PLACER;
   }

   public List<FoliagePlacer.Foliage> getFoliages(IWorldGenerationReader reader, Random rand, int treeHeight, BlockPos p_230382_4_, Set<BlockPos> p_230382_5_, MutableBoundingBox p_230382_6_, BaseTreeFeatureConfig p_230382_7_) {
      List<FoliagePlacer.Foliage> list = Lists.newArrayList();
      BlockPos blockpos = p_230382_4_.down();
      func_236909_a_(reader, blockpos);
      func_236909_a_(reader, blockpos.east());
      func_236909_a_(reader, blockpos.south());
      func_236909_a_(reader, blockpos.south().east());
      Direction direction = Direction.Plane.HORIZONTAL.random(rand);
      int i = treeHeight - rand.nextInt(4);
      int j = 2 - rand.nextInt(3);
      int k = p_230382_4_.getX();
      int l = p_230382_4_.getY();
      int i1 = p_230382_4_.getZ();
      int j1 = k;
      int k1 = i1;
      int l1 = l + treeHeight - 1;

      for(int i2 = 0; i2 < treeHeight; ++i2) {
         if (i2 >= i && j > 0) {
            j1 += direction.getXOffset();
            k1 += direction.getZOffset();
            --j;
         }

         int j2 = l + i2;
         BlockPos blockpos1 = new BlockPos(j1, j2, k1);
         if (TreeFeature.isAirOrLeavesAt(reader, blockpos1)) {
            func_236911_a_(reader, rand, blockpos1, p_230382_5_, p_230382_6_, p_230382_7_);
            func_236911_a_(reader, rand, blockpos1.east(), p_230382_5_, p_230382_6_, p_230382_7_);
            func_236911_a_(reader, rand, blockpos1.south(), p_230382_5_, p_230382_6_, p_230382_7_);
            func_236911_a_(reader, rand, blockpos1.east().south(), p_230382_5_, p_230382_6_, p_230382_7_);
         }
      }

      list.add(new FoliagePlacer.Foliage(new BlockPos(j1, l1, k1), 0, true));

      for(int l2 = -1; l2 <= 2; ++l2) {
         for(int i3 = -1; i3 <= 2; ++i3) {
            if ((l2 < 0 || l2 > 1 || i3 < 0 || i3 > 1) && rand.nextInt(3) <= 0) {
               int j3 = rand.nextInt(3) + 2;

               for(int k2 = 0; k2 < j3; ++k2) {
                  func_236911_a_(reader, rand, new BlockPos(k + l2, l1 - k2 - 1, i1 + i3), p_230382_5_, p_230382_6_, p_230382_7_);
               }

               list.add(new FoliagePlacer.Foliage(new BlockPos(j1 + l2, l1, k1 + i3), 0, false));
            }
         }
      }

      return list;
   }
}