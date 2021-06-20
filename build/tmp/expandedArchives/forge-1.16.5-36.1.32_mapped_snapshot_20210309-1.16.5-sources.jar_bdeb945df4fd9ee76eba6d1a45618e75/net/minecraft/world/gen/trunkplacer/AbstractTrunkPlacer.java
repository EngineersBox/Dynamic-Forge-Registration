package net.minecraft.world.gen.trunkplacer;

import com.mojang.datafixers.Products.P3;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;

public abstract class AbstractTrunkPlacer {
   public static final Codec<AbstractTrunkPlacer> CODEC = Registry.TRUNK_REPLACER.dispatch(AbstractTrunkPlacer::getPlacerType, TrunkPlacerType::getCodec);
   protected final int baseHeight;
   protected final int heightRandA;
   protected final int heightRandB;

   protected static <P extends AbstractTrunkPlacer> P3<Mu<P>, Integer, Integer, Integer> getAbstractTrunkCodec(Instance<P> instance) {
      return instance.group(Codec.intRange(0, 32).fieldOf("base_height").forGetter((placer) -> {
         return placer.baseHeight;
      }), Codec.intRange(0, 24).fieldOf("height_rand_a").forGetter((placer) -> {
         return placer.heightRandA;
      }), Codec.intRange(0, 24).fieldOf("height_rand_b").forGetter((placer) -> {
         return placer.heightRandB;
      }));
   }

   public AbstractTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
      this.baseHeight = baseHeight;
      this.heightRandA = heightRandA;
      this.heightRandB = heightRandB;
   }

   protected abstract TrunkPlacerType<?> getPlacerType();

   public abstract List<FoliagePlacer.Foliage> getFoliages(IWorldGenerationReader reader, Random rand, int treeHeight, BlockPos p_230382_4_, Set<BlockPos> p_230382_5_, MutableBoundingBox p_230382_6_, BaseTreeFeatureConfig p_230382_7_);

   public int getHeight(Random rand) {
      return this.baseHeight + rand.nextInt(this.heightRandA + 1) + rand.nextInt(this.heightRandB + 1);
   }

   protected static void func_236913_a_(IWorldWriter writer, BlockPos pos, BlockState state, MutableBoundingBox area) {
      TreeFeature.setBlockStateWithoutUpdate(writer, pos, state);
      area.expandTo(new MutableBoundingBox(pos, pos));
   }

   private static boolean func_236912_a_(IWorldGenerationBaseReader reader, BlockPos pos) {
      return reader.hasBlockState(pos, (state) -> {
         Block block = state.getBlock();
         return Feature.isDirt(block) && !state.matchesBlock(Blocks.GRASS_BLOCK) && !state.matchesBlock(Blocks.MYCELIUM);
      });
   }

   protected static void func_236909_a_(IWorldGenerationReader reader, BlockPos pos) {
      if (!func_236912_a_(reader, pos)) {
         TreeFeature.setBlockStateWithoutUpdate(reader, pos, Blocks.DIRT.getDefaultState());
      }

   }

   protected static boolean func_236911_a_(IWorldGenerationReader reader, Random rand, BlockPos pos, Set<BlockPos> p_236911_3_, MutableBoundingBox p_236911_4_, BaseTreeFeatureConfig config) {
      if (TreeFeature.isReplaceableAt(reader, pos)) {
         func_236913_a_(reader, pos, config.trunkProvider.getBlockState(rand, pos), p_236911_4_);
         p_236911_3_.add(pos.toImmutable());
         return true;
      } else {
         return false;
      }
   }

   protected static void func_236910_a_(IWorldGenerationReader reader, Random rand, BlockPos.Mutable p_236910_2_, Set<BlockPos> p_236910_3_, MutableBoundingBox p_236910_4_, BaseTreeFeatureConfig config) {
      if (TreeFeature.isLogsAt(reader, p_236910_2_)) {
         func_236911_a_(reader, rand, p_236910_2_, p_236910_3_, p_236910_4_, config);
      }

   }
}