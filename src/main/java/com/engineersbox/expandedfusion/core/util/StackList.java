package com.engineersbox.expandedfusion.core.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * ArrayList designed to hold non-empty ItemStacks. Ignores any empty stacks that are added. Has
 * some convenience methods for selecting stacks.
 *
 * @since 3.0.0(?)
 */
public final class StackList extends ArrayList<ItemStack> {
    private StackList() {
    }

    /**
     * Create a StackList of the provided stacks, automatically removing any empty stacks.
     *
     * @param stacks The {@link ItemStack}s, may be empty but not null
     * @return A new list of all non-empty (valid) stacks
     */
    public static StackList of(final ItemStack... stacks) {
        StackList newList = new StackList();
        Collections.addAll(newList, stacks);
        return newList;
    }

    /**
     * Create a StackList from the non-empty (valid) stacks in the provided inventory.
     *
     * @param inventory The {@link IInventory}
     * @return A new list of all non-empty stacks from the inventory
     * @since 3.0.6
     */
    public static StackList from(final IInventory inventory) {
        StackList newList = new StackList();
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            newList.add(inventory.getStackInSlot(i));
        }
        return newList;
    }

    public static StackList from(final Iterable<INBT> tagList) {
        StackList newList = new StackList();
        for (INBT nbt : tagList) {
            if (nbt instanceof CompoundNBT) {
                newList.add(ItemStack.read((CompoundNBT) nbt));
            }
        }
        return newList;
    }

    //region Convenience methods

    public ItemStack firstOfType(final Class<?> itemClass) {
        return firstMatch(itemClassMatcher(itemClass));
    }

    public ItemStack firstMatch(final Predicate<ItemStack> predicate) {
        return stream().filter(predicate).findFirst().orElse(ItemStack.EMPTY);
    }

    public ItemStack uniqueOfType(final Class<?> itemClass) {
        return uniqueMatch(itemClassMatcher(itemClass));
    }

    public ItemStack uniqueMatch(final Predicate<ItemStack> predicate) {
        return stream().filter(predicate).collect(Collectors.collectingAndThen(Collectors.toList(),
                list -> list.size() == 1 ? list.get(0) : ItemStack.EMPTY));
    }

    public Collection<ItemStack> allOfType(final Class<?> itemClass) {
        return allMatches(itemClassMatcher(itemClass));
    }

    public Collection<ItemStack> allMatches(final Predicate<ItemStack> predicate) {
        return stream().filter(predicate).collect(Collectors.toList());
    }

    public int countOfType(final Class<?> itemClass) {
        return countOfMatches(itemClassMatcher(itemClass));
    }

    public int countOfMatches(final Predicate<ItemStack> predicate) {
        return (int) stream().filter(predicate).count();
    }

    private static Predicate<ItemStack> itemClassMatcher(final Class<?> itemClass) {
        return stack -> itemClass.isInstance(stack.getItem());
    }

    //endregion

    //region ArrayList overrides

    @Override
    public boolean add(final ItemStack itemStack) {
        return !itemStack.isEmpty() && super.add(itemStack);
    }

    @Override
    public boolean addAll(final Collection<? extends ItemStack> c) {
        boolean added = false;
        for (ItemStack stack : c) {
            if (!stack.isEmpty()) {
                added |= super.add(stack);
            }
        }
        return added;
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends ItemStack> c) {
        boolean added = false;
        for (ItemStack stack : c) {
            if (!stack.isEmpty()) {
                super.add(index, stack);
                added = true;
            }
        }
        return added;
    }

    @Override
    public void add(final int index, final ItemStack element) {
        if (!element.isEmpty()) {
            super.add(index, element);
        }
    }

    //endregion
}
