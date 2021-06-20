package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SingleRandomFeature implements IFeatureConfig {
   public static final Codec<SingleRandomFeature> CODEC = ConfiguredFeature.field_242764_c.fieldOf("features").xmap(SingleRandomFeature::new, (p_236643_0_) -> {
      return p_236643_0_.features;
   }).codec();
   public final List<Supplier<ConfiguredFeature<?, ?>>> features;

   public SingleRandomFeature(List<Supplier<ConfiguredFeature<?, ?>>> features) {
      this.features = features;
   }

   public Stream<ConfiguredFeature<?, ?>> getConfiguredFeatures() {
      return this.features.stream().flatMap((supplier) -> {
         return supplier.get().getConfiguredFeatures();
      });
   }
}