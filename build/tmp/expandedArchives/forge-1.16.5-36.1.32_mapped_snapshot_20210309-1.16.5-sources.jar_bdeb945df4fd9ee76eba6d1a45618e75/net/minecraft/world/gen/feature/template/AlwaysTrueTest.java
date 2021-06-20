package net.minecraft.world.gen.feature.template;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.util.math.BlockPos;

public class AlwaysTrueTest extends PosRuleTest {
   public static final Codec<AlwaysTrueTest> CODEC;
   public static final AlwaysTrueTest field_237100_b_ = new AlwaysTrueTest();

   private AlwaysTrueTest() {
   }

   public boolean test(BlockPos p_230385_1_, BlockPos p_230385_2_, BlockPos p_230385_3_, Random rand) {
      return true;
   }

   protected IPosRuleTests<?> func_230384_a_() {
      return IPosRuleTests.field_237103_a_;
   }

   static {
      CODEC = Codec.unit(() -> {
         return field_237100_b_;
      });
   }
}