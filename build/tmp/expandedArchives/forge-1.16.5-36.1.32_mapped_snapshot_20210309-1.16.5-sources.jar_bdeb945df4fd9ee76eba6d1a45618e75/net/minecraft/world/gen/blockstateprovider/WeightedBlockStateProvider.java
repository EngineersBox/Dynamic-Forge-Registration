package net.minecraft.world.gen.blockstateprovider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.Random;
import net.minecraft.block.BlockState;
import net.minecraft.util.WeightedList;
import net.minecraft.util.math.BlockPos;

public class WeightedBlockStateProvider extends BlockStateProvider {
   public static final Codec<WeightedBlockStateProvider> CODEC = WeightedList.getCodec(BlockState.CODEC).comapFlatMap(WeightedBlockStateProvider::encode, (provider) -> {
      return provider.weightedStates;
   }).fieldOf("entries").codec();
   private final WeightedList<BlockState> weightedStates;

   private static DataResult<WeightedBlockStateProvider> encode(WeightedList<BlockState> weightedStates) {
      return weightedStates.isEmpty() ? DataResult.error("WeightedStateProvider with no states") : DataResult.success(new WeightedBlockStateProvider(weightedStates));
   }

   private WeightedBlockStateProvider(WeightedList<BlockState> weightedStates) {
      this.weightedStates = weightedStates;
   }

   protected BlockStateProviderType<?> getProviderType() {
      return BlockStateProviderType.WEIGHTED_STATE_PROVIDER;
   }

   public WeightedBlockStateProvider() {
      this(new WeightedList<>());
   }

   /**
    * Adds the blockstate with the specified weight to the weighted states of the provider.
    */
   public WeightedBlockStateProvider addWeightedBlockstate(BlockState blockStateIn, int weightIn) {
      this.weightedStates.addWeighted(blockStateIn, weightIn);
      return this;
   }

   public BlockState getBlockState(Random randomIn, BlockPos blockPosIn) {
      return this.weightedStates.getRandomValue(randomIn);
   }
}