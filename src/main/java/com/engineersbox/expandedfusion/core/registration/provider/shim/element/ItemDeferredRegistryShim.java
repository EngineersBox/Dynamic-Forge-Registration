package com.engineersbox.expandedfusion.core.registration.provider.shim.element;

import com.engineersbox.expandedfusion.core.registration.provider.shim.RegistryShim;
import com.engineersbox.expandedfusion.elements.item.NoPlaceBucketItem;
import com.engineersbox.expandedfusion.core.registration.contexts.Registration;
import com.engineersbox.expandedfusion.core.registration.registryObject.element.ItemRegistryObject;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

public class ItemDeferredRegistryShim extends RegistryShim<Item> {

    @Inject
    public ItemDeferredRegistryShim(@Named("modId") final String modID) {
        this.modID = modID;
    }

    public BucketItem createBucketItem(final Supplier<FlowingFluid> fluid,
                                       final String tabGroupName) {
        return new BucketItem(
                fluid,
                new Item.Properties()
                        .group(Registration.getTabGroup(StringUtils.isEmpty(tabGroupName.trim()) ? this.modID : tabGroupName))
                        .maxStackSize(1)
                        .containerItem(Items.BUCKET)
        );
    }

    public NoPlaceBucketItem createNoPlaceBucketItem(final Supplier<Fluid> fluid,
                                                     final String tabGroupName) {
        return new NoPlaceBucketItem(
                fluid,
                new Item.Properties()
                        .group(Registration.getTabGroup(StringUtils.isEmpty(tabGroupName.trim()) ? this.modID : tabGroupName))
                        .maxStackSize(1)
                        .containerItem(Items.BUCKET)
        );
    }

    public ItemRegistryObject<? extends Item> register(final String name, final Supplier<? extends Item> item) {
        return new ItemRegistryObject<>(Registration.ITEMS.register(name, item));
    }

}
