package net.minecraft.world.gen.surfacebuilders;

import net.minecraft.block.Blocks;
import net.minecraft.util.registry.WorldGenRegistries;

public class ConfiguredSurfaceBuilders {
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> BADLANDS = register("badlands", SurfaceBuilder.BADLANDS.func_242929_a(SurfaceBuilder.RED_SAND_WHITE_TERRACOTTA_GRAVEL_CONFIG));
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> BASALT_DELTAS = register("basalt_deltas", SurfaceBuilder.BASALT_DELTAS.func_242929_a(SurfaceBuilder.BASALT_DELTAS_CONFIG));
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> CRIMSON_FOREST = register("crimson_forest", SurfaceBuilder.NETHER_FOREST.func_242929_a(SurfaceBuilder.CRIMSON_FOREST_CONFIG));
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> DESERT = register("desert", SurfaceBuilder.DEFAULT.func_242929_a(SurfaceBuilder.SAND_SAND_GRAVEL_CONFIG));
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> END = register("end", SurfaceBuilder.DEFAULT.func_242929_a(SurfaceBuilder.END_STONE_CONFIG));
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> ERODED_BADLANDS = register("eroded_badlands", SurfaceBuilder.ERODED_BADLANDS.func_242929_a(SurfaceBuilder.RED_SAND_WHITE_TERRACOTTA_GRAVEL_CONFIG));
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> FROZEN_OCEAN = register("frozen_ocean", SurfaceBuilder.FROZEN_OCEAN.func_242929_a(SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG));
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> FULL_SAND = register("full_sand", SurfaceBuilder.DEFAULT.func_242929_a(SurfaceBuilder.SAND_CONFIG));
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> GIANT_TREE_TAIGA = register("giant_tree_taiga", SurfaceBuilder.GIANT_TREE_TAIGA.func_242929_a(SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG));
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> GRASS = register("grass", SurfaceBuilder.DEFAULT.func_242929_a(SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG));
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> GRAVELLY_MOUNTAIN = register("gravelly_mountain", SurfaceBuilder.GRAVELLY_MOUNTAIN.func_242929_a(SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG));
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> ICE_SPIKES = register("ice_spikes", SurfaceBuilder.DEFAULT.func_242929_a(new SurfaceBuilderConfig(Blocks.SNOW_BLOCK.getDefaultState(), Blocks.DIRT.getDefaultState(), Blocks.GRAVEL.getDefaultState())));
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> MOUNTAIN = register("mountain", SurfaceBuilder.MOUNTAIN.func_242929_a(SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG));
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> MYCELIUM = register("mycelium", SurfaceBuilder.DEFAULT.func_242929_a(SurfaceBuilder.MYCELIUM_DIRT_GRAVEL_CONFIG));
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> NETHER = register("nether", SurfaceBuilder.NETHER.func_242929_a(SurfaceBuilder.NETHERRACK_CONFIG));
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> NOPE = register("nope", SurfaceBuilder.NOPE.func_242929_a(SurfaceBuilder.STONE_STONE_GRAVEL_CONFIG));
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> OCEAN_SAND = register("ocean_sand", SurfaceBuilder.DEFAULT.func_242929_a(SurfaceBuilder.GRASS_DIRT_SAND_CONFIG));
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> SHATTERED_SAVANNA = register("shattered_savanna", SurfaceBuilder.SHATTERED_SAVANNA.func_242929_a(SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG));
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> SOUL_SAND_VALLEY = register("soul_sand_valley", SurfaceBuilder.SOUL_SAND_VALLEY.func_242929_a(SurfaceBuilder.SOUL_SAND_VALLEY_CONFIG));
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> STONE = register("stone", SurfaceBuilder.DEFAULT.func_242929_a(SurfaceBuilder.STONE_STONE_GRAVEL_CONFIG));
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> SWAMP = register("swamp", SurfaceBuilder.SWAMP.func_242929_a(SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG));
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> WARPED_FOREST = register("warped_forest", SurfaceBuilder.NETHER_FOREST.func_242929_a(SurfaceBuilder.WARPED_FOREST_CONFIG));
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> WOODED_BADLANDS = register("wooded_badlands", SurfaceBuilder.WOODED_BADLANDS.func_242929_a(SurfaceBuilder.RED_SAND_WHITE_TERRACOTTA_GRAVEL_CONFIG));

   private static <SC extends ISurfaceBuilderConfig> ConfiguredSurfaceBuilder<SC> register(String name, ConfiguredSurfaceBuilder<SC> configuredSurfaceBuilder) {
      return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_SURFACE_BUILDER, name, configuredSurfaceBuilder);
   }
}