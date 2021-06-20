package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Random;
import java.util.function.Supplier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;

public class ConfiguredRandomFeatureList {
   public static final Codec<ConfiguredRandomFeatureList> CODEC = RecordCodecBuilder.create((builder) -> {
      return builder.group(ConfiguredFeature.field_236264_b_.fieldOf("feature").forGetter((p_242789_0_) -> {
         return p_242789_0_.feature;
      }), Codec.floatRange(0.0F, 1.0F).fieldOf("chance").forGetter((p_236432_0_) -> {
         return p_236432_0_.chance;
      })).apply(builder, ConfiguredRandomFeatureList::new);
   });
   public final Supplier<ConfiguredFeature<?, ?>> feature;
   public final float chance;

   public ConfiguredRandomFeatureList(ConfiguredFeature<?, ?> feature, float chance) {
      this(() -> {
         return feature;
      }, chance);
   }

   private ConfiguredRandomFeatureList(Supplier<ConfiguredFeature<?, ?>> feature, float chance) {
      this.feature = feature;
      this.chance = chance;
   }

   public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos) {
      return this.feature.get().generate(reader, generator, rand, pos);
   }
}