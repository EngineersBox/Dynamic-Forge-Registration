package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;

public class BlockStateFeatureConfig implements IFeatureConfig {
   public static final Codec<BlockStateFeatureConfig> CODEC = BlockState.CODEC.fieldOf("state").xmap(BlockStateFeatureConfig::new, (config) -> {
      return config.state;
   }).codec();
   public final BlockState state;

   public BlockStateFeatureConfig(BlockState state) {
      this.state = state;
   }
}