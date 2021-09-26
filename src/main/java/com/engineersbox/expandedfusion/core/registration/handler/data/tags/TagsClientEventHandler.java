package com.engineersbox.expandedfusion.core.registration.handler.data.tags;

import com.engineersbox.expandedfusion.core.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.event.annotation.modloadingcontext.DataEventHandler;
import com.engineersbox.expandedfusion.core.event.annotation.InternalEventHandler;
import com.engineersbox.expandedfusion.core.event.annotation.Subscriber;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryObjectContext;
import com.engineersbox.expandedfusion.core.registration.registryObject.element.BlockRegistryObject;
import com.engineersbox.expandedfusion.core.registration.registryObject.element.FluidRegistryObject;
import com.engineersbox.expandedfusion.core.registration.registryObject.element.ItemRegistryObject;
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
import org.apache.commons.lang3.tuple.Pair;
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
                final Map<ITag.INamedTag<Block>, Pair<Set<String>, Boolean>> tagsToBeRegistered = RegistryObjectContext.getBlockTagsToBeRegistered();
                tagsToBeRegistered.forEach((final ITag.INamedTag<Block> blockTag, Pair<Set<String>, Boolean> providerPair) -> {
                    final TagsProvider.Builder<Block> builder = this.getOrCreateBuilder(blockTag);
                    providerPair.getKey().stream()
                            .map(RegistryObjectContext::getBlockRegistryObject)
                            .map(BlockRegistryObject::asBlock)
                            .forEach(builder::add);
                    builder.replace(providerPair.getValue());
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
                final Map<ITag.INamedTag<Item>, Pair<Set<String>, Boolean>> tagsToBeRegistered = RegistryObjectContext.getItemTagsToBeRegistered();
                tagsToBeRegistered.forEach((final ITag.INamedTag<Item> blockTag, Pair<Set<String>, Boolean> providerPair) -> {
                    final TagsProvider.Builder<Item> builder = this.getOrCreateBuilder(blockTag);
                    providerPair.getKey().stream()
                            .map(RegistryObjectContext::getItemRegistryObject)
                            .map(ItemRegistryObject::asItem)
                            .forEach(builder::add);
                    builder.replace(providerPair.getValue());
                });
                RegistryObjectContext.getBlockTagsToBeRegisteredAsItemTags().forEach(this::copyBlockTagsToItemTags);
            }

            @Override
            public String getName() {
                return String.format("[Mod ID: %s] Item Tags", modId);
            }

            @SuppressWarnings("java:S3011")
            private void copyBlockTagsToItemTags(ITag.INamedTag<Block> blockTag, ITag.INamedTag<Item> itemTag) {
                LOGGER.debug("Mirroring {} => {}", blockTag.getName().getPath(), itemTag.getName().getPath());
                final Method method;
                try {
                    method = ItemTagsProvider.class.getDeclaredMethod("copy", ITag.INamedTag.class, ITag.INamedTag.class);
                } catch (final NoSuchMethodException e) {
                    LOGGER.error("Could not find copy method for block -> item tag replication", e);
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
                // TODO: Fix tags creating nested fluids directory inside forge/tags/fluids
                addFluidTagsToProvider(RegistryObjectContext.getSourceFluidTagsToBeRegistered());
                addFluidTagsToProvider(RegistryObjectContext.getFlowingFluidTagsToBeRegistered());
            }

            private void addFluidTagsToProvider(final Map<ITag.INamedTag<Fluid>, Pair<Set<String>, Boolean>> tagsToBeRegistered) {
                tagsToBeRegistered.forEach((final ITag.INamedTag<Fluid> blockTag, Pair<Set<String>, Boolean> providerPair) -> {
                    final TagsProvider.Builder<Fluid> builder = this.getOrCreateBuilder(blockTag);
                    providerPair.getKey().stream()
                            .map(RegistryObjectContext::getSourceFluidRegistryObject)
                            .map(FluidRegistryObject::asFluid)
                            .forEach(builder::add);
                    builder.replace(providerPair.getValue());
                });
            }

            @Override
            public String getName() {
                return String.format("[Mod ID: %s] Fluid Tags", modId);
            }
        });
    }

}
