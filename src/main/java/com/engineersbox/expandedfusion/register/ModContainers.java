package com.engineersbox.expandedfusion.register;

import com.engineersbox.expandedfusion.ExpandedFusion;
import com.engineersbox.expandedfusion.elements.block.fusionControlComputer.FusionControlComputerContainer;
import com.engineersbox.expandedfusion.elements.block.fusionControlComputer.FusionControlComputerScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {
    public static ContainerType<FusionControlComputerContainer> FUSION_CONTROL_COMPUTER;

    private ModContainers() {}

    public static void registerAll(final RegistryEvent.Register<ContainerType<?>> event) {
        FUSION_CONTROL_COMPUTER = register("fusion_control_computer", FusionControlComputerContainer::new);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerScreens(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(FUSION_CONTROL_COMPUTER, FusionControlComputerScreen::new);
    }

    private static <C extends Container> ContainerType<C> register(final String name, final ContainerType.IFactory<C> containerFactory) {
        ContainerType<C> type = new ContainerType<>(containerFactory);
        return register(name, type);
    }

    private static <C extends Container> ContainerType<C> register(final String name, final ContainerType<C> containerType) {
        containerType.setRegistryName(ExpandedFusion.getId(name));
        ForgeRegistries.CONTAINERS.register(containerType);
        return containerType;
    }
}
