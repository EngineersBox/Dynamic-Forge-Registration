package net.minecraft.world.gen.feature;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.registry.Registry;

public class LiquidsConfig implements IFeatureConfig {
   public static final Codec<LiquidsConfig> CODEC = RecordCodecBuilder.create((builder) -> {
      return builder.group(FluidState.field_237213_a_.fieldOf("state").forGetter((config) -> {
         return config.state;
      }), Codec.BOOL.fieldOf("requires_block_below").orElse(true).forGetter((config) -> {
         return config.needsBlockBelow;
      }), Codec.INT.fieldOf("rock_count").orElse(4).forGetter((config) -> {
         return config.rockAmount;
      }), Codec.INT.fieldOf("hole_count").orElse(1).forGetter((config) -> {
         return config.holeAmount;
      }), Registry.BLOCK.listOf().fieldOf("valid_blocks").<Set<Block>>xmap(ImmutableSet::copyOf, ImmutableList::copyOf).forGetter((config) -> {
         return config.acceptedBlocks;
      })).apply(builder, LiquidsConfig::new);
   });
   public final FluidState state;
   public final boolean needsBlockBelow;
   public final int rockAmount;
   public final int holeAmount;
   public final Set<Block> acceptedBlocks;

   public LiquidsConfig(FluidState state, boolean needBlockBelow, int rockAmount, int holeAmount, Set<Block> acceptedBlocks) {
      this.state = state;
      this.needsBlockBelow = needBlockBelow;
      this.rockAmount = rockAmount;
      this.holeAmount = holeAmount;
      this.acceptedBlocks = acceptedBlocks;
   }
}