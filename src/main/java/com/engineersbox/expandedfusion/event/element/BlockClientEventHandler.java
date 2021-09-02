package com.engineersbox.expandedfusion.event.element;

import com.engineersbox.expandedfusion.core.elements.machine.AbstractMachineBlock;
import com.engineersbox.expandedfusion.core.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.event.annotation.ClientEventHandler;
import com.engineersbox.expandedfusion.core.event.annotation.Subscriber;
import com.engineersbox.expandedfusion.core.registration.contexts.Registration;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryObjectContext;
import com.engineersbox.expandedfusion.core.registration.exception.handler.SubscriptionEventHandlerException;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplGrouping;
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
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;

@SuppressWarnings("unused")
@ClientEventHandler
public class BlockClientEventHandler implements EventSubscriptionHandler {

    @SuppressWarnings("unused")
    @Subscriber
    public void registerRenderTypes(final FMLClientSetupEvent event) {
        Registration.getBlocks(AbstractMachineBlock.class).forEach(block ->
                RenderTypeLookup.setRenderLayer(block, RenderType.getTranslucent()));
    }

}
