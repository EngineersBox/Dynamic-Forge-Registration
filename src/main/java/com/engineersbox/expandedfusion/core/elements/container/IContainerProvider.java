package com.engineersbox.expandedfusion.core.elements.container;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

public interface IContainerProvider<T extends Container> {

    ContainerType<T> asContainerType();

}
