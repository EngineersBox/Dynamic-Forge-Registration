package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClimberPathNavigator extends GroundPathNavigator {
   /** Current path navigation target */
   private BlockPos targetPosition;

   public ClimberPathNavigator(MobEntity entityLivingIn, World worldIn) {
      super(entityLivingIn, worldIn);
   }

   /**
    * Returns path to given BlockPos
    */
   public Path getPathToPos(BlockPos pos, int accuracy) {
      this.targetPosition = pos;
      return super.getPathToPos(pos, accuracy);
   }

   /**
    * Returns a path to the given entity or null
    */
   public Path pathfind(Entity entityIn, int p_75494_2_) {
      this.targetPosition = entityIn.getPosition();
      return super.pathfind(entityIn, p_75494_2_);
   }

   /**
    * Try to find and set a path to EntityLiving. Returns true if successful. Args : entity, speed
    */
   public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn) {
      Path path = this.pathfind(entityIn, 0);
      if (path != null) {
         return this.setPath(path, speedIn);
      } else {
         this.targetPosition = entityIn.getPosition();
         this.speed = speedIn;
         return true;
      }
   }

   public void tick() {
      if (!this.noPath()) {
         super.tick();
      } else {
         if (this.targetPosition != null) {
            // FORGE: Fix MC-94054
            if (!this.targetPosition.withinDistance(this.entity.getPositionVec(), Math.max((double)this.entity.getWidth(), 1.0D)) && (!(this.entity.getPosY() > (double)this.targetPosition.getY()) || !(new BlockPos((double)this.targetPosition.getX(), this.entity.getPosY(), (double)this.targetPosition.getZ())).withinDistance(this.entity.getPositionVec(), Math.max((double)this.entity.getWidth(), 1.0D)))) {
               this.entity.getMoveHelper().setMoveTo((double)this.targetPosition.getX(), (double)this.targetPosition.getY(), (double)this.targetPosition.getZ(), this.speed);
            } else {
               this.targetPosition = null;
            }
         }

      }
   }
}
