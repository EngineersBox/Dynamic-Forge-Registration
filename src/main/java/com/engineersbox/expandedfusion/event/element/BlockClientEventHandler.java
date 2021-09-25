package com.engineersbox.expandedfusion.event.element;

import com.engineersbox.expandedfusion.core.elements.machine.AbstractMachineBlock;
import com.engineersbox.expandedfusion.core.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.event.annotation.modloadingcontext.ClientEventHandler;
import com.engineersbox.expandedfusion.core.event.annotation.Subscriber;
import com.engineersbox.expandedfusion.core.registration.contexts.provider.ElementRegistryProvider;
import com.engineersbox.expandedfusion.core.registration.registryObject.RegistryObjectWrapper;
import com.google.inject.Inject;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@ClientEventHandler
public class BlockClientEventHandler implements EventSubscriptionHandler {

    private final Set<? extends Block> machineBlocks;

    @Inject
    public BlockClientEventHandler(final ElementRegistryProvider elementRegistryProvider) {
        this.machineBlocks = elementRegistryProvider.blocks.values()
                .stream()
                .map(RegistryObjectWrapper::get)
                .filter(AbstractMachineBlock.class::isInstance)
                .collect(Collectors.toSet());
    }

    @SuppressWarnings("unused")
    @Subscriber
    public void registerRenderTypes(final FMLClientSetupEvent event) {
        this.machineBlocks.forEach((final Block block) ->
                RenderTypeLookup.setRenderLayer(block, RenderType.getTranslucent()));
    }

}
