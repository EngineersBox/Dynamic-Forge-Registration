package com.engineersbox.expandedfusion.register;

import com.engineersbox.expandedfusion.elements.item.CanisterItem;
import com.engineersbox.expandedfusion.elements.item.EmptyCanisterItem;
import com.engineersbox.expandedfusion.elements.item.NoPlaceBucketItem;
import com.engineersbox.expandedfusion.core.registration.registryObject.ItemRegistryObject;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.function.Supplier;

public final class ModItems {
//    static {
//        Metals.registerItems();
//        CraftingItems.register();
//    }

//    public static final ItemRegistryObject<WrenchItem> WRENCH = register("wrench", WrenchItem::new);
//    public static final ItemRegistryObject<CanisterItem> CANISTER = register("canister", CanisterItem::new);
//    public static final ItemRegistryObject<EmptyCanisterItem> EMPTY_CANISTER = register("empty_canister", EmptyCanisterItem::new);

//    public static final ItemRegistryObject<BucketItem> OIL_BUCKET = register("oil_bucket", () ->
//            createBucketItem(() -> ModFluids.OIL));

    private static BucketItem createBucketItem(final Supplier<FlowingFluid> fluid) {
        return new BucketItem(fluid, new Item.Properties().group(Registration.CREATIVE_TAB_ITEM_GROUP).maxStackSize(1).containerItem(Items.BUCKET));
    }

    private static NoPlaceBucketItem createNoPlaceBucketItem(final Supplier<Fluid> fluid) {
        return new NoPlaceBucketItem(fluid, new Item.Properties().group(Registration.CREATIVE_TAB_ITEM_GROUP).maxStackSize(1).containerItem(Items.BUCKET));
    }

    private static <T extends Item> ItemRegistryObject<T> register(final String name, final Supplier<T> item) {
        return new ItemRegistryObject<>(Registration.ITEMS.register(name, item));
    }
}
