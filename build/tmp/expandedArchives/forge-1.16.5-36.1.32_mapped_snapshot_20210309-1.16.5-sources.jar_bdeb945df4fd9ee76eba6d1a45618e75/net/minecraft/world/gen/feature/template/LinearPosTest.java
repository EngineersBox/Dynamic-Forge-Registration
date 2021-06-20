package net.minecraft.world.gen.feature.template;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Random;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class LinearPosTest extends PosRuleTest {
   public static final Codec<LinearPosTest> CODEC = RecordCodecBuilder.create((p_237092_0_) -> {
      return p_237092_0_.group(Codec.FLOAT.fieldOf("min_chance").orElse(0.0F).forGetter((test) -> {
         return test.minChance;
      }), Codec.FLOAT.fieldOf("max_chance").orElse(0.0F).forGetter((test) -> {
         return test.maxChance;
      }), Codec.INT.fieldOf("min_dist").orElse(0).forGetter((test) -> {
         return test.minDist;
      }), Codec.INT.fieldOf("max_dist").orElse(0).forGetter((test) -> {
         return test.maxDist;
      })).apply(p_237092_0_, LinearPosTest::new);
   });
   private final float minChance;
   private final float maxChance;
   private final int minDist;
   private final int maxDist;

   public LinearPosTest(float minChance, float maxChance, int minDist, int maxDist) {
      if (minDist >= maxDist) {
         throw new IllegalArgumentException("Invalid range: [" + minDist + "," + maxDist + "]");
      } else {
         this.minChance = minChance;
         this.maxChance = maxChance;
         this.minDist = minDist;
         this.maxDist = maxDist;
      }
   }

   public boolean test(BlockPos p_230385_1_, BlockPos p_230385_2_, BlockPos p_230385_3_, Random rand) {
      int i = p_230385_2_.manhattanDistance(p_230385_3_);
      float f = rand.nextFloat();
      return (double)f <= MathHelper.clampedLerp((double)this.minChance, (double)this.maxChance, MathHelper.func_233020_c_((double)i, (double)this.minDist, (double)this.maxDist));
   }

   protected IPosRuleTests<?> func_230384_a_() {
      return IPosRuleTests.field_237104_b_;
   }
}