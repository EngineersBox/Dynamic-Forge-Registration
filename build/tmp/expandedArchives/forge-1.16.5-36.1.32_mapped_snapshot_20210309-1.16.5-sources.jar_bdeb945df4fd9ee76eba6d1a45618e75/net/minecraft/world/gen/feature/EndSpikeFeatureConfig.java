package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;

public class EndSpikeFeatureConfig implements IFeatureConfig {
   public static final Codec<EndSpikeFeatureConfig> CODEC = RecordCodecBuilder.create((builder) -> {
      return builder.group(Codec.BOOL.fieldOf("crystal_invulnerable").orElse(false).forGetter((config) -> {
         return config.crystalInvulnerable;
      }), EndSpikeFeature.EndSpike.CODEC.listOf().fieldOf("spikes").forGetter((config) -> {
         return config.spikes;
      }), BlockPos.CODEC.optionalFieldOf("crystal_beam_target").forGetter((config) -> {
         return Optional.ofNullable(config.crystalBeamTarget);
      })).apply(builder, EndSpikeFeatureConfig::new);
   });
   private final boolean crystalInvulnerable;
   private final List<EndSpikeFeature.EndSpike> spikes;
   @Nullable
   private final BlockPos crystalBeamTarget;

   public EndSpikeFeatureConfig(boolean crystalInvulnerable, List<EndSpikeFeature.EndSpike> spikes, @Nullable BlockPos crystalBeamTarget) {
      this(crystalInvulnerable, spikes, Optional.ofNullable(crystalBeamTarget));
   }

   private EndSpikeFeatureConfig(boolean crystalInvulnerable, List<EndSpikeFeature.EndSpike> spikes, Optional<BlockPos> crystalBeamTarget) {
      this.crystalInvulnerable = crystalInvulnerable;
      this.spikes = spikes;
      this.crystalBeamTarget = crystalBeamTarget.orElse((BlockPos)null);
   }

   public boolean isCrystalInvulnerable() {
      return this.crystalInvulnerable;
   }

   public List<EndSpikeFeature.EndSpike> getSpikes() {
      return this.spikes;
   }

   @Nullable
   public BlockPos getCrystalBeamTarget() {
      return this.crystalBeamTarget;
   }
}