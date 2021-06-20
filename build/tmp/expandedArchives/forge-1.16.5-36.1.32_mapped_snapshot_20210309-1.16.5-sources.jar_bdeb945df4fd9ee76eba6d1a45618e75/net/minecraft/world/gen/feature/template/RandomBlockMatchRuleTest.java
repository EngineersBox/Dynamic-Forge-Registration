package net.minecraft.world.gen.feature.template;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.registry.Registry;

public class RandomBlockMatchRuleTest extends RuleTest {
   public static final Codec<RandomBlockMatchRuleTest> CODEC = RecordCodecBuilder.create((p_237118_0_) -> {
      return p_237118_0_.group(Registry.BLOCK.fieldOf("block").forGetter((test) -> {
         return test.block;
      }), Codec.FLOAT.fieldOf("probability").forGetter((test) -> {
         return test.probability;
      })).apply(p_237118_0_, RandomBlockMatchRuleTest::new);
   });
   private final Block block;
   private final float probability;

   public RandomBlockMatchRuleTest(Block block, float probability) {
      this.block = block;
      this.probability = probability;
   }

   public boolean test(BlockState state, Random rand) {
      return state.matchesBlock(this.block) && rand.nextFloat() < this.probability;
   }

   protected IRuleTestType<?> getType() {
      return IRuleTestType.RANDOM_BLOCK_MATCH;
   }
}