package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class CoralMushroomFeature extends CoralFeature {
   public CoralMushroomFeature(Codec<NoFeatureConfig> codec) {
      super(codec);
   }

   protected boolean func_204623_a(IWorld world, Random rand, BlockPos pos, BlockState state) {
      int i = rand.nextInt(3) + 3;
      int j = rand.nextInt(3) + 3;
      int k = rand.nextInt(3) + 3;
      int l = rand.nextInt(3) + 1;
      BlockPos.Mutable blockpos$mutable = pos.toMutable();

      for(int i1 = 0; i1 <= j; ++i1) {
         for(int j1 = 0; j1 <= i; ++j1) {
            for(int k1 = 0; k1 <= k; ++k1) {
               blockpos$mutable.setPos(i1 + pos.getX(), j1 + pos.getY(), k1 + pos.getZ());
               blockpos$mutable.move(Direction.DOWN, l);
               if ((i1 != 0 && i1 != j || j1 != 0 && j1 != i) && (k1 != 0 && k1 != k || j1 != 0 && j1 != i) && (i1 != 0 && i1 != j || k1 != 0 && k1 != k) && (i1 == 0 || i1 == j || j1 == 0 || j1 == i || k1 == 0 || k1 == k) && !(rand.nextFloat() < 0.1F) && !this.func_204624_b(world, rand, blockpos$mutable, state)) {
               }
            }
         }
      }

      return true;
   }
}