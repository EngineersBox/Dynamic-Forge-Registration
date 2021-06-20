package net.minecraft.world.gen.carver;

import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ProbabilityConfig;

public class ConfiguredCarvers {
   public static final ConfiguredCarver<ProbabilityConfig> CAVE = register("cave", WorldCarver.CAVE.func_242761_a(new ProbabilityConfig(0.14285715F)));
   public static final ConfiguredCarver<ProbabilityConfig> CANYON = register("canyon", WorldCarver.CANYON.func_242761_a(new ProbabilityConfig(0.02F)));
   public static final ConfiguredCarver<ProbabilityConfig> OCEAN_CAVE = register("ocean_cave", WorldCarver.CAVE.func_242761_a(new ProbabilityConfig(0.06666667F)));
   public static final ConfiguredCarver<ProbabilityConfig> UNDERWATER_CANYON = register("underwater_canyon", WorldCarver.UNDERWATER_CANYON.func_242761_a(new ProbabilityConfig(0.02F)));
   public static final ConfiguredCarver<ProbabilityConfig> UNDERWATER_CAVE = register("underwater_cave", WorldCarver.UNDERWATER_CAVE.func_242761_a(new ProbabilityConfig(0.06666667F)));
   public static final ConfiguredCarver<ProbabilityConfig> NETHER_CAVE = register("nether_cave", WorldCarver.NETHER_CAVE.func_242761_a(new ProbabilityConfig(0.2F)));

   private static <WC extends ICarverConfig> ConfiguredCarver<WC> register(String name, ConfiguredCarver<WC> carver) {
      return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_CARVER, name, carver);
   }
}