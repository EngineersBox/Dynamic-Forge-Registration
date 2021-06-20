package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public class HugeFungusConfig implements IFeatureConfig {
   public static final Codec<HugeFungusConfig> CODEC = RecordCodecBuilder.create((builder) -> {
      return builder.group(BlockState.CODEC.fieldOf("valid_base_block").forGetter((config) -> {
         return config.validBaseBlock;
      }), BlockState.CODEC.fieldOf("stem_state").forGetter((config) -> {
         return config.stemState;
      }), BlockState.CODEC.fieldOf("hat_state").forGetter((config) -> {
         return config.hatState;
      }), BlockState.CODEC.fieldOf("decor_state").forGetter((config) -> {
         return config.decorState;
      }), Codec.BOOL.fieldOf("planted").orElse(false).forGetter((config) -> {
         return config.planted;
      })).apply(builder, HugeFungusConfig::new);
   });
   public static final HugeFungusConfig CRIMSON_FUNGI_PLANTED = new HugeFungusConfig(Blocks.CRIMSON_NYLIUM.getDefaultState(), Blocks.CRIMSON_STEM.getDefaultState(), Blocks.NETHER_WART_BLOCK.getDefaultState(), Blocks.SHROOMLIGHT.getDefaultState(), true);
   public static final HugeFungusConfig CRIMSON_FUNGI;
   public static final HugeFungusConfig WARPED_FUNGI_PLANTED = new HugeFungusConfig(Blocks.WARPED_NYLIUM.getDefaultState(), Blocks.WARPED_STEM.getDefaultState(), Blocks.WARPED_WART_BLOCK.getDefaultState(), Blocks.SHROOMLIGHT.getDefaultState(), true);
   public static final HugeFungusConfig WARPED_FUNGI;
   public final BlockState validBaseBlock;
   public final BlockState stemState;
   public final BlockState hatState;
   public final BlockState decorState;
   public final boolean planted;

   public HugeFungusConfig(BlockState validBaseBlock, BlockState stemState, BlockState hatState, BlockState decorState, boolean planted) {
      this.validBaseBlock = validBaseBlock;
      this.stemState = stemState;
      this.hatState = hatState;
      this.decorState = decorState;
      this.planted = planted;
   }

   static {
      CRIMSON_FUNGI = new HugeFungusConfig(CRIMSON_FUNGI_PLANTED.validBaseBlock, CRIMSON_FUNGI_PLANTED.stemState, CRIMSON_FUNGI_PLANTED.hatState, CRIMSON_FUNGI_PLANTED.decorState, false);
      WARPED_FUNGI = new HugeFungusConfig(WARPED_FUNGI_PLANTED.validBaseBlock, WARPED_FUNGI_PLANTED.stemState, WARPED_FUNGI_PLANTED.hatState, WARPED_FUNGI_PLANTED.decorState, false);
   }
}