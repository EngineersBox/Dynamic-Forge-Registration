package com.engineersbox.expandedfusion.core.registration.provider.service.element;

import com.engineersbox.expandedfusion.core.registration.provider.service.RegistryService;
import com.engineersbox.expandedfusion.core.registration.registryObject.element.ContainerRegistryObject;
import com.engineersbox.expandedfusion.core.registration.contexts.Registration;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

import java.util.function.Supplier;

public class ContainerDeferredRegistryService extends RegistryService<Container> {

    private final Registration registration;

    @Inject
    public ContainerDeferredRegistryService(@Named("modId") final String modID,
                                            final Registration registration) {
        this.modID = modID;
        this.registration = registration;
    }

    public <C extends Container> ContainerRegistryObject<C> register(final String name, final ContainerType.IFactory<C> containerFactory) {
        final ContainerType<C> type = new ContainerType<>(containerFactory);
        return register(name, () -> type);
    }

    public <C extends Container> ContainerRegistryObject<C> register(final String name, final Supplier<ContainerType<C>> containerType) {
        return new ContainerRegistryObject<>(this.registration.getContainerRegister().register(name, containerType));
    }

}
