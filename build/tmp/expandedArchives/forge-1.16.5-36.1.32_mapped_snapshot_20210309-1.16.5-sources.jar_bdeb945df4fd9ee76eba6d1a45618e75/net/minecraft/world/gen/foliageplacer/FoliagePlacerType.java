package net.minecraft.world.gen.foliageplacer;

import com.mojang.serialization.Codec;
import net.minecraft.util.registry.Registry;

public class FoliagePlacerType<P extends FoliagePlacer> extends net.minecraftforge.registries.ForgeRegistryEntry<FoliagePlacerType<?>> {
   public static final FoliagePlacerType<BlobFoliagePlacer> BLOB = register("blob_foliage_placer", BlobFoliagePlacer.CODEC);
   public static final FoliagePlacerType<SpruceFoliagePlacer> SPRUCE = register("spruce_foliage_placer", SpruceFoliagePlacer.CODEC);
   public static final FoliagePlacerType<PineFoliagePlacer> PINE = register("pine_foliage_placer", PineFoliagePlacer.CODEC);
   public static final FoliagePlacerType<AcaciaFoliagePlacer> ACACIA = register("acacia_foliage_placer", AcaciaFoliagePlacer.CODEC);
   public static final FoliagePlacerType<BushFoliagePlacer> BUSH = register("bush_foliage_placer", BushFoliagePlacer.CODEC);
   public static final FoliagePlacerType<FancyFoliagePlacer> FANCY = register("fancy_foliage_placer", FancyFoliagePlacer.CODEC);
   public static final FoliagePlacerType<JungleFoliagePlacer> JUNGLE = register("jungle_foliage_placer", JungleFoliagePlacer.CODEC);
   public static final FoliagePlacerType<MegaPineFoliagePlacer> MEGA_PINE = register("mega_pine_foliage_placer", MegaPineFoliagePlacer.CODEC);
   public static final FoliagePlacerType<DarkOakFoliagePlacer> DARK_OAK = register("dark_oak_foliage_placer", DarkOakFoliagePlacer.CODEC);
   private final Codec<P> codec;

   private static <P extends FoliagePlacer> FoliagePlacerType<P> register(String name, Codec<P> codec) {
      return Registry.register(Registry.FOLIAGE_PLACER_TYPE, name, new FoliagePlacerType<>(codec));
   }

   public FoliagePlacerType(Codec<P> codec) {
      this.codec = codec;
   }

   public Codec<P> getCodec() {
      return this.codec;
   }
}
