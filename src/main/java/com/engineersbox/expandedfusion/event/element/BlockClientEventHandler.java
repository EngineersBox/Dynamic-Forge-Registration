package com.engineersbox.expandedfusion.event.element;

import com.engineersbox.expandedfusion.core.elements.machine.AbstractMachineBlock;
import com.engineersbox.expandedfusion.core.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.event.annotation.modloadingcontext.ClientEventHandler;
import com.engineersbox.expandedfusion.core.event.annotation.Subscriber;
import com.engineersbox.expandedfusion.core.registration.contexts.Registration;
import com.google.inject.Inject;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@SuppressWarnings("unused")
@ClientEventHandler
public class BlockClientEventHandler implements EventSubscriptionHandler {

    private final Registration registration;

    @Inject
    public BlockClientEventHandler(final Registration registration) {
        this.registration = registration;
    }

    @SuppressWarnings("unused")
    @Subscriber
    public void registerRenderTypes(final FMLClientSetupEvent event) {
        this.registration.getBlocksByClass(AbstractMachineBlock.class).forEach(block ->
                RenderTypeLookup.setRenderLayer(block, RenderType.getTranslucent()));
    }

}
