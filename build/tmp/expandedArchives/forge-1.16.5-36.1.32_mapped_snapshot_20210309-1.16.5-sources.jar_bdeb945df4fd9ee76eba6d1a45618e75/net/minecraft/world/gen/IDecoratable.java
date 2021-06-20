package net.minecraft.world.gen;

import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;

public interface IDecoratable<R> {
   R withPlacement(ConfiguredPlacement<?> placement);

   default R chance(int chance) {
      return this.withPlacement(Placement.CHANCE.configure(new ChanceConfig(chance)));
   }

   default R countSpread(FeatureSpread spread) {
      return this.withPlacement(Placement.COUNT.configure(new FeatureSpreadConfig(spread)));
   }

   default R count(int amount) {
      return this.countSpread(FeatureSpread.create(amount));
   }

   default R variableCount(int max) {
      return this.countSpread(FeatureSpread.create(0, max));
   }

   default R range(int maxHeight) {
      return this.withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(0, 0, maxHeight)));
   }

   default R square() {
      return this.withPlacement(Placement.SQUARE.configure(NoPlacementConfig.INSTANCE));
   }
}