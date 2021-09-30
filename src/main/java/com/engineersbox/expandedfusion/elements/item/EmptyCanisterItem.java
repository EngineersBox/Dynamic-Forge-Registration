package com.engineersbox.expandedfusion.elements.item;

import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.engineersbox.expandedfusion.core.registration.annotation.element.item.ItemProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LocaleEntry;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

@LangMetadata(
        locales = @LocaleEntry(key = LangKey.EN_US, mapping = EmptyCanisterItem.NAME_MAPPING)
)
@ItemProvider(
    name = EmptyCanisterItem.PROVIDER_NAME
)
public class EmptyCanisterItem extends CanisterItem {
    public static final String PROVIDER_NAME = "empty_canister";
    public static final String NAME_MAPPING = "Empty Canister";

    public EmptyCanisterItem() {
        super();
    }

    @Override
    public FluidStack getFluid(final ItemStack stack) {
        return FluidStack.EMPTY;
    }

    @Override
    public void fillItemGroup(final ItemGroup group, final NonNullList<ItemStack> items) {
        if (isInGroup(group)) {
            items.add(new ItemStack(this));
        }
    }
}
