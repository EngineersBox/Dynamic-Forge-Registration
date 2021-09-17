package com.engineersbox.expandedfusion.core.registration.provider.shim.element;

import com.engineersbox.expandedfusion.core.registration.provider.shim.RegistryShim;
import com.engineersbox.expandedfusion.core.registration.registryObject.element.ContainerRegistryObject;
import com.engineersbox.expandedfusion.core.registration.contexts.Registration;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

import java.util.function.Supplier;

public class ContainerDeferredRegistryShim extends RegistryShim<Container> {

    private final Registration registration;

    @Inject
    public ContainerDeferredRegistryShim(@Named("modId") final String modID,
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
