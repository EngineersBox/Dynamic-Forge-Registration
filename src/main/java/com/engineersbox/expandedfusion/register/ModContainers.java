package com.engineersbox.expandedfusion.register;

import com.engineersbox.expandedfusion.ExpandedFusion;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {
//    public static ContainerType<FusionControlComputerContainer> FUSION_CONTROL_COMPUTER;

    private ModContainers() {}

    public static void registerAll(final RegistryEvent.Register<ContainerType<?>> event) {
//        FUSION_CONTROL_COMPUTER = register("fusion_control_computer", FusionControlComputerContainer::new);
    }

    public static <C extends Container> ContainerType<C> register(final String name, final ContainerType.IFactory<C> containerFactory) {
        final ContainerType<C> type = new ContainerType<>(containerFactory);
        return register(name, type);
    }

    public static <C extends Container> ContainerType<C> register(final String name, final ContainerType<C> containerType) {
        containerType.setRegistryName(ExpandedFusion.getId(name));
        ForgeRegistries.CONTAINERS.register(containerType);
        return containerType;
    }
}
