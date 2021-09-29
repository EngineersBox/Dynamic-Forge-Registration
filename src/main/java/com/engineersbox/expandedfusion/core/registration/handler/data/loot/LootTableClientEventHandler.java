package com.engineersbox.expandedfusion.core.registration.handler.data.loot;

import com.engineersbox.expandedfusion.core.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.event.annotation.InternalEventHandler;
import com.engineersbox.expandedfusion.core.event.annotation.Subscriber;
import com.engineersbox.expandedfusion.core.event.annotation.modloadingcontext.DataEventHandler;
import com.engineersbox.expandedfusion.core.registration.contexts.provider.ElementRegistryProvider;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import com.mojang.datafixers.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@InternalEventHandler
@DataEventHandler
public class LootTableClientEventHandler implements EventSubscriptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(LootTableClientEventHandler.class);
    private final String modId;
    private final ElementRegistryProvider elementRegistryProvider;

    @Inject
    public LootTableClientEventHandler(@Named("modId") final String modId,
                                       final ElementRegistryProvider elementRegistryProvider) {
        this.modId = modId;
        this.elementRegistryProvider = elementRegistryProvider;
    }

    @Subscriber
    public void createBlockLootTables(final GatherDataEvent gatherEvent) {
        gatherEvent.getGenerator().addProvider(new LootTableProvider(gatherEvent.getGenerator()) {

            @Override
            public String getName() {
                return String.format("[Mod ID: %s] Block Loot Tables", modId);
            }

            @Override
            protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
                return ImmutableList.of(Pair.of(() -> new ModBlockLootTables(elementRegistryProvider), LootParameterSets.BLOCK));
            }

            @Override
            protected void validate(final Map<ResourceLocation, LootTable> resourceMappedTables, final ValidationTracker validationTracker) {
                resourceMappedTables.forEach((final ResourceLocation id, final LootTable table) ->
                        LootTableManager.validateLootTable(validationTracker, id, table));
            }

        });
    }

}
