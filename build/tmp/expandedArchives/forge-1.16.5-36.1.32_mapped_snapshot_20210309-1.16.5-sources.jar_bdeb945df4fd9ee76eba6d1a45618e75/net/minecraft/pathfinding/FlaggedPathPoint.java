package net.minecraft.pathfinding;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FlaggedPathPoint extends PathPoint {
   private float shortestDistance = Float.MAX_VALUE;
   /** The nearest path point of the path that is constructed */
   private PathPoint nearest;
   private boolean field_224767_o;

   public FlaggedPathPoint(PathPoint pathPointIn) {
      super(pathPointIn.x, pathPointIn.y, pathPointIn.z);
   }

   @OnlyIn(Dist.CLIENT)
   public FlaggedPathPoint(int x, int y, int z) {
      super(x, y, z);
   }

   public void func_224761_a(float p_224761_1_, PathPoint pointIn) {
      if (p_224761_1_ < this.shortestDistance) {
         this.shortestDistance = p_224761_1_;
         this.nearest = pointIn;
      }

   }

   /**
    * Gets the nearest path point of the path that is constructed
    */
   public PathPoint getNearest() {
      return this.nearest;
   }

   public void func_224764_e() {
      this.field_224767_o = true;
   }

   @OnlyIn(Dist.CLIENT)
   public static FlaggedPathPoint func_224760_c(PacketBuffer buffer) {
      FlaggedPathPoint flaggedpathpoint = new FlaggedPathPoint(buffer.readInt(), buffer.readInt(), buffer.readInt());
      flaggedpathpoint.field_222861_j = buffer.readFloat();
      flaggedpathpoint.costMalus = buffer.readFloat();
      flaggedpathpoint.visited = buffer.readBoolean();
      flaggedpathpoint.nodeType = PathNodeType.values()[buffer.readInt()];
      flaggedpathpoint.totalCost = buffer.readFloat();
      return flaggedpathpoint;
   }
}