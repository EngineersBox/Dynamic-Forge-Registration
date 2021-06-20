package net.minecraft.world.spawner;

import java.util.List;
import java.util.Random;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.GameRules;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;

public class CatSpawner implements ISpecialSpawner {
   private int field_221125_a;

   public int onUpdate(ServerWorld world, boolean spawnHostiles, boolean spawnPassives) {
      if (spawnPassives && world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {
         --this.field_221125_a;
         if (this.field_221125_a > 0) {
            return 0;
         } else {
            this.field_221125_a = 1200;
            PlayerEntity playerentity = world.getRandomPlayer();
            if (playerentity == null) {
               return 0;
            } else {
               Random random = world.rand;
               int i = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
               int j = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
               BlockPos blockpos = playerentity.getPosition().add(i, 0, j);
               if (!world.isAreaLoaded(blockpos.getX() - 10, blockpos.getY() - 10, blockpos.getZ() - 10, blockpos.getX() + 10, blockpos.getY() + 10, blockpos.getZ() + 10)) {
                  return 0;
               } else {
                  if (WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, world, blockpos, EntityType.CAT)) {
                     if (world.doesVillageHaveAllSections(blockpos, 2)) {
                        return this.func_221121_a(world, blockpos);
                     }

                     if (world.getStructureManager().getStructureStart(blockpos, true, Structure.SWAMP_HUT).isValid()) {
                        return this.func_221123_a(world, blockpos);
                     }
                  }

                  return 0;
               }
            }
         }
      } else {
         return 0;
      }
   }

   private int func_221121_a(ServerWorld worldIn, BlockPos pos) {
      int i = 48;
      if (worldIn.getPointOfInterestManager().getCountInRange(PointOfInterestType.HOME.getPredicate(), pos, 48, PointOfInterestManager.Status.IS_OCCUPIED) > 4L) {
         List<CatEntity> list = worldIn.getEntitiesWithinAABB(CatEntity.class, (new AxisAlignedBB(pos)).grow(48.0D, 8.0D, 48.0D));
         if (list.size() < 5) {
            return this.spawnCat(pos, worldIn);
         }
      }

      return 0;
   }

   private int func_221123_a(ServerWorld worldIn, BlockPos pos) {
      int i = 16;
      List<CatEntity> list = worldIn.getEntitiesWithinAABB(CatEntity.class, (new AxisAlignedBB(pos)).grow(16.0D, 8.0D, 16.0D));
      return list.size() < 1 ? this.spawnCat(pos, worldIn) : 0;
   }

   private int spawnCat(BlockPos pos, ServerWorld worldIn) {
      CatEntity catentity = EntityType.CAT.create(worldIn);
      if (catentity == null) {
         return 0;
      } else {
         catentity.moveToBlockPosAndAngles(pos, 0.0F, 0.0F); // Fix MC-147659: Some witch huts spawn the incorrect cat
         if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(catentity, worldIn, pos.getX(), pos.getY(), pos.getZ(), null, SpawnReason.NATURAL) == -1) return 0;
         catentity.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(pos), SpawnReason.NATURAL, (ILivingEntityData)null, (CompoundNBT)null);
         worldIn.func_242417_l(catentity);
         return 1;
      }
   }
}
