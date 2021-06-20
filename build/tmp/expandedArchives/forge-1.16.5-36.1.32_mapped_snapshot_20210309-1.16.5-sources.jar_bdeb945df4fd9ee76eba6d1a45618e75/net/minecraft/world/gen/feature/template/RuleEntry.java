package net.minecraft.world.gen.feature.template;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public class RuleEntry {
   public static final Codec<RuleEntry> CODEC = RecordCodecBuilder.create((p_237111_0_) -> {
      return p_237111_0_.group(RuleTest.CODEC.fieldOf("input_predicate").forGetter((entry) -> {
         return entry.inputPredicate;
      }), RuleTest.CODEC.fieldOf("location_predicate").forGetter((entry) -> {
         return entry.locationPredicate;
      }), PosRuleTest.CODEC.optionalFieldOf("position_predicate", AlwaysTrueTest.field_237100_b_).forGetter((entry) -> {
         return entry.positionPredicate;
      }), BlockState.CODEC.fieldOf("output_state").forGetter((entry) -> {
         return entry.outputState;
      }), CompoundNBT.CODEC.optionalFieldOf("output_nbt").forGetter((entry) -> {
         return Optional.ofNullable(entry.outputNbt);
      })).apply(p_237111_0_, RuleEntry::new);
   });
   private final RuleTest inputPredicate;
   private final RuleTest locationPredicate;
   private final PosRuleTest positionPredicate;
   private final BlockState outputState;
   @Nullable
   private final CompoundNBT outputNbt;

   public RuleEntry(RuleTest inputPredicate, RuleTest locationPredicate, BlockState outputState) {
      this(inputPredicate, locationPredicate, AlwaysTrueTest.field_237100_b_, outputState, Optional.empty());
   }

   public RuleEntry(RuleTest inputPredicate, RuleTest locationPredicate, PosRuleTest positionPredicate, BlockState outputState) {
      this(inputPredicate, locationPredicate, positionPredicate, outputState, Optional.empty());
   }

   public RuleEntry(RuleTest inputPredicate, RuleTest locationPredicate, PosRuleTest positionPredicate, BlockState outputState, Optional<CompoundNBT> outputNBT) {
      this.inputPredicate = inputPredicate;
      this.locationPredicate = locationPredicate;
      this.positionPredicate = positionPredicate;
      this.outputState = outputState;
      this.outputNbt = outputNBT.orElse((CompoundNBT)null);
   }

   public boolean test(BlockState p_237110_1_, BlockState p_237110_2_, BlockPos p_237110_3_, BlockPos p_237110_4_, BlockPos p_237110_5_, Random rand) {
      return this.inputPredicate.test(p_237110_1_, rand) && this.locationPredicate.test(p_237110_2_, rand) && this.positionPredicate.test(p_237110_3_, p_237110_4_, p_237110_5_, rand);
   }

   public BlockState getOutputState() {
      return this.outputState;
   }

   @Nullable
   public CompoundNBT getOutputNbt() {
      return this.outputNbt;
   }
}