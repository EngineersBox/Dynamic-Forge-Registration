package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.placement.ConfiguredPlacement;

public class DecoratedFeatureConfig implements IFeatureConfig {
   public static final Codec<DecoratedFeatureConfig> CODEC = RecordCodecBuilder.create((builder) -> {
      return builder.group(ConfiguredFeature.field_236264_b_.fieldOf("feature").forGetter((config) -> {
         return config.feature;
      }), ConfiguredPlacement.CODEC.fieldOf("decorator").forGetter((config) -> {
         return config.decorator;
      })).apply(builder, DecoratedFeatureConfig::new);
   });
   public final Supplier<ConfiguredFeature<?, ?>> feature;
   public final ConfiguredPlacement<?> decorator;

   public DecoratedFeatureConfig(Supplier<ConfiguredFeature<?, ?>> feature, ConfiguredPlacement<?> decorator) {
      this.feature = feature;
      this.decorator = decorator;
   }

   public String toString() {
      return String.format("< %s [%s | %s] >", this.getClass().getSimpleName(), Registry.FEATURE.getKey(this.feature.get().getFeature()), this.decorator);
   }

   public Stream<ConfiguredFeature<?, ?>> getConfiguredFeatures() {
      return this.feature.get().getConfiguredFeatures();
   }
}