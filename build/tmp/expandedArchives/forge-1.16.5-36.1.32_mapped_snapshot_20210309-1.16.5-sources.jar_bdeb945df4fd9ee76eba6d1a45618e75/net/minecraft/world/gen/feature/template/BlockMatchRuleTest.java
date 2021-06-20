package net.minecraft.world.gen.feature.template;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.registry.Registry;

public class BlockMatchRuleTest extends RuleTest {
   public static final Codec<BlockMatchRuleTest> CODEC = Registry.BLOCK.fieldOf("block").xmap(BlockMatchRuleTest::new, (test) -> {
      return test.block;
   }).codec();
   private final Block block;

   public BlockMatchRuleTest(Block block) {
      this.block = block;
   }

   public boolean test(BlockState state, Random rand) {
      return state.matchesBlock(this.block);
   }

   protected IRuleTestType<?> getType() {
      return IRuleTestType.BLOCK_MATCH;
   }
}