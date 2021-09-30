package com.engineersbox.expandedfusion.elements.item;

import com.engineersbox.expandedfusion.ExpandedFusion;
import com.engineersbox.expandedfusion.core.elements.fluid.IFluidContainer;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LocaleEntry;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryObjectContext;
import com.engineersbox.expandedfusion.core.util.TextUtil;
import com.engineersbox.expandedfusion.core.registration.contexts.Registration;
import com.engineersbox.expandedfusion.core.registration.annotation.element.item.ItemProvider;
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

@LangMetadata(
        locales = @LocaleEntry(key = LangKey.EN_US, mapping = CanisterItem.NAME_MAPPING)
)
@ItemProvider(
    name = CanisterItem.PROVIDER_NAME
)
public class CanisterItem extends Item implements IFluidContainer {
    public static final String PROVIDER_NAME = "canister";
    public static final String NAME_MAPPING = "Canister";

    public CanisterItem() {
        super(new Item.Properties().group(Registration.getTabGroup(ExpandedFusion.MOD_ID)));
    }

    public static ItemStack getStack(@Nullable final Fluid fluid) {
        return getStack(fluid, 1);
    }

    public static ItemStack getStack(@Nullable final Fluid fluid, final int count) {
        final IItemProvider item = RegistryObjectContext.getItemRegistryObject(fluid != null ? CanisterItem.PROVIDER_NAME : EmptyCanisterItem.PROVIDER_NAME);
        final ItemStack result = new ItemStack(item, count);
        if (fluid != null) {
            ResourceLocation fluidId = Objects.requireNonNull(fluid.getRegistryName());
            result.getOrCreateTag().putString("CanisterFluid", fluidId.toString());
        }
        return result;
    }

    public static String getFluidKey(final ItemStack stack) {
        return stack.hasTag() ? stack.getOrCreateTag().getString("CanisterFluid") : null;
    }

    @Override
    public FluidStack getFluid(final ItemStack stack) {
        if (!(stack.getItem() instanceof CanisterItem)) {
            return FluidStack.EMPTY;
        }
        final String fluidKey = getFluidKey(stack);
        if (fluidKey == null) {
            return FluidStack.EMPTY;
        }
        final ResourceLocation fluidId = ResourceLocation.tryCreate(fluidKey);
        if (fluidId == null) {
            return FluidStack.EMPTY;
        }
        final Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidId);
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
        final FluidStack fluid = getFluid(stack);
        final ITextComponent fluidText = fluid.isEmpty() ? TextUtil.translate("misc", "empty") : fluid.getDisplayName();
        return new TranslationTextComponent(this.getTranslationKey(), fluidText);
    }

    @Override
    public boolean hasContainerItem(final ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(final ItemStack itemStack) {
        return new ItemStack(RegistryObjectContext.getItemRegistryObject(EmptyCanisterItem.PROVIDER_NAME));
    }

    @Override
    public void fillItemGroup(final ItemGroup group, final NonNullList<ItemStack> items) {
        if (!isInGroup(group)) {
            return;
        }
        items.add(getStack(null));
        ForgeRegistries.FLUIDS.getValues().stream()
                .filter(f -> f.isSource(f.getDefaultState()))
                .map(CanisterItem::getStack)
                .forEach(items::add);
    }
}
