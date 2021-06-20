package net.minecraft.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;

public class BreatheAirGoal extends Goal {
   private final CreatureEntity creature;

   public BreatheAirGoal(CreatureEntity creature) {
      this.creature = creature;
      this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
   }

   /**
    * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
    * method as well.
    */
   public boolean shouldExecute() {
      return this.creature.getAir() < 140;
   }

   /**
    * Returns whether an in-progress EntityAIBase should continue executing
    */
   public boolean shouldContinueExecuting() {
      return this.shouldExecute();
   }

   public boolean isPreemptible() {
      return false;
   }

   /**
    * Execute a one shot task or start executing a continuous task
    */
   public void startExecuting() {
      this.navigate();
   }

   private void navigate() {
      Iterable<BlockPos> iterable = BlockPos.getAllInBoxMutable(MathHelper.floor(this.creature.getPosX() - 1.0D), MathHelper.floor(this.creature.getPosY()), MathHelper.floor(this.creature.getPosZ() - 1.0D), MathHelper.floor(this.creature.getPosX() + 1.0D), MathHelper.floor(this.creature.getPosY() + 8.0D), MathHelper.floor(this.creature.getPosZ() + 1.0D));
      BlockPos blockpos = null;

      for(BlockPos blockpos1 : iterable) {
         if (this.canBreatheAt(this.creature.world, blockpos1)) {
            blockpos = blockpos1;
            break;
         }
      }

      if (blockpos == null) {
         blockpos = new BlockPos(this.creature.getPosX(), this.creature.getPosY() + 8.0D, this.creature.getPosZ());
      }

      this.creature.getNavigator().tryMoveToXYZ((double)blockpos.getX(), (double)(blockpos.getY() + 1), (double)blockpos.getZ(), 1.0D);
   }

   /**
    * Keep ticking a continuous task that has already been started
    */
   public void tick() {
      this.navigate();
      this.creature.moveRelative(0.02F, new Vector3d((double)this.creature.moveStrafing, (double)this.creature.moveVertical, (double)this.creature.moveForward));
      this.creature.move(MoverType.SELF, this.creature.getMotion());
   }

   private boolean canBreatheAt(IWorldReader worldIn, BlockPos pos) {
      BlockState blockstate = worldIn.getBlockState(pos);
      return (worldIn.getFluidState(pos).isEmpty() || blockstate.matchesBlock(Blocks.BUBBLE_COLUMN)) && blockstate.allowsMovement(worldIn, pos, PathType.LAND);
   }
}