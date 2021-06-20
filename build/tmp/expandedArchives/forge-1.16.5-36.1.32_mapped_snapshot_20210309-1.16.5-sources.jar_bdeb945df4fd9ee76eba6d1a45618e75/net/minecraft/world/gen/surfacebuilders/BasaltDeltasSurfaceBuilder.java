package net.minecraft.world.gen.surfacebuilders;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public class BasaltDeltasSurfaceBuilder extends ValleySurfaceBuilder {
   private static final BlockState BASALT = Blocks.BASALT.getDefaultState();
   private static final BlockState BLACKSTONE = Blocks.BLACKSTONE.getDefaultState();
   private static final BlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
   private static final ImmutableList<BlockState> BASALT_BLACKSTONE_STATES = ImmutableList.of(BASALT, BLACKSTONE);
   private static final ImmutableList<BlockState> BASALT_STATE = ImmutableList.of(BASALT);

   public BasaltDeltasSurfaceBuilder(Codec<SurfaceBuilderConfig> codec) {
      super(codec);
   }

   protected ImmutableList<BlockState> func_230387_a_() {
      return BASALT_BLACKSTONE_STATES;
   }

   protected ImmutableList<BlockState> func_230388_b_() {
      return BASALT_STATE;
   }

   protected BlockState func_230389_c_() {
      return GRAVEL;
   }
}