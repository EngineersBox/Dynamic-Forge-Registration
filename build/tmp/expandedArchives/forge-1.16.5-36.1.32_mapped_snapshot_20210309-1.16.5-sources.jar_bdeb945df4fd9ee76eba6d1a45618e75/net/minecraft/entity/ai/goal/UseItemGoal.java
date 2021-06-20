package net.minecraft.entity.ai.goal;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;

public class UseItemGoal<T extends MobEntity> extends Goal {
   private final T user;
   private final ItemStack stack;
   private final Predicate<? super T> canStart;
   private final SoundEvent sound;

   public UseItemGoal(T user, ItemStack stack, @Nullable SoundEvent sound, Predicate<? super T> canStart) {
      this.user = user;
      this.stack = stack;
      this.sound = sound;
      this.canStart = canStart;
   }

   /**
    * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
    * method as well.
    */
   public boolean shouldExecute() {
      return this.canStart.test(this.user);
   }

   /**
    * Returns whether an in-progress EntityAIBase should continue executing
    */
   public boolean shouldContinueExecuting() {
      return this.user.isHandActive();
   }

   /**
    * Execute a one shot task or start executing a continuous task
    */
   public void startExecuting() {
      this.user.setItemStackToSlot(EquipmentSlotType.MAINHAND, this.stack.copy());
      this.user.setActiveHand(Hand.MAIN_HAND);
   }

   /**
    * Reset the task's internal state. Called when this task is interrupted by another one
    */
   public void resetTask() {
      this.user.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
      if (this.sound != null) {
         this.user.playSound(this.sound, 1.0F, this.user.getRNG().nextFloat() * 0.2F + 0.9F);
      }

   }
}