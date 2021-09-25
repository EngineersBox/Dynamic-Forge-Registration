package com.engineersbox.expandedfusion.core.registration.contexts.provider;

import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplGrouping;
import com.engineersbox.expandedfusion.core.registration.registryObject.element.*;
import com.google.inject.Singleton;
import net.minecraft.block.Block;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;

import java.util.HashMap;
import java.util.Map;

@Singleton
public final class ElementRegistryProvider {
    public final Map<String, BlockRegistryObject<? extends Block>> blocks = new HashMap<>();
    public final Map<String, TileEntityRegistryObject<? extends TileEntity>> tileEntities = new HashMap<>();
    public final Map<String, ContainerRegistryObject<? extends Container>> containers = new HashMap<>();
    public final Map<String, BlockImplGrouping> screensToBeRegistered = new HashMap<>();
    public final Map<String, BlockImplGrouping> renderersToBeRegistered = new HashMap<>();
    public final Map<String, FluidRegistryObject<? extends Fluid>> sourceFluids = new HashMap<>();
    public final Map<String, FluidRegistryObject<? extends FlowingFluid>> flowingFluids = new HashMap<>();
    public final Map<String, ItemRegistryObject<? extends Item>> items = new HashMap<>();
}
