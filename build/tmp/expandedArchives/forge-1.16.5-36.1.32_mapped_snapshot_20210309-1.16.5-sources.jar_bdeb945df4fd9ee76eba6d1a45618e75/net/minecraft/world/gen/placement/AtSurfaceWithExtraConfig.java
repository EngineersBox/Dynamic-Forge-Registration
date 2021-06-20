package net.minecraft.world.gen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class AtSurfaceWithExtraConfig implements IPlacementConfig {
   public static final Codec<AtSurfaceWithExtraConfig> CODEC = RecordCodecBuilder.create((builder) -> {
      return builder.group(Codec.INT.fieldOf("count").forGetter((config) -> {
         return config.count;
      }), Codec.FLOAT.fieldOf("extra_chance").forGetter((config) -> {
         return config.extraChance;
      }), Codec.INT.fieldOf("extra_count").forGetter((config) -> {
         return config.extraCount;
      })).apply(builder, AtSurfaceWithExtraConfig::new);
   });
   public final int count;
   public final float extraChance;
   public final int extraCount;

   public AtSurfaceWithExtraConfig(int count, float extraChanceIn, int extraCountIn) {
      this.count = count;
      this.extraChance = extraChanceIn;
      this.extraCount = extraCountIn;
   }
}