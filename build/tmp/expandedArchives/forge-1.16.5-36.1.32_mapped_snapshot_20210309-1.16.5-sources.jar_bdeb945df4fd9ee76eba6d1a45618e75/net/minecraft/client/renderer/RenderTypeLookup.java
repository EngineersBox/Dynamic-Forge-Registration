package net.minecraft.client.renderer;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderTypeLookup {
   @Deprecated
   private static final Map<Block, RenderType> TYPES_BY_BLOCK = Util.make(Maps.newHashMap(), (map) -> {
      RenderType rendertype = RenderType.getTripwire();
      map.put(Blocks.TRIPWIRE, rendertype);
      RenderType rendertype1 = RenderType.getCutoutMipped();
      map.put(Blocks.GRASS_BLOCK, rendertype1);
      map.put(Blocks.IRON_BARS, rendertype1);
      map.put(Blocks.GLASS_PANE, rendertype1);
      map.put(Blocks.TRIPWIRE_HOOK, rendertype1);
      map.put(Blocks.HOPPER, rendertype1);
      map.put(Blocks.CHAIN, rendertype1);
      map.put(Blocks.JUNGLE_LEAVES, rendertype1);
      map.put(Blocks.OAK_LEAVES, rendertype1);
      map.put(Blocks.SPRUCE_LEAVES, rendertype1);
      map.put(Blocks.ACACIA_LEAVES, rendertype1);
      map.put(Blocks.BIRCH_LEAVES, rendertype1);
      map.put(Blocks.DARK_OAK_LEAVES, rendertype1);
      RenderType rendertype2 = RenderType.getCutout();
      map.put(Blocks.OAK_SAPLING, rendertype2);
      map.put(Blocks.SPRUCE_SAPLING, rendertype2);
      map.put(Blocks.BIRCH_SAPLING, rendertype2);
      map.put(Blocks.JUNGLE_SAPLING, rendertype2);
      map.put(Blocks.ACACIA_SAPLING, rendertype2);
      map.put(Blocks.DARK_OAK_SAPLING, rendertype2);
      map.put(Blocks.GLASS, rendertype2);
      map.put(Blocks.WHITE_BED, rendertype2);
      map.put(Blocks.ORANGE_BED, rendertype2);
      map.put(Blocks.MAGENTA_BED, rendertype2);
      map.put(Blocks.LIGHT_BLUE_BED, rendertype2);
      map.put(Blocks.YELLOW_BED, rendertype2);
      map.put(Blocks.LIME_BED, rendertype2);
      map.put(Blocks.PINK_BED, rendertype2);
      map.put(Blocks.GRAY_BED, rendertype2);
      map.put(Blocks.LIGHT_GRAY_BED, rendertype2);
      map.put(Blocks.CYAN_BED, rendertype2);
      map.put(Blocks.PURPLE_BED, rendertype2);
      map.put(Blocks.BLUE_BED, rendertype2);
      map.put(Blocks.BROWN_BED, rendertype2);
      map.put(Blocks.GREEN_BED, rendertype2);
      map.put(Blocks.RED_BED, rendertype2);
      map.put(Blocks.BLACK_BED, rendertype2);
      map.put(Blocks.POWERED_RAIL, rendertype2);
      map.put(Blocks.DETECTOR_RAIL, rendertype2);
      map.put(Blocks.COBWEB, rendertype2);
      map.put(Blocks.GRASS, rendertype2);
      map.put(Blocks.FERN, rendertype2);
      map.put(Blocks.DEAD_BUSH, rendertype2);
      map.put(Blocks.SEAGRASS, rendertype2);
      map.put(Blocks.TALL_SEAGRASS, rendertype2);
      map.put(Blocks.DANDELION, rendertype2);
      map.put(Blocks.POPPY, rendertype2);
      map.put(Blocks.BLUE_ORCHID, rendertype2);
      map.put(Blocks.ALLIUM, rendertype2);
      map.put(Blocks.AZURE_BLUET, rendertype2);
      map.put(Blocks.RED_TULIP, rendertype2);
      map.put(Blocks.ORANGE_TULIP, rendertype2);
      map.put(Blocks.WHITE_TULIP, rendertype2);
      map.put(Blocks.PINK_TULIP, rendertype2);
      map.put(Blocks.OXEYE_DAISY, rendertype2);
      map.put(Blocks.CORNFLOWER, rendertype2);
      map.put(Blocks.WITHER_ROSE, rendertype2);
      map.put(Blocks.LILY_OF_THE_VALLEY, rendertype2);
      map.put(Blocks.BROWN_MUSHROOM, rendertype2);
      map.put(Blocks.RED_MUSHROOM, rendertype2);
      map.put(Blocks.TORCH, rendertype2);
      map.put(Blocks.WALL_TORCH, rendertype2);
      map.put(Blocks.SOUL_TORCH, rendertype2);
      map.put(Blocks.SOUL_WALL_TORCH, rendertype2);
      map.put(Blocks.FIRE, rendertype2);
      map.put(Blocks.SOUL_FIRE, rendertype2);
      map.put(Blocks.SPAWNER, rendertype2);
      map.put(Blocks.REDSTONE_WIRE, rendertype2);
      map.put(Blocks.WHEAT, rendertype2);
      map.put(Blocks.OAK_DOOR, rendertype2);
      map.put(Blocks.LADDER, rendertype2);
      map.put(Blocks.RAIL, rendertype2);
      map.put(Blocks.IRON_DOOR, rendertype2);
      map.put(Blocks.REDSTONE_TORCH, rendertype2);
      map.put(Blocks.REDSTONE_WALL_TORCH, rendertype2);
      map.put(Blocks.CACTUS, rendertype2);
      map.put(Blocks.SUGAR_CANE, rendertype2);
      map.put(Blocks.REPEATER, rendertype2);
      map.put(Blocks.OAK_TRAPDOOR, rendertype2);
      map.put(Blocks.SPRUCE_TRAPDOOR, rendertype2);
      map.put(Blocks.BIRCH_TRAPDOOR, rendertype2);
      map.put(Blocks.JUNGLE_TRAPDOOR, rendertype2);
      map.put(Blocks.ACACIA_TRAPDOOR, rendertype2);
      map.put(Blocks.DARK_OAK_TRAPDOOR, rendertype2);
      map.put(Blocks.CRIMSON_TRAPDOOR, rendertype2);
      map.put(Blocks.WARPED_TRAPDOOR, rendertype2);
      map.put(Blocks.ATTACHED_PUMPKIN_STEM, rendertype2);
      map.put(Blocks.ATTACHED_MELON_STEM, rendertype2);
      map.put(Blocks.PUMPKIN_STEM, rendertype2);
      map.put(Blocks.MELON_STEM, rendertype2);
      map.put(Blocks.VINE, rendertype2);
      map.put(Blocks.LILY_PAD, rendertype2);
      map.put(Blocks.NETHER_WART, rendertype2);
      map.put(Blocks.BREWING_STAND, rendertype2);
      map.put(Blocks.COCOA, rendertype2);
      map.put(Blocks.BEACON, rendertype2);
      map.put(Blocks.FLOWER_POT, rendertype2);
      map.put(Blocks.POTTED_OAK_SAPLING, rendertype2);
      map.put(Blocks.POTTED_SPRUCE_SAPLING, rendertype2);
      map.put(Blocks.POTTED_BIRCH_SAPLING, rendertype2);
      map.put(Blocks.POTTED_JUNGLE_SAPLING, rendertype2);
      map.put(Blocks.POTTED_ACACIA_SAPLING, rendertype2);
      map.put(Blocks.POTTED_DARK_OAK_SAPLING, rendertype2);
      map.put(Blocks.POTTED_FERN, rendertype2);
      map.put(Blocks.POTTED_DANDELION, rendertype2);
      map.put(Blocks.POTTED_POPPY, rendertype2);
      map.put(Blocks.POTTED_BLUE_ORCHID, rendertype2);
      map.put(Blocks.POTTED_ALLIUM, rendertype2);
      map.put(Blocks.POTTED_AZURE_BLUET, rendertype2);
      map.put(Blocks.POTTED_RED_TULIP, rendertype2);
      map.put(Blocks.POTTED_ORANGE_TULIP, rendertype2);
      map.put(Blocks.POTTED_WHITE_TULIP, rendertype2);
      map.put(Blocks.POTTED_PINK_TULIP, rendertype2);
      map.put(Blocks.POTTED_OXEYE_DAISY, rendertype2);
      map.put(Blocks.POTTED_CORNFLOWER, rendertype2);
      map.put(Blocks.POTTED_LILY_OF_THE_VALLEY, rendertype2);
      map.put(Blocks.POTTED_WITHER_ROSE, rendertype2);
      map.put(Blocks.POTTED_RED_MUSHROOM, rendertype2);
      map.put(Blocks.POTTED_BROWN_MUSHROOM, rendertype2);
      map.put(Blocks.POTTED_DEAD_BUSH, rendertype2);
      map.put(Blocks.POTTED_CACTUS, rendertype2);
      map.put(Blocks.CARROTS, rendertype2);
      map.put(Blocks.POTATOES, rendertype2);
      map.put(Blocks.COMPARATOR, rendertype2);
      map.put(Blocks.ACTIVATOR_RAIL, rendertype2);
      map.put(Blocks.IRON_TRAPDOOR, rendertype2);
      map.put(Blocks.SUNFLOWER, rendertype2);
      map.put(Blocks.LILAC, rendertype2);
      map.put(Blocks.ROSE_BUSH, rendertype2);
      map.put(Blocks.PEONY, rendertype2);
      map.put(Blocks.TALL_GRASS, rendertype2);
      map.put(Blocks.LARGE_FERN, rendertype2);
      map.put(Blocks.SPRUCE_DOOR, rendertype2);
      map.put(Blocks.BIRCH_DOOR, rendertype2);
      map.put(Blocks.JUNGLE_DOOR, rendertype2);
      map.put(Blocks.ACACIA_DOOR, rendertype2);
      map.put(Blocks.DARK_OAK_DOOR, rendertype2);
      map.put(Blocks.END_ROD, rendertype2);
      map.put(Blocks.CHORUS_PLANT, rendertype2);
      map.put(Blocks.CHORUS_FLOWER, rendertype2);
      map.put(Blocks.BEETROOTS, rendertype2);
      map.put(Blocks.KELP, rendertype2);
      map.put(Blocks.KELP_PLANT, rendertype2);
      map.put(Blocks.TURTLE_EGG, rendertype2);
      map.put(Blocks.DEAD_TUBE_CORAL, rendertype2);
      map.put(Blocks.DEAD_BRAIN_CORAL, rendertype2);
      map.put(Blocks.DEAD_BUBBLE_CORAL, rendertype2);
      map.put(Blocks.DEAD_FIRE_CORAL, rendertype2);
      map.put(Blocks.DEAD_HORN_CORAL, rendertype2);
      map.put(Blocks.TUBE_CORAL, rendertype2);
      map.put(Blocks.BRAIN_CORAL, rendertype2);
      map.put(Blocks.BUBBLE_CORAL, rendertype2);
      map.put(Blocks.FIRE_CORAL, rendertype2);
      map.put(Blocks.HORN_CORAL, rendertype2);
      map.put(Blocks.DEAD_TUBE_CORAL_FAN, rendertype2);
      map.put(Blocks.DEAD_BRAIN_CORAL_FAN, rendertype2);
      map.put(Blocks.DEAD_BUBBLE_CORAL_FAN, rendertype2);
      map.put(Blocks.DEAD_FIRE_CORAL_FAN, rendertype2);
      map.put(Blocks.DEAD_HORN_CORAL_FAN, rendertype2);
      map.put(Blocks.TUBE_CORAL_FAN, rendertype2);
      map.put(Blocks.BRAIN_CORAL_FAN, rendertype2);
      map.put(Blocks.BUBBLE_CORAL_FAN, rendertype2);
      map.put(Blocks.FIRE_CORAL_FAN, rendertype2);
      map.put(Blocks.HORN_CORAL_FAN, rendertype2);
      map.put(Blocks.DEAD_TUBE_CORAL_WALL_FAN, rendertype2);
      map.put(Blocks.DEAD_BRAIN_CORAL_WALL_FAN, rendertype2);
      map.put(Blocks.DEAD_BUBBLE_CORAL_WALL_FAN, rendertype2);
      map.put(Blocks.DEAD_FIRE_CORAL_WALL_FAN, rendertype2);
      map.put(Blocks.DEAD_HORN_CORAL_WALL_FAN, rendertype2);
      map.put(Blocks.TUBE_CORAL_WALL_FAN, rendertype2);
      map.put(Blocks.BRAIN_CORAL_WALL_FAN, rendertype2);
      map.put(Blocks.BUBBLE_CORAL_WALL_FAN, rendertype2);
      map.put(Blocks.FIRE_CORAL_WALL_FAN, rendertype2);
      map.put(Blocks.HORN_CORAL_WALL_FAN, rendertype2);
      map.put(Blocks.SEA_PICKLE, rendertype2);
      map.put(Blocks.CONDUIT, rendertype2);
      map.put(Blocks.BAMBOO_SAPLING, rendertype2);
      map.put(Blocks.BAMBOO, rendertype2);
      map.put(Blocks.POTTED_BAMBOO, rendertype2);
      map.put(Blocks.SCAFFOLDING, rendertype2);
      map.put(Blocks.STONECUTTER, rendertype2);
      map.put(Blocks.LANTERN, rendertype2);
      map.put(Blocks.SOUL_LANTERN, rendertype2);
      map.put(Blocks.CAMPFIRE, rendertype2);
      map.put(Blocks.SOUL_CAMPFIRE, rendertype2);
      map.put(Blocks.SWEET_BERRY_BUSH, rendertype2);
      map.put(Blocks.WEEPING_VINES, rendertype2);
      map.put(Blocks.WEEPING_VINES_PLANT, rendertype2);
      map.put(Blocks.TWISTING_VINES, rendertype2);
      map.put(Blocks.TWISTING_VINES_PLANT, rendertype2);
      map.put(Blocks.NETHER_SPROUTS, rendertype2);
      map.put(Blocks.CRIMSON_FUNGUS, rendertype2);
      map.put(Blocks.WARPED_FUNGUS, rendertype2);
      map.put(Blocks.CRIMSON_ROOTS, rendertype2);
      map.put(Blocks.WARPED_ROOTS, rendertype2);
      map.put(Blocks.POTTED_CRIMSON_FUNGUS, rendertype2);
      map.put(Blocks.POTTED_WARPED_FUNGUS, rendertype2);
      map.put(Blocks.POTTED_CRIMSON_ROOTS, rendertype2);
      map.put(Blocks.POTTED_WARPED_ROOTS, rendertype2);
      map.put(Blocks.CRIMSON_DOOR, rendertype2);
      map.put(Blocks.WARPED_DOOR, rendertype2);
      RenderType rendertype3 = RenderType.getTranslucent();
      map.put(Blocks.ICE, rendertype3);
      map.put(Blocks.NETHER_PORTAL, rendertype3);
      map.put(Blocks.WHITE_STAINED_GLASS, rendertype3);
      map.put(Blocks.ORANGE_STAINED_GLASS, rendertype3);
      map.put(Blocks.MAGENTA_STAINED_GLASS, rendertype3);
      map.put(Blocks.LIGHT_BLUE_STAINED_GLASS, rendertype3);
      map.put(Blocks.YELLOW_STAINED_GLASS, rendertype3);
      map.put(Blocks.LIME_STAINED_GLASS, rendertype3);
      map.put(Blocks.PINK_STAINED_GLASS, rendertype3);
      map.put(Blocks.GRAY_STAINED_GLASS, rendertype3);
      map.put(Blocks.LIGHT_GRAY_STAINED_GLASS, rendertype3);
      map.put(Blocks.CYAN_STAINED_GLASS, rendertype3);
      map.put(Blocks.PURPLE_STAINED_GLASS, rendertype3);
      map.put(Blocks.BLUE_STAINED_GLASS, rendertype3);
      map.put(Blocks.BROWN_STAINED_GLASS, rendertype3);
      map.put(Blocks.GREEN_STAINED_GLASS, rendertype3);
      map.put(Blocks.RED_STAINED_GLASS, rendertype3);
      map.put(Blocks.BLACK_STAINED_GLASS, rendertype3);
      map.put(Blocks.WHITE_STAINED_GLASS_PANE, rendertype3);
      map.put(Blocks.ORANGE_STAINED_GLASS_PANE, rendertype3);
      map.put(Blocks.MAGENTA_STAINED_GLASS_PANE, rendertype3);
      map.put(Blocks.LIGHT_BLUE_STAINED_GLASS_PANE, rendertype3);
      map.put(Blocks.YELLOW_STAINED_GLASS_PANE, rendertype3);
      map.put(Blocks.LIME_STAINED_GLASS_PANE, rendertype3);
      map.put(Blocks.PINK_STAINED_GLASS_PANE, rendertype3);
      map.put(Blocks.GRAY_STAINED_GLASS_PANE, rendertype3);
      map.put(Blocks.LIGHT_GRAY_STAINED_GLASS_PANE, rendertype3);
      map.put(Blocks.CYAN_STAINED_GLASS_PANE, rendertype3);
      map.put(Blocks.PURPLE_STAINED_GLASS_PANE, rendertype3);
      map.put(Blocks.BLUE_STAINED_GLASS_PANE, rendertype3);
      map.put(Blocks.BROWN_STAINED_GLASS_PANE, rendertype3);
      map.put(Blocks.GREEN_STAINED_GLASS_PANE, rendertype3);
      map.put(Blocks.RED_STAINED_GLASS_PANE, rendertype3);
      map.put(Blocks.BLACK_STAINED_GLASS_PANE, rendertype3);
      map.put(Blocks.SLIME_BLOCK, rendertype3);
      map.put(Blocks.HONEY_BLOCK, rendertype3);
      map.put(Blocks.FROSTED_ICE, rendertype3);
      map.put(Blocks.BUBBLE_COLUMN, rendertype3);
   });
   @Deprecated
   private static final Map<Fluid, RenderType> TYPES_BY_FLUID = Util.make(Maps.newHashMap(), (map) -> {
      RenderType rendertype = RenderType.getTranslucent();
      map.put(Fluids.FLOWING_WATER, rendertype);
      map.put(Fluids.WATER, rendertype);
   });
   private static boolean fancyGraphics;

   @Deprecated // FORGE: Use canRenderInLayer
   public static RenderType getChunkRenderType(BlockState blockStateIn) {
      Block block = blockStateIn.getBlock();
      if (block instanceof LeavesBlock) {
         return fancyGraphics ? RenderType.getCutoutMipped() : RenderType.getSolid();
      } else {
         RenderType rendertype = TYPES_BY_BLOCK.get(block);
         return rendertype != null ? rendertype : RenderType.getSolid();
      }
   }

   @Deprecated // FORGE: Use canRenderInLayer
   public static RenderType func_239221_b_(BlockState state) {
      Block block = state.getBlock();
      if (block instanceof LeavesBlock) {
         return fancyGraphics ? RenderType.getCutoutMipped() : RenderType.getSolid();
      } else {
         RenderType rendertype = TYPES_BY_BLOCK.get(block);
         if (rendertype != null) {
            return rendertype == RenderType.getTranslucent() ? RenderType.getTranslucentMovingBlock() : rendertype;
         } else {
            return RenderType.getSolid();
         }
      }
   }

   public static RenderType func_239220_a_(BlockState state, boolean isEntity) {
      if (canRenderInLayer(state, RenderType.getTranslucent())) {
         if (!Minecraft.isFabulousGraphicsEnabled()) {
            return Atlases.getTranslucentCullBlockType();
         } else {
            return isEntity ? Atlases.getTranslucentCullBlockType() : Atlases.getItemEntityTranslucentCullType();
         }
      } else {
         return Atlases.getCutoutBlockType();
      }
   }

   public static RenderType func_239219_a_(ItemStack stack, boolean isEntity) {
      Item item = stack.getItem();
      if (item instanceof BlockItem) {
         Block block = ((BlockItem)item).getBlock();
         return func_239220_a_(block.getDefaultState(), isEntity);
      } else {
         return isEntity ? Atlases.getTranslucentCullBlockType() : Atlases.getItemEntityTranslucentCullType();
      }
   }

   @Deprecated // FORGE: Use canRenderInLayer
   public static RenderType getRenderType(FluidState fluidStateIn) {
      RenderType rendertype = TYPES_BY_FLUID.get(fluidStateIn.getFluid());
      return rendertype != null ? rendertype : RenderType.getSolid();
   }

   // FORGE START

   private static final Map<net.minecraftforge.registries.IRegistryDelegate<Block>, java.util.function.Predicate<RenderType>> blockRenderChecks = Maps.newHashMap();
   private static final Map<net.minecraftforge.registries.IRegistryDelegate<Fluid>, java.util.function.Predicate<RenderType>> fluidRenderChecks = Maps.newHashMap();
   static {
      TYPES_BY_BLOCK.forEach(RenderTypeLookup::setRenderLayer);
      TYPES_BY_FLUID.forEach(RenderTypeLookup::setRenderLayer);
   }

   public static boolean canRenderInLayer(BlockState state, RenderType type) {
      Block block = state.getBlock();
      if (block instanceof LeavesBlock) {
         return fancyGraphics ? type == RenderType.getCutoutMipped() : type == RenderType.getSolid();
      } else {
         java.util.function.Predicate<RenderType> rendertype;
         synchronized (RenderTypeLookup.class) {
            rendertype = blockRenderChecks.get(block.delegate);
         }
         return rendertype != null ? rendertype.test(type) : type == RenderType.getSolid();
      }
   }

   public static boolean canRenderInLayer(FluidState fluid, RenderType type) {
      java.util.function.Predicate<RenderType> rendertype;
      synchronized (RenderTypeLookup.class) {
         rendertype = fluidRenderChecks.get(fluid.getFluid().delegate);
      }
      return rendertype != null ? rendertype.test(type) : type == RenderType.getSolid();
   }

   public static void setRenderLayer(Block block, RenderType type) {
      java.util.Objects.requireNonNull(type);
      setRenderLayer(block, type::equals);
   }

   public static synchronized void setRenderLayer(Block block, java.util.function.Predicate<RenderType> predicate) {
      blockRenderChecks.put(block.delegate, predicate);
   }

   public static void setRenderLayer(Fluid fluid, RenderType type) {
      java.util.Objects.requireNonNull(type);
      setRenderLayer(fluid, type::equals);
   }

   public static synchronized void setRenderLayer(Fluid fluid, java.util.function.Predicate<RenderType> predicate) {
      fluidRenderChecks.put(fluid.delegate, predicate);
   }

   public static void setFancyGraphics(boolean fancyIn) {
      fancyGraphics = fancyIn;
   }
}
