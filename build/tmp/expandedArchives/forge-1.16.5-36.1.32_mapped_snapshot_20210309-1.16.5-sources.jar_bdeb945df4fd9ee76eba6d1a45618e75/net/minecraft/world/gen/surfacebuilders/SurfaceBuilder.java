package net.minecraft.world.gen.surfacebuilders;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;

public abstract class SurfaceBuilder<C extends ISurfaceBuilderConfig> extends net.minecraftforge.registries.ForgeRegistryEntry<SurfaceBuilder<?>> {
   private static final BlockState DIRT = Blocks.DIRT.getDefaultState();
   private static final BlockState GRASS_BLOCK = Blocks.GRASS_BLOCK.getDefaultState();
   private static final BlockState PODZOL = Blocks.PODZOL.getDefaultState();
   private static final BlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
   private static final BlockState STONE = Blocks.STONE.getDefaultState();
   private static final BlockState COARSE_DIRT = Blocks.COARSE_DIRT.getDefaultState();
   private static final BlockState SAND = Blocks.SAND.getDefaultState();
   private static final BlockState RED_SAND = Blocks.RED_SAND.getDefaultState();
   private static final BlockState WHITE_TERRACOTTA = Blocks.WHITE_TERRACOTTA.getDefaultState();
   private static final BlockState MYCELIUM = Blocks.MYCELIUM.getDefaultState();
   private static final BlockState SOUL_SAND = Blocks.SOUL_SAND.getDefaultState();
   private static final BlockState NETHERRACK = Blocks.NETHERRACK.getDefaultState();
   private static final BlockState END_STONE = Blocks.END_STONE.getDefaultState();
   private static final BlockState CRIMSON_NYLIUM = Blocks.CRIMSON_NYLIUM.getDefaultState();
   private static final BlockState WARPED_NYLIUM = Blocks.WARPED_NYLIUM.getDefaultState();
   private static final BlockState NETHER_WART_BLOCK = Blocks.NETHER_WART_BLOCK.getDefaultState();
   private static final BlockState WARPED_WART_BLOCK = Blocks.WARPED_WART_BLOCK.getDefaultState();
   private static final BlockState BLACKSTONE = Blocks.BLACKSTONE.getDefaultState();
   private static final BlockState BASALT = Blocks.BASALT.getDefaultState();
   private static final BlockState MAGMA_BLOCK = Blocks.MAGMA_BLOCK.getDefaultState();
   public static final SurfaceBuilderConfig PODZOL_DIRT_GRAVEL_CONFIG = new SurfaceBuilderConfig(PODZOL, DIRT, GRAVEL);
   public static final SurfaceBuilderConfig GRAVEL_CONFIG = new SurfaceBuilderConfig(GRAVEL, GRAVEL, GRAVEL);
   public static final SurfaceBuilderConfig GRASS_DIRT_GRAVEL_CONFIG = new SurfaceBuilderConfig(GRASS_BLOCK, DIRT, GRAVEL);
   public static final SurfaceBuilderConfig STONE_STONE_GRAVEL_CONFIG = new SurfaceBuilderConfig(STONE, STONE, GRAVEL);
   public static final SurfaceBuilderConfig CORASE_DIRT_DIRT_GRAVEL_CONFIG = new SurfaceBuilderConfig(COARSE_DIRT, DIRT, GRAVEL);
   public static final SurfaceBuilderConfig SAND_SAND_GRAVEL_CONFIG = new SurfaceBuilderConfig(SAND, SAND, GRAVEL);
   public static final SurfaceBuilderConfig GRASS_DIRT_SAND_CONFIG = new SurfaceBuilderConfig(GRASS_BLOCK, DIRT, SAND);
   public static final SurfaceBuilderConfig SAND_CONFIG = new SurfaceBuilderConfig(SAND, SAND, SAND);
   public static final SurfaceBuilderConfig RED_SAND_WHITE_TERRACOTTA_GRAVEL_CONFIG = new SurfaceBuilderConfig(RED_SAND, WHITE_TERRACOTTA, GRAVEL);
   public static final SurfaceBuilderConfig MYCELIUM_DIRT_GRAVEL_CONFIG = new SurfaceBuilderConfig(MYCELIUM, DIRT, GRAVEL);
   public static final SurfaceBuilderConfig NETHERRACK_CONFIG = new SurfaceBuilderConfig(NETHERRACK, NETHERRACK, NETHERRACK);
   public static final SurfaceBuilderConfig SOUL_SAND_VALLEY_CONFIG = new SurfaceBuilderConfig(SOUL_SAND, SOUL_SAND, SOUL_SAND);
   public static final SurfaceBuilderConfig END_STONE_CONFIG = new SurfaceBuilderConfig(END_STONE, END_STONE, END_STONE);
   public static final SurfaceBuilderConfig CRIMSON_FOREST_CONFIG = new SurfaceBuilderConfig(CRIMSON_NYLIUM, NETHERRACK, NETHER_WART_BLOCK);
   public static final SurfaceBuilderConfig WARPED_FOREST_CONFIG = new SurfaceBuilderConfig(WARPED_NYLIUM, NETHERRACK, WARPED_WART_BLOCK);
   public static final SurfaceBuilderConfig BASALT_DELTAS_CONFIG = new SurfaceBuilderConfig(BLACKSTONE, BASALT, MAGMA_BLOCK);
   public static final SurfaceBuilder<SurfaceBuilderConfig> DEFAULT = register("default", new DefaultSurfaceBuilder(SurfaceBuilderConfig.CODEC));
   public static final SurfaceBuilder<SurfaceBuilderConfig> MOUNTAIN = register("mountain", new MountainSurfaceBuilder(SurfaceBuilderConfig.CODEC));
   public static final SurfaceBuilder<SurfaceBuilderConfig> SHATTERED_SAVANNA = register("shattered_savanna", new ShatteredSavannaSurfaceBuilder(SurfaceBuilderConfig.CODEC));
   public static final SurfaceBuilder<SurfaceBuilderConfig> GRAVELLY_MOUNTAIN = register("gravelly_mountain", new GravellyMountainSurfaceBuilder(SurfaceBuilderConfig.CODEC));
   public static final SurfaceBuilder<SurfaceBuilderConfig> GIANT_TREE_TAIGA = register("giant_tree_taiga", new GiantTreeTaigaSurfaceBuilder(SurfaceBuilderConfig.CODEC));
   public static final SurfaceBuilder<SurfaceBuilderConfig> SWAMP = register("swamp", new SwampSurfaceBuilder(SurfaceBuilderConfig.CODEC));
   public static final SurfaceBuilder<SurfaceBuilderConfig> BADLANDS = register("badlands", new BadlandsSurfaceBuilder(SurfaceBuilderConfig.CODEC));
   public static final SurfaceBuilder<SurfaceBuilderConfig> WOODED_BADLANDS = register("wooded_badlands", new WoodedBadlandsSurfaceBuilder(SurfaceBuilderConfig.CODEC));
   public static final SurfaceBuilder<SurfaceBuilderConfig> ERODED_BADLANDS = register("eroded_badlands", new ErodedBadlandsSurfaceBuilder(SurfaceBuilderConfig.CODEC));
   public static final SurfaceBuilder<SurfaceBuilderConfig> FROZEN_OCEAN = register("frozen_ocean", new FrozenOceanSurfaceBuilder(SurfaceBuilderConfig.CODEC));
   public static final SurfaceBuilder<SurfaceBuilderConfig> NETHER = register("nether", new NetherSurfaceBuilder(SurfaceBuilderConfig.CODEC));
   public static final SurfaceBuilder<SurfaceBuilderConfig> NETHER_FOREST = register("nether_forest", new NetherForestsSurfaceBuilder(SurfaceBuilderConfig.CODEC));
   public static final SurfaceBuilder<SurfaceBuilderConfig> SOUL_SAND_VALLEY = register("soul_sand_valley", new SoulSandValleySurfaceBuilder(SurfaceBuilderConfig.CODEC));
   public static final SurfaceBuilder<SurfaceBuilderConfig> BASALT_DELTAS = register("basalt_deltas", new BasaltDeltasSurfaceBuilder(SurfaceBuilderConfig.CODEC));
   public static final SurfaceBuilder<SurfaceBuilderConfig> NOPE = register("nope", new NoopSurfaceBuilder(SurfaceBuilderConfig.CODEC));
   private final Codec<ConfiguredSurfaceBuilder<C>> codec;

   private static <C extends ISurfaceBuilderConfig, F extends SurfaceBuilder<C>> F register(String key, F builderIn) {
      return Registry.register(Registry.SURFACE_BUILDER, key, builderIn);
   }

   public SurfaceBuilder(Codec<C> codec) {
      this.codec = codec.fieldOf("config").xmap(this::func_242929_a, ConfiguredSurfaceBuilder::getConfig).codec();
   }

   public Codec<ConfiguredSurfaceBuilder<C>> getCodec() {
      return this.codec;
   }

   public ConfiguredSurfaceBuilder<C> func_242929_a(C config) {
      return new ConfiguredSurfaceBuilder<>(this, config);
   }

   public abstract void buildSurface(Random random, IChunk chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, C config);

   public void setSeed(long seed) {
   }
}
