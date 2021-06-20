package net.minecraft.item;

import java.util.Optional;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompassItem extends Item implements IVanishable {
   private static final Logger LOGGER = LogManager.getLogger();

   public CompassItem(Item.Properties builder) {
      super(builder);
   }

   public static boolean hasLodestone(ItemStack stack) {
      CompoundNBT compoundnbt = stack.getTag();
      return compoundnbt != null && (compoundnbt.contains("LodestoneDimension") || compoundnbt.contains("LodestonePos"));
   }

   /**
    * Returns true if this item has an enchantment glint. By default, this returns <code>stack.isItemEnchanted()</code>,
    * but other items can override it (for instance, written books always return true).
    *  
    * Note that if you override this method, you generally want to also call the super version (on {@link Item}) to get
    * the glint for enchanted items. Of course, that is unnecessary if the overwritten version always returns true.
    */
   public boolean hasEffect(ItemStack stack) {
      return hasLodestone(stack) || super.hasEffect(stack);
   }

   public static Optional<RegistryKey<World>> getLodestoneDimension(CompoundNBT nbt) {
      return World.CODEC.parse(NBTDynamicOps.INSTANCE, nbt.get("LodestoneDimension")).result();
   }

   /**
    * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
    * update it's contents.
    */
   public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
      if (!worldIn.isRemote) {
         if (hasLodestone(stack)) {
            CompoundNBT compoundnbt = stack.getOrCreateTag();
            if (compoundnbt.contains("LodestoneTracked") && !compoundnbt.getBoolean("LodestoneTracked")) {
               return;
            }

            Optional<RegistryKey<World>> optional = getLodestoneDimension(compoundnbt);
            if (optional.isPresent() && optional.get() == worldIn.getDimensionKey() && compoundnbt.contains("LodestonePos") && !((ServerWorld)worldIn).getPointOfInterestManager().hasTypeAtPosition(PointOfInterestType.LODESTONE, NBTUtil.readBlockPos(compoundnbt.getCompound("LodestonePos")))) {
               compoundnbt.remove("LodestonePos");
            }
         }

      }
   }

   /**
    * Called when this item is used when targetting a Block
    */
   public ActionResultType onItemUse(ItemUseContext context) {
      BlockPos blockpos = context.getPos();
      World world = context.getWorld();
      if (!world.getBlockState(blockpos).matchesBlock(Blocks.LODESTONE)) {
         return super.onItemUse(context);
      } else {
         world.playSound((PlayerEntity)null, blockpos, SoundEvents.ITEM_LODESTONE_COMPASS_LOCK, SoundCategory.PLAYERS, 1.0F, 1.0F);
         PlayerEntity playerentity = context.getPlayer();
         ItemStack itemstack = context.getItem();
         boolean flag = !playerentity.abilities.isCreativeMode && itemstack.getCount() == 1;
         if (flag) {
            this.write(world.getDimensionKey(), blockpos, itemstack.getOrCreateTag());
         } else {
            ItemStack itemstack1 = new ItemStack(Items.COMPASS, 1);
            CompoundNBT compoundnbt = itemstack.hasTag() ? itemstack.getTag().copy() : new CompoundNBT();
            itemstack1.setTag(compoundnbt);
            if (!playerentity.abilities.isCreativeMode) {
               itemstack.shrink(1);
            }

            this.write(world.getDimensionKey(), blockpos, compoundnbt);
            if (!playerentity.inventory.addItemStackToInventory(itemstack1)) {
               playerentity.dropItem(itemstack1, false);
            }
         }

         return ActionResultType.func_233537_a_(world.isRemote);
      }
   }

   private void write(RegistryKey<World> lodestoneDimension, BlockPos lodestonePos, CompoundNBT nbt) {
      nbt.put("LodestonePos", NBTUtil.writeBlockPos(lodestonePos));
      World.CODEC.encodeStart(NBTDynamicOps.INSTANCE, lodestoneDimension).resultOrPartial(LOGGER::error).ifPresent((p_234668_1_) -> {
         nbt.put("LodestoneDimension", p_234668_1_);
      });
      nbt.putBoolean("LodestoneTracked", true);
   }

   /**
    * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
    * different names based on their damage or NBT.
    */
   public String getTranslationKey(ItemStack stack) {
      return hasLodestone(stack) ? "item.minecraft.lodestone_compass" : super.getTranslationKey(stack);
   }
}