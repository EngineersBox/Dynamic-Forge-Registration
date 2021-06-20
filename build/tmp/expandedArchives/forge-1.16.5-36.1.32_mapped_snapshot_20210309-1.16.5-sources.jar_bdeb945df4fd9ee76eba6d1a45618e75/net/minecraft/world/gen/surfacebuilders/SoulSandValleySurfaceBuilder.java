package net.minecraft.world.gen.surfacebuilders;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public class SoulSandValleySurfaceBuilder extends ValleySurfaceBuilder {
   private static final BlockState SOUL_SAND = Blocks.SOUL_SAND.getDefaultState();
   private static final BlockState SOUL_SOIL = Blocks.SOUL_SOIL.getDefaultState();
   private static final BlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
   private static final ImmutableList<BlockState> SOUL_STATES = ImmutableList.of(SOUL_SAND, SOUL_SOIL);

   public SoulSandValleySurfaceBuilder(Codec<SurfaceBuilderConfig> codec) {
      super(codec);
   }

   protected ImmutableList<BlockState> func_230387_a_() {
      return SOUL_STATES;
   }

   protected ImmutableList<BlockState> func_230388_b_() {
      return SOUL_STATES;
   }

   protected BlockState func_230389_c_() {
      return GRAVEL;
   }
}