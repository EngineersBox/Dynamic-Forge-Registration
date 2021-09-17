package com.engineersbox.expandedfusion.core.registration.provider.element;

import com.engineersbox.expandedfusion.core.registration.annotation.element.item.ItemProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.resolver.RegistrationPhaseHandler;
import com.engineersbox.expandedfusion.core.registration.contexts.provider.ElementRegistryProvider;
import com.engineersbox.expandedfusion.core.registration.exception.provider.element.ProviderElementRegistrationException;
import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplClassGroupings;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.item.ItemImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.item.ItemImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.shim.element.ItemDeferredRegistryShim;
import com.engineersbox.expandedfusion.core.registration.provider.shim.RegistryShim;
import com.engineersbox.expandedfusion.core.registration.resolver.ResolverPhase;
import com.google.inject.Inject;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@RegistrationPhaseHandler(ResolverPhase.ITEM)
public class ItemProviderRegistrationResolver extends RegistrationResolver {

    private final ItemImplClassGrouping implClassGroupings;
    private final ItemDeferredRegistryShim itemDeferredRegistryShim;
    final ElementRegistryProvider elementRegistryProvider;

    @Inject
    public ItemProviderRegistrationResolver(final ElementRegistryProvider elementRegistryProvider,
                                            final ImplClassGroupings<ItemImplGrouping> implClassGroupings,
                                            final RegistryShim<Item> itemDeferredRegistryShim) {
        this.elementRegistryProvider = elementRegistryProvider;
        this.itemDeferredRegistryShim = (ItemDeferredRegistryShim) itemDeferredRegistryShim;
        this.implClassGroupings = (ItemImplClassGrouping) implClassGroupings;
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
            throw new ProviderElementRegistrationException(String.format(
                    "Item implementation %s has no plausible annotation",
                    name
            ));
        }
        if (!itemProvider.name().equals(name)) {
            throw new ProviderElementRegistrationException(String.format(
                    "Mismatched provider element name against annotation: %s != %s",
                    name,
                    itemProvider.name()
            ));
        }
        final Class<? extends Item> itemImpl = group.getItem();
        if (itemImpl == null) {
            throw new ProviderElementRegistrationException(String.format(
                    "No item implementation could be found with associated annotation: %s",
                    name
            ));
        }
        registerItem(name, itemImpl);
    }

    private void registerItem(final String name,
                              final Class<? extends Item> itemImpl) {
        final Supplier<Item> itemSupplier = () -> super.<Item>instantiateWithDefaultConstructor(itemImpl);
        this.elementRegistryProvider.items.put(name, this.itemDeferredRegistryShim.register(name, itemSupplier));
    }
}
