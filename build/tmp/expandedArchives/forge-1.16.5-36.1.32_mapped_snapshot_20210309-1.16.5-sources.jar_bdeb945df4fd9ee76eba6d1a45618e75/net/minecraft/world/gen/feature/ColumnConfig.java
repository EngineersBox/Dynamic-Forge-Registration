package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class ColumnConfig implements IFeatureConfig {
   public static final Codec<ColumnConfig> CODEC = RecordCodecBuilder.create((builder) -> {
      return builder.group(FeatureSpread.createCodec(0, 2, 1).fieldOf("reach").forGetter((config) -> {
         return config.reach;
      }), FeatureSpread.createCodec(1, 5, 5).fieldOf("height").forGetter((config) -> {
         return config.height;
      })).apply(builder, ColumnConfig::new);
   });
   private final FeatureSpread reach;
   private final FeatureSpread height;

   public ColumnConfig(FeatureSpread reach, FeatureSpread height) {
      this.reach = reach;
      this.height = height;
   }

   public FeatureSpread getReach() {
      return this.reach;
   }

   public FeatureSpread getHeight() {
      return this.height;
   }
}