package com.engineersbox.expandedfusion.core.registration.handler;

import com.engineersbox.expandedfusion.core.util.ColorGetter;
import com.engineersbox.expandedfusion.elements.item.CanisterItem;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryInjectionContext;
import com.engineersbox.expandedfusion.core.registration.registryObject.ItemRegistryObject;
import com.engineersbox.expandedfusion.core.registration.resolver.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.registration.resolver.event.annotation.Subscriber;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;

public class ItemClientEventHandler implements EventSubscriptionHandler {

    @SuppressWarnings("unchecked,unused")
    @Subscriber
    @OnlyIn(Dist.CLIENT)
    public static void registerItemColors(final ColorHandlerEvent.Item event) {
        final ItemRegistryObject<? extends Item> potentialCanister = RegistryInjectionContext.getItemRegistryObject(CanisterItem.PROVIDER_NAME);
        if (!(potentialCanister.asItem() instanceof CanisterItem)) {
            return;
        }
        final ItemRegistryObject<CanisterItem> canister = (ItemRegistryObject<CanisterItem>) potentialCanister;
        event.getItemColors().register((stack, tintIndex) -> {
            if (tintIndex == 1) {
                return ColorGetter.getColor(canister.get().getFluid(stack).getFluid());
            }
            return 0xFFFFFF;
        }, canister);
    }

}
