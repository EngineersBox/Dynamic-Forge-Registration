package net.minecraft.tileentity;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.PistonHeadBlock;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.PistonType;
import net.minecraft.util.AabbHelper;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PistonTileEntity extends TileEntity implements ITickableTileEntity {
   private BlockState pistonState;
   private Direction pistonFacing;
   /** if this piston is extending or not */
   private boolean extending;
   private boolean shouldHeadBeRendered;
   private static final ThreadLocal<Direction> MOVING_ENTITY = ThreadLocal.withInitial(() -> {
      return null;
   });
   private float progress;
   /** the progress in (de)extending */
   private float lastProgress;
   private long lastTicked;
   private int field_242697_l;

   public PistonTileEntity() {
      super(TileEntityType.PISTON);
   }

   public PistonTileEntity(BlockState pistonStateIn, Direction pistonFacingIn, boolean extendingIn, boolean shouldHeadBeRenderedIn) {
      this();
      this.pistonState = pistonStateIn;
      this.pistonFacing = pistonFacingIn;
      this.extending = extendingIn;
      this.shouldHeadBeRendered = shouldHeadBeRenderedIn;
   }

   /**
    * Get an NBT compound to sync to the client with SPacketChunkData, used for initial loading of the chunk or when
    * many blocks change at once. This compound comes back to you clientside in {@link handleUpdateTag}
    */
   public CompoundNBT getUpdateTag() {
      return this.write(new CompoundNBT());
   }

   /**
    * Returns true if a piston is extending
    */
   public boolean isExtending() {
      return this.extending;
   }

   public Direction getFacing() {
      return this.pistonFacing;
   }

   public boolean shouldPistonHeadBeRendered() {
      return this.shouldHeadBeRendered;
   }

   /**
    * Get interpolated progress value (between lastProgress and progress) given the fractional time between ticks as an
    * argument
    */
   public float getProgress(float ticks) {
      if (ticks > 1.0F) {
         ticks = 1.0F;
      }

      return MathHelper.lerp(ticks, this.lastProgress, this.progress);
   }

   @OnlyIn(Dist.CLIENT)
   public float getOffsetX(float ticks) {
      return (float)this.pistonFacing.getXOffset() * this.getExtendedProgress(this.getProgress(ticks));
   }

   @OnlyIn(Dist.CLIENT)
   public float getOffsetY(float ticks) {
      return (float)this.pistonFacing.getYOffset() * this.getExtendedProgress(this.getProgress(ticks));
   }

   @OnlyIn(Dist.CLIENT)
   public float getOffsetZ(float ticks) {
      return (float)this.pistonFacing.getZOffset() * this.getExtendedProgress(this.getProgress(ticks));
   }

   private float getExtendedProgress(float progress) {
      return this.extending ? progress - 1.0F : 1.0F - progress;
   }

   private BlockState getCollisionRelatedBlockState() {
      return !this.isExtending() && this.shouldPistonHeadBeRendered() && this.pistonState.getBlock() instanceof PistonBlock ? Blocks.PISTON_HEAD.getDefaultState().with(PistonHeadBlock.SHORT, Boolean.valueOf(this.progress > 0.25F)).with(PistonHeadBlock.TYPE, this.pistonState.matchesBlock(Blocks.STICKY_PISTON) ? PistonType.STICKY : PistonType.DEFAULT).with(PistonHeadBlock.FACING, this.pistonState.get(PistonBlock.FACING)) : this.pistonState;
   }

   private void moveCollidedEntities(float progress) {
      Direction direction = this.getMotionDirection();
      double d0 = (double)(progress - this.progress);
      VoxelShape voxelshape = this.getCollisionRelatedBlockState().getCollisionShapeUncached(this.world, this.getPos());
      if (!voxelshape.isEmpty()) {
         AxisAlignedBB axisalignedbb = this.moveByPositionAndProgress(voxelshape.getBoundingBox());
         List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity((Entity)null, AabbHelper.func_227019_a_(axisalignedbb, direction, d0).union(axisalignedbb));
         if (!list.isEmpty()) {
            List<AxisAlignedBB> list1 = voxelshape.toBoundingBoxList();
            boolean flag = this.pistonState.isSlimeBlock(); //TODO: is this patch really needed the logic of the original seems sound revisit later
            Iterator iterator = list.iterator();

            while(true) {
               Entity entity;
               while(true) {
                  if (!iterator.hasNext()) {
                     return;
                  }

                  entity = (Entity)iterator.next();
                  if (entity.getPushReaction() != PushReaction.IGNORE) {
                     if (!flag) {
                        break;
                     }

                     if (!(entity instanceof ServerPlayerEntity)) {
                        Vector3d vector3d = entity.getMotion();
                        double d1 = vector3d.x;
                        double d2 = vector3d.y;
                        double d3 = vector3d.z;
                        switch(direction.getAxis()) {
                        case X:
                           d1 = (double)direction.getXOffset();
                           break;
                        case Y:
                           d2 = (double)direction.getYOffset();
                           break;
                        case Z:
                           d3 = (double)direction.getZOffset();
                        }

                        entity.setMotion(d1, d2, d3);
                        break;
                     }
                  }
               }

               double d4 = 0.0D;

               for(AxisAlignedBB axisalignedbb2 : list1) {
                  AxisAlignedBB axisalignedbb1 = AabbHelper.func_227019_a_(this.moveByPositionAndProgress(axisalignedbb2), direction, d0);
                  AxisAlignedBB axisalignedbb3 = entity.getBoundingBox();
                  if (axisalignedbb1.intersects(axisalignedbb3)) {
                     d4 = Math.max(d4, getMovement(axisalignedbb1, direction, axisalignedbb3));
                     if (d4 >= d0) {
                        break;
                     }
                  }
               }

               if (!(d4 <= 0.0D)) {
                  d4 = Math.min(d4, d0) + 0.01D;
                  pushEntity(direction, entity, d4, direction);
                  if (!this.extending && this.shouldHeadBeRendered) {
                     this.fixEntityWithinPistonBase(entity, direction, d0);
                  }
               }
            }
         }
      }
   }

   private static void pushEntity(Direction direction, Entity entity, double progress, Direction p_227022_4_) {
      MOVING_ENTITY.set(direction);
      entity.move(MoverType.PISTON, new Vector3d(progress * (double)p_227022_4_.getXOffset(), progress * (double)p_227022_4_.getYOffset(), progress * (double)p_227022_4_.getZOffset()));
      MOVING_ENTITY.set((Direction)null);
   }

   private void func_227024_g_(float progress) {
      if (this.isHoney()) {
         Direction direction = this.getMotionDirection();
         if (direction.getAxis().isHorizontal()) {
            double d0 = this.pistonState.getCollisionShapeUncached(this.world, this.pos).getEnd(Direction.Axis.Y);
            AxisAlignedBB axisalignedbb = this.moveByPositionAndProgress(new AxisAlignedBB(0.0D, d0, 0.0D, 1.0D, 1.5000000999999998D, 1.0D));
            double d1 = (double)(progress - this.progress);

            for(Entity entity : this.world.getEntitiesInAABBexcluding((Entity)null, axisalignedbb, (entity) -> {
               return canPushEntity(axisalignedbb, entity);
            })) {
               pushEntity(direction, entity, d1, direction);
            }

         }
      }
   }

   private static boolean canPushEntity(AxisAlignedBB shape, Entity entity) {
      return entity.getPushReaction() == PushReaction.NORMAL && entity.isOnGround() && entity.getPosX() >= shape.minX && entity.getPosX() <= shape.maxX && entity.getPosZ() >= shape.minZ && entity.getPosZ() <= shape.maxZ;
   }

   private boolean isHoney() {
      return this.pistonState.matchesBlock(Blocks.HONEY_BLOCK);
   }

   public Direction getMotionDirection() {
      return this.extending ? this.pistonFacing : this.pistonFacing.getOpposite();
   }

   private static double getMovement(AxisAlignedBB headShape, Direction direction, AxisAlignedBB facing) {
      switch(direction) {
      case EAST:
         return headShape.maxX - facing.minX;
      case WEST:
         return facing.maxX - headShape.minX;
      case UP:
      default:
         return headShape.maxY - facing.minY;
      case DOWN:
         return facing.maxY - headShape.minY;
      case SOUTH:
         return headShape.maxZ - facing.minZ;
      case NORTH:
         return facing.maxZ - headShape.minZ;
      }
   }

   private AxisAlignedBB moveByPositionAndProgress(AxisAlignedBB boundingBox) {
      double d0 = (double)this.getExtendedProgress(this.progress);
      return boundingBox.offset((double)this.pos.getX() + d0 * (double)this.pistonFacing.getXOffset(), (double)this.pos.getY() + d0 * (double)this.pistonFacing.getYOffset(), (double)this.pos.getZ() + d0 * (double)this.pistonFacing.getZOffset());
   }

   private void fixEntityWithinPistonBase(Entity p_190605_1_, Direction p_190605_2_, double progress) {
      AxisAlignedBB axisalignedbb = p_190605_1_.getBoundingBox();
      AxisAlignedBB axisalignedbb1 = VoxelShapes.fullCube().getBoundingBox().offset(this.pos);
      if (axisalignedbb.intersects(axisalignedbb1)) {
         Direction direction = p_190605_2_.getOpposite();
         double d0 = getMovement(axisalignedbb1, direction, axisalignedbb) + 0.01D;
         double d1 = getMovement(axisalignedbb1, direction, axisalignedbb.intersect(axisalignedbb1)) + 0.01D;
         if (Math.abs(d0 - d1) < 0.01D) {
            d0 = Math.min(d0, progress) + 0.01D;
            pushEntity(p_190605_2_, p_190605_1_, d0, direction);
         }
      }

   }

   public BlockState getPistonState() {
      return this.pistonState;
   }

   /**
    * removes a piston's tile entity (and if the piston is moving, stops it)
    */
   public void clearPistonTileEntity() {
      if (this.world != null && (this.lastProgress < 1.0F || this.world.isRemote)) {
         this.progress = 1.0F;
         this.lastProgress = this.progress;
         this.world.removeTileEntity(this.pos);
         this.remove();
         if (this.world.getBlockState(this.pos).matchesBlock(Blocks.MOVING_PISTON)) {
            BlockState blockstate;
            if (this.shouldHeadBeRendered) {
               blockstate = Blocks.AIR.getDefaultState();
            } else {
               blockstate = Block.getValidBlockForPosition(this.pistonState, this.world, this.pos);
            }

            this.world.setBlockState(this.pos, blockstate, 3);
            this.world.neighborChanged(this.pos, blockstate.getBlock(), this.pos);
         }
      }

   }

   public void tick() {
      this.lastTicked = this.world.getGameTime();
      this.lastProgress = this.progress;
      if (this.lastProgress >= 1.0F) {
         if (this.world.isRemote && this.field_242697_l < 5) {
            ++this.field_242697_l;
         } else {
            this.world.removeTileEntity(this.pos);
            this.remove();
            if (this.pistonState != null && this.world.getBlockState(this.pos).matchesBlock(Blocks.MOVING_PISTON)) {
               BlockState blockstate = Block.getValidBlockForPosition(this.pistonState, this.world, this.pos);
               if (blockstate.isAir()) {
                  this.world.setBlockState(this.pos, this.pistonState, 84);
                  Block.replaceBlock(this.pistonState, blockstate, this.world, this.pos, 3);
               } else {
                  if (blockstate.hasProperty(BlockStateProperties.WATERLOGGED) && blockstate.get(BlockStateProperties.WATERLOGGED)) {
                     blockstate = blockstate.with(BlockStateProperties.WATERLOGGED, Boolean.valueOf(false));
                  }

                  this.world.setBlockState(this.pos, blockstate, 67);
                  this.world.neighborChanged(this.pos, blockstate.getBlock(), this.pos);
               }
            }

         }
      } else {
         float f = this.progress + 0.5F;
         this.moveCollidedEntities(f);
         this.func_227024_g_(f);
         this.progress = f;
         if (this.progress >= 1.0F) {
            this.progress = 1.0F;
         }

      }
   }

   public void read(BlockState state, CompoundNBT nbt) {
      super.read(state, nbt);
      this.pistonState = NBTUtil.readBlockState(nbt.getCompound("blockState"));
      this.pistonFacing = Direction.byIndex(nbt.getInt("facing"));
      this.progress = nbt.getFloat("progress");
      this.lastProgress = this.progress;
      this.extending = nbt.getBoolean("extending");
      this.shouldHeadBeRendered = nbt.getBoolean("source");
   }

   public CompoundNBT write(CompoundNBT compound) {
      super.write(compound);
      compound.put("blockState", NBTUtil.writeBlockState(this.pistonState));
      compound.putInt("facing", this.pistonFacing.getIndex());
      compound.putFloat("progress", this.lastProgress);
      compound.putBoolean("extending", this.extending);
      compound.putBoolean("source", this.shouldHeadBeRendered);
      return compound;
   }

   public VoxelShape getCollisionShape(IBlockReader reader, BlockPos pos) {
      VoxelShape voxelshape;
      if (!this.extending && this.shouldHeadBeRendered) {
         voxelshape = this.pistonState.with(PistonBlock.EXTENDED, Boolean.valueOf(true)).getCollisionShapeUncached(reader, pos);
      } else {
         voxelshape = VoxelShapes.empty();
      }

      Direction direction = MOVING_ENTITY.get();
      if ((double)this.progress < 1.0D && direction == this.getMotionDirection()) {
         return voxelshape;
      } else {
         BlockState blockstate;
         if (this.shouldPistonHeadBeRendered()) {
            blockstate = Blocks.PISTON_HEAD.getDefaultState().with(PistonHeadBlock.FACING, this.pistonFacing).with(PistonHeadBlock.SHORT, Boolean.valueOf(this.extending != 1.0F - this.progress < 0.25F));
         } else {
            blockstate = this.pistonState;
         }

         float f = this.getExtendedProgress(this.progress);
         double d0 = (double)((float)this.pistonFacing.getXOffset() * f);
         double d1 = (double)((float)this.pistonFacing.getYOffset() * f);
         double d2 = (double)((float)this.pistonFacing.getZOffset() * f);
         return VoxelShapes.or(voxelshape, blockstate.getCollisionShapeUncached(reader, pos).withOffset(d0, d1, d2));
      }
   }

   public long getLastTicked() {
      return this.lastTicked;
   }

   @OnlyIn(Dist.CLIENT)
   public double getMaxRenderDistanceSquared() {
      return 68.0D;
   }
}
