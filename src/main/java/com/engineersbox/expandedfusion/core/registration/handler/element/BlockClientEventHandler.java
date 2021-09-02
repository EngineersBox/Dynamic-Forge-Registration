package com.engineersbox.expandedfusion.core.registration.handler.element;

import com.engineersbox.expandedfusion.core.elements.machine.AbstractMachineBlock;
import com.engineersbox.expandedfusion.core.event.annotation.ClientEventHandler;
import com.engineersbox.expandedfusion.core.reflection.CheckedInstantiator;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.RendererProvider;
import com.engineersbox.expandedfusion.core.registration.exception.handler.SubscriptionEventHandlerException;
import com.engineersbox.expandedfusion.core.registration.contexts.Registration;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryObjectContext;
import com.engineersbox.expandedfusion.core.registration.exception.provider.element.ProviderElementRegistrationException;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplGrouping;
import com.engineersbox.expandedfusion.core.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.event.annotation.Subscriber;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;

@ClientEventHandler
public class BlockClientEventHandler implements EventSubscriptionHandler {

    @SuppressWarnings("unchecked, unused")
    @Subscriber
    public <T extends Container, U extends Screen & IHasContainer<T>> void registerScreens(final FMLClientSetupEvent event) {
        RegistryObjectContext.getScreensToBeRegistered().forEach((final String name, final BlockImplGrouping group) -> {
            final ContainerType<T> containerType = (ContainerType<T>) RegistryObjectContext.getContainerRegistryObject(name).asContainerType();
            final Class<? extends ContainerScreen<? extends T>> screen = (Class<? extends ContainerScreen<? extends T>>) group.getScreen();
            if (screen == null) {
                throw new SubscriptionEventHandlerException(String.format(
                        "Tile entity %s has no bound container screen",
                        name
                ));
            }
            final ScreenManager.IScreenFactory<T, U> factory = (final T container,
                                                                final PlayerInventory playerInventory,
                                                                final ITextComponent titleIn) ->
                    (U) instantiateScreenWithIScreenFactoryParams(container, playerInventory, titleIn, screen);
            ScreenManager.registerFactory(containerType, factory);
        });
    }

    @SuppressWarnings("unchecked,rawtypes")
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
        if (constructors.isEmpty()) {
            throw new SubscriptionEventHandlerException(String.format(
                    "No accessible constructors could be found for %s",
                    screen
            ));
        }
        try {
            return (ContainerScreen<T>) new ArrayList<>(constructors).get(0).newInstance(container, playerInventory, titleIn);
        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new SubscriptionEventHandlerException(e);
        }
    }

    @SuppressWarnings("unused")
    @Subscriber
    public void registerRenderTypes(final FMLClientSetupEvent event) {
        Registration.getBlocks(AbstractMachineBlock.class).forEach(block ->
                RenderTypeLookup.setRenderLayer(block, RenderType.getTranslucent()));
    }

    @SuppressWarnings("unused,rawtypes")
    @Subscriber
    public void registerTileEntityRenderers(final FMLClientSetupEvent event) {
        RegistryObjectContext.getRenderersToBeRegistered().forEach((final String name, final BlockImplGrouping group) -> {
            final RendererProvider rendererProvider = group.getRendererProviderAnnotation();
            if (rendererProvider == null) {
                throw new ProviderElementRegistrationException(String.format(
                        "Tile entity renderer implementation %s has no plausible annotation",
                        name
                ));
            }
            if (!rendererProvider.name().equals(name)) {
                throw new ProviderElementRegistrationException(String.format(
                        "Mismatched provider element name against annotation: %s != %s",
                        name,
                        rendererProvider.name()
                ));
            }
            final Class<? extends TileEntityRenderer> rendererImpl = group.getRenderer();
            if (rendererImpl == null) {
                throw new ProviderElementRegistrationException(String.format(
                        "No tile entity renderer implementation could be found with associated annotation: %s",
                        name
                ));
            }
            ClientRegistry.bindTileEntityRenderer(
                    RegistryObjectContext.getTileEntityRegistryObject(name).asTileEntityType(),
                    (final TileEntityRendererDispatcher dispatcher) -> {
                        try {
                            return new CheckedInstantiator<TileEntityRenderer<? super TileEntity>>()
                                    .withParameterTypes(TileEntityRendererDispatcher.class)
                                    .withParameters(dispatcher)
                                    .newInstance();
                        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException e) {
                            throw new ProviderElementRegistrationException(e);
                        }
                    }
            );
        });
    }
}
