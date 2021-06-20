package net.minecraft.world.gen.feature.template;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;

public class RuleStructureProcessor extends StructureProcessor {
   public static final Codec<RuleStructureProcessor> CODEC = RuleEntry.CODEC.listOf().fieldOf("rules").xmap(RuleStructureProcessor::new, (processor) -> {
      return processor.rules;
   }).codec();
   private final ImmutableList<RuleEntry> rules;

   public RuleStructureProcessor(List<? extends RuleEntry> rules) {
      this.rules = ImmutableList.copyOf(rules);
   }

   @Nullable
   public Template.BlockInfo func_230386_a_(IWorldReader world, BlockPos p_230386_2_, BlockPos p_230386_3_, Template.BlockInfo p_230386_4_, Template.BlockInfo p_230386_5_, PlacementSettings settings) {
      Random random = new Random(MathHelper.getPositionRandom(p_230386_5_.pos));
      BlockState blockstate = world.getBlockState(p_230386_5_.pos);

      for(RuleEntry ruleentry : this.rules) {
         if (ruleentry.test(p_230386_5_.state, blockstate, p_230386_4_.pos, p_230386_5_.pos, p_230386_3_, random)) {
            return new Template.BlockInfo(p_230386_5_.pos, ruleentry.getOutputState(), ruleentry.getOutputNbt());
         }
      }

      return p_230386_5_;
   }

   protected IStructureProcessorType<?> getType() {
      return IStructureProcessorType.RULE;
   }
}