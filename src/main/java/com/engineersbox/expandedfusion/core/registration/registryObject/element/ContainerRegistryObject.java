package com.engineersbox.expandedfusion.core.registration.registryObject.element;


import com.engineersbox.expandedfusion.core.elements.container.IContainerProvider;
import com.engineersbox.expandedfusion.core.registration.registryObject.RegistryObjectWrapper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;

public class ContainerRegistryObject<T extends Container> extends RegistryObjectWrapper<ContainerType<T>> implements IContainerProvider<T> {

    public ContainerRegistryObject(final RegistryObject<ContainerType<T>> container) {
        super(container);
    }

    @Override
    public ContainerType<T> asContainerType() {
        return this.registryObject.get();
    }

}
