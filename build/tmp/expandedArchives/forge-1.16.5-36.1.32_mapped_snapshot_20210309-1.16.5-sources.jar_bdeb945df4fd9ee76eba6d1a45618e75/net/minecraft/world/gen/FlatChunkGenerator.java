package net.minecraft.world.gen;

import com.mojang.serialization.Codec;
import java.util.Arrays;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Blockreader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.SingleBiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FlatChunkGenerator extends ChunkGenerator {
   public static final Codec<FlatChunkGenerator> CODEC = FlatGenerationSettings.CODEC.fieldOf("settings").xmap(FlatChunkGenerator::new, FlatChunkGenerator::getSettings).codec();
   private final FlatGenerationSettings settings;

   public FlatChunkGenerator(FlatGenerationSettings settings) {
      super(new SingleBiomeProvider(settings.getConfiguredBiome()), new SingleBiomeProvider(settings.getBiome()), settings.func_236943_d_(), 0L);
      this.settings = settings;
   }

   protected Codec<? extends ChunkGenerator> func_230347_a_() {
      return CODEC;
   }

   @OnlyIn(Dist.CLIENT)
   public ChunkGenerator func_230349_a_(long p_230349_1_) {
      return this;
   }

   public FlatGenerationSettings getSettings() {
      return this.settings;
   }

   /**
    * Generate the SURFACE part of a chunk
    */
   public void generateSurface(WorldGenRegion p_225551_1_, IChunk p_225551_2_) {
   }

   public int getGroundHeight() {
      BlockState[] ablockstate = this.settings.getStates();

      for(int i = 0; i < ablockstate.length; ++i) {
         BlockState blockstate = ablockstate[i] == null ? Blocks.AIR.getDefaultState() : ablockstate[i];
         if (!Heightmap.Type.MOTION_BLOCKING.getHeightLimitPredicate().test(blockstate)) {
            return i - 1;
         }
      }

      return ablockstate.length;
   }

   public void func_230352_b_(IWorld p_230352_1_, StructureManager p_230352_2_, IChunk p_230352_3_) {
      BlockState[] ablockstate = this.settings.getStates();
      BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
      Heightmap heightmap = p_230352_3_.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
      Heightmap heightmap1 = p_230352_3_.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);

      for(int i = 0; i < ablockstate.length; ++i) {
         BlockState blockstate = ablockstate[i];
         if (blockstate != null) {
            for(int j = 0; j < 16; ++j) {
               for(int k = 0; k < 16; ++k) {
                  p_230352_3_.setBlockState(blockpos$mutable.setPos(j, i, k), blockstate, false);
                  heightmap.update(j, i, k, blockstate);
                  heightmap1.update(j, i, k, blockstate);
               }
            }
         }
      }

   }

   public int getHeight(int x, int z, Heightmap.Type heightmapType) {
      BlockState[] ablockstate = this.settings.getStates();

      for(int i = ablockstate.length - 1; i >= 0; --i) {
         BlockState blockstate = ablockstate[i];
         if (blockstate != null && heightmapType.getHeightLimitPredicate().test(blockstate)) {
            return i + 1;
         }
      }

      return 0;
   }

   public IBlockReader func_230348_a_(int p_230348_1_, int p_230348_2_) {
      return new Blockreader(Arrays.stream(this.settings.getStates()).map((state) -> {
         return state == null ? Blocks.AIR.getDefaultState() : state;
      }).toArray((size) -> {
         return new BlockState[size];
      }));
   }
}