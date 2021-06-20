package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class TwoFeatureChoiceConfig implements IFeatureConfig {
   public static final Codec<TwoFeatureChoiceConfig> CODEC = RecordCodecBuilder.create((p_236581_0_) -> {
      return p_236581_0_.group(ConfiguredFeature.field_236264_b_.fieldOf("feature_true").forGetter((config) -> {
         return config.featureTrue;
      }), ConfiguredFeature.field_236264_b_.fieldOf("feature_false").forGetter((config) -> {
         return config.featureFalse;
      })).apply(p_236581_0_, TwoFeatureChoiceConfig::new);
   });
   public final Supplier<ConfiguredFeature<?, ?>> featureTrue;
   public final Supplier<ConfiguredFeature<?, ?>> featureFalse;

   public TwoFeatureChoiceConfig(Supplier<ConfiguredFeature<?, ?>> featureTrue, Supplier<ConfiguredFeature<?, ?>> featureFalse) {
      this.featureTrue = featureTrue;
      this.featureFalse = featureFalse;
   }

   public Stream<ConfiguredFeature<?, ?>> getConfiguredFeatures() {
      return Stream.concat(this.featureTrue.get().getConfiguredFeatures(), this.featureFalse.get().getConfiguredFeatures());
   }
}