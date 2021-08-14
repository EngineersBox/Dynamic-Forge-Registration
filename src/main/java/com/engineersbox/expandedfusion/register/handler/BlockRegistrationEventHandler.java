package com.engineersbox.expandedfusion.register.handler;

import com.engineersbox.expandedfusion.core.common.machine.AbstractMachineBlock;
import com.engineersbox.expandedfusion.register.Registration;
import com.engineersbox.expandedfusion.register.contexts.RegistryInjectionContext;
import com.engineersbox.expandedfusion.register.provider.grouping.block.BlockImplGrouping;
import com.engineersbox.expandedfusion.register.resolver.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.register.resolver.event.annotation.Subscriber;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;

public class BlockRegistrationEventHandler implements EventSubscriptionHandler {

    @SuppressWarnings("unchecked, unused")
    @OnlyIn(Dist.CLIENT)
    @Subscriber
    public <T extends Container, U extends Screen & IHasContainer<T>> void registerScreens(final FMLClientSetupEvent event) {
        RegistryInjectionContext.getScreensToBeRegistered().forEach((final String name, final BlockImplGrouping group) -> {
            final ContainerType<T> containerType = (ContainerType<T>) RegistryInjectionContext.getContainerType(name);
            final Class<? extends ContainerScreen<? extends T>> screen = (Class<? extends ContainerScreen<? extends T>>) group.getScreen();
            if (screen == null) {
                throw new RuntimeException(); // TODO: Implement an exception for this
            }
            final ScreenManager.IScreenFactory<T, U> factory = (final T container,
                                                                final PlayerInventory playerInventory,
                                                                final ITextComponent titleIn) ->
                    (U) instantiateScreenWithIScreenFactoryParams(container, playerInventory, titleIn, screen);
            ScreenManager.registerFactory(containerType, factory);
        });
    }

    @SuppressWarnings("unchecked")
    private <T extends Container> ContainerScreen<T> instantiateScreenWithIScreenFactoryParams(final T container,
                                                                                               final PlayerInventory playerInventory,
                                                                                               final ITextComponent titleIn,
                                                                                               final Class<? extends ContainerScreen<? extends T>> screen) {
        final Set<Constructor> constructors = ReflectionUtils.getConstructors(
                screen,
                (c) -> {
                    final Class<?>[] paramTypes = c.getParameterTypes();
                    if (paramTypes.length != 3) {
                        return false;
                    }
                    return Container.class.isAssignableFrom(paramTypes[0])
                            && PlayerInventory.class.isAssignableFrom(paramTypes[1])
                            && ITextComponent.class.isAssignableFrom(paramTypes[2]);
                }
        );
        if (constructors.size() < 1) {
            throw new RuntimeException(); // TODO: Implement an exception for this
        }
        try {
            return (ContainerScreen<T>) new ArrayList<>(constructors).get(0).newInstance(container, playerInventory, titleIn);
        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e); // TODO: Implement an exception for this
        }
    }

    @SuppressWarnings("unused")
    @OnlyIn(Dist.CLIENT)
    @Subscriber
    public static void registerRenderTypes(final FMLClientSetupEvent event) {
        Registration.getBlocks(AbstractMachineBlock.class).forEach(block ->
                RenderTypeLookup.setRenderLayer(block, RenderType.getTranslucent()));
    }

}
