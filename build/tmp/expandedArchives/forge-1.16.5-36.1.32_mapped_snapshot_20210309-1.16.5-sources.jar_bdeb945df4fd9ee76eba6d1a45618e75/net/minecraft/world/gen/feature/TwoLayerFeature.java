package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.OptionalInt;

public class TwoLayerFeature extends AbstractFeatureSizeType {
   public static final Codec<TwoLayerFeature> CODEC = RecordCodecBuilder.create((p_236732_0_) -> {
      return p_236732_0_.group(Codec.intRange(0, 81).fieldOf("limit").orElse(1).forGetter((p_236735_0_) -> {
         return p_236735_0_.limit;
      }), Codec.intRange(0, 16).fieldOf("lower_size").orElse(0).forGetter((p_236734_0_) -> {
         return p_236734_0_.lowerSize;
      }), Codec.intRange(0, 16).fieldOf("upper_size").orElse(1).forGetter((p_236733_0_) -> {
         return p_236733_0_.upperSize;
      }), func_236706_a_()).apply(p_236732_0_, TwoLayerFeature::new);
   });
   private final int limit;
   private final int lowerSize;
   private final int upperSize;

   public TwoLayerFeature(int limit, int lowerSize, int upperSize) {
      this(limit, lowerSize, upperSize, OptionalInt.empty());
   }

   public TwoLayerFeature(int limit, int lowerSize, int upperSize, OptionalInt p_i232026_4_) {
      super(p_i232026_4_);
      this.limit = limit;
      this.lowerSize = lowerSize;
      this.upperSize = upperSize;
   }

   protected FeatureSizeType<?> func_230370_b_() {
      return FeatureSizeType.TWO_LAYERS_FEATURE_SIZE;
   }

   public int func_230369_a_(int p_230369_1_, int p_230369_2_) {
      return p_230369_2_ < this.limit ? this.lowerSize : this.upperSize;
   }
}