package com.engineersbox.expandedfusion.core.registration.anonymous.element;

import com.engineersbox.expandedfusion.core.registration.contexts.Registration;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AnonymousElement {

    public final Map<String, AttributedSupplier<Block>> blockSuppliers;
    public final Map<String, Supplier<Item>> itemSuppliers;
    public final Map<String, Supplier<Fluid>> sourceFluidSuppliers;
    public final Map<String, Supplier<FlowingFluid>> flowingFluidSuppliers;
    public final Map<String, ITag.INamedTag<Block>> blockTags;
    public final Map<String, ITag.INamedTag<Item>> itemTags;

    private AnonymousElement() {
        this.blockSuppliers = new HashMap<>();
        this.itemSuppliers = new HashMap<>();
        this.sourceFluidSuppliers = new HashMap<>();
        this.flowingFluidSuppliers = new HashMap<>();
        this.blockTags = new HashMap<>();
        this.itemTags = new HashMap<>();
    }

    public static class Builder {

        private final AnonymousElement anonymousElement;
        private String name;

        public Builder() {
            anonymousElement = new AnonymousElement();
        }

        public Builder(final String name) {
            this();
            this.name = name;
        }

        public Builder block(final String providerName, final String group, final Supplier<Block> supplier) {
            this.anonymousElement.blockSuppliers.put(providerName, new AttributedSupplier<>(supplier, group));
            return this;
        }

        public Builder block(final String group, final Supplier<Block> supplier, final String suffix) {
            this.anonymousElement.blockSuppliers.put(this.name + suffix, new AttributedSupplier<>(supplier, group));
            return this;
        }

        public Builder block(final String group, final Supplier<Block> supplier, final String prefix, final String suffix) {
            this.anonymousElement.blockSuppliers.put(prefix + this.name + suffix, new AttributedSupplier<>(supplier, group));
            return this;
        }

        public Builder block(final String providerName, final String group, final AbstractBlock.Properties props) {
            this.block(providerName, group, () -> new Block(props));
            return this;
        }

        public Builder block(final String group, final AbstractBlock.Properties props, final String suffix) {
            this.block(this.name + suffix, group, () -> new Block(props));
            return this;
        }

        public Builder block(final String group, final AbstractBlock.Properties props, final String prefix, final String suffix) {
            this.block(prefix + this.name + suffix, group, () -> new Block(props));
            return this;
        }

        public Builder item(final String providerName, final Supplier<Item> supplier) {
            this.anonymousElement.itemSuppliers.put(providerName, supplier);
            return this;
        }

        public Builder item(final Supplier<Item> supplier, final String suffix) {
            this.anonymousElement.itemSuppliers.put(this.name + suffix, supplier);
            return this;
        }

        public Builder item(final Supplier<Item> supplier, final String prefix, final String suffix) {
            this.anonymousElement.itemSuppliers.put(prefix + this.name + suffix, supplier);
            return this;
        }

        public Builder item(final String providerName, final Item.Properties props) {
            this.item(providerName, () -> new Item(props));
            return this;
        }

        public Builder item(final Item.Properties props, final String suffix) {
            this.item(this.name + suffix, () -> new Item(props));
            return this;
        }

        public Builder item(final Item.Properties props, final String prefix, final String suffix) {
            this.item(prefix + this.name + suffix, () -> new Item(props));
            return this;
        }

        public Builder item(final String providerName, final String group) {
            this.item(providerName, () -> new Item(new Item.Properties().group(Registration.getTabGroup(group))));
            return this;
        }

        public Builder item(final String group, final String prefix, final String suffix) {
            this.item(prefix + this.name + suffix, () -> new Item(new Item.Properties().group(Registration.getTabGroup(group))));
            return this;
        }

        public Builder sourceFluid(final String providerName, final Supplier<Fluid> supplier) {
            this.anonymousElement.sourceFluidSuppliers.put(providerName, supplier);
            return this;
        }

        public Builder sourceFluid(final Supplier<Fluid> supplier, final String suffix) {
            this.sourceFluid(this.name + suffix, supplier);
            return this;
        }

        public Builder sourceFluid(final Supplier<Fluid> supplier, final String prefix, final String suffix) {
            this.sourceFluid(prefix + this.name + suffix, supplier);
            return this;
        }

        public Builder flowingFluid(final String providerName, final Supplier<FlowingFluid> supplier) {
            this.anonymousElement.flowingFluidSuppliers.put(providerName, supplier);
            return this;
        }

        public Builder flowingFluid(final Supplier<FlowingFluid> supplier, final String suffix) {
            this.flowingFluid(this.name + suffix, supplier);
            return this;
        }

        public Builder flowingFluid(final Supplier<FlowingFluid> supplier, final String prefix, final String suffix) {
            this.flowingFluid(prefix + this.name + suffix, supplier);
            return this;
        }

        public Builder blockTag(final String providerName, final ITag.INamedTag<Block> tag) {
            this.anonymousElement.blockTags.put(providerName, tag);
            return this;
        }

        public Builder blockTag(final String providerName, final ResourceLocation tag) {
            return this.blockTag(providerName, BlockTags.makeWrapperTag(tag.toString()));
        }

        public Builder blockTag(final String providerName, final String base, final String tag) {
            return this.blockTag(providerName, new ResourceLocation(base, tag));
        }

        public Builder blockTag(final String providerName, final String tag) {
            return this.blockTag(providerName, "forge", tag);
        }

        public Builder itemTag(final String providerName, final ITag.INamedTag<Item> tag) {
            this.anonymousElement.itemTags.put(providerName, tag);
            return this;
        }

        public Builder itemTag(final String providerName, final ResourceLocation tag) {
            return this.itemTag(providerName, ItemTags.makeWrapperTag(tag.toString()));
        }

        public Builder itemTag(final String providerName, final String base, final String tag) {
            return this.itemTag(providerName, new ResourceLocation(base, tag));
        }

        public Builder itemTag(final String providerName, final String tag) {
            return this.itemTag(providerName, "forge", tag);
        }

        public AnonymousElement build() {
            return this.anonymousElement;
        }
    }
}
