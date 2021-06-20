package net.minecraft.world.gen.feature.template;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public abstract class PosRuleTest {
   public static final Codec<PosRuleTest> CODEC = Registry.POS_RULE_TEST.dispatch("predicate_type", PosRuleTest::func_230384_a_, IPosRuleTests::codec);

   public abstract boolean test(BlockPos p_230385_1_, BlockPos p_230385_2_, BlockPos p_230385_3_, Random rand);

   protected abstract IPosRuleTests<?> func_230384_a_();
}