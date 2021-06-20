package net.minecraft.world.gen.feature.template;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.block.BlockState;
import net.minecraft.util.registry.Registry;

public abstract class RuleTest {
   public static final Codec<RuleTest> CODEC = Registry.RULE_TEST.dispatch("predicate_type", RuleTest::getType, IRuleTestType::codec);

   public abstract boolean test(BlockState state, Random rand);

   protected abstract IRuleTestType<?> getType();
}