package com.engineersbox.expandedfusion.register;

import com.engineersbox.expandedfusion.core.util.ColorGetter;
import com.engineersbox.expandedfusion.elements.item.CanisterItem;
import com.engineersbox.expandedfusion.elements.item.EmptyCanisterItem;
import com.engineersbox.expandedfusion.elements.item.NoPlaceBucketItem;
import com.engineersbox.expandedfusion.register.handlers.ItemRegistryObject;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;

import java.util.function.Supplier;

public final class ModItems {
//    static {
//        Metals.registerItems();
//        CraftingItems.register();
//    }

//    public static final ItemRegistryObject<WrenchItem> WRENCH = register("wrench", WrenchItem::new);
    public static final ItemRegistryObject<CanisterItem> CANISTER = register("canister", () ->
            new CanisterItem(new Item.Properties().group(Registration.CREATIVE_TAB_ITEM_GROUP)));
    public static final ItemRegistryObject<EmptyCanisterItem> EMPTY_CANISTER = register("empty_canister", () ->
            new EmptyCanisterItem(new Item.Properties().group(Registration.CREATIVE_TAB_ITEM_GROUP)));

//    public static final ItemRegistryObject<BucketItem> OIL_BUCKET = register("oil_bucket", () ->
//            createBucketItem(() -> ModFluids.OIL));

    private ModItems() {}

    static void register() {}

    @OnlyIn(Dist.CLIENT)
    public static void registerItemColors(final ColorHandlerEvent.Item event) {
        event.getItemColors().register((stack, tintIndex) -> {
            if (tintIndex == 1) {
                return ColorGetter.getColor(CANISTER.get().getFluid(stack).getFluid());
            }
            return 0xFFFFFF;
        }, CANISTER);
    }

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
