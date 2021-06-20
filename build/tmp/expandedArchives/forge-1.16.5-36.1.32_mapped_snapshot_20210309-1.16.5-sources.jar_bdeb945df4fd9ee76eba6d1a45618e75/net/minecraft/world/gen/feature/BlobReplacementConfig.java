package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;

public class BlobReplacementConfig implements IFeatureConfig {
   public static final Codec<BlobReplacementConfig> CODEC = RecordCodecBuilder.create((builder) -> {
      return builder.group(BlockState.CODEC.fieldOf("target").forGetter((config) -> {
         return config.target;
      }), BlockState.CODEC.fieldOf("state").forGetter((config) -> {
         return config.state;
      }), FeatureSpread.CODEC.fieldOf("radius").forGetter((config) -> {
         return config.radius;
      })).apply(builder, BlobReplacementConfig::new);
   });
   public final BlockState target;
   public final BlockState state;
   private final FeatureSpread radius;

   public BlobReplacementConfig(BlockState target, BlockState state, FeatureSpread radius) {
      this.target = target;
      this.state = state;
      this.radius = radius;
   }

   public FeatureSpread getRadius() {
      return this.radius;
   }
}