package net.minecraft.world.gen.feature.template;

import com.mojang.serialization.Codec;
import net.minecraft.util.registry.Registry;

public interface IPosRuleTests<P extends PosRuleTest> {
   IPosRuleTests<AlwaysTrueTest> field_237103_a_ = register("always_true", AlwaysTrueTest.CODEC);
   IPosRuleTests<LinearPosTest> field_237104_b_ = register("linear_pos", LinearPosTest.CODEC);
   IPosRuleTests<AxisAlignedLinearPosTest> field_237105_c_ = register("axis_aligned_linear_pos", AxisAlignedLinearPosTest.CODEC);

   Codec<P> codec();

   static <P extends PosRuleTest> IPosRuleTests<P> register(String name, Codec<P> codec) {
      return Registry.register(Registry.POS_RULE_TEST, name, () -> {
         return codec;
      });
   }
}