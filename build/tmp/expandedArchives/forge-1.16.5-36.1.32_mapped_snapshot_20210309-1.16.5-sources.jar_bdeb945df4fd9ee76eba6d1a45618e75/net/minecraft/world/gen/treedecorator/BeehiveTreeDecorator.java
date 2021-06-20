package net.minecraft.world.gen.treedecorator;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.tileentity.BeehiveTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.Feature;

public class BeehiveTreeDecorator extends TreeDecorator {
   public static final Codec<BeehiveTreeDecorator> field_236863_a_ = Codec.floatRange(0.0F, 1.0F).fieldOf("probability").xmap(BeehiveTreeDecorator::new, (p_236865_0_) -> {
      return p_236865_0_.probability;
   }).codec();
   /** Probability to generate a beehive */
   private final float probability;

   public BeehiveTreeDecorator(float probabilityIn) {
      this.probability = probabilityIn;
   }

   protected TreeDecoratorType<?> getDecoratorType() {
      return TreeDecoratorType.BEEHIVE;
   }

   public void func_225576_a_(ISeedReader world, Random rand, List<BlockPos> p_225576_3_, List<BlockPos> p_225576_4_, Set<BlockPos> p_225576_5_, MutableBoundingBox p_225576_6_) {
      if (!(rand.nextFloat() >= this.probability)) {
         Direction direction = BeehiveBlock.getGenerationDirection(rand);
         int i = !p_225576_4_.isEmpty() ? Math.max(p_225576_4_.get(0).getY() - 1, p_225576_3_.get(0).getY()) : Math.min(p_225576_3_.get(0).getY() + 1 + rand.nextInt(3), p_225576_3_.get(p_225576_3_.size() - 1).getY());
         List<BlockPos> list = p_225576_3_.stream().filter((p_236864_1_) -> {
            return p_236864_1_.getY() == i;
         }).collect(Collectors.toList());
         if (!list.isEmpty()) {
            BlockPos blockpos = list.get(rand.nextInt(list.size()));
            BlockPos blockpos1 = blockpos.offset(direction);
            if (Feature.isAirAt(world, blockpos1) && Feature.isAirAt(world, blockpos1.offset(Direction.SOUTH))) {
               BlockState blockstate = Blocks.BEE_NEST.getDefaultState().with(BeehiveBlock.FACING, Direction.SOUTH);
               this.func_227423_a_(world, blockpos1, blockstate, p_225576_5_, p_225576_6_);
               TileEntity tileentity = world.getTileEntity(blockpos1);
               if (tileentity instanceof BeehiveTileEntity) {
                  BeehiveTileEntity beehivetileentity = (BeehiveTileEntity)tileentity;
                  int j = 2 + rand.nextInt(2);

                  for(int k = 0; k < j; ++k) {
                     BeeEntity beeentity = new BeeEntity(EntityType.BEE, world.getWorld());
                     beehivetileentity.tryEnterHive(beeentity, false, rand.nextInt(599));
                  }
               }

            }
         }
      }
   }
}