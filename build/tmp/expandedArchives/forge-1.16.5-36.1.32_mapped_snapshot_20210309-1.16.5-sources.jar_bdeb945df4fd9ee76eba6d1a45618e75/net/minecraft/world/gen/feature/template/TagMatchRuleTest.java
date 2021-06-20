package net.minecraft.world.gen.feature.template;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;

public class TagMatchRuleTest extends RuleTest {
   public static final Codec<TagMatchRuleTest> CODEC = ITag.getTagCodec(() -> {
      return TagCollectionManager.getManager().getBlockTags();
   }).fieldOf("tag").xmap(TagMatchRuleTest::new, (test) -> {
      return test.tag;
   }).codec();
   private final ITag<Block> tag;

   public TagMatchRuleTest(ITag<Block> tag) {
      this.tag = tag;
   }

   public boolean test(BlockState state, Random rand) {
      return state.isIn(this.tag);
   }

   protected IRuleTestType<?> getType() {
      return IRuleTestType.TAG_MATCH;
   }
}