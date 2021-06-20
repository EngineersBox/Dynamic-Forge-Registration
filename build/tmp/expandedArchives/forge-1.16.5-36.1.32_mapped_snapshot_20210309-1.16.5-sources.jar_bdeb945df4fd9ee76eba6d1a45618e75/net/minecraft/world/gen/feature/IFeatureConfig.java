package net.minecraft.world.gen.feature;

import java.util.stream.Stream;

public interface IFeatureConfig {
   NoFeatureConfig NO_FEATURE_CONFIG = NoFeatureConfig.INSTANCE;

   default Stream<ConfiguredFeature<?, ?>> getConfiguredFeatures() {
      return Stream.empty();
   }
}