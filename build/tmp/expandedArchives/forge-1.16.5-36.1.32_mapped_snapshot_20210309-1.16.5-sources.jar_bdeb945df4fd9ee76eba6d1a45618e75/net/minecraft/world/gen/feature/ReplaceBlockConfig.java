package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;

public class ReplaceBlockConfig implements IFeatureConfig {
   public static final Codec<ReplaceBlockConfig> CODEC = RecordCodecBuilder.create((builder) -> {
      return builder.group(BlockState.CODEC.fieldOf("target").forGetter((config) -> {
         return config.target;
      }), BlockState.CODEC.fieldOf("state").forGetter((config) -> {
         return config.state;
      })).apply(builder, ReplaceBlockConfig::new);
   });
   public final BlockState target;
   public final BlockState state;

   public ReplaceBlockConfig(BlockState target, BlockState state) {
      this.target = target;
      this.state = state;
   }
}