package net.minecraft.world.gen;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Supplier;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.FillLayerConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FlatGenerationSettings {
   private static final Logger LOGGER = LogManager.getLogger();
   public static final Codec<FlatGenerationSettings> CODEC = RecordCodecBuilder.<FlatGenerationSettings>create((p_236938_0_) -> {
      return p_236938_0_.group(RegistryLookupCodec.getLookUpCodec(Registry.BIOME_KEY).forGetter((settings) -> {
         return settings.biomeRegistry;
      }), DimensionStructuresSettings.field_236190_a_.fieldOf("structures").forGetter(FlatGenerationSettings::func_236943_d_), FlatLayerInfo.CODEC.listOf().fieldOf("layers").forGetter(FlatGenerationSettings::getFlatLayers), Codec.BOOL.fieldOf("lakes").orElse(false).forGetter((settings) -> {
         return settings.hasLakes;
      }), Codec.BOOL.fieldOf("features").orElse(false).forGetter((settings) -> {
         return settings.hasFeatures;
      }), Biome.BIOME_CODEC.optionalFieldOf("biome").orElseGet(Optional::empty).forGetter((settings) -> {
         return Optional.of(settings.biomeToUse);
      })).apply(p_236938_0_, FlatGenerationSettings::new);
   }).stable();
   private static final Map<Structure<?>, StructureFeature<?, ?>> STRUCTURES = Util.make(Maps.newHashMap(), (structureFeatureMap) -> {
      structureFeatureMap.put(Structure.MINESHAFT, StructureFeatures.MINESHAFT);
      structureFeatureMap.put(Structure.VILLAGE, StructureFeatures.VILLAGE_PLAINS);
      structureFeatureMap.put(Structure.STRONGHOLD, StructureFeatures.STRONGHOLD);
      structureFeatureMap.put(Structure.SWAMP_HUT, StructureFeatures.SWAMP_HUT);
      structureFeatureMap.put(Structure.DESERT_PYRAMID, StructureFeatures.DESERT_PYRAMID);
      structureFeatureMap.put(Structure.JUNGLE_PYRAMID, StructureFeatures.JUNGLE_PYRAMID);
      structureFeatureMap.put(Structure.IGLOO, StructureFeatures.IGLOO);
      structureFeatureMap.put(Structure.OCEAN_RUIN, StructureFeatures.OCEAN_RUIN_COLD);
      structureFeatureMap.put(Structure.SHIPWRECK, StructureFeatures.SHIPWRECK);
      structureFeatureMap.put(Structure.MONUMENT, StructureFeatures.MONUMENT);
      structureFeatureMap.put(Structure.END_CITY, StructureFeatures.END_CITY);
      structureFeatureMap.put(Structure.WOODLAND_MANSION, StructureFeatures.MANSION);
      structureFeatureMap.put(Structure.FORTRESS, StructureFeatures.FORTRESS);
      structureFeatureMap.put(Structure.PILLAGER_OUTPOST, StructureFeatures.PILLAGER_OUTPOST);
      structureFeatureMap.put(Structure.RUINED_PORTAL, StructureFeatures.RUINED_PORTAL);
      structureFeatureMap.put(Structure.BASTION_REMNANT, StructureFeatures.BASTION_REMNANT);
   });
   private final Registry<Biome> biomeRegistry;
   private final DimensionStructuresSettings field_236933_f_;
   private final List<FlatLayerInfo> flatLayers = Lists.newArrayList();
   private Supplier<Biome> biomeToUse;
   private final BlockState[] states = new BlockState[256];
   private boolean allAir;
   private boolean hasFeatures = false;
   private boolean hasLakes = false;

   public FlatGenerationSettings(Registry<Biome> p_i242012_1_, DimensionStructuresSettings p_i242012_2_, List<FlatLayerInfo> flatLayers, boolean hasLakes, boolean hasFeatures, Optional<Supplier<Biome>> biomesToUse) {
      this(p_i242012_2_, p_i242012_1_);
      if (hasLakes) {
         this.setHasLakes();
      }

      if (hasFeatures) {
         this.setHasFeatures();
      }

      this.flatLayers.addAll(flatLayers);
      this.updateLayers();
      if (!biomesToUse.isPresent()) {
         LOGGER.error("Unknown biome, defaulting to plains");
         this.biomeToUse = () -> {
            return p_i242012_1_.getOrThrow(Biomes.PLAINS);
         };
      } else {
         this.biomeToUse = biomesToUse.get();
      }

   }

   public FlatGenerationSettings(DimensionStructuresSettings p_i242011_1_, Registry<Biome> p_i242011_2_) {
      this.biomeRegistry = p_i242011_2_;
      this.field_236933_f_ = p_i242011_1_;
      this.biomeToUse = () -> {
         return p_i242011_2_.getOrThrow(Biomes.PLAINS);
      };
   }

   @OnlyIn(Dist.CLIENT)
   public FlatGenerationSettings setStructureSettings(DimensionStructuresSettings p_236937_1_) {
      return this.withSettings(this.flatLayers, p_236937_1_);
   }

   @OnlyIn(Dist.CLIENT)
   public FlatGenerationSettings withSettings(List<FlatLayerInfo> flatLayers, DimensionStructuresSettings p_241527_2_) {
      FlatGenerationSettings flatgenerationsettings = new FlatGenerationSettings(p_241527_2_, this.biomeRegistry);

      for(FlatLayerInfo flatlayerinfo : flatLayers) {
         flatgenerationsettings.flatLayers.add(new FlatLayerInfo(flatlayerinfo.getLayerCount(), flatlayerinfo.getLayerMaterial().getBlock()));
         flatgenerationsettings.updateLayers();
      }

      flatgenerationsettings.func_242870_a(this.biomeToUse);
      if (this.hasFeatures) {
         flatgenerationsettings.setHasFeatures();
      }

      if (this.hasLakes) {
         flatgenerationsettings.setHasLakes();
      }

      return flatgenerationsettings;
   }

   public void setHasFeatures() {
      this.hasFeatures = true;
   }

   public void setHasLakes() {
      this.hasLakes = true;
   }

   public Biome getConfiguredBiome() {
      Biome biome = this.getBiome();
      BiomeGenerationSettings biomegenerationsettings = biome.getGenerationSettings();
      BiomeGenerationSettings.Builder biomegenerationsettings$builder = (new BiomeGenerationSettings.Builder()).withSurfaceBuilder(biomegenerationsettings.getSurfaceBuilder());
      if (this.hasLakes) {
         biomegenerationsettings$builder.withFeature(GenerationStage.Decoration.LAKES, Features.LAKE_WATER);
         biomegenerationsettings$builder.withFeature(GenerationStage.Decoration.LAKES, Features.LAKE_LAVA);
      }

      Map<Structure<?>, StructureFeature<?, ?>> map = new java.util.HashMap<>(STRUCTURES);
      net.minecraft.util.registry.WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE.stream().filter(f -> !map.containsKey(f.field_236268_b_)).forEach(f -> map.put(f.field_236268_b_, f));
      for(Entry<Structure<?>, StructureSeparationSettings> entry : this.field_236933_f_.func_236195_a_().entrySet()) {
         if (!map.containsKey(entry.getKey())) {
            LOGGER.error("FORGE: There's no known StructureFeature for {} when preparing the {} flatworld biome." +
                    " The structure will be skipped and may not spawn." +
                    " Please register your StructureFeatures in the WorldGenRegistries!", entry.getKey().getStructureName(), biome.getRegistryName());
            continue;
         }
         biomegenerationsettings$builder.withStructure(biomegenerationsettings.getStructure(map.get(entry.getKey())));
      }

      boolean flag = (!this.allAir || this.biomeRegistry.getOptionalKey(biome).equals(Optional.of(Biomes.THE_VOID))) && this.hasFeatures;
      if (flag) {
         List<List<Supplier<ConfiguredFeature<?, ?>>>> list = biomegenerationsettings.getFeatures();

         for(int i = 0; i < list.size(); ++i) {
            if (i != GenerationStage.Decoration.UNDERGROUND_STRUCTURES.ordinal() && i != GenerationStage.Decoration.SURFACE_STRUCTURES.ordinal()) {
               for(Supplier<ConfiguredFeature<?, ?>> supplier : list.get(i)) {
                  biomegenerationsettings$builder.withFeature(i, supplier);
               }
            }
         }
      }

      BlockState[] ablockstate = this.getStates();

      for(int j = 0; j < ablockstate.length; ++j) {
         BlockState blockstate = ablockstate[j];
         if (blockstate != null && !Heightmap.Type.MOTION_BLOCKING.getHeightLimitPredicate().test(blockstate)) {
            this.states[j] = null;
            biomegenerationsettings$builder.withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, Feature.FILL_LAYER.withConfiguration(new FillLayerConfig(j, blockstate)));
         }
      }

      return (new Biome.Builder()).precipitation(biome.getPrecipitation()).category(biome.getCategory()).depth(biome.getDepth()).scale(biome.getScale()).temperature(biome.getTemperature()).downfall(biome.getDownfall()).setEffects(biome.getAmbience()).withGenerationSettings(biomegenerationsettings$builder.build()).withMobSpawnSettings(biome.getMobSpawnInfo()).build().setRegistryName(biome.getRegistryName());
   }

   public DimensionStructuresSettings func_236943_d_() {
      return this.field_236933_f_;
   }

   /**
    * Return the biome used on this preset.
    */
   public Biome getBiome() {
      return this.biomeToUse.get();
   }

   @OnlyIn(Dist.CLIENT)
   public void func_242870_a(Supplier<Biome> p_242870_1_) {
      this.biomeToUse = p_242870_1_;
   }

   /**
    * Return the list of layers on this preset.
    */
   public List<FlatLayerInfo> getFlatLayers() {
      return this.flatLayers;
   }

   public BlockState[] getStates() {
      return this.states;
   }

   public void updateLayers() {
      Arrays.fill(this.states, 0, this.states.length, (Object)null);
      int i = 0;

      for(FlatLayerInfo flatlayerinfo : this.flatLayers) {
         flatlayerinfo.setMinY(i);
         i += flatlayerinfo.getLayerCount();
      }

      this.allAir = true;

      for(FlatLayerInfo flatlayerinfo1 : this.flatLayers) {
         for(int j = flatlayerinfo1.getMinY(); j < flatlayerinfo1.getMinY() + flatlayerinfo1.getLayerCount(); ++j) {
            BlockState blockstate = flatlayerinfo1.getLayerMaterial();
            if (!blockstate.matchesBlock(Blocks.AIR)) {
               this.allAir = false;
               this.states[j] = blockstate;
            }
         }
      }

   }

   public static FlatGenerationSettings func_242869_a(Registry<Biome> biomeRegistry) {
      DimensionStructuresSettings dimensionstructuressettings = new DimensionStructuresSettings(Optional.of(DimensionStructuresSettings.field_236192_c_), Maps.newHashMap(ImmutableMap.of(Structure.VILLAGE, DimensionStructuresSettings.field_236191_b_.get(Structure.VILLAGE))));
      FlatGenerationSettings flatgenerationsettings = new FlatGenerationSettings(dimensionstructuressettings, biomeRegistry);
      flatgenerationsettings.biomeToUse = () -> {
         return biomeRegistry.getOrThrow(Biomes.PLAINS);
      };
      flatgenerationsettings.getFlatLayers().add(new FlatLayerInfo(1, Blocks.BEDROCK));
      flatgenerationsettings.getFlatLayers().add(new FlatLayerInfo(2, Blocks.DIRT));
      flatgenerationsettings.getFlatLayers().add(new FlatLayerInfo(1, Blocks.GRASS_BLOCK));
      flatgenerationsettings.updateLayers();
      return flatgenerationsettings;
   }
}
