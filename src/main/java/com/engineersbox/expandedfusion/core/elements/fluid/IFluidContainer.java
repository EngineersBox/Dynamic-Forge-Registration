package com.engineersbox.expandedfusion.core.elements.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * An item that contains a fluid
 */
public interface IFluidContainer {
    /**
     * Gets the fluid stored in the item
     *
     * @param stack The fluid container
     * @return The stored fluid
     */
    FluidStack getFluid(final ItemStack stack);

    /**
     * Creates a fluid container filled with the given fluid. Should not modify the empty
     * container.
     *
     * @param empty The empty fluid container
     * @param fluid The fluid to store
     * @return A new filled container
     */
    ItemStack fillWithFluid(final ItemStack empty, final FluidStack fluid);

    /**
     * Convenience method to get the fluid of either a bucket or {@code IFluidContainer}.
     *
     * @param stack The bucket or fluid container item
     * @return The stored fluid, or {@link FluidStack#EMPTY} if the container is empty or not a
     * valid fluid container
     */
    static FluidStack getBucketOrContainerFluid(final ItemStack stack) {
        final Item item = stack.getItem();
        //noinspection ChainOfInstanceofChecks
        if (item instanceof BucketItem) {
            final Fluid fluid = ((BucketItem) item).getFluid();
            return new FluidStack(fluid, 1000);
        }
        if (item instanceof IFluidContainer) {
            return ((IFluidContainer) item).getFluid(stack);
        }
        return FluidStack.EMPTY;
    }

    /**
     * Convenience method to fill either a bucekt or {@code IFluidContainer} with a fluid.
     *
     * @param empty The empty fluid container
     * @param fluid The fluid to store
     * @return A filled fluid container, or {@link ItemStack#EMPTY} if the container is not a valid
     * fluid container
     */
    static ItemStack fillBucketOrFluidContainer(final ItemStack empty, final FluidStack fluid) {
        final Item item = empty.getItem();
        //noinspection ChainOfInstanceofChecks
        if (item instanceof BucketItem) {
            return new ItemStack(fluid.getFluid().getFilledBucket());
        }
        if (item instanceof IFluidContainer) {
            return ((IFluidContainer) item).fillWithFluid(empty, fluid);
        }
        return ItemStack.EMPTY;
    }
}
