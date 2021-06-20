package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;

public class ReplaceBlockFeature extends Feature<ReplaceBlockConfig> {
   public ReplaceBlockFeature(Codec<ReplaceBlockConfig> codec) {
      super(codec);
   }

   public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, ReplaceBlockConfig config) {
      if (reader.getBlockState(pos).matchesBlock(config.target.getBlock())) {
         reader.setBlockState(pos, config.state, 2);
      }

      return true;
   }
}