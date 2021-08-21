package com.engineersbox.expandedfusion.core.registration.provider;

import com.engineersbox.expandedfusion.core.registration.registryObject.*;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.block.BlockImplGrouping;
import com.google.inject.Singleton;
import net.minecraft.block.Block;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class RegistryProvider {
    public final Map<String, BlockRegistryObject<? extends Block>> blocks = new HashMap<>();
    public final Map<String, TileEntityRegistryObject<? extends TileEntity>> tileEntities = new HashMap<>();
    public final Map<String, ContainerRegistryObject<? extends Container>> containers = new HashMap<>();
    public final Map<String, BlockImplGrouping> screensToBeRegistered = new HashMap<>();
    public final Map<String, FluidRegistryObject<? extends Fluid>> sourceFluids = new HashMap<>();
    public final Map<String, FluidRegistryObject<? extends FlowingFluid>> flowingFluids = new HashMap<>();
    public final Map<String, ItemRegistryObject<? extends Item>> items = new HashMap<>();
}
