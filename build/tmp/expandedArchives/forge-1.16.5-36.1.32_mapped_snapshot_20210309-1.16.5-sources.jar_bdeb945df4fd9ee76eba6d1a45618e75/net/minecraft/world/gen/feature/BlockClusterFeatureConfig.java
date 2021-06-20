package net.minecraft.world.gen.feature;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.blockplacer.BlockPlacer;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;

public class BlockClusterFeatureConfig implements IFeatureConfig {
   public static final Codec<BlockClusterFeatureConfig> CODEC = RecordCodecBuilder.create((builder) -> {
      return builder.group(BlockStateProvider.CODEC.fieldOf("state_provider").forGetter((config) -> {
         return config.stateProvider;
      }), BlockPlacer.CODEC.fieldOf("block_placer").forGetter((config) -> {
         return config.blockPlacer;
      }), BlockState.CODEC.listOf().fieldOf("whitelist").forGetter((config) -> {
         return config.whitelist.stream().map(Block::getDefaultState).collect(Collectors.toList());
      }), BlockState.CODEC.listOf().fieldOf("blacklist").forGetter((config) -> {
         return ImmutableList.copyOf(config.blacklist);
      }), Codec.INT.fieldOf("tries").orElse(128).forGetter((config) -> {
         return config.tryCount;
      }), Codec.INT.fieldOf("xspread").orElse(7).forGetter((config) -> {
         return config.xSpread;
      }), Codec.INT.fieldOf("yspread").orElse(3).forGetter((config) -> {
         return config.ySpread;
      }), Codec.INT.fieldOf("zspread").orElse(7).forGetter((config) -> {
         return config.zSpread;
      }), Codec.BOOL.fieldOf("can_replace").orElse(false).forGetter((config) -> {
         return config.isReplaceable;
      }), Codec.BOOL.fieldOf("project").orElse(true).forGetter((config) -> {
         return config.project;
      }), Codec.BOOL.fieldOf("need_water").orElse(false).forGetter((config) -> {
         return config.requiresWater;
      })).apply(builder, BlockClusterFeatureConfig::new);
   });
   public final BlockStateProvider stateProvider;
   public final BlockPlacer blockPlacer;
   public final Set<Block> whitelist;
   public final Set<BlockState> blacklist;
   public final int tryCount;
   public final int xSpread;
   public final int ySpread;
   public final int zSpread;
   public final boolean isReplaceable;
   public final boolean project;
   public final boolean requiresWater;

   private BlockClusterFeatureConfig(BlockStateProvider stateProvider, BlockPlacer blockPlacer, List<BlockState> whitelist, List<BlockState> blacklist, int tryCount, int xSpread, int ySpread, int zSpread, boolean isReplaceable, boolean project, boolean requiresWater) {
      this(stateProvider, blockPlacer, whitelist.stream().map(AbstractBlock.AbstractBlockState::getBlock).collect(Collectors.toSet()), ImmutableSet.copyOf(blacklist), tryCount, xSpread, ySpread, zSpread, isReplaceable, project, requiresWater);
   }

   private BlockClusterFeatureConfig(BlockStateProvider stateProvider, BlockPlacer blockPlacer, Set<Block> whitelist, Set<BlockState> blacklist, int tryCount, int xSpread, int ySpread, int zSpread, boolean isReplaceable, boolean project, boolean requiresWater) {
      this.stateProvider = stateProvider;
      this.blockPlacer = blockPlacer;
      this.whitelist = whitelist;
      this.blacklist = blacklist;
      this.tryCount = tryCount;
      this.xSpread = xSpread;
      this.ySpread = ySpread;
      this.zSpread = zSpread;
      this.isReplaceable = isReplaceable;
      this.project = project;
      this.requiresWater = requiresWater;
   }

   public static class Builder {
      private final BlockStateProvider stateProvider;
      private final BlockPlacer blockPlacer;
      private Set<Block> whitelist = ImmutableSet.of();
      private Set<BlockState> blacklist = ImmutableSet.of();
      private int tryCount = 64;
      private int xSpread = 7;
      private int ySpread = 3;
      private int zSpread = 7;
      private boolean isReplaceable;
      private boolean project = true;
      private boolean requiresWater = false;

      public Builder(BlockStateProvider stateProvider, BlockPlacer blockPlacer) {
         this.stateProvider = stateProvider;
         this.blockPlacer = blockPlacer;
      }

      public BlockClusterFeatureConfig.Builder whitelist(Set<Block> whitelist) {
         this.whitelist = whitelist;
         return this;
      }

      public BlockClusterFeatureConfig.Builder blacklist(Set<BlockState> blacklist) {
         this.blacklist = blacklist;
         return this;
      }

      public BlockClusterFeatureConfig.Builder tries(int tries) {
         this.tryCount = tries;
         return this;
      }

      public BlockClusterFeatureConfig.Builder xSpread(int xSpread) {
         this.xSpread = xSpread;
         return this;
      }

      public BlockClusterFeatureConfig.Builder ySpread(int ySpread) {
         this.ySpread = ySpread;
         return this;
      }

      public BlockClusterFeatureConfig.Builder zSpread(int zSpread) {
         this.zSpread = zSpread;
         return this;
      }

      public BlockClusterFeatureConfig.Builder replaceable() {
         this.isReplaceable = true;
         return this;
      }

      public BlockClusterFeatureConfig.Builder preventProjection() {
         this.project = false;
         return this;
      }

      public BlockClusterFeatureConfig.Builder requiresWater() {
         this.requiresWater = true;
         return this;
      }

      public BlockClusterFeatureConfig build() {
         return new BlockClusterFeatureConfig(this.stateProvider, this.blockPlacer, this.whitelist, this.blacklist, this.tryCount, this.xSpread, this.ySpread, this.zSpread, this.isReplaceable, this.project, this.requiresWater);
      }
   }
}