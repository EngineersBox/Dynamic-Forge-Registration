package com.engineersbox.expandedfusion.core.util;

import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemHandlerHelper;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public final class InventoryUtils {
    private InventoryUtils() {throw new IllegalAccessError("Utility class");}

    /**
     * Creates slots for the player's inventory for a {@link Container}. Convenience method to
     * improve readability of Container code.
     *
     * @param playerInventory Player's inventory
     * @param startX          X-position of top-left slot
     * @param startY          Y-position of top-left slot
     * @return A collection of slots to be added
     * @since 4.1.1
     */
    public static Collection<Slot> createPlayerSlots(final PlayerInventory playerInventory, final int startX, final int startY) {
        Collection<Slot> list = new ArrayList<>();
        // Backpack
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                list.add(new Slot(playerInventory, x + y * 9 + 9, startX + x * 18, startY + y * 18));
            }
        }
        // Hotbar
        for (int x = 0; x < 9; ++x) {
            list.add(new Slot(playerInventory, x, 8 + x * 18, startY + 58));
        }
        return list;
    }

    public static boolean canItemsStackTag(final ItemStack a, final ItemStack b) {
        if (a.getItem() != b.getItem())
            return false;
        if (a.getTag() == null && b.getTag() != null)
            return false;
        return (a.getTag() == null || a.getTag().equals(b.getTag())) && a.areCapsCompatible(b);
    }

    /**
     * Obtain the first matching stack. {@link StackList} has a similar method, but this avoids
     * creating the entire list when it isn't needed.
     *
     * @param inv       The inventory to search
     * @param predicate Condition to match
     * @return The first matching stack, or {@link ItemStack#EMPTY} if there is none
     * @since 3.1.0 (was in StackHelper from 3.0.6)
     */
    public static ItemStack firstMatch(final IInventory inv, final Predicate<ItemStack> predicate) {
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && predicate.test(stack)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack mergeItem(final IInventory inventory, final int slotStart, final int slotEndExclusive, final ItemStack stack) {
        if (inventory == null || stack.isEmpty()) {
            return stack;
        }

        // Merge into non-empty slots first
        for (int i = slotStart; i < slotEndExclusive && !stack.isEmpty(); ++i) {
            ItemStack inSlot = inventory.getStackInSlot(i);
            if (canItemsStack(inSlot, stack)) {
                int amountCanFit = MathUtils.min(inSlot.getMaxStackSize() - inSlot.getCount(), stack.getCount(), inventory.getInventoryStackLimit());
                inSlot.grow(amountCanFit);
                stack.shrink(amountCanFit);
                inventory.setInventorySlotContents(i, inSlot);
            }
        }

        // Fill empty slots next
        for (int i = slotStart; i < slotEndExclusive && !stack.isEmpty(); ++i) {
            if (inventory.getStackInSlot(i).isEmpty()) {
                int amountCanFit = MathUtils.min(stack.getCount(), inventory.getInventoryStackLimit());
                ItemStack toInsert = stack.copy();
                toInsert.setCount(amountCanFit);
                stack.shrink(amountCanFit);
                inventory.setInventorySlotContents(i, toInsert);
            }
        }

        return stack;
    }

    public static Collection<ItemStack> mergeItems(final IInventory inventory, final int slotStart, final int slotEndExclusive, final Collection<ItemStack> stacks) {
        if (inventory == null && stacks.isEmpty()) {
            return ImmutableList.of();
        }

        ImmutableList.Builder<ItemStack> leftovers = ImmutableList.builder();

        for (ItemStack stack : stacks) {
            stack = mergeItem(inventory, slotStart, slotEndExclusive, stack);

            // Failed to merge?
            if (!stack.isEmpty()) {
                leftovers.add(stack);
            }
        }

        return leftovers.build();
    }

    /**
     * Gets the total number of matching items in all slots in the inventory.
     *
     * @param inventory  The inventory
     * @param ingredient The items to match ({@link net.minecraft.item.crafting.Ingredient}, etc.)
     * @return The number of items in all matching item stacks
     */
    public static int getTotalCount(final IInventory inventory, final Predicate<ItemStack> ingredient) {
        int total = 0;
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty() && ingredient.test(stack)) {
                total += stack.getCount();
            }
        }
        return total;
    }

    /**
     * Consumes (removes) items from the inventory. This is useful for machines, which may have
     * multiple input slots and recipes that consume multiple of one item.
     *
     * @param inventory The inventory
     * @param ingredient The items to match ({@link net.minecraft.item.crafting.Ingredient}, etc.)
     * @param amount The total number of items to remove
     */
    public static void consumeItems(final IInventory inventory, final Predicate<ItemStack> ingredient, int amount) {
        int amountLeft = amount;
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty() && ingredient.test(stack)) {
                int toRemove = Math.min(amountLeft, stack.getCount());

                stack.shrink(toRemove);
                if (stack.isEmpty()) {
                    inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                }

                amountLeft -= toRemove;
                if (amountLeft == 0) {
                    return;
                }
            }
        }
    }

    public static boolean canItemsStack(final ItemStack a, final ItemStack b) {
        // Determine if the item stacks can be merged
        if (a.isEmpty() || b.isEmpty()) return true;
        return ItemHandlerHelper.canItemStacksStack(a, b) && a.getCount() + b.getCount() <= a.getMaxStackSize();
    }

    public static boolean mergeItem(final IInventory inventory, final ItemStack stack, final int slot) {
        ItemStack current = inventory.getStackInSlot(slot);
        if (current.isEmpty()) {
            inventory.setInventorySlotContents(slot, stack);
            return true;
        } else if (canItemsStack(stack, current)) {
            current.grow(stack.getCount());
            return true;
        }
        return false;
    }

//    public static boolean isFilledFluidContainer(final ItemStack stack) {
//        Item item = stack.getItem();
//        return (item instanceof BucketItem && ((BucketItem) item).getFluid() != Fluids.EMPTY)
//                || (item instanceof CanisterItem && !((CanisterItem) item).getFluid(stack).isEmpty());
//    }
//
//    public static boolean isEmptyFluidContainer(final ItemStack stack) {
//        Item item = stack.getItem();
//        return (item instanceof BucketItem && ((BucketItem) item).getFluid() == Fluids.EMPTY)
//                || (item instanceof CanisterItem && ((CanisterItem) item).getFluid(stack).isEmpty());
//    }
}
