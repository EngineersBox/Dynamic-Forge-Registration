package net.minecraft.world.gen;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKeyCodec;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.NoiseSettings;
import net.minecraft.world.gen.settings.ScalingSettings;
import net.minecraft.world.gen.settings.SlideSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;

public final class DimensionSettings {
   public static final Codec<DimensionSettings> CODEC = RecordCodecBuilder.create((builder) -> {
      return builder.group(DimensionStructuresSettings.field_236190_a_.fieldOf("structures").forGetter(DimensionSettings::getStructures), NoiseSettings.field_236156_a_.fieldOf("noise").forGetter(DimensionSettings::getNoise), BlockState.CODEC.fieldOf("default_block").forGetter(DimensionSettings::getDefaultBlock), BlockState.CODEC.fieldOf("default_fluid").forGetter(DimensionSettings::getDefaultFluid), Codec.intRange(-20, 276).fieldOf("bedrock_roof_position").forGetter(DimensionSettings::getBedrockRoofPosition), Codec.intRange(-20, 276).fieldOf("bedrock_floor_position").forGetter(DimensionSettings::getBedrockFloorPosition), Codec.intRange(0, 255).fieldOf("sea_level").forGetter(DimensionSettings::getSeaLevel), Codec.BOOL.fieldOf("disable_mob_generation").forGetter(DimensionSettings::isMobGenerationDisabled)).apply(builder, DimensionSettings::new);
   });
   public static final Codec<Supplier<DimensionSettings>> DIMENSION_SETTINGS_CODEC = RegistryKeyCodec.create(Registry.NOISE_SETTINGS_KEY, CODEC);
   private final DimensionStructuresSettings structures;
   private final NoiseSettings noise;
   private final BlockState defaultBlock;
   private final BlockState defaultFluid;
   private final int bedrockRoofPosition;
   private final int bedrockFloorPosition;
   private final int seaLevel;
   private final boolean disableMobGeneration;
   public static final RegistryKey<DimensionSettings> OVERWORLD = RegistryKey.getOrCreateKey(Registry.NOISE_SETTINGS_KEY, new ResourceLocation("overworld"));
   public static final RegistryKey<DimensionSettings> AMPLIFIED = RegistryKey.getOrCreateKey(Registry.NOISE_SETTINGS_KEY, new ResourceLocation("amplified"));
   public static final RegistryKey<DimensionSettings> NETHER = RegistryKey.getOrCreateKey(Registry.NOISE_SETTINGS_KEY, new ResourceLocation("nether"));
   public static final RegistryKey<DimensionSettings> END = RegistryKey.getOrCreateKey(Registry.NOISE_SETTINGS_KEY, new ResourceLocation("end"));
   public static final RegistryKey<DimensionSettings> CAVES = RegistryKey.getOrCreateKey(Registry.NOISE_SETTINGS_KEY, new ResourceLocation("caves"));
   public static final RegistryKey<DimensionSettings> FLOATING_ISLANDS = RegistryKey.getOrCreateKey(Registry.NOISE_SETTINGS_KEY, new ResourceLocation("floating_islands"));
   private static final DimensionSettings DEFAULT_SETTINGS = registerDimensionSettings(OVERWORLD, func_242743_a(new DimensionStructuresSettings(true), false, OVERWORLD.getLocation()));

   private DimensionSettings(DimensionStructuresSettings structures, NoiseSettings noise, BlockState defaultBlock, BlockState defaultFluid, int bedrockRoofPosition, int bedrockFloorPosition, int seaLevel, boolean disableMobGeneration) {
      this.structures = structures;
      this.noise = noise;
      this.defaultBlock = defaultBlock;
      this.defaultFluid = defaultFluid;
      this.bedrockRoofPosition = bedrockRoofPosition;
      this.bedrockFloorPosition = bedrockFloorPosition;
      this.seaLevel = seaLevel;
      this.disableMobGeneration = disableMobGeneration;
   }

   public DimensionStructuresSettings getStructures() {
      return this.structures;
   }

   public NoiseSettings getNoise() {
      return this.noise;
   }

   public BlockState getDefaultBlock() {
      return this.defaultBlock;
   }

   public BlockState getDefaultFluid() {
      return this.defaultFluid;
   }

   public int getBedrockRoofPosition() {
      return this.bedrockRoofPosition;
   }

   public int getBedrockFloorPosition() {
      return this.bedrockFloorPosition;
   }

   public int getSeaLevel() {
      return this.seaLevel;
   }

   @Deprecated
   protected boolean isMobGenerationDisabled() {
      return this.disableMobGeneration;
   }

   public boolean func_242744_a(RegistryKey<DimensionSettings> p_242744_1_) {
      return Objects.equals(this, WorldGenRegistries.NOISE_SETTINGS.getValueForKey(p_242744_1_));
   }

   private static DimensionSettings registerDimensionSettings(RegistryKey<DimensionSettings> key, DimensionSettings settings) {
      WorldGenRegistries.register(WorldGenRegistries.NOISE_SETTINGS, key.getLocation(), settings);
      return settings;
   }

   public static DimensionSettings getDefaultDimensionSettings() {
      return DEFAULT_SETTINGS;
   }

   private static DimensionSettings func_242742_a(DimensionStructuresSettings p_242742_0_, BlockState p_242742_1_, BlockState p_242742_2_, ResourceLocation resourceLocation, boolean disableMobGeneration, boolean p_242742_5_) {
      return new DimensionSettings(p_242742_0_, new NoiseSettings(128, new ScalingSettings(2.0D, 1.0D, 80.0D, 160.0D), new SlideSettings(-3000, 64, -46), new SlideSettings(-30, 7, 1), 2, 1, 0.0D, 0.0D, true, false, p_242742_5_, false), p_242742_1_, p_242742_2_, -10, -10, 0, disableMobGeneration);
   }

   private static DimensionSettings func_242741_a(DimensionStructuresSettings p_242741_0_, BlockState p_242741_1_, BlockState p_242741_2_, ResourceLocation p_242741_3_) {
      Map<Structure<?>, StructureSeparationSettings> map = Maps.newHashMap(DimensionStructuresSettings.field_236191_b_);
      map.put(Structure.RUINED_PORTAL, new StructureSeparationSettings(25, 10, 34222645));
      return new DimensionSettings(new DimensionStructuresSettings(Optional.ofNullable(p_242741_0_.func_236199_b_()), map), new NoiseSettings(128, new ScalingSettings(1.0D, 3.0D, 80.0D, 60.0D), new SlideSettings(120, 3, 0), new SlideSettings(320, 4, -1), 1, 2, 0.0D, 0.019921875D, false, false, false, false), p_242741_1_, p_242741_2_, 0, 0, 32, false);
   }

   private static DimensionSettings func_242743_a(DimensionStructuresSettings p_242743_0_, boolean p_242743_1_, ResourceLocation p_242743_2_) {
      double d0 = 0.9999999814507745D;
      return new DimensionSettings(p_242743_0_, new NoiseSettings(256, new ScalingSettings(0.9999999814507745D, 0.9999999814507745D, 80.0D, 160.0D), new SlideSettings(-10, 3, 0), new SlideSettings(-30, 0, 0), 1, 2, 1.0D, -0.46875D, true, true, false, p_242743_1_), Blocks.STONE.getDefaultState(), Blocks.WATER.getDefaultState(), -10, 0, 63, false);
   }

   static {
      registerDimensionSettings(AMPLIFIED, func_242743_a(new DimensionStructuresSettings(true), true, AMPLIFIED.getLocation()));
      registerDimensionSettings(NETHER, func_242741_a(new DimensionStructuresSettings(false), Blocks.NETHERRACK.getDefaultState(), Blocks.LAVA.getDefaultState(), NETHER.getLocation()));
      registerDimensionSettings(END, func_242742_a(new DimensionStructuresSettings(false), Blocks.END_STONE.getDefaultState(), Blocks.AIR.getDefaultState(), END.getLocation(), true, true));
      registerDimensionSettings(CAVES, func_242741_a(new DimensionStructuresSettings(true), Blocks.STONE.getDefaultState(), Blocks.WATER.getDefaultState(), CAVES.getLocation()));
      registerDimensionSettings(FLOATING_ISLANDS, func_242742_a(new DimensionStructuresSettings(true), Blocks.STONE.getDefaultState(), Blocks.WATER.getDefaultState(), FLOATING_ISLANDS.getLocation(), false, false));
   }
}