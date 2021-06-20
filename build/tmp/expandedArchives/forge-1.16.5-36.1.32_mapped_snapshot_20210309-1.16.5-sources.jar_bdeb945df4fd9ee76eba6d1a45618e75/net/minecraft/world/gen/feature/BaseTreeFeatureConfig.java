package net.minecraft.world.gen.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.trunkplacer.AbstractTrunkPlacer;

public class BaseTreeFeatureConfig implements IFeatureConfig {
   public static final Codec<BaseTreeFeatureConfig> CODEC = RecordCodecBuilder.create((builder) -> {
      return builder.group(BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter((config) -> {
         return config.trunkProvider;
      }), BlockStateProvider.CODEC.fieldOf("leaves_provider").forGetter((config) -> {
         return config.leavesProvider;
      }), FoliagePlacer.CODEC.fieldOf("foliage_placer").forGetter((config) -> {
         return config.foliagePlacer;
      }), AbstractTrunkPlacer.CODEC.fieldOf("trunk_placer").forGetter((config) -> {
         return config.trunkPlacer;
      }), AbstractFeatureSizeType.CODEC.fieldOf("minimum_size").forGetter((config) -> {
         return config.minimumSize;
      }), TreeDecorator.field_236874_c_.listOf().fieldOf("decorators").forGetter((config) -> {
         return config.decorators;
      }), Codec.INT.fieldOf("max_water_depth").orElse(0).forGetter((config) -> {
         return config.maxWaterDepth;
      }), Codec.BOOL.fieldOf("ignore_vines").orElse(false).forGetter((config) -> {
         return config.ignoreVines;
      }), Heightmap.Type.CODEC.fieldOf("heightmap").forGetter((config) -> {
         return config.heightmap;
      })).apply(builder, BaseTreeFeatureConfig::new);
   });
   //TODO: Review this, see if we can hook in the sapling into the Codec
   public final BlockStateProvider trunkProvider;
   public final BlockStateProvider leavesProvider;
   public final List<TreeDecorator> decorators;
   public transient boolean forcePlacement;
   public final FoliagePlacer foliagePlacer;
   public final AbstractTrunkPlacer trunkPlacer;
   public final AbstractFeatureSizeType minimumSize;
   public final int maxWaterDepth;
   public final boolean ignoreVines;
   public final Heightmap.Type heightmap;

   protected BaseTreeFeatureConfig(BlockStateProvider trunkProvider, BlockStateProvider leavesProvider, FoliagePlacer foliagePlacer, AbstractTrunkPlacer trunkPlacer, AbstractFeatureSizeType minimumSize, List<TreeDecorator> decorators, int maxWaterDepth, boolean ignoreVines, Heightmap.Type heightmap) {
      this.trunkProvider = trunkProvider;
      this.leavesProvider = leavesProvider;
      this.decorators = decorators;
      this.foliagePlacer = foliagePlacer;
      this.minimumSize = minimumSize;
      this.trunkPlacer = trunkPlacer;
      this.maxWaterDepth = maxWaterDepth;
      this.ignoreVines = ignoreVines;
      this.heightmap = heightmap;
   }

   public void forcePlacement() {
      this.forcePlacement = true;
   }

   public BaseTreeFeatureConfig copy(List<TreeDecorator> decorators) {
      return new BaseTreeFeatureConfig(this.trunkProvider, this.leavesProvider, this.foliagePlacer, this.trunkPlacer, this.minimumSize, decorators, this.maxWaterDepth, this.ignoreVines, this.heightmap);
   }

   public static class Builder {
      public final BlockStateProvider trunkProvider;
      public final BlockStateProvider leavesProvider;
      private final FoliagePlacer foliagePlacer;
      private final AbstractTrunkPlacer trunkPlacer;
      private final AbstractFeatureSizeType minimumSize;
      private List<TreeDecorator> decorators = ImmutableList.of();
      private int maxWaterDepth;
      private boolean ignoreVines;
      private Heightmap.Type heightmap = Heightmap.Type.OCEAN_FLOOR;

      public Builder(BlockStateProvider trunkProvider, BlockStateProvider leavesProvider, FoliagePlacer foliagePlacer, AbstractTrunkPlacer trunkPlacer, AbstractFeatureSizeType minimumSize) {
         this.trunkProvider = trunkProvider;
         this.leavesProvider = leavesProvider;
         this.foliagePlacer = foliagePlacer;
         this.trunkPlacer = trunkPlacer;
         this.minimumSize = minimumSize;
      }

      public BaseTreeFeatureConfig.Builder setDecorators(List<TreeDecorator> decorators) {
         this.decorators = decorators;
         return this;
      }

      public BaseTreeFeatureConfig.Builder setMaxWaterDepth(int maxWaterDepth) {
         this.maxWaterDepth = maxWaterDepth;
         return this;
      }

      public BaseTreeFeatureConfig.Builder setIgnoreVines() {
         this.ignoreVines = true;
         return this;
      }

      public BaseTreeFeatureConfig.Builder setHeightmap(Heightmap.Type heightmap) {
         this.heightmap = heightmap;
         return this;
      }

      public BaseTreeFeatureConfig build() {
         return new BaseTreeFeatureConfig(this.trunkProvider, this.leavesProvider, this.foliagePlacer, this.trunkPlacer, this.minimumSize, this.decorators, this.maxWaterDepth, this.ignoreVines, this.heightmap);
      }
   }
}
