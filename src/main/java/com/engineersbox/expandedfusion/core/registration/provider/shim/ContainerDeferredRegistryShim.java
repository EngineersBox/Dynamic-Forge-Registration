package com.engineersbox.expandedfusion.core.registration.provider.shim;

import com.google.inject.Inject;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerDeferredRegistryShim extends RegistryShim<Container> {

    @Inject
    public ContainerDeferredRegistryShim(final String modID) {
        this.modID = modID;
    }

    public <C extends Container> ContainerType<C> register(final String name, final ContainerType.IFactory<C> containerFactory) {
        final ContainerType<C> type = new ContainerType<>(containerFactory);
        return register(name, type);
    }

    public <C extends Container> ContainerType<C> register(final String name, final ContainerType<C> containerType) {
        containerType.setRegistryName(this.getId(name));
        ForgeRegistries.CONTAINERS.register(containerType);
        return containerType;
    }

}
