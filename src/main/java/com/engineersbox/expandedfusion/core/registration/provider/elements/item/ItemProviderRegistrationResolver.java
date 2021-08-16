package com.engineersbox.expandedfusion.core.registration.provider.elements.item;

import com.engineersbox.expandedfusion.core.registration.annotation.provider.item.ItemProvider;
import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.RegistryProvider;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplClassGroupings;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.item.ItemImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.shim.ItemDeferredRegistryShim;
import com.engineersbox.expandedfusion.core.registration.provider.shim.RegistryShim;
import com.google.inject.Inject;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

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
        final ItemProvider itemProvider = group.getItemProviderAnnotation();
        if (itemProvider == null) {
            throw new RuntimeException(); // TODO: Implement exception for this
        }
        if (!itemProvider.name().equals(name)) {
            throw new RuntimeException(); // TODO: Implement exception for this
        }
        final Class<? extends Item> itemImpl = group.getItem();
        if (itemImpl == null) {
            throw new RuntimeException(); // TODO: Implement exception for this
        }
        registerItem(name, itemImpl);
    }

    private void registerItem(final String name,
                              final Class<? extends Item> itemImpl) {
        final Supplier<Item> itemSupplier = () -> super.<Item>instantiateWithDefaultConstructor(itemImpl);
        this.registryProvider.items.put(name, this.itemDeferredRegistryShim.register(name, itemSupplier));
    }
}
