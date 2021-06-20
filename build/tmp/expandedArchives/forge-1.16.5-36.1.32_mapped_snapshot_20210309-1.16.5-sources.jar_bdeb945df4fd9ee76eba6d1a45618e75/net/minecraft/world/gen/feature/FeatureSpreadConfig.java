package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.placement.IPlacementConfig;

public class FeatureSpreadConfig implements IPlacementConfig, IFeatureConfig {
   public static final Codec<FeatureSpreadConfig> CODEC = FeatureSpread.createCodec(-10, 128, 128).fieldOf("count").xmap(FeatureSpreadConfig::new, FeatureSpreadConfig::getSpread).codec();
   private final FeatureSpread spread;

   public FeatureSpreadConfig(int base) {
      this.spread = FeatureSpread.create(base);
   }

   public FeatureSpreadConfig(FeatureSpread spread) {
      this.spread = spread;
   }

   public FeatureSpread getSpread() {
      return this.spread;
   }
}