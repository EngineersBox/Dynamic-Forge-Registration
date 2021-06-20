package net.minecraft.pathfinding;

import javax.annotation.Nullable;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.MobEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.Region;

public class WalkAndSwimNodeProcessor extends WalkNodeProcessor {
   private float walkPriority;
   private float field_203248_l;

   public void func_225578_a_(Region p_225578_1_, MobEntity p_225578_2_) {
      super.func_225578_a_(p_225578_1_, p_225578_2_);
      p_225578_2_.setPathPriority(PathNodeType.WATER, 0.0F);
      this.walkPriority = p_225578_2_.getPathPriority(PathNodeType.WALKABLE);
      p_225578_2_.setPathPriority(PathNodeType.WALKABLE, 6.0F);
      this.field_203248_l = p_225578_2_.getPathPriority(PathNodeType.WATER_BORDER);
      p_225578_2_.setPathPriority(PathNodeType.WATER_BORDER, 4.0F);
   }

   /**
    * This method is called when all nodes have been processed and PathEntity is created.
    * {@link net.minecraft.world.pathfinder.WalkNodeProcessor WalkNodeProcessor} uses this to change its field {@link
    * net.minecraft.world.pathfinder.WalkNodeProcessor#avoidsWater avoidsWater}
    */
   public void postProcess() {
      this.entity.setPathPriority(PathNodeType.WALKABLE, this.walkPriority);
      this.entity.setPathPriority(PathNodeType.WATER_BORDER, this.field_203248_l);
      super.postProcess();
   }

   public PathPoint getStart() {
      return this.openPoint(MathHelper.floor(this.entity.getBoundingBox().minX), MathHelper.floor(this.entity.getBoundingBox().minY + 0.5D), MathHelper.floor(this.entity.getBoundingBox().minZ));
   }

   public FlaggedPathPoint func_224768_a(double x, double y, double z) {
      return new FlaggedPathPoint(this.openPoint(MathHelper.floor(x), MathHelper.floor(y + 0.5D), MathHelper.floor(z)));
   }

   public int func_222859_a(PathPoint[] p_222859_1_, PathPoint p_222859_2_) {
      int i = 0;
      int j = 1;
      BlockPos blockpos = new BlockPos(p_222859_2_.x, p_222859_2_.y, p_222859_2_.z);
      double d0 = this.func_203246_a(blockpos);
      PathPoint pathpoint = this.func_203245_a(p_222859_2_.x, p_222859_2_.y, p_222859_2_.z + 1, 1, d0);
      PathPoint pathpoint1 = this.func_203245_a(p_222859_2_.x - 1, p_222859_2_.y, p_222859_2_.z, 1, d0);
      PathPoint pathpoint2 = this.func_203245_a(p_222859_2_.x + 1, p_222859_2_.y, p_222859_2_.z, 1, d0);
      PathPoint pathpoint3 = this.func_203245_a(p_222859_2_.x, p_222859_2_.y, p_222859_2_.z - 1, 1, d0);
      PathPoint pathpoint4 = this.func_203245_a(p_222859_2_.x, p_222859_2_.y + 1, p_222859_2_.z, 0, d0);
      PathPoint pathpoint5 = this.func_203245_a(p_222859_2_.x, p_222859_2_.y - 1, p_222859_2_.z, 1, d0);
      if (pathpoint != null && !pathpoint.visited) {
         p_222859_1_[i++] = pathpoint;
      }

      if (pathpoint1 != null && !pathpoint1.visited) {
         p_222859_1_[i++] = pathpoint1;
      }

      if (pathpoint2 != null && !pathpoint2.visited) {
         p_222859_1_[i++] = pathpoint2;
      }

      if (pathpoint3 != null && !pathpoint3.visited) {
         p_222859_1_[i++] = pathpoint3;
      }

      if (pathpoint4 != null && !pathpoint4.visited) {
         p_222859_1_[i++] = pathpoint4;
      }

      if (pathpoint5 != null && !pathpoint5.visited) {
         p_222859_1_[i++] = pathpoint5;
      }

      boolean flag = pathpoint3 == null || pathpoint3.nodeType == PathNodeType.OPEN || pathpoint3.costMalus != 0.0F;
      boolean flag1 = pathpoint == null || pathpoint.nodeType == PathNodeType.OPEN || pathpoint.costMalus != 0.0F;
      boolean flag2 = pathpoint2 == null || pathpoint2.nodeType == PathNodeType.OPEN || pathpoint2.costMalus != 0.0F;
      boolean flag3 = pathpoint1 == null || pathpoint1.nodeType == PathNodeType.OPEN || pathpoint1.costMalus != 0.0F;
      if (flag && flag3) {
         PathPoint pathpoint6 = this.func_203245_a(p_222859_2_.x - 1, p_222859_2_.y, p_222859_2_.z - 1, 1, d0);
         if (pathpoint6 != null && !pathpoint6.visited) {
            p_222859_1_[i++] = pathpoint6;
         }
      }

      if (flag && flag2) {
         PathPoint pathpoint7 = this.func_203245_a(p_222859_2_.x + 1, p_222859_2_.y, p_222859_2_.z - 1, 1, d0);
         if (pathpoint7 != null && !pathpoint7.visited) {
            p_222859_1_[i++] = pathpoint7;
         }
      }

      if (flag1 && flag3) {
         PathPoint pathpoint8 = this.func_203245_a(p_222859_2_.x - 1, p_222859_2_.y, p_222859_2_.z + 1, 1, d0);
         if (pathpoint8 != null && !pathpoint8.visited) {
            p_222859_1_[i++] = pathpoint8;
         }
      }

      if (flag1 && flag2) {
         PathPoint pathpoint9 = this.func_203245_a(p_222859_2_.x + 1, p_222859_2_.y, p_222859_2_.z + 1, 1, d0);
         if (pathpoint9 != null && !pathpoint9.visited) {
            p_222859_1_[i++] = pathpoint9;
         }
      }

      return i;
   }

   private double func_203246_a(BlockPos pos) {
      if (!this.entity.isInWater()) {
         BlockPos blockpos = pos.down();
         VoxelShape voxelshape = this.blockaccess.getBlockState(blockpos).getCollisionShapeUncached(this.blockaccess, blockpos);
         return (double)blockpos.getY() + (voxelshape.isEmpty() ? 0.0D : voxelshape.getEnd(Direction.Axis.Y));
      } else {
         return (double)pos.getY() + 0.5D;
      }
   }

   @Nullable
   private PathPoint func_203245_a(int x, int y, int z, int p_203245_4_, double p_203245_5_) {
      PathPoint pathpoint = null;
      BlockPos blockpos = new BlockPos(x, y, z);
      double d0 = this.func_203246_a(blockpos);
      if (d0 - p_203245_5_ > 1.125D) {
         return null;
      } else {
         PathNodeType pathnodetype = this.determineNodeType(this.blockaccess, x, y, z, this.entity, this.entitySizeX, this.entitySizeY, this.entitySizeZ, false, false);
         float f = this.entity.getPathPriority(pathnodetype);
         double d1 = (double)this.entity.getWidth() / 2.0D;
         if (f >= 0.0F) {
            pathpoint = this.openPoint(x, y, z);
            pathpoint.nodeType = pathnodetype;
            pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
         }

         if (pathnodetype != PathNodeType.WATER && pathnodetype != PathNodeType.WALKABLE) {
            if (pathpoint == null && p_203245_4_ > 0 && pathnodetype != PathNodeType.FENCE && pathnodetype != PathNodeType.UNPASSABLE_RAIL && pathnodetype != PathNodeType.TRAPDOOR) {
               pathpoint = this.func_203245_a(x, y + 1, z, p_203245_4_ - 1, p_203245_5_);
            }

            if (pathnodetype == PathNodeType.OPEN) {
               AxisAlignedBB axisalignedbb = new AxisAlignedBB((double)x - d1 + 0.5D, (double)y + 0.001D, (double)z - d1 + 0.5D, (double)x + d1 + 0.5D, (double)((float)y + this.entity.getHeight()), (double)z + d1 + 0.5D);
               if (!this.entity.world.hasNoCollisions(this.entity, axisalignedbb)) {
                  return null;
               }

               PathNodeType pathnodetype1 = this.determineNodeType(this.blockaccess, x, y - 1, z, this.entity, this.entitySizeX, this.entitySizeY, this.entitySizeZ, false, false);
               if (pathnodetype1 == PathNodeType.BLOCKED) {
                  pathpoint = this.openPoint(x, y, z);
                  pathpoint.nodeType = PathNodeType.WALKABLE;
                  pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
                  return pathpoint;
               }

               if (pathnodetype1 == PathNodeType.WATER) {
                  pathpoint = this.openPoint(x, y, z);
                  pathpoint.nodeType = PathNodeType.WATER;
                  pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
                  return pathpoint;
               }

               int i = 0;

               while(y > 0 && pathnodetype == PathNodeType.OPEN) {
                  --y;
                  if (i++ >= this.entity.getMaxFallHeight()) {
                     return null;
                  }

                  pathnodetype = this.determineNodeType(this.blockaccess, x, y, z, this.entity, this.entitySizeX, this.entitySizeY, this.entitySizeZ, false, false);
                  f = this.entity.getPathPriority(pathnodetype);
                  if (pathnodetype != PathNodeType.OPEN && f >= 0.0F) {
                     pathpoint = this.openPoint(x, y, z);
                     pathpoint.nodeType = pathnodetype;
                     pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
                     break;
                  }

                  if (f < 0.0F) {
                     return null;
                  }
               }
            }

            return pathpoint;
         } else {
            if (y < this.entity.world.getSeaLevel() - 10 && pathpoint != null) {
               ++pathpoint.costMalus;
            }

            return pathpoint;
         }
      }
   }

   /**
    * Returns the exact path node type according to abilities and settings of the entity
    */
   protected PathNodeType refineNodeType(IBlockReader worldIn, boolean canOpenDoors, boolean canEnterDoors, BlockPos pos, PathNodeType nodeType) {
      if (nodeType == PathNodeType.RAIL && !(worldIn.getBlockState(pos).getBlock() instanceof AbstractRailBlock) && !(worldIn.getBlockState(pos.down()).getBlock() instanceof AbstractRailBlock)) {
         nodeType = PathNodeType.UNPASSABLE_RAIL;
      }

      if (nodeType == PathNodeType.DOOR_OPEN || nodeType == PathNodeType.DOOR_WOOD_CLOSED || nodeType == PathNodeType.DOOR_IRON_CLOSED) {
         nodeType = PathNodeType.BLOCKED;
      }

      if (nodeType == PathNodeType.LEAVES) {
         nodeType = PathNodeType.BLOCKED;
      }

      return nodeType;
   }

   /**
    * Returns the node type at the specified postion taking the block below into account
    */
   public PathNodeType getFloorNodeType(IBlockReader worldIn, int x, int y, int z) {
      BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
      PathNodeType pathnodetype = func_237238_b_(worldIn, blockpos$mutable.setPos(x, y, z));
      if (pathnodetype == PathNodeType.WATER) {
         for(Direction direction : Direction.values()) {
            PathNodeType pathnodetype2 = func_237238_b_(worldIn, blockpos$mutable.setPos(x, y, z).move(direction));
            if (pathnodetype2 == PathNodeType.BLOCKED) {
               return PathNodeType.WATER_BORDER;
            }
         }

         return PathNodeType.WATER;
      } else {
         if (pathnodetype == PathNodeType.OPEN && y >= 1) {
            BlockState blockstate = worldIn.getBlockState(new BlockPos(x, y - 1, z));
            PathNodeType pathnodetype1 = func_237238_b_(worldIn, blockpos$mutable.setPos(x, y - 1, z));
            if (pathnodetype1 != PathNodeType.WALKABLE && pathnodetype1 != PathNodeType.OPEN && pathnodetype1 != PathNodeType.LAVA) {
               pathnodetype = PathNodeType.WALKABLE;
            } else {
               pathnodetype = PathNodeType.OPEN;
            }

            if (pathnodetype1 == PathNodeType.DAMAGE_FIRE || blockstate.matchesBlock(Blocks.MAGMA_BLOCK) || blockstate.isIn(BlockTags.CAMPFIRES)) {
               pathnodetype = PathNodeType.DAMAGE_FIRE;
            }

            if (pathnodetype1 == PathNodeType.DAMAGE_CACTUS) {
               pathnodetype = PathNodeType.DAMAGE_CACTUS;
            }

            if (pathnodetype1 == PathNodeType.DAMAGE_OTHER) {
               pathnodetype = PathNodeType.DAMAGE_OTHER;
            }
         }

         if (pathnodetype == PathNodeType.WALKABLE) {
            pathnodetype = getSurroundingDanger(worldIn, blockpos$mutable.setPos(x, y, z), pathnodetype);
         }

         return pathnodetype;
      }
   }
}