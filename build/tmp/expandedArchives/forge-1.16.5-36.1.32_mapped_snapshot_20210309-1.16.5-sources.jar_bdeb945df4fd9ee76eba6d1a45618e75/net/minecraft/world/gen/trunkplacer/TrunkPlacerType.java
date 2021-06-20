package net.minecraft.world.gen.trunkplacer;

import com.mojang.serialization.Codec;
import net.minecraft.util.registry.Registry;

public class TrunkPlacerType<P extends AbstractTrunkPlacer> {
   public static final TrunkPlacerType<StraightTrunkPlacer> STRAIGHT_TRUNK_PLACER = register("straight_trunk_placer", StraightTrunkPlacer.CODEC);
   public static final TrunkPlacerType<ForkyTrunkPlacer> FORKING_TRUNK_PLACER = register("forking_trunk_placer", ForkyTrunkPlacer.field_236896_a_);
   public static final TrunkPlacerType<GiantTrunkPlacer> GIANT_TRUNK_PLACER = register("giant_trunk_placer", GiantTrunkPlacer.field_236898_a_);
   public static final TrunkPlacerType<MegaJungleTrunkPlacer> MEGA_TRUNK_PLACER = register("mega_jungle_trunk_placer", MegaJungleTrunkPlacer.field_236901_b_);
   public static final TrunkPlacerType<DarkOakTrunkPlacer> DARK_OAK_TRUNK_PLACER = register("dark_oak_trunk_placer", DarkOakTrunkPlacer.CODEC);
   public static final TrunkPlacerType<FancyTrunkPlacer> FANCY_TRUNK_PLACER = register("fancy_trunk_placer", FancyTrunkPlacer.field_236884_a_);
   private final Codec<P> codec;

   private static <P extends AbstractTrunkPlacer> TrunkPlacerType<P> register(String key, Codec<P> codec) {
      return Registry.register(Registry.TRUNK_REPLACER, key, new TrunkPlacerType<>(codec));
   }

   private TrunkPlacerType(Codec<P> codec) {
      this.codec = codec;
   }

   public Codec<P> getCodec() {
      return this.codec;
   }
}