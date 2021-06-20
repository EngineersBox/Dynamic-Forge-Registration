package net.minecraft.entity.ai.goal;

import java.util.List;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.GameRules;

public class ResetAngerGoal<T extends MobEntity & IAngerable> extends Goal {
   private final T mob;
   private final boolean resetsNearbyPeers;
   private int revengeTimer;

   public ResetAngerGoal(T mob, boolean resetsNearbyPeers) {
      this.mob = mob;
      this.resetsNearbyPeers = resetsNearbyPeers;
   }

   /**
    * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
    * method as well.
    */
   public boolean shouldExecute() {
      return this.mob.world.getGameRules().getBoolean(GameRules.UNIVERSAL_ANGER) && this.shouldGetRevengeOnPlayer();
   }

   private boolean shouldGetRevengeOnPlayer() {
      return this.mob.getRevengeTarget() != null && this.mob.getRevengeTarget().getType() == EntityType.PLAYER && this.mob.getRevengeTimer() > this.revengeTimer;
   }

   /**
    * Execute a one shot task or start executing a continuous task
    */
   public void startExecuting() {
      this.revengeTimer = this.mob.getRevengeTimer();
      this.mob.func_241355_J__();
      if (this.resetsNearbyPeers) {
         this.getAllies().stream().filter((mob) -> {
            return mob != this.mob;
         }).map((mob) -> {
            return (IAngerable)mob;
         }).forEach(IAngerable::func_241355_J__);
      }

      super.startExecuting();
   }

   private List<MobEntity> getAllies() {
      double d0 = this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
      AxisAlignedBB axisalignedbb = AxisAlignedBB.fromVector(this.mob.getPositionVec()).grow(d0, 10.0D, d0);
      return this.mob.world.getLoadedEntitiesWithinAABB(this.mob.getClass(), axisalignedbb);
   }
}