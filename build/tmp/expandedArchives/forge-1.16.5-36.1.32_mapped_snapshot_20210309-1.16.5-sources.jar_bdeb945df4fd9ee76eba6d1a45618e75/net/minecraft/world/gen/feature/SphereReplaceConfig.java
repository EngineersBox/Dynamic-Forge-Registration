package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.block.BlockState;

public class SphereReplaceConfig implements IFeatureConfig {
   public static final Codec<SphereReplaceConfig> CODEC = RecordCodecBuilder.create((builder) -> {
      return builder.group(BlockState.CODEC.fieldOf("state").forGetter((config) -> {
         return config.state;
      }), FeatureSpread.createCodec(0, 4, 4).fieldOf("radius").forGetter((config) -> {
         return config.radius;
      }), Codec.intRange(0, 4).fieldOf("half_height").forGetter((config) -> {
         return config.halfHeight;
      }), BlockState.CODEC.listOf().fieldOf("targets").forGetter((config) -> {
         return config.targets;
      })).apply(builder, SphereReplaceConfig::new);
   });
   public final BlockState state;
   public final FeatureSpread radius;
   public final int halfHeight;
   public final List<BlockState> targets;

   public SphereReplaceConfig(BlockState state, FeatureSpread radius, int halfHeight, List<BlockState> targets) {
      this.state = state;
      this.radius = radius;
      this.halfHeight = halfHeight;
      this.targets = targets;
   }
}