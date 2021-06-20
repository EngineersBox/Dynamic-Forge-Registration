package net.minecraft.world.gen.feature.template;

import com.mojang.serialization.Codec;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class NopProcessor extends StructureProcessor {
   public static final Codec<NopProcessor> CODEC;
   public static final NopProcessor INSTANCE = new NopProcessor();

   private NopProcessor() {
   }

   @Nullable
   public Template.BlockInfo func_230386_a_(IWorldReader world, BlockPos p_230386_2_, BlockPos p_230386_3_, Template.BlockInfo p_230386_4_, Template.BlockInfo p_230386_5_, PlacementSettings settings) {
      return p_230386_5_;
   }

   protected IStructureProcessorType<?> getType() {
      return IStructureProcessorType.NOP;
   }

   static {
      CODEC = Codec.unit(() -> {
         return INSTANCE;
      });
   }
}