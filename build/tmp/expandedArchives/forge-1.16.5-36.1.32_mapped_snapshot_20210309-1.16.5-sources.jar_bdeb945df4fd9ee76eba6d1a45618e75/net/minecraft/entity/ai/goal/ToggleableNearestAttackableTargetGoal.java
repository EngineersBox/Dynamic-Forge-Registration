package net.minecraft.entity.ai.goal;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;

public class ToggleableNearestAttackableTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
   private boolean active = true;

   public ToggleableNearestAttackableTargetGoal(AbstractRaiderEntity raider, Class<T> targetClass, int targetChance, boolean checkSight, boolean nearbyOnly, @Nullable Predicate<LivingEntity> targetPredicate) {
      super(raider, targetClass, targetChance, checkSight, nearbyOnly, targetPredicate);
   }

   public void setActive(boolean active) {
      this.active = active;
   }

   /**
    * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
    * method as well.
    */
   public boolean shouldExecute() {
      return this.active && super.shouldExecute();
   }
}