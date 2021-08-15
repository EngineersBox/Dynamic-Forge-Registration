package com.engineersbox.expandedfusion.register.provider.shim;

import com.engineersbox.expandedfusion.elements.item.NoPlaceBucketItem;
import com.engineersbox.expandedfusion.register.Registration;
import com.engineersbox.expandedfusion.register.registry.ItemRegistryObject;
import com.google.inject.Inject;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.function.Supplier;

public class ItemDeferredRegistryShim extends RegistryShim<Item> {

    @Inject
    public ItemDeferredRegistryShim(final String modID) {
        this.modID = modID;
    }

    public BucketItem createBucketItem(final Supplier<FlowingFluid> fluid) {
        return new BucketItem(fluid, new Item.Properties().group(Registration.CREATIVE_TAB_ITEM_GROUP).maxStackSize(1).containerItem(Items.BUCKET));
    }

    public NoPlaceBucketItem createNoPlaceBucketItem(final Supplier<Fluid> fluid) {
        return new NoPlaceBucketItem(fluid, new Item.Properties().group(Registration.CREATIVE_TAB_ITEM_GROUP).maxStackSize(1).containerItem(Items.BUCKET));
    }

    public ItemRegistryObject<? extends Item> register(final String name, final Supplier<? extends Item> item) {
        return new ItemRegistryObject<>(Registration.ITEMS.register(name, item));
    }

}
