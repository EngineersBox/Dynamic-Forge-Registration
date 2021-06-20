package net.minecraft.world.gen.feature.template;

import com.mojang.serialization.Codec;
import net.minecraft.util.registry.Registry;

public interface IRuleTestType<P extends RuleTest> {
   IRuleTestType<AlwaysTrueRuleTest> ALWAYS_TRUE = register("always_true", AlwaysTrueRuleTest.CODEC);
   IRuleTestType<BlockMatchRuleTest> BLOCK_MATCH = register("block_match", BlockMatchRuleTest.CODEC);
   IRuleTestType<BlockStateMatchRuleTest> BLOCKSTATE_MATCH = register("blockstate_match", BlockStateMatchRuleTest.CODEC);
   IRuleTestType<TagMatchRuleTest> TAG_MATCH = register("tag_match", TagMatchRuleTest.CODEC);
   IRuleTestType<RandomBlockMatchRuleTest> RANDOM_BLOCK_MATCH = register("random_block_match", RandomBlockMatchRuleTest.CODEC);
   IRuleTestType<RandomBlockStateMatchRuleTest> RANDOM_BLOCKSTATE_MATCH = register("random_blockstate_match", RandomBlockStateMatchRuleTest.CODEC);

   Codec<P> codec();

   static <P extends RuleTest> IRuleTestType<P> register(String name, Codec<P> codec) {
      return Registry.register(Registry.RULE_TEST, name, () -> {
         return codec;
      });
   }
}