package com.engineersbox.expandedfusion.core.util;

import com.engineersbox.expandedfusion.core.util.generator.LayeredStreamGenerator;
import com.engineersbox.expandedfusion.core.util.math.MathUtils;
import com.engineersbox.expandedfusion.elements.item.CanisterItem;
import com.google.common.collect.Range;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public final class InventoryUtils {

    public static Collection<Slot> createPlayerSlots(final PlayerInventory playerInventory, final int startX, final int startY) {
        final Optional<Collection<Slot>> list = new LayeredStreamGenerator<Slot>()
            .setBounds(
                Range.closedOpen(0, 9),
                Range.closedOpen(0, 3)
            ).setConsumers(
                (int ...x) -> Optional.of(new Slot(
                    playerInventory,
                    x[0],
                    8 + x[0] * 18,
                    startY + 58
                )),
                (int ...xy) -> Optional.of(new Slot(
                    playerInventory,
                    xy[0] + xy[1] * 9 + 9,
                    startX + xy[0] * 18,
                    startY + xy[1] * 18
                ))
            ).apply();
        return list.orElse(Collections.emptyList());
    }

    public static boolean canItemsStackTag(final ItemStack a, final ItemStack b) {
        if (a.getItem() != b.getItem())
            return false;
        if (a.getTag() == null && b.getTag() != null)
            return false;
        return (a.getTag() == null || a.getTag().equals(b.getTag())) && a.areCapsCompatible(b);
    }

    public static ItemStack firstMatch(final IInventory inv, final Predicate<ItemStack> predicate) {
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            final ItemStack stack = inv.getStackInSlot(i);
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
            final ItemStack inSlot = inventory.getStackInSlot(i);
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
                final ItemStack toInsert = stack.copy();
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

        final ImmutableList.Builder<ItemStack> leftovers = ImmutableList.builder();

        stacks.forEach((stack) -> {
            stack = mergeItem(inventory, slotStart, slotEndExclusive, stack);

            // Failed to merge?
            if (!stack.isEmpty()) {
                leftovers.add(stack);
            }
        });

        return leftovers.build();
    }

    public static int getTotalCount(final IInventory inventory, final Predicate<ItemStack> ingredient) {
        final AtomicInteger total = new AtomicInteger();
        new LayeredStreamGenerator<Void>()
            .setBounds(Range.closedOpen(0, inventory.getSizeInventory()))
            .setConsumers((i) -> {
                final ItemStack stack = inventory.getStackInSlot(i[0]);
                if (!stack.isEmpty() && ingredient.test(stack)) {
                    total.addAndGet(stack.getCount());
                }
                return Optional.empty();
            }).apply();
        return total.get();
    }

    public static void consumeItems(final IInventory inventory, final Predicate<ItemStack> ingredient, final int amount) {
        int amountLeft = amount;
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            final ItemStack stack = inventory.getStackInSlot(i);
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
        final ItemStack current = inventory.getStackInSlot(slot);
        if (current.isEmpty()) {
            inventory.setInventorySlotContents(slot, stack);
            return true;
        } else if (canItemsStack(stack, current)) {
            current.grow(stack.getCount());
            return true;
        }
        return false;
    }

    public static boolean isFilledFluidContainer(final ItemStack stack) {
        final Item item = stack.getItem();
        return (item instanceof BucketItem && ((BucketItem) item).getFluid() != Fluids.EMPTY)
                || (item instanceof CanisterItem && !((CanisterItem) item).getFluid(stack).isEmpty());
    }

    public static boolean isEmptyFluidContainer(final ItemStack stack) {
        final Item item = stack.getItem();
        return (item instanceof BucketItem && ((BucketItem) item).getFluid() == Fluids.EMPTY)
                || (item instanceof CanisterItem && ((CanisterItem) item).getFluid(stack).isEmpty());
    }
}
