package com.engineersbox.expandedfusion.elements.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * A bucket with right-click functionality removed. Used for fluids without blocks.
 */
public class NoPlaceBucketItem extends BucketItem {
    public NoPlaceBucketItem(final Supplier<? extends Fluid> supplier,
                             final Properties builder) {
        super(supplier, builder);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(final World worldIn,
                                                    final PlayerEntity playerIn,
                                                    final Hand handIn) {
        return new ActionResult<>(ActionResultType.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final CompoundNBT nbt) {
        return new FluidBucketWrapper(stack);
    }
}
