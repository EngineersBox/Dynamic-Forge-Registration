package net.minecraft.world.gen.feature.template;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Random;
import net.minecraft.block.BlockState;

public class RandomBlockStateMatchRuleTest extends RuleTest {
   public static final Codec<RandomBlockStateMatchRuleTest> CODEC = RecordCodecBuilder.create((p_237122_0_) -> {
      return p_237122_0_.group(BlockState.CODEC.fieldOf("block_state").forGetter((test) -> {
         return test.state;
      }), Codec.FLOAT.fieldOf("probability").forGetter((test) -> {
         return test.probability;
      })).apply(p_237122_0_, RandomBlockStateMatchRuleTest::new);
   });
   private final BlockState state;
   private final float probability;

   public RandomBlockStateMatchRuleTest(BlockState state, float probability) {
      this.state = state;
      this.probability = probability;
   }

   public boolean test(BlockState state, Random rand) {
      return state == this.state && rand.nextFloat() < this.probability;
   }

   protected IRuleTestType<?> getType() {
      return IRuleTestType.RANDOM_BLOCKSTATE_MATCH;
   }
}