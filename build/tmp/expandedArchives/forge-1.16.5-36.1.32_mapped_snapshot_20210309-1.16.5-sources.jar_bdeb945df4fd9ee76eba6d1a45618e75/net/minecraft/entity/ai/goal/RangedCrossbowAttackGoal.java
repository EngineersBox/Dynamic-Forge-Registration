package net.minecraft.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.RangedInteger;

public class RangedCrossbowAttackGoal<T extends MonsterEntity & IRangedAttackMob & ICrossbowUser> extends Goal {
   public static final RangedInteger field_241381_a_ = new RangedInteger(20, 40);
   private final T shooter;
   private RangedCrossbowAttackGoal.CrossbowState state = RangedCrossbowAttackGoal.CrossbowState.UNCHARGED;
   private final double speed;
   private final float followDistanceSq;
   private int field_220752_e;
   private int field_220753_f;
   private int field_241382_h_;

   public RangedCrossbowAttackGoal(T shooter, double speed, float followDistance) {
      this.shooter = shooter;
      this.speed = speed;
      this.followDistanceSq = followDistance * followDistance;
      this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
   }

   /**
    * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
    * method as well.
    */
   public boolean shouldExecute() {
      return this.hasValidTarget() && this.canEquipCrossbow();
   }

   private boolean canEquipCrossbow() {
      return this.shooter.canEquip(Items.CROSSBOW);
   }

   /**
    * Returns whether an in-progress EntityAIBase should continue executing
    */
   public boolean shouldContinueExecuting() {
      return this.hasValidTarget() && (this.shouldExecute() || !this.shooter.getNavigator().noPath()) && this.canEquipCrossbow();
   }

   private boolean hasValidTarget() {
      return this.shooter.getAttackTarget() != null && this.shooter.getAttackTarget().isAlive();
   }

   /**
    * Reset the task's internal state. Called when this task is interrupted by another one
    */
   public void resetTask() {
      super.resetTask();
      this.shooter.setAggroed(false);
      this.shooter.setAttackTarget((LivingEntity)null);
      this.field_220752_e = 0;
      if (this.shooter.isHandActive()) {
         this.shooter.resetActiveHand();
         this.shooter.setCharging(false);
         CrossbowItem.setCharged(this.shooter.getActiveItemStack(), false);
      }

   }

   /**
    * Keep ticking a continuous task that has already been started
    */
   public void tick() {
      LivingEntity livingentity = this.shooter.getAttackTarget();
      if (livingentity != null) {
         boolean flag = this.shooter.getEntitySenses().canSee(livingentity);
         boolean flag1 = this.field_220752_e > 0;
         if (flag != flag1) {
            this.field_220752_e = 0;
         }

         if (flag) {
            ++this.field_220752_e;
         } else {
            --this.field_220752_e;
         }

         double d0 = this.shooter.getDistanceSq(livingentity);
         boolean flag2 = (d0 > (double)this.followDistanceSq || this.field_220752_e < 5) && this.field_220753_f == 0;
         if (flag2) {
            --this.field_241382_h_;
            if (this.field_241382_h_ <= 0) {
               this.shooter.getNavigator().tryMoveToEntityLiving(livingentity, this.isUnchargedState() ? this.speed : this.speed * 0.5D);
               this.field_241382_h_ = field_241381_a_.getRandomWithinRange(this.shooter.getRNG());
            }
         } else {
            this.field_241382_h_ = 0;
            this.shooter.getNavigator().clearPath();
         }

         this.shooter.getLookController().setLookPositionWithEntity(livingentity, 30.0F, 30.0F);
         if (this.state == RangedCrossbowAttackGoal.CrossbowState.UNCHARGED) {
            if (!flag2) {
               this.shooter.setActiveHand(ProjectileHelper.getHandWith(this.shooter, Items.CROSSBOW));
               this.state = RangedCrossbowAttackGoal.CrossbowState.CHARGING;
               this.shooter.setCharging(true);
            }
         } else if (this.state == RangedCrossbowAttackGoal.CrossbowState.CHARGING) {
            if (!this.shooter.isHandActive()) {
               this.state = RangedCrossbowAttackGoal.CrossbowState.UNCHARGED;
            }

            int i = this.shooter.getItemInUseMaxCount();
            ItemStack itemstack = this.shooter.getActiveItemStack();
            if (i >= CrossbowItem.getChargeTime(itemstack)) {
               this.shooter.stopActiveHand();
               this.state = RangedCrossbowAttackGoal.CrossbowState.CHARGED;
               this.field_220753_f = 20 + this.shooter.getRNG().nextInt(20);
               this.shooter.setCharging(false);
            }
         } else if (this.state == RangedCrossbowAttackGoal.CrossbowState.CHARGED) {
            --this.field_220753_f;
            if (this.field_220753_f == 0) {
               this.state = RangedCrossbowAttackGoal.CrossbowState.READY_TO_ATTACK;
            }
         } else if (this.state == RangedCrossbowAttackGoal.CrossbowState.READY_TO_ATTACK && flag) {
            this.shooter.attackEntityWithRangedAttack(livingentity, 1.0F);
            ItemStack itemstack1 = this.shooter.getHeldItem(ProjectileHelper.getHandWith(this.shooter, Items.CROSSBOW));
            CrossbowItem.setCharged(itemstack1, false);
            this.state = RangedCrossbowAttackGoal.CrossbowState.UNCHARGED;
         }

      }
   }

   private boolean isUnchargedState() {
      return this.state == RangedCrossbowAttackGoal.CrossbowState.UNCHARGED;
   }

   static enum CrossbowState {
      UNCHARGED,
      CHARGING,
      CHARGED,
      READY_TO_ATTACK;
   }
}