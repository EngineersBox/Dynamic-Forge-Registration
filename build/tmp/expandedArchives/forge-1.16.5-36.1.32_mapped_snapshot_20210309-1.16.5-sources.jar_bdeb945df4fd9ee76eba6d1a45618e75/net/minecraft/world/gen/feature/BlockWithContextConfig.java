package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.block.BlockState;

public class BlockWithContextConfig implements IFeatureConfig {
   public static final Codec<BlockWithContextConfig> CODEC = RecordCodecBuilder.create((builder) -> {
      return builder.group(BlockState.CODEC.fieldOf("to_place").forGetter((config) -> {
         return config.toPlace;
      }), BlockState.CODEC.listOf().fieldOf("place_on").forGetter((config) -> {
         return config.placeOn;
      }), BlockState.CODEC.listOf().fieldOf("place_in").forGetter((config) -> {
         return config.placeIn;
      }), BlockState.CODEC.listOf().fieldOf("place_under").forGetter((config) -> {
         return config.placeUnder;
      })).apply(builder, BlockWithContextConfig::new);
   });
   public final BlockState toPlace;
   public final List<BlockState> placeOn;
   public final List<BlockState> placeIn;
   public final List<BlockState> placeUnder;

   public BlockWithContextConfig(BlockState toPlace, List<BlockState> placeOn, List<BlockState> placeIn, List<BlockState> placeUnder) {
      this.toPlace = toPlace;
      this.placeOn = placeOn;
      this.placeIn = placeIn;
      this.placeUnder = placeUnder;
   }
}