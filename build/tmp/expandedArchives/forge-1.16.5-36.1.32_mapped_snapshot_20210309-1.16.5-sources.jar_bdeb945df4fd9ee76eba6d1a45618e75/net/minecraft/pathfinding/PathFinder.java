package net.minecraft.pathfinding;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Region;

public class PathFinder {
   private final PathPoint[] pathOptions = new PathPoint[32];
   private final int searchDepth;
   private final NodeProcessor nodeProcessor;
   private final PathHeap pathHeap = new PathHeap();

   public PathFinder(NodeProcessor nodeProcessorIn, int searchDepthIn) {
      this.nodeProcessor = nodeProcessorIn;
      this.searchDepth = searchDepthIn;
   }

   /**
    * Finds a path to one of the specified positions and post-processes it or returns null if no path could be found
    * within given accuracy
    */
   @Nullable
   public Path getNewPath(Region regionIn, MobEntity mob, Set<BlockPos> targetPositions, float maxRange, int accuracy, float searchDepthMultiplier) {
      this.pathHeap.clearPath();
      this.nodeProcessor.func_225578_a_(regionIn, mob);
      PathPoint pathpoint = this.nodeProcessor.getStart();
      Map<FlaggedPathPoint, BlockPos> map = targetPositions.stream().collect(Collectors.toMap((pos) -> {
         return this.nodeProcessor.func_224768_a((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
      }, Function.identity()));
      Path path = this.generatePath(pathpoint, map, maxRange, accuracy, searchDepthMultiplier);
      this.nodeProcessor.postProcess();
      return path;
   }

   /**
    * Generates a path from start to one of the targets or returns null if no path could be generated
    */
   @Nullable
   private Path generatePath(PathPoint start, Map<FlaggedPathPoint, BlockPos> targetPosMap, float maxRange, int accuracy, float searchDepthMultiplier) {
      Set<FlaggedPathPoint> set = targetPosMap.keySet();
      start.accumulatedCost = 0.0F;
      start.costToTarget = this.func_224776_a(start, set);
      start.totalCost = start.costToTarget;
      this.pathHeap.clearPath();
      this.pathHeap.addPoint(start);
      Set<PathPoint> set1 = ImmutableSet.of();
      int i = 0;
      Set<FlaggedPathPoint> set2 = Sets.newHashSetWithExpectedSize(set.size());
      int j = (int)((float)this.searchDepth * searchDepthMultiplier);

      while(!this.pathHeap.isPathEmpty()) {
         ++i;
         if (i >= j) {
            break;
         }

         PathPoint pathpoint = this.pathHeap.dequeue();
         pathpoint.visited = true;

         for(FlaggedPathPoint flaggedpathpoint : set) {
            if (pathpoint.func_224757_c(flaggedpathpoint) <= (float)accuracy) {
               flaggedpathpoint.func_224764_e();
               set2.add(flaggedpathpoint);
            }
         }

         if (!set2.isEmpty()) {
            break;
         }

         if (!(pathpoint.distanceTo(start) >= maxRange)) {
            int k = this.nodeProcessor.func_222859_a(this.pathOptions, pathpoint);

            for(int l = 0; l < k; ++l) {
               PathPoint pathpoint1 = this.pathOptions[l];
               float f = pathpoint.distanceTo(pathpoint1);
               pathpoint1.field_222861_j = pathpoint.field_222861_j + f;
               float f1 = pathpoint.accumulatedCost + f + pathpoint1.costMalus;
               if (pathpoint1.field_222861_j < maxRange && (!pathpoint1.isAssigned() || f1 < pathpoint1.accumulatedCost)) {
                  pathpoint1.previous = pathpoint;
                  pathpoint1.accumulatedCost = f1;
                  pathpoint1.costToTarget = this.func_224776_a(pathpoint1, set) * 1.5F;
                  if (pathpoint1.isAssigned()) {
                     this.pathHeap.changeTotalCost(pathpoint1, pathpoint1.accumulatedCost + pathpoint1.costToTarget);
                  } else {
                     pathpoint1.totalCost = pathpoint1.accumulatedCost + pathpoint1.costToTarget;
                     this.pathHeap.addPoint(pathpoint1);
                  }
               }
            }
         }
      }

      Optional<Path> optional = !set2.isEmpty() ? set2.stream().map((p_224778_2_) -> {
         return this.unfoldPathPoint(p_224778_2_.getNearest(), targetPosMap.get(p_224778_2_), true);
      }).min(Comparator.comparingInt(Path::getCurrentPathLength)) : set.stream().map((targetPoint) -> {
         return this.unfoldPathPoint(targetPoint.getNearest(), targetPosMap.get(targetPoint), false);
      }).min(Comparator.comparingDouble(Path::func_224769_l).thenComparingInt(Path::getCurrentPathLength));
      return !optional.isPresent() ? null : optional.get();
   }

   private float func_224776_a(PathPoint p_224776_1_, Set<FlaggedPathPoint> p_224776_2_) {
      float f = Float.MAX_VALUE;

      for(FlaggedPathPoint flaggedpathpoint : p_224776_2_) {
         float f1 = p_224776_1_.distanceTo(flaggedpathpoint);
         flaggedpathpoint.func_224761_a(f1, p_224776_1_);
         f = Math.min(f1, f);
      }

      return f;
   }

   /**
    * Converts a recursive path point structure into a path
    */
   private Path unfoldPathPoint(PathPoint point, BlockPos targetPos, boolean reachesTarget) {
      List<PathPoint> list = Lists.newArrayList();
      PathPoint pathpoint = point;
      list.add(0, point);

      while(pathpoint.previous != null) {
         pathpoint = pathpoint.previous;
         list.add(0, pathpoint);
      }

      return new Path(list, targetPos, reachesTarget);
   }
}