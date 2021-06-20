package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.OptionalInt;

public class ThreeLayerFeature extends AbstractFeatureSizeType {
   public static final Codec<ThreeLayerFeature> CODEC = RecordCodecBuilder.create((builder) -> {
      return builder.group(Codec.intRange(0, 80).fieldOf("limit").orElse(1).forGetter((feature) -> {
         return feature.limit;
      }), Codec.intRange(0, 80).fieldOf("upper_limit").orElse(1).forGetter((feature) -> {
         return feature.upperLimit;
      }), Codec.intRange(0, 16).fieldOf("lower_size").orElse(0).forGetter((feature) -> {
         return feature.lowerSize;
      }), Codec.intRange(0, 16).fieldOf("middle_size").orElse(1).forGetter((feature) -> {
         return feature.middleSize;
      }), Codec.intRange(0, 16).fieldOf("upper_size").orElse(1).forGetter((feature) -> {
         return feature.upperSize;
      }), func_236706_a_()).apply(builder, ThreeLayerFeature::new);
   });
   private final int limit;
   private final int upperLimit;
   private final int lowerSize;
   private final int middleSize;
   private final int upperSize;

   public ThreeLayerFeature(int limit, int upperLimit, int lowerSize, int middleSize, int upperSize, OptionalInt size) {
      super(size);
      this.limit = limit;
      this.upperLimit = upperLimit;
      this.lowerSize = lowerSize;
      this.middleSize = middleSize;
      this.upperSize = upperSize;
   }

   protected FeatureSizeType<?> func_230370_b_() {
      return FeatureSizeType.THREE_LAYERS_FEATURE_SIZE;
   }

   public int func_230369_a_(int p_230369_1_, int p_230369_2_) {
      if (p_230369_2_ < this.limit) {
         return this.lowerSize;
      } else {
         return p_230369_2_ >= p_230369_1_ - this.upperLimit ? this.upperSize : this.middleSize;
      }
   }
}