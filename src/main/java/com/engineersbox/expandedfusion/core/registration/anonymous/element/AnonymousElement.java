package com.engineersbox.expandedfusion.core.registration.anonymous.element;

import com.engineersbox.expandedfusion.core.registration.anonymous.element.builder.BlockBuilder;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.builder.FlowingFluidBuilder;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.builder.ItemBuilder;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.builder.SourceFluidBuilder;
import net.minecraft.block.Block;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class AnonymousElement {

    public final Map<String, AttributedSupplier<Block, Block>> blockSuppliers;
    public final Map<String, AttributedSupplier<Item, Item>> itemSuppliers;
    public final Map<String, AttributedSupplier<Fluid, Fluid>> sourceFluidSuppliers;
    public final Map<String, AttributedSupplier<FlowingFluid, Fluid>> flowingFluidSuppliers;

    private AnonymousElement() {
        this.blockSuppliers = new HashMap<>();
        this.itemSuppliers = new HashMap<>();
        this.sourceFluidSuppliers = new HashMap<>();
        this.flowingFluidSuppliers = new HashMap<>();
    }

    public static class Builder {

        private final AnonymousElement anonymousElement;

        public Builder() {
            anonymousElement = new AnonymousElement();
        }

        public void addBlockSupplier(final String providerName,
                                     final AttributedSupplier<Block, Block> supplier) {
            this.anonymousElement.blockSuppliers.put(providerName, supplier);
        }

        public void addItemSupplier(final String providerName,
                                    final AttributedSupplier<Item, Item> supplier) {
            this.anonymousElement.itemSuppliers.put(providerName, supplier);
        }

        public void addSourceFluidSupplier(final String providerName,
                                           final AttributedSupplier<Fluid, Fluid> supplier) {
            this.anonymousElement.sourceFluidSuppliers.put(providerName, supplier);
        }

        public void addFlowingFluidSupplier(final String providerName,
                                            final AttributedSupplier<FlowingFluid, Fluid> supplier) {
            this.anonymousElement.flowingFluidSuppliers.put(providerName, supplier);
        }

        public BlockBuilder block(final String providerName) {
            return new BlockBuilder(this, providerName);
        }

        public ItemBuilder item(final String providerName) {
            return new ItemBuilder(this, providerName);
        }

        public SourceFluidBuilder sourceFluid(final String providerName) {
            return new SourceFluidBuilder(this, providerName);
        }

        public FlowingFluidBuilder flowingFluid(final String providerName) {
            return new FlowingFluidBuilder(this, providerName);
        }

        public AnonymousElement build() {
            return this.anonymousElement;
        }
    }
}
