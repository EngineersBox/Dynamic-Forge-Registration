package com.engineersbox.expandedfusion.core.registration.handler.data.recipe;

import com.engineersbox.expandedfusion.core.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.event.annotation.DataEventHandler;
import com.engineersbox.expandedfusion.core.event.annotation.InternalEventHandler;
import com.engineersbox.expandedfusion.core.event.annotation.Subscriber;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.FluidTagsProvider;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
@InternalEventHandler
@DataEventHandler
public class TagsClientEventHandler implements EventSubscriptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(TagsClientEventHandler.class);
    private final String modId;
    private BlockTagsProvider blockTagsProvider = null;

    @Inject
    public TagsClientEventHandler(@Named("modId") final String modId) {
        this.modId = modId;
    }

    @Subscriber(priority = 0)
    public void registerBlockTags(final GatherDataEvent gatherEvent) {
        this.blockTagsProvider = new BlockTagsProvider(gatherEvent.getGenerator(), modId, gatherEvent.getExistingFileHelper()){
            // TODO: Finish
        };
        gatherEvent.getGenerator().addProvider(this.blockTagsProvider);
    }

    @Subscriber
    public void registerItemTags(final GatherDataEvent gatherEvent) {
        if (this.blockTagsProvider == null) {
            LOGGER.warn("BlockTagsProvider was not instantiated prior to event publishing for item tags registration. Using default instantiation.");
            this.blockTagsProvider = new BlockTagsProvider(gatherEvent.getGenerator(), modId, gatherEvent.getExistingFileHelper());
        }
        gatherEvent.getGenerator().addProvider(new ItemTagsProvider(gatherEvent.getGenerator(), this.blockTagsProvider, this.modId, gatherEvent.getExistingFileHelper()) {
            // TODO: Finish
        });
    }

    @Subscriber
    public void registerFluidTags(final GatherDataEvent gatherEvent) {
        gatherEvent.getGenerator().addProvider(new FluidTagsProvider(gatherEvent.getGenerator(), this.modId, gatherEvent.getExistingFileHelper()){
            // TODO: Finish
        });
    }

}
