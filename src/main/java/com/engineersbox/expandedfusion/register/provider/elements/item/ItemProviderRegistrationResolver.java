package com.engineersbox.expandedfusion.register.provider.elements.item;

import com.engineersbox.expandedfusion.register.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.register.provider.RegistryProvider;
import com.engineersbox.expandedfusion.register.provider.grouping.ImplClassGroupings;
import com.engineersbox.expandedfusion.register.provider.grouping.item.ItemImplGrouping;
import com.engineersbox.expandedfusion.register.provider.shim.ItemDeferredRegistryShim;
import com.engineersbox.expandedfusion.register.provider.shim.RegistryShim;
import com.google.inject.Inject;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;

public class ItemProviderRegistrationResolver extends RegistrationResolver {

    private final ImplClassGroupings<ItemImplGrouping> implClassGroupings;
    private final ItemDeferredRegistryShim itemDeferredRegistryShim;
    final RegistryProvider registryProvider;

    @Inject
    public ItemProviderRegistrationResolver(final RegistryProvider registryProvider,
                                            final ImplClassGroupings<ItemImplGrouping> implClassGroupings,
                                            final RegistryShim<Item> itemDeferredRegistryShim) {
        this.registryProvider = registryProvider;
        this.itemDeferredRegistryShim = (ItemDeferredRegistryShim) itemDeferredRegistryShim;
        this.implClassGroupings = implClassGroupings;
        this.implClassGroupings.collectAnnotatedResources();
    }

    @Override
    public void registerAll() {
        this.implClassGroupings.getClassGroupings().forEach(this::registerProviderAnnotatedItem);
    }

    private void registerProviderAnnotatedItem(@Nonnull final String name,
                                               @Nonnull final ItemImplGrouping group) {
        // TODO: Finish this
    }
}
