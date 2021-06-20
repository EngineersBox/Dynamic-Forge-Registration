package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.util.math.BlockPos;

public class EndGatewayConfig implements IFeatureConfig {
   public static final Codec<EndGatewayConfig> CODEC = RecordCodecBuilder.create((builder) -> {
      return builder.group(BlockPos.CODEC.optionalFieldOf("exit").forGetter((config) -> {
         return config.exit;
      }), Codec.BOOL.fieldOf("exact").forGetter((config) -> {
         return config.exact;
      })).apply(builder, EndGatewayConfig::new);
   });
   private final Optional<BlockPos> exit;
   private final boolean exact;

   private EndGatewayConfig(Optional<BlockPos> exit, boolean exact) {
      this.exit = exit;
      this.exact = exact;
   }

   public static EndGatewayConfig create(BlockPos exit, boolean exact) {
      return new EndGatewayConfig(Optional.of(exit), exact);
   }

   public static EndGatewayConfig getEmptyConfig() {
      return new EndGatewayConfig(Optional.empty(), false);
   }

   public Optional<BlockPos> getExit() {
      return this.exit;
   }

   public boolean isExact() {
      return this.exact;
   }
}