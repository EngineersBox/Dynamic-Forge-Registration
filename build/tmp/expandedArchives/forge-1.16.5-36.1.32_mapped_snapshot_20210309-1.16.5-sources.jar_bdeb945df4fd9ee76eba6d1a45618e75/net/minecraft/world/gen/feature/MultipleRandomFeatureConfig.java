package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class MultipleRandomFeatureConfig implements IFeatureConfig {
   public static final Codec<MultipleRandomFeatureConfig> CODEC = RecordCodecBuilder.create((builder) -> {
      return builder.apply2(MultipleRandomFeatureConfig::new, ConfiguredRandomFeatureList.CODEC.listOf().fieldOf("features").forGetter((config) -> {
         return config.features;
      }), ConfiguredFeature.field_236264_b_.fieldOf("default").forGetter((config) -> {
         return config.defaultFeature;
      }));
   });
   public final List<ConfiguredRandomFeatureList> features;
   public final Supplier<ConfiguredFeature<?, ?>> defaultFeature;

   public MultipleRandomFeatureConfig(List<ConfiguredRandomFeatureList> features, ConfiguredFeature<?, ?> defaultFeature) {
      this(features, () -> {
         return defaultFeature;
      });
   }

   private MultipleRandomFeatureConfig(List<ConfiguredRandomFeatureList> features, Supplier<ConfiguredFeature<?, ?>> defaultFeature) {
      this.features = features;
      this.defaultFeature = defaultFeature;
   }

   public Stream<ConfiguredFeature<?, ?>> getConfiguredFeatures() {
      return Stream.concat(this.features.stream().flatMap((p_242812_0_) -> {
         return p_242812_0_.feature.get().getConfiguredFeatures();
      }), this.defaultFeature.get().getConfiguredFeatures());
   }
}