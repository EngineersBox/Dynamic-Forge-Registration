package com.engineersbox.expandedfusion.core.registration.handler.data.tags;

import com.engineersbox.expandedfusion.core.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.event.annotation.DataEventHandler;
import com.engineersbox.expandedfusion.core.event.annotation.InternalEventHandler;
import com.engineersbox.expandedfusion.core.event.annotation.Subscriber;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryObjectContext;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.FluidTagsProvider;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.data.TagsProvider;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

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

            @Override
            protected void registerTags() {
                final Map<ITag.INamedTag<Block>, Set<String>> tagsToBeRegistered = RegistryObjectContext.getBlockTagsToBeRegistered();
                tagsToBeRegistered.forEach((final ITag.INamedTag<Block> blockTag, final Set<String> providerNames) -> {
                    final TagsProvider.Builder<Block> builder = this.getOrCreateBuilder(blockTag);
                    providerNames.forEach((final String providerName) -> builder.add(RegistryObjectContext.getBlockRegistryObject(providerName).asBlock()));
                });
            }

            @Override
            public String getName() {
                return String.format("[Mod ID: %s] Block Tags", modId);
            }
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

            @Override
            protected void registerTags() {
                final Map<ITag.INamedTag<Item>, Set<String>> tagsToBeRegistered = RegistryObjectContext.getItemTagsToBeRegistered();
                tagsToBeRegistered.forEach((final ITag.INamedTag<Item> blockTag, final Set<String> providerNames) -> {
                    final TagsProvider.Builder<Item> builder = this.getOrCreateBuilder(blockTag);
                    providerNames.forEach((final String providerName) -> builder.add(RegistryObjectContext.getItemRegistryObject(providerName).asItem()));
                });
                RegistryObjectContext.getBlockTagsToBeRegisteredAsItemTags().forEach(this::copyBlockTagsToItemTags);
            }

            @Override
            public String getName() {
                return String.format("[Mod ID: %s] Item Tags", modId);
            }

            @SuppressWarnings("java:S3011")
            private void copyBlockTagsToItemTags(ITag.INamedTag<Block> blockTag, ITag.INamedTag<Item> itemTag) {
                LOGGER.warn("{} => {}", blockTag.getName().getPath(), itemTag.getName().getPath());
                final Method method;
                try {
                    method = ItemTagsProvider.class.getDeclaredMethod("copy", ITag.INamedTag.class, ITag.INamedTag.class);
                } catch (final NoSuchMethodException e) {
                    LOGGER.error("Could not find copy method for block -> item tag replication" ,e);
                    return;
                }
                method.setAccessible(true);
                try {
                    method.invoke(this, blockTag, itemTag);
                } catch (final InvocationTargetException | IllegalAccessException e) {
                    LOGGER.error("Could not invoke copy method for block -> item tag replication", e);
                }
            }
        });
    }

    @Subscriber
    public void registerFluidTags(final GatherDataEvent gatherEvent) {
        gatherEvent.getGenerator().addProvider(new FluidTagsProvider(gatherEvent.getGenerator(), this.modId, gatherEvent.getExistingFileHelper()){

            @Override
            protected void registerTags() {
                Map<ITag.INamedTag<Fluid>, Set<String>> tagsToBeRegistered = RegistryObjectContext.getSourceFluidTagsToBeRegistered();
                tagsToBeRegistered.forEach((final ITag.INamedTag<Fluid> blockTag, final Set<String> providerNames) -> {
                    final TagsProvider.Builder<Fluid> builder = this.getOrCreateBuilder(blockTag);
                    providerNames.forEach((final String providerName) -> builder.add(RegistryObjectContext.getSourceFluidRegistryObject(providerName).asFluid()));
                });
                tagsToBeRegistered = RegistryObjectContext.getFlowingFluidTagsToBeRegistered();
                tagsToBeRegistered.forEach((final ITag.INamedTag<Fluid> blockTag, final Set<String> providerNames) -> {
                    final TagsProvider.Builder<Fluid> builder = this.getOrCreateBuilder(blockTag);
                    providerNames.forEach((final String providerName) -> builder.add(RegistryObjectContext.getFlowingFluidRegistryObject(providerName).asFluid()));
                });
            }

            @Override
            public String getName() {
                return String.format("[Mod ID: %s] Fluid Tags", modId);
            }
        });
    }

}
