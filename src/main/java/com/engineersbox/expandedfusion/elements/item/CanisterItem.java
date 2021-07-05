package com.engineersbox.expandedfusion.elements.item;

import com.engineersbox.expandedfusion.core.api.IFluidContainer;
import com.engineersbox.expandedfusion.core.util.TextUtil;
import com.engineersbox.expandedfusion.register.ModItems;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;

public class CanisterItem extends Item implements IFluidContainer {
    public CanisterItem(final Properties properties) {
        super(properties);
        //addPropertyOverride(SilentMechanisms.getId("fluid_level"), (stack, world, entity) -> getFluid(stack).getAmount());
    }

    public static ItemStack getStack(@Nullable final Fluid fluid) {
        return getStack(fluid, 1);
    }

    public static ItemStack getStack(@Nullable final Fluid fluid, final int count) {
        IItemProvider item = fluid != null ? ModItems.CANISTER : ModItems.EMPTY_CANISTER;
        ItemStack result = new ItemStack(item, count);
        if (fluid != null) {
            ResourceLocation fluidId = Objects.requireNonNull(fluid.getRegistryName());
            result.getOrCreateTag().putString("CanisterFluid", fluidId.toString());
        }
        return result;
    }

/*    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new FluidCanisterWrapper(stack);
    }*/

    public static String getFluidKey(final ItemStack stack) {
        return stack.hasTag() ? stack.getOrCreateTag().getString("CanisterFluid") : "";
    }

    @Override
    public FluidStack getFluid(final ItemStack stack) {
        if (!(stack.getItem() instanceof CanisterItem)) {
            return FluidStack.EMPTY;
        }
        ResourceLocation fluidId = ResourceLocation.tryCreate(getFluidKey(stack));
        if (fluidId == null) {
            return FluidStack.EMPTY;
        }
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidId);
        if (fluid == null) {
            return FluidStack.EMPTY;
        }
        return new FluidStack(fluid, 1000);
    }

    @Override
    public ItemStack fillWithFluid(final ItemStack empty, final FluidStack fluid) {
        return getStack(fluid.getFluid());
    }

    @Override
    public ITextComponent getDisplayName(final ItemStack stack) {
        FluidStack fluid = getFluid(stack);
        ITextComponent fluidText = fluid.isEmpty() ? TextUtil.translate("misc", "empty") : fluid.getDisplayName();
        return new TranslationTextComponent(this.getTranslationKey(), fluidText);
    }

    @Override
    public boolean hasContainerItem(final ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(final ItemStack itemStack) {
        return new ItemStack(ModItems.EMPTY_CANISTER);
    }

    @Override
    public void fillItemGroup(final ItemGroup group, final NonNullList<ItemStack> items) {
        if (isInGroup(group)) {
            items.add(getStack(null));
            ForgeRegistries.FLUIDS.getValues().stream()
                    .filter(f -> f.isSource(f.getDefaultState()))
                    .map(CanisterItem::getStack)
                    .forEach(items::add);
        }
    }
}
