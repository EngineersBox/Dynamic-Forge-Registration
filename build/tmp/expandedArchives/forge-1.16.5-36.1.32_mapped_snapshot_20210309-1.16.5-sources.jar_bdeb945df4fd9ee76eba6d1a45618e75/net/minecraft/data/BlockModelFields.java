package net.minecraft.data;

import com.google.gson.JsonPrimitive;
import net.minecraft.util.ResourceLocation;

public class BlockModelFields {
   public static final BlockModeInfo<BlockModelFields.Rotation> X_ROTATIONS = new BlockModeInfo<>("x", (rotation) -> {
      return new JsonPrimitive(rotation.degrees);
   });
   public static final BlockModeInfo<BlockModelFields.Rotation> Y_ROTATIONS = new BlockModeInfo<>("y", (rotation) -> {
      return new JsonPrimitive(rotation.degrees);
   });
   public static final BlockModeInfo<ResourceLocation> MODEL = new BlockModeInfo<>("model", (resourceLocation) -> {
      return new JsonPrimitive(resourceLocation.toString());
   });
   public static final BlockModeInfo<Boolean> UVLOCK = new BlockModeInfo<>("uvlock", JsonPrimitive::new);
   public static final BlockModeInfo<Integer> WEIGHT = new BlockModeInfo<>("weight", JsonPrimitive::new);

   public static enum Rotation {
      R0(0),
      R90(90),
      R180(180),
      R270(270);

      private final int degrees;

      private Rotation(int degrees) {
         this.degrees = degrees;
      }
   }
}