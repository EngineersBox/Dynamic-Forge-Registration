package net.minecraft.world.gen.surfacebuilders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;

public class SurfaceBuilderConfig implements ISurfaceBuilderConfig {
   public static final Codec<SurfaceBuilderConfig> CODEC = RecordCodecBuilder.create((p_237204_0_) -> {
      return p_237204_0_.group(BlockState.CODEC.fieldOf("top_material").forGetter((config) -> {
         return config.topMaterial;
      }), BlockState.CODEC.fieldOf("under_material").forGetter((config) -> {
         return config.underMaterial;
      }), BlockState.CODEC.fieldOf("underwater_material").forGetter((config) -> {
         return config.underWaterMaterial;
      })).apply(p_237204_0_, SurfaceBuilderConfig::new);
   });
   private final BlockState topMaterial;
   private final BlockState underMaterial;
   private final BlockState underWaterMaterial;

   public SurfaceBuilderConfig(BlockState topMaterial, BlockState underMaterial, BlockState underWaterMaterial) {
      this.topMaterial = topMaterial;
      this.underMaterial = underMaterial;
      this.underWaterMaterial = underWaterMaterial;
   }

   public BlockState getTop() {
      return this.topMaterial;
   }

   public BlockState getUnder() {
      return this.underMaterial;
   }

   public BlockState getUnderWaterMaterial() {
      return this.underWaterMaterial;
   }
}