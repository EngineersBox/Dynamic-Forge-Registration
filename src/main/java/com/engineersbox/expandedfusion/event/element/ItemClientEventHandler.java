package com.engineersbox.expandedfusion.event.element;

import com.engineersbox.expandedfusion.core.event.annotation.modloadingcontext.ClientEventHandler;
import com.engineersbox.expandedfusion.core.util.ColorGetter;
import com.engineersbox.expandedfusion.elements.item.CanisterItem;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryObjectContext;
import com.engineersbox.expandedfusion.core.registration.registryObject.element.ItemRegistryObject;
import com.engineersbox.expandedfusion.core.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.event.annotation.Subscriber;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ColorHandlerEvent;

@SuppressWarnings("unused")
@ClientEventHandler
public class ItemClientEventHandler implements EventSubscriptionHandler {

    @SuppressWarnings({"unchecked","unused"})
    @Subscriber
    public static void registerItemColors(final ColorHandlerEvent.Item event) {
        final ItemRegistryObject<? extends Item> potentialCanister = RegistryObjectContext.getItemRegistryObject(CanisterItem.PROVIDER_NAME);
        if (!(potentialCanister.asItem() instanceof CanisterItem)) {
            return;
        }
        final ItemRegistryObject<CanisterItem> canister = (ItemRegistryObject<CanisterItem>) potentialCanister;
        event.getItemColors().register((final ItemStack stack, final int tintIndex) -> {
            if (tintIndex == 1) {
                return ColorGetter.getColor(canister.get().getFluid(stack).getFluid());
            }
            return 0xFFFFFF;
        }, canister);
    }

}
