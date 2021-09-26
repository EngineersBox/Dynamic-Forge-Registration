package com.engineersbox.expandedfusion.core.registration.anonymous.element;

import com.engineersbox.expandedfusion.core.registration.contexts.Registration;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

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

        public Builder block(final String providerName,
                             final String group,
                             final Supplier<Block> supplier) {
            this.anonymousElement.blockSuppliers.put(providerName, new AttributedSupplier<Block, Block>().supplier(supplier).tabGroup(group));
            return this;
        }
        public Builder block(final String providerName,
                             final String group,
                             final Supplier<Block> supplier,
                             final ITag.INamedTag<Block> tag) {
            this.anonymousElement.blockSuppliers.put(providerName, new AttributedSupplier<Block, Block>().supplier(supplier).tabGroup(group).tag(tag));
            return this;
        }
        public Builder block(final String providerName,
                             final String group,
                             final Supplier<Block> supplier,
                             final ITag.INamedTag<Block> tag,
                             final ITag.INamedTag<Item> itemTagToMirrorBlockTagTo) {
            this.anonymousElement.blockSuppliers.put(providerName, new AttributedSupplier<Block, Block>().supplier(supplier).tabGroup(group).tag(tag).mirroredTag(itemTagToMirrorBlockTagTo));
            return this;
        }
        public Builder block(final String providerName,
                             final String group,
                             final Supplier<Block> supplier,
                             final ITag.INamedTag<Block> tag,
                             final ResourceLocation itemResourceToMirrorBlockTagTo) {
            this.anonymousElement.blockSuppliers.put(providerName, new AttributedSupplier<Block, Block>().supplier(supplier).tabGroup(group).tag(tag).mirroredTagResource(itemResourceToMirrorBlockTagTo));
            return this;
        }
        public Builder block(final String providerName,
                             final String group,
                             final Supplier<Block> supplier,
                             final ResourceLocation tagResource) {
            this.anonymousElement.blockSuppliers.put(providerName, new AttributedSupplier<Block, Block>().supplier(supplier).tabGroup(group).tagResource(tagResource));
            return this;
        }
        public Builder block(final String providerName,
                             final String group,
                             final Supplier<Block> supplier,
                             final ResourceLocation tagResource,
                             final ITag.INamedTag<Item> itemTagToMirrorBlockTagTo) {
            this.anonymousElement.blockSuppliers.put(providerName, new AttributedSupplier<Block, Block>().supplier(supplier).tabGroup(group).tagResource(tagResource).mirroredTag(itemTagToMirrorBlockTagTo));
            return this;
        }
        public Builder block(final String providerName,
                             final String group,
                             final Supplier<Block> supplier,
                             final ResourceLocation tagResource,
                             final ResourceLocation mirroredTagResource) {
            this.anonymousElement.blockSuppliers.put(providerName, new AttributedSupplier<Block, Block>().supplier(supplier).tabGroup(group).tagResource(tagResource).mirroredTagResource(mirroredTagResource));
            return this;
        }

        public Builder block(final String providerName,
                             final String group,
                             final AbstractBlock.Properties props) {
            return block(providerName, group, () -> new Block(props));
        }
        public Builder noDefaultLootTableBlock(final String providerName,
                                               final String group,
                                               final AbstractBlock.Properties props) {
            return block(providerName, group, () -> new NoDefaultLootTableBlock(props));
        }
        public Builder block(final String providerName,
                             final String group,
                             final AbstractBlock.Properties props,
                             final ITag.INamedTag<Block> tag) {
            return block(providerName, group, () -> new Block(props), tag);
        }
        public Builder noDefaultLootTableBlock(final String providerName,
                                               final String group,
                                               final AbstractBlock.Properties props,
                                               final ITag.INamedTag<Block> tag) {
            return block(providerName, group, () -> new NoDefaultLootTableBlock(props), tag);
        }
        public Builder block(final String providerName,
                             final String group,
                             final AbstractBlock.Properties props,
                             final ITag.INamedTag<Block> tag,
                             final ITag.INamedTag<Item> itemTagToMirrorBlockTagTo) {
            return block(providerName, group, () -> new Block(props), tag, itemTagToMirrorBlockTagTo);
        }
        public Builder noDefaultLootTableBlock(final String providerName,
                                               final String group,
                                               final AbstractBlock.Properties props,
                                               final ITag.INamedTag<Block> tag,
                                               final ITag.INamedTag<Item> itemTagToMirrorBlockTagTo) {
            return block(providerName, group, () -> new NoDefaultLootTableBlock(props), tag, itemTagToMirrorBlockTagTo);
        }
        public Builder block(final String providerName,
                             final String group,
                             final AbstractBlock.Properties props,
                             final ITag.INamedTag<Block> tag,
                             final ResourceLocation mirroredTagResource) {
            return block(providerName, group, () -> new Block(props), tag, mirroredTagResource);
        }
        public Builder noDefaultLootTableBlock(final String providerName,
                                               final String group,
                                               final AbstractBlock.Properties props,
                                               final ITag.INamedTag<Block> tag,
                                               final ResourceLocation mirroredTagResource) {
            return block(providerName, group, () -> new NoDefaultLootTableBlock(props), tag, mirroredTagResource);
        }
        public Builder block(final String providerName,
                             final String group,
                             final AbstractBlock.Properties props,
                             final ResourceLocation tagResource) {
            return block(providerName, group, () -> new Block(props), tagResource);
        }
        public Builder noDefaultLootTableBlock(final String providerName,
                                               final String group,
                                               final AbstractBlock.Properties props,
                                               final ResourceLocation tagResource) {
            return block(providerName, group, () -> new NoDefaultLootTableBlock(props), tagResource);
        }
        public Builder block(final String providerName,
                             final String group,
                             final AbstractBlock.Properties props,
                             final ResourceLocation tagResource,
                             final ITag.INamedTag<Item> itemTagToMirrorBlockTagTo) {
            return block(providerName, group, () -> new Block(props), tagResource, itemTagToMirrorBlockTagTo);
        }
        public Builder noDefaultLootTableBlock(final String providerName,
                                               final String group,
                                               final AbstractBlock.Properties props,
                                               final ResourceLocation tagResource,
                                               final ITag.INamedTag<Item> itemTagToMirrorBlockTagTo) {
            return block(providerName, group, () -> new NoDefaultLootTableBlock(props), tagResource, itemTagToMirrorBlockTagTo);
        }
        public Builder block(final String providerName,
                             final String group,
                             final AbstractBlock.Properties props,
                             final ResourceLocation tagResource,
                             final ResourceLocation mirroredTagResource) {
            return block(providerName, group, () -> new Block(props), tagResource, mirroredTagResource);
        }
        public Builder noDefaultLootTableBlock(final String providerName,
                                               final String group,
                                               final AbstractBlock.Properties props,
                                               final ResourceLocation tagResource,
                                               final ResourceLocation mirroredTagResource) {
            return block(providerName, group, () -> new NoDefaultLootTableBlock(props), tagResource, mirroredTagResource);
        }

        public Builder item(final String providerName,
                            final Supplier<Item> supplier) {
            this.anonymousElement.itemSuppliers.put(providerName, new AttributedSupplier<Item, Item>().supplier(supplier));
            return this;
        }
        public Builder item(final String providerName,
                            final Supplier<Item> supplier,
                            final ITag.INamedTag<Item> tag) {
            this.anonymousElement.itemSuppliers.put(providerName, new AttributedSupplier<Item, Item>().supplier(supplier).tag(tag));
            return this;
        }
        public Builder item(final String providerName,
                            final Supplier<Item> supplier,
                            final ResourceLocation tagResource) {
            this.anonymousElement.itemSuppliers.put(providerName, new AttributedSupplier<Item, Item>().supplier(supplier).tagResource(tagResource));
            return this;
        }

        public Builder item(final String providerName,
                            final Item.Properties props) {
            this.item(providerName, () -> new Item(props));
            return this;
        }
        public Builder item(final String providerName,
                            final Item.Properties props,
                            final ITag.INamedTag<Item> tag) {
            return item(providerName, () -> new Item(props), tag);
        }
        public Builder item(final String providerName,
                            final Item.Properties props,
                            final ResourceLocation tagResource) {
            return item(providerName, () -> new Item(props), tagResource);
        }

        public Builder item(final String providerName,
                            final String group) {
            this.item(providerName, () -> new Item(new Item.Properties().group(Registration.getTabGroup(group))));
            return this;
        }
        public Builder item(final String providerName,
                            final String group,
                            final ITag.INamedTag<Item> tag) {
            return item(providerName, () -> new Item(new Item.Properties().group(Registration.getTabGroup(group))), tag);
        }
        public Builder item(final String providerName,
                            final String group,
                            final ResourceLocation tagResource) {
            return item(providerName, () -> new Item(new Item.Properties().group(Registration.getTabGroup(group))), tagResource);
        }

        public Builder sourceFluid(final String providerName,
                                   final Supplier<Fluid> supplier) {
            this.anonymousElement.sourceFluidSuppliers.put(providerName, new AttributedSupplier<Fluid, Fluid>().supplier(supplier));
            return this;
        }
        public Builder sourceFluid(final String providerName,
                                   final Supplier<Fluid> supplier,
                                   final ITag.INamedTag<Fluid> tag) {
            this.anonymousElement.sourceFluidSuppliers.put(providerName, new AttributedSupplier<Fluid, Fluid>().supplier(supplier).tag(tag));
            return this;
        }
        public Builder sourceFluid(final String providerName,
                                   final Supplier<Fluid> supplier,
                                   final ResourceLocation tagResource) {
            this.anonymousElement.sourceFluidSuppliers.put(providerName,new AttributedSupplier<Fluid, Fluid>().supplier(supplier).tagResource(tagResource));
            return this;
        }

        public Builder flowingFluid(final String providerName,
                                    final Supplier<FlowingFluid> supplier) {
            this.anonymousElement.flowingFluidSuppliers.put(providerName, new AttributedSupplier<FlowingFluid, Fluid>().supplier(supplier));
            return this;
        }
        public Builder flowingFluid(final String providerName,
                                    final Supplier<FlowingFluid> supplier,
                                    final ITag.INamedTag<Fluid> tag) {
            this.anonymousElement.flowingFluidSuppliers.put(providerName, new AttributedSupplier<FlowingFluid, Fluid>().supplier(supplier).tag(tag));
            return this;
        }
        public Builder flowingFluid(final String providerName,
                                    final Supplier<FlowingFluid> supplier,
                                    final ResourceLocation tagResource) {
            this.anonymousElement.flowingFluidSuppliers.put(providerName,new AttributedSupplier<FlowingFluid, Fluid>().supplier(supplier).tagResource(tagResource));
            return this;
        }

        public AnonymousElement build() {
            return this.anonymousElement;
        }
    }
}
