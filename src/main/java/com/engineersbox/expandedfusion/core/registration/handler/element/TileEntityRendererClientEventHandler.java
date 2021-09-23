package com.engineersbox.expandedfusion.core.registration.handler.element;

import com.engineersbox.expandedfusion.core.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.event.annotation.modloadingcontext.ClientEventHandler;
import com.engineersbox.expandedfusion.core.event.annotation.InternalEventHandler;
import com.engineersbox.expandedfusion.core.event.annotation.Subscriber;
import com.engineersbox.expandedfusion.core.reflection.CheckedInstantiator;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.RendererProvider;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryObjectContext;
import com.engineersbox.expandedfusion.core.registration.exception.provider.element.ProviderElementRegistrationException;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplGrouping;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("unused")
@InternalEventHandler
@ClientEventHandler
public class TileEntityRendererClientEventHandler implements EventSubscriptionHandler {

    @SuppressWarnings({"unused","rawtypes"})
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
