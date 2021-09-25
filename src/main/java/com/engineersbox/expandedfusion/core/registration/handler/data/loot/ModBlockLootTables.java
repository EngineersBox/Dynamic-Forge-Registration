package com.engineersbox.expandedfusion.core.registration.handler.data.loot;

import com.engineersbox.expandedfusion.core.registration.annotation.data.loot.NoDefaultLootItem;
import com.engineersbox.expandedfusion.core.registration.contexts.provider.ElementRegistryProvider;
import com.engineersbox.expandedfusion.core.registration.registryObject.RegistryObjectWrapper;
import com.engineersbox.expandedfusion.core.registration.registryObject.element.BlockRegistryObject;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ModBlockLootTables extends BlockLootTables {

    private final ElementRegistryProvider elementRegistryProvider;

    public ModBlockLootTables(final ElementRegistryProvider elementRegistryProvider) {
        this.elementRegistryProvider = elementRegistryProvider;
    }

    private Set<? extends Block> getNonFluidBlocks() {
        return this.elementRegistryProvider.blocks.entrySet()
                .stream()
                .filter((final Map.Entry<String, BlockRegistryObject<? extends Block>> entry) ->
                        !this.elementRegistryProvider.sourceFluids.containsKey(entry.getKey())
                        && !this.elementRegistryProvider.flowingFluids.containsKey(entry.getKey()))
                .map(Map.Entry::getValue)
                .map(RegistryObjectWrapper::get)
                .collect(Collectors.toSet());
    }

    @Override
    protected void addTables() {
        final Set<? extends Block> blocks = getNonFluidBlocks();
        blocks.stream()
                .filter(block -> block.asItem() != Items.AIR && !block.getClass().isAnnotationPresent(NoDefaultLootItem.class))
                .forEach(this::registerDropSelfLootTable);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return new ArrayList<>(getNonFluidBlocks());
    }

}
