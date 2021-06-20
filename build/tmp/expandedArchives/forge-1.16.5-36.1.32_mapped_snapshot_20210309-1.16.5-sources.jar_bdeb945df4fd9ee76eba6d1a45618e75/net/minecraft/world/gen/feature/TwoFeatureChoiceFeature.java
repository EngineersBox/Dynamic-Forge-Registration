package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;

public class TwoFeatureChoiceFeature extends Feature<TwoFeatureChoiceConfig> {
   public TwoFeatureChoiceFeature(Codec<TwoFeatureChoiceConfig> codec) {
      super(codec);
   }

   public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, TwoFeatureChoiceConfig config) {
      boolean flag = rand.nextBoolean();
      return flag ? config.featureTrue.get().generate(reader, generator, rand, pos) : config.featureFalse.get().generate(reader, generator, rand, pos);
   }
}