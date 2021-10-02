package com.engineersbox.expandedfusion.core.registration.anonymous.element;

import com.engineersbox.expandedfusion.core.registration.anonymous.element.annotated.block.LangMappedBlock;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.annotated.block.NoDefaultLootTableBlock;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.annotated.fluid.LangMappedFlowingFluid;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.annotated.fluid.LangMappedSourceFluid;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.annotated.item.LangMappedItem;
import com.engineersbox.expandedfusion.core.registration.contexts.Registration;
import com.engineersbox.expandedfusion.core.registration.exception.anonymous.UninitialisedRuntimeClassPackageNameException;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.ElementProvider;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.ForgeFlowingFluid;

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

        private static final String UNINITIALISED_PACKAGE_NAME = "UNINITIALISED";

        @Inject
        @Named("packageName")
        private static String PACKAGE_NAME = UNINITIALISED_PACKAGE_NAME;

        private static final String DYNAMIC_BLOCK_CLASS_SUFFIX = "_DYN_BLOCK";
        private static final String DYNAMIC_ITEM_CLASS_SUFFIX = "_DYN_ITEM";
        private static final String DYNAMIC_SOURCE_FLUID_CLASS_SUFFIX = "_DYN_SOURCE_FLUID";
        private static final String DYNAMIC_FLOWING_FLUID_CLASS_SUFFIX = "_DYN_FLOWING_FLUID";

        private final AnonymousElement anonymousElement;

        public Builder() {
            anonymousElement = new AnonymousElement();
        }

        // TODO: Implement ability to supply recipes

        public Builder block(final String providerName,
                             final String group,
                             final Supplier<Block> supplier) {
            this.anonymousElement.blockSuppliers.put(providerName, new AttributedSupplier<Block, Block>(ElementProvider.BLOCK).supplier(supplier).tabGroup(group));
            return this;
        }
        public Builder block(final String providerName,
                             final Map<LangKey, String> langMapping,
                             final String group,
                             final Supplier<Block> supplier) {
            this.anonymousElement.blockSuppliers.put(providerName, new AttributedSupplier<Block, Block>(ElementProvider.BLOCK).supplier(supplier).tabGroup(group).langMappings(langMapping));
            return this;
        }
        public Builder block(final String providerName,
                             final String group,
                             final Supplier<Block> supplier,
                             final ITag.INamedTag<Block> tag) {
            this.anonymousElement.blockSuppliers.put(providerName, new AttributedSupplier<Block, Block>(ElementProvider.BLOCK).supplier(supplier).tabGroup(group).tag(tag));
            return this;
        }
        public Builder block(final String providerName,
                             final Map<LangKey, String> langMapping,
                             final String group,
                             final Supplier<Block> supplier,
                             final ITag.INamedTag<Block> tag) {
            this.anonymousElement.blockSuppliers.put(providerName, new AttributedSupplier<Block, Block>(ElementProvider.BLOCK).supplier(supplier).tabGroup(group).tag(tag).langMappings(langMapping));
            return this;
        }
        public Builder block(final String providerName,
                             final String group,
                             final Supplier<Block> supplier,
                             final ITag.INamedTag<Block> tag,
                             final ITag.INamedTag<Item> itemTagToMirrorBlockTagTo) {
            this.anonymousElement.blockSuppliers.put(providerName, new AttributedSupplier<Block, Block>(ElementProvider.BLOCK).supplier(supplier).tabGroup(group).tag(tag).mirroredTag(itemTagToMirrorBlockTagTo));
            return this;
        }
        public Builder block(final String providerName,
                             final Map<LangKey, String> langMapping,
                             final String group,
                             final Supplier<Block> supplier,
                             final ITag.INamedTag<Block> tag,
                             final ITag.INamedTag<Item> itemTagToMirrorBlockTagTo) {
            this.anonymousElement.blockSuppliers.put(providerName, new AttributedSupplier<Block, Block>(ElementProvider.BLOCK).supplier(supplier).tabGroup(group).tag(tag).mirroredTag(itemTagToMirrorBlockTagTo).langMappings(langMapping));
            return this;
        }
        public Builder block(final String providerName,
                             final String group,
                             final Supplier<Block> supplier,
                             final ITag.INamedTag<Block> tag,
                             final ResourceLocation itemResourceToMirrorBlockTagTo) {
            this.anonymousElement.blockSuppliers.put(providerName, new AttributedSupplier<Block, Block>(ElementProvider.BLOCK).supplier(supplier).tabGroup(group).tag(tag).mirroredTagResource(itemResourceToMirrorBlockTagTo));
            return this;
        }
        public Builder block(final String providerName,
                             final Map<LangKey, String> langMapping,
                             final String group,
                             final Supplier<Block> supplier,
                             final ITag.INamedTag<Block> tag,
                             final ResourceLocation itemResourceToMirrorBlockTagTo) {
            this.anonymousElement.blockSuppliers.put(providerName, new AttributedSupplier<Block, Block>(ElementProvider.BLOCK).supplier(supplier).tabGroup(group).tag(tag).mirroredTagResource(itemResourceToMirrorBlockTagTo).langMappings(langMapping));
            return this;
        }
        public Builder block(final String providerName,
                             final String group,
                             final Supplier<Block> supplier,
                             final ResourceLocation tagResource) {
            this.anonymousElement.blockSuppliers.put(providerName, new AttributedSupplier<Block, Block>(ElementProvider.BLOCK).supplier(supplier).tabGroup(group).tagResource(tagResource));
            return this;
        }
        public Builder block(final String providerName,
                             final Map<LangKey, String> langMapping,
                             final String group,
                             final Supplier<Block> supplier,
                             final ResourceLocation tagResource) {
            this.anonymousElement.blockSuppliers.put(providerName, new AttributedSupplier<Block, Block>(ElementProvider.BLOCK).supplier(supplier).tabGroup(group).tagResource(tagResource).langMappings(langMapping));
            return this;
        }
        public Builder block(final String providerName,
                             final String group,
                             final Supplier<Block> supplier,
                             final ResourceLocation tagResource,
                             final ITag.INamedTag<Item> itemTagToMirrorBlockTagTo) {
            this.anonymousElement.blockSuppliers.put(providerName, new AttributedSupplier<Block, Block>(ElementProvider.BLOCK).supplier(supplier).tabGroup(group).tagResource(tagResource).mirroredTag(itemTagToMirrorBlockTagTo));
            return this;
        }
        public Builder block(final String providerName,
                             final Map<LangKey, String> langMapping,
                             final String group,
                             final Supplier<Block> supplier,
                             final ResourceLocation tagResource,
                             final ITag.INamedTag<Item> itemTagToMirrorBlockTagTo) {
            this.anonymousElement.blockSuppliers.put(providerName, new AttributedSupplier<Block, Block>(ElementProvider.BLOCK).supplier(supplier).tabGroup(group).tagResource(tagResource).mirroredTag(itemTagToMirrorBlockTagTo).langMappings(langMapping));
            return this;
        }
        public Builder block(final String providerName,
                             final String group,
                             final Supplier<Block> supplier,
                             final ResourceLocation tagResource,
                             final ResourceLocation itemResourceToMirrorBlockTagTo) {
            this.anonymousElement.blockSuppliers.put(providerName, new AttributedSupplier<Block, Block>(ElementProvider.BLOCK).supplier(supplier).tabGroup(group).tagResource(tagResource).mirroredTagResource(itemResourceToMirrorBlockTagTo));
            return this;
        }
        public Builder block(final String providerName,
                             final Map<LangKey, String> langMapping,
                             final String group,
                             final Supplier<Block> supplier,
                             final ResourceLocation tagResource,
                             final ResourceLocation itemResourceToMirrorBlockTagTo) {
            this.anonymousElement.blockSuppliers.put(providerName, new AttributedSupplier<Block, Block>(ElementProvider.BLOCK).supplier(supplier).tabGroup(group).tagResource(tagResource).mirroredTagResource(itemResourceToMirrorBlockTagTo).langMappings(langMapping));
            return this;
        }

        public Builder block(final String providerName,
                             final String group,
                             final AbstractBlock.Properties props) {
            return block(providerName, group, () -> new LangMappedBlock(props));
        }
        public Builder block(final String providerName,
                             final Map<LangKey, String> langMapping,
                             final String group,
                             final AbstractBlock.Properties props) {
            createLangMappedBlock(
                    LangMappedBlock.class,
                    providerName,
                    langMapping,
                    props
            );
            final LangMappedBlock langMappedBlock = createLangMappedBlock(
                    LangMappedBlock.class,
                    providerName,
                    langMapping,
                    props
            );
            return block(providerName, group, () -> langMappedBlock);
        }
        public Builder noDefaultLootTableBlock(final String providerName,
                                               final String group,
                                               final AbstractBlock.Properties props) {
            return block(providerName, group, () -> new NoDefaultLootTableBlock(props));
        }
        public Builder noDefaultLootTableBlock(final String providerName,
                                               final Map<LangKey, String> langMapping,
                                               final String group,
                                               final AbstractBlock.Properties props) {
            final NoDefaultLootTableBlock langMappedBlock = createLangMappedBlock(
                    NoDefaultLootTableBlock.class,
                    providerName,
                    langMapping,
                    props
            );
            return block(providerName, group, () -> langMappedBlock);
        }
        public Builder block(final String providerName,
                             final String group,
                             final AbstractBlock.Properties props,
                             final ITag.INamedTag<Block> tag) {
            return block(providerName, group, () -> new LangMappedBlock(props), tag);
        }
        public Builder block(final String providerName,
                             final Map<LangKey, String> langMapping,
                             final String group,
                             final AbstractBlock.Properties props,
                             final ITag.INamedTag<Block> tag) {
            final LangMappedBlock langMappedBlock = createLangMappedBlock(
                    LangMappedBlock.class,
                    providerName,
                    langMapping,
                    props
            );
            return block(providerName, group, () -> langMappedBlock, tag);
        }
        public Builder noDefaultLootTableBlock(final String providerName,
                                               final String group,
                                               final AbstractBlock.Properties props,
                                               final ITag.INamedTag<Block> tag) {
            return block(providerName, group, () -> new NoDefaultLootTableBlock(props), tag);
        }
        public Builder noDefaultLootTableBlock(final String providerName,
                                               final Map<LangKey, String> langMapping,
                                               final String group,
                                               final AbstractBlock.Properties props,
                                               final ITag.INamedTag<Block> tag) {
            final NoDefaultLootTableBlock langMappedBlock = createLangMappedBlock(
                    NoDefaultLootTableBlock.class,
                    providerName,
                    langMapping,
                    props
            );
            return block(providerName, group, () -> langMappedBlock, tag);
        }
        public Builder block(final String providerName,
                             final String group,
                             final AbstractBlock.Properties props,
                             final ITag.INamedTag<Block> tag,
                             final ITag.INamedTag<Item> itemTagToMirrorBlockTagTo) {
            return block(providerName, group, () -> new LangMappedBlock(props), tag, itemTagToMirrorBlockTagTo);
        }
        public Builder block(final String providerName,
                             final Map<LangKey, String> langMapping,
                             final String group,
                             final AbstractBlock.Properties props,
                             final ITag.INamedTag<Block> tag,
                             final ITag.INamedTag<Item> itemTagToMirrorBlockTagTo) {
            final LangMappedBlock langMappedBlock = createLangMappedBlock(
                    LangMappedBlock.class,
                    providerName,
                    langMapping,
                    props
            );
            return block(providerName, group, () -> langMappedBlock, tag, itemTagToMirrorBlockTagTo);
        }
        public Builder noDefaultLootTableBlock(final String providerName,
                                               final String group,
                                               final AbstractBlock.Properties props,
                                               final ITag.INamedTag<Block> tag,
                                               final ITag.INamedTag<Item> itemTagToMirrorBlockTagTo) {
            return block(providerName, group, () -> new NoDefaultLootTableBlock(props), tag, itemTagToMirrorBlockTagTo);
        }
        public Builder noDefaultLootTableBlock(final String providerName,
                                               final Map<LangKey, String> langMapping,
                                               final String group,
                                               final AbstractBlock.Properties props,
                                               final ITag.INamedTag<Block> tag,
                                               final ITag.INamedTag<Item> itemTagToMirrorBlockTagTo) {
            final NoDefaultLootTableBlock langMappedBlock = createLangMappedBlock(
                    NoDefaultLootTableBlock.class,
                    providerName,
                    langMapping,
                    props
            );
            return block(providerName, group, () -> langMappedBlock, tag, itemTagToMirrorBlockTagTo);
        }
        public Builder block(final String providerName,
                             final String group,
                             final AbstractBlock.Properties props,
                             final ITag.INamedTag<Block> tag,
                             final ResourceLocation mirroredTagResource) {
            return block(providerName, group, () -> new LangMappedBlock(props), tag, mirroredTagResource);
        }
        public Builder block(final String providerName,
                             final Map<LangKey, String> langMapping,
                             final String group,
                             final AbstractBlock.Properties props,
                             final ITag.INamedTag<Block> tag,
                             final ResourceLocation mirroredTagResource) {
            final LangMappedBlock langMappedBlock = createLangMappedBlock(
                    LangMappedBlock.class,
                    providerName,
                    langMapping,
                    props
            );
            return block(providerName, group, () -> langMappedBlock, tag, mirroredTagResource);
        }
        public Builder noDefaultLootTableBlock(final String providerName,
                                               final String group,
                                               final AbstractBlock.Properties props,
                                               final ITag.INamedTag<Block> tag,
                                               final ResourceLocation mirroredTagResource) {
            return block(providerName, group, () -> new NoDefaultLootTableBlock(props), tag, mirroredTagResource);
        }
        public Builder noDefaultLootTableBlock(final String providerName,
                                               final Map<LangKey, String> langMapping,
                                               final String group,
                                               final AbstractBlock.Properties props,
                                               final ITag.INamedTag<Block> tag,
                                               final ResourceLocation mirroredTagResource) {
            final NoDefaultLootTableBlock langMappedBlock = createLangMappedBlock(
                    NoDefaultLootTableBlock.class,
                    providerName,
                    langMapping,
                    props
            );
            return block(providerName, group, () -> langMappedBlock, tag, mirroredTagResource);
        }
        public Builder block(final String providerName,
                             final String group,
                             final AbstractBlock.Properties props,
                             final ResourceLocation tagResource) {
            return block(providerName, group, () -> new LangMappedBlock(props), tagResource);
        }
        public Builder block(final String providerName,
                             final Map<LangKey, String> langMapping,
                             final String group,
                             final AbstractBlock.Properties props,
                             final ResourceLocation tagResource) {
            final LangMappedBlock langMappedBlock = createLangMappedBlock(
                    LangMappedBlock.class,
                    providerName,
                    langMapping,
                    props
            );
            return block(providerName, group, () -> langMappedBlock, tagResource);
        }
        public Builder noDefaultLootTableBlock(final String providerName,
                                               final String group,
                                               final AbstractBlock.Properties props,
                                               final ResourceLocation tagResource) {
            return block(providerName, group, () -> new NoDefaultLootTableBlock(props), tagResource);
        }
        public Builder noDefaultLootTableBlock(final String providerName,
                                               final Map<LangKey, String> langMapping,
                                               final String group,
                                               final AbstractBlock.Properties props,
                                               final ResourceLocation tagResource) {
            final NoDefaultLootTableBlock langMappedBlock = createLangMappedBlock(
                    NoDefaultLootTableBlock.class,
                    providerName,
                    langMapping,
                    props
            );
            return block(providerName, group, () -> langMappedBlock, tagResource);
        }
        public Builder block(final String providerName,
                             final String group,
                             final AbstractBlock.Properties props,
                             final ResourceLocation tagResource,
                             final ITag.INamedTag<Item> itemTagToMirrorBlockTagTo) {
            return block(providerName, group, () -> new LangMappedBlock(props), tagResource, itemTagToMirrorBlockTagTo);
        }
        public Builder block(final String providerName,
                             final Map<LangKey, String> langMapping,
                             final String group,
                             final AbstractBlock.Properties props,
                             final ResourceLocation tagResource,
                             final ITag.INamedTag<Item> itemTagToMirrorBlockTagTo) {
            final LangMappedBlock langMappedBlock = createLangMappedBlock(
                    LangMappedBlock.class,
                    providerName,
                    langMapping,
                    props
            );
            return block(providerName, group, () -> langMappedBlock, tagResource, itemTagToMirrorBlockTagTo);
        }
        public Builder noDefaultLootTableBlock(final String providerName,
                                               final String group,
                                               final AbstractBlock.Properties props,
                                               final ResourceLocation tagResource,
                                               final ITag.INamedTag<Item> itemTagToMirrorBlockTagTo) {
            return block(providerName, group, () -> new NoDefaultLootTableBlock(props), tagResource, itemTagToMirrorBlockTagTo);
        }
        public Builder noDefaultLootTableBlock(final String providerName,
                                               final Map<LangKey, String> langMapping,
                                               final String group,
                                               final AbstractBlock.Properties props,
                                               final ResourceLocation tagResource,
                                               final ITag.INamedTag<Item> itemTagToMirrorBlockTagTo) {
            final NoDefaultLootTableBlock langMappedBlock = createLangMappedBlock(
                    NoDefaultLootTableBlock.class,
                    providerName,
                    langMapping,
                    props
            );
            return block(providerName, group, () -> langMappedBlock, tagResource, itemTagToMirrorBlockTagTo);
        }
        public Builder block(final String providerName,
                             final String group,
                             final AbstractBlock.Properties props,
                             final ResourceLocation tagResource,
                             final ResourceLocation itemResourceToMirrorBlockTagTo) {
            return block(providerName, group, () -> new LangMappedBlock(props), tagResource, itemResourceToMirrorBlockTagTo);
        }
        public Builder block(final String providerName,
                             final Map<LangKey, String> langMapping,
                             final String group,
                             final AbstractBlock.Properties props,
                             final ResourceLocation tagResource,
                             final ResourceLocation itemResourceToMirrorBlockTagTo) {
            final LangMappedBlock langMappedBlock = createLangMappedBlock(
                    LangMappedBlock.class,
                    providerName,
                    langMapping,
                    props
            );
            return block(providerName, group, () -> langMappedBlock, tagResource, itemResourceToMirrorBlockTagTo);
        }
        public Builder noDefaultLootTableBlock(final String providerName,
                                               final String group,
                                               final AbstractBlock.Properties props,
                                               final ResourceLocation tagResource,
                                               final ResourceLocation itemResourceToMirrorBlockTagTo) {
            return block(providerName, group, () -> new NoDefaultLootTableBlock(props), tagResource, itemResourceToMirrorBlockTagTo);
        }
        public Builder noDefaultLootTableBlock(final String providerName,
                                               final Map<LangKey, String> langMapping,
                                               final String group,
                                               final AbstractBlock.Properties props,
                                               final ResourceLocation tagResource,
                                               final ResourceLocation itemResourceToMirrorBlockTagTo) {
            final NoDefaultLootTableBlock langMappedBlock = createLangMappedBlock(
                    NoDefaultLootTableBlock.class,
                    providerName,
                    langMapping,
                    props
            );
            return block(providerName, group, () -> langMappedBlock, tagResource, itemResourceToMirrorBlockTagTo);
        }

        public Builder item(final String providerName,
                            final Supplier<Item> supplier) {
            this.anonymousElement.itemSuppliers.put(providerName, new AttributedSupplier<Item, Item>(ElementProvider.ITEM).supplier(supplier));
            return this;
        }
        public Builder item(final String providerName,
                            final Map<LangKey, String> langMapping,
                            final Supplier<Item> supplier) {
            this.anonymousElement.itemSuppliers.put(providerName, new AttributedSupplier<Item, Item>(ElementProvider.ITEM).supplier(supplier).langMappings(langMapping));
            return this;
        }
        public Builder item(final String providerName,
                            final Supplier<Item> supplier,
                            final ITag.INamedTag<Item> tag) {
            this.anonymousElement.itemSuppliers.put(providerName, new AttributedSupplier<Item, Item>(ElementProvider.ITEM).supplier(supplier).tag(tag));
            return this;
        }
        public Builder item(final String providerName,
                            final Map<LangKey, String> langMapping,
                            final Supplier<Item> supplier,
                            final ITag.INamedTag<Item> tag) {
            this.anonymousElement.itemSuppliers.put(providerName, new AttributedSupplier<Item, Item>(ElementProvider.ITEM).supplier(supplier).tag(tag).langMappings(langMapping));
            return this;
        }
        public Builder item(final String providerName,
                            final Supplier<Item> supplier,
                            final ResourceLocation tagResource) {
            this.anonymousElement.itemSuppliers.put(providerName, new AttributedSupplier<Item, Item>(ElementProvider.ITEM).supplier(supplier).tagResource(tagResource));
            return this;
        }
        public Builder item(final String providerName,
                            final Map<LangKey, String> langMapping,
                            final Supplier<Item> supplier,
                            final ResourceLocation tagResource) {
            this.anonymousElement.itemSuppliers.put(providerName, new AttributedSupplier<Item, Item>(ElementProvider.ITEM).supplier(supplier).tagResource(tagResource).langMappings(langMapping));
            return this;
        }

        public Builder item(final String providerName,
                            final Item.Properties props) {
            return item(providerName, () -> new LangMappedItem(props));
        }
        public Builder item(final String providerName,
                            final Map<LangKey, String> langMapping,
                            final Item.Properties props) {
            final LangMappedItem langMappedItem = createLangMappedItem(
                    providerName,
                    langMapping,
                    props
            );
            return item(providerName, () -> langMappedItem);
        }
        public Builder item(final String providerName,
                            final Item.Properties props,
                            final ITag.INamedTag<Item> tag) {
            return item(providerName, () -> new LangMappedItem(props), tag);
        }
        public Builder item(final String providerName,
                            final Map<LangKey, String> langMapping,
                            final Item.Properties props,
                            final ITag.INamedTag<Item> tag) {
            final LangMappedItem langMappedItem = createLangMappedItem(
                    providerName,
                    langMapping,
                    props
            );
            return item(providerName, () -> langMappedItem, tag);
        }
        public Builder item(final String providerName,
                            final Item.Properties props,
                            final ResourceLocation tagResource) {
            return item(providerName, () -> new LangMappedItem(props), tagResource);
        }
        public Builder item(final String providerName,
                            final Map<LangKey, String> langMapping,
                            final Item.Properties props,
                            final ResourceLocation tagResource) {
            final LangMappedItem langMappedItem = createLangMappedItem(
                    providerName,
                    langMapping,
                    props
            );
            return item(providerName, () -> langMappedItem, tagResource);
        }

        public Builder item(final String providerName,
                            final String group) {
            this.item(providerName, () -> new LangMappedItem(new Item.Properties().group(Registration.getTabGroup(group))));
            return this;
        }
        public Builder item(final String providerName,
                            final Map<LangKey, String> langMapping,
                            final String group) {
            final LangMappedItem langMappedItem = createLangMappedItem(
                    providerName,
                    langMapping,
                    new Item.Properties().group(Registration.getTabGroup(group))
            );
            return item(providerName, () -> langMappedItem);
        }
        public Builder item(final String providerName,
                            final String group,
                            final ITag.INamedTag<Item> tag) {
            return item(providerName, () -> new LangMappedItem(new Item.Properties().group(Registration.getTabGroup(group))), tag);
        }
        public Builder item(final String providerName,
                            final Map<LangKey, String> langMapping,
                            final String group,
                            final ITag.INamedTag<Item> tag) {
            final LangMappedItem langMappedItem = createLangMappedItem(
                    providerName,
                    langMapping,
                    new Item.Properties().group(Registration.getTabGroup(group))
            );
            return item(providerName, () -> langMappedItem, tag);
        }
        public Builder item(final String providerName,
                            final String group,
                            final ResourceLocation tagResource) {
            return item(providerName, () -> new LangMappedItem(new Item.Properties().group(Registration.getTabGroup(group))), tagResource);
        }
        public Builder item(final String providerName,
                            final Map<LangKey, String> langMapping,
                            final String group,
                            final ResourceLocation tagResource) {
            final LangMappedItem langMappedItem = createLangMappedItem(
                    providerName,
                    langMapping,
                    new Item.Properties().group(Registration.getTabGroup(group))
            );
            return item(providerName, () -> langMappedItem, tagResource);
        }

        public Builder sourceFluid(final String providerName,
                                   final Supplier<Fluid> supplier) {
            this.anonymousElement.sourceFluidSuppliers.put(providerName, new AttributedSupplier<Fluid, Fluid>(ElementProvider.FLUID).supplier(supplier));
            return this;
        }
        public Builder sourceFluid(final String providerName,
                                   final Map<LangKey, String> langMapping,
                                   final Supplier<Fluid> supplier) {
            this.anonymousElement.sourceFluidSuppliers.put(providerName, new AttributedSupplier<Fluid, Fluid>(ElementProvider.FLUID).supplier(supplier).langMappings(langMapping));
            return this;
        }
        public Builder sourceFluid(final String providerName,
                                   final Supplier<Fluid> supplier,
                                   final ITag.INamedTag<Fluid> tag) {
            this.anonymousElement.sourceFluidSuppliers.put(providerName, new AttributedSupplier<Fluid, Fluid>(ElementProvider.FLUID).supplier(supplier).tag(tag));
            return this;
        }
        public Builder sourceFluid(final String providerName,
                                   final Map<LangKey, String> langMapping,
                                   final Supplier<Fluid> supplier,
                                   final ITag.INamedTag<Fluid> tag) {
            this.anonymousElement.sourceFluidSuppliers.put(providerName, new AttributedSupplier<Fluid, Fluid>(ElementProvider.FLUID).supplier(supplier).tag(tag).langMappings(langMapping));
            return this;
        }
        public Builder sourceFluid(final String providerName,
                                   final Supplier<Fluid> supplier,
                                   final ResourceLocation tagResource) {
            this.anonymousElement.sourceFluidSuppliers.put(providerName,new AttributedSupplier<Fluid, Fluid>(ElementProvider.FLUID).supplier(supplier).tagResource(tagResource));
            return this;
        }
        public Builder sourceFluid(final String providerName,
                                   final Map<LangKey, String> langMapping,
                                   final Supplier<Fluid> supplier,
                                   final ResourceLocation tagResource) {
            this.anonymousElement.sourceFluidSuppliers.put(providerName,new AttributedSupplier<Fluid, Fluid>(ElementProvider.FLUID).supplier(supplier).tagResource(tagResource).langMappings(langMapping));
            return this;
        }

        public Builder sourceFluid(final String providerName,
                                   final ForgeFlowingFluid.Properties props) {
            final LangMappedSourceFluid sourceFluid = new LangMappedSourceFluid(props);
            return sourceFluid(providerName, () -> sourceFluid);
        }
        public Builder sourceFluid(final String providerName,
                                   final Map<LangKey, String> langMapping,
                                   final ForgeFlowingFluid.Properties props) {
            final LangMappedSourceFluid sourceFluid = createLangMappedFluid(
                    LangMappedSourceFluid.class,
                    providerName,
                    langMapping,
                    props
            );
            return sourceFluid(providerName, () -> sourceFluid);
        }
        public Builder sourceFluid(final String providerName,
                                   final ForgeFlowingFluid.Properties props,
                                   final ITag.INamedTag<Fluid> tag) {
            final LangMappedSourceFluid sourceFluid = new LangMappedSourceFluid(props);
            return sourceFluid(providerName, () -> sourceFluid, tag);
        }
        public Builder sourceFluid(final String providerName,
                                   final Map<LangKey, String> langMapping,
                                   final ForgeFlowingFluid.Properties props,
                                   final ITag.INamedTag<Fluid> tag) {
            final LangMappedSourceFluid sourceFluid = createLangMappedFluid(
                    LangMappedSourceFluid.class,
                    providerName,
                    langMapping,
                    props
            );
            return sourceFluid(providerName, () -> sourceFluid, tag);
        }
        public Builder sourceFluid(final String providerName,
                                   final ForgeFlowingFluid.Properties props,
                                   final ResourceLocation tagResource) {
            final LangMappedSourceFluid sourceFluid = new LangMappedSourceFluid(props);
            return sourceFluid(providerName, () -> sourceFluid, tagResource);
        }
        public Builder sourceFluid(final String providerName,
                                   final Map<LangKey, String> langMapping,
                                   final ForgeFlowingFluid.Properties props,
                                   final ResourceLocation tagResource) {
            final LangMappedSourceFluid sourceFluid = createLangMappedFluid(
                    LangMappedSourceFluid.class,
                    providerName,
                    langMapping,
                    props
            );
            return sourceFluid(providerName, () -> sourceFluid, tagResource);
        }

        public Builder flowingFluid(final String providerName,
                                    final Supplier<FlowingFluid> supplier) {
            this.anonymousElement.flowingFluidSuppliers.put(providerName, new AttributedSupplier<FlowingFluid, Fluid>(ElementProvider.FLUID).supplier(supplier));
            return this;
        }
        public Builder flowingFluid(final String providerName,
                                    final Map<LangKey, String> langMapping,
                                    final Supplier<FlowingFluid> supplier) {
            this.anonymousElement.flowingFluidSuppliers.put(providerName, new AttributedSupplier<FlowingFluid, Fluid>(ElementProvider.FLUID).supplier(supplier).langMappings(langMapping));
            return this;
        }
        public Builder flowingFluid(final String providerName,
                                    final Supplier<FlowingFluid> supplier,
                                    final ITag.INamedTag<Fluid> tag) {
            this.anonymousElement.flowingFluidSuppliers.put(providerName, new AttributedSupplier<FlowingFluid, Fluid>(ElementProvider.FLUID).supplier(supplier).tag(tag));
            return this;
        }
        public Builder flowingFluid(final String providerName,
                                    final Map<LangKey, String> langMapping,
                                    final Supplier<FlowingFluid> supplier,
                                    final ITag.INamedTag<Fluid> tag) {
            this.anonymousElement.flowingFluidSuppliers.put(providerName, new AttributedSupplier<FlowingFluid, Fluid>(ElementProvider.FLUID).supplier(supplier).tag(tag).langMappings(langMapping));
            return this;
        }
        public Builder flowingFluid(final String providerName,
                                    final Supplier<FlowingFluid> supplier,
                                    final ResourceLocation tagResource) {
            this.anonymousElement.flowingFluidSuppliers.put(providerName,new AttributedSupplier<FlowingFluid, Fluid>(ElementProvider.FLUID).supplier(supplier).tagResource(tagResource));
            return this;
        }
        public Builder flowingFluid(final String providerName,
                                    final Map<LangKey, String> langMapping,
                                    final Supplier<FlowingFluid> supplier,
                                    final ResourceLocation tagResource) {
            this.anonymousElement.flowingFluidSuppliers.put(providerName,new AttributedSupplier<FlowingFluid, Fluid>(ElementProvider.FLUID).supplier(supplier).tagResource(tagResource).langMappings(langMapping));
            return this;
        }

        public Builder flowingFluid(final String providerName,
                                    final ForgeFlowingFluid.Properties props) {
            final LangMappedFlowingFluid flowingFluid = new LangMappedFlowingFluid(props);
            return sourceFluid(providerName, () -> flowingFluid);
        }
        public Builder flowingFluid(final String providerName,
                                    final Map<LangKey, String> langMapping,
                                    final ForgeFlowingFluid.Properties props) {
            final LangMappedFlowingFluid flowingFluid = createLangMappedFluid(
                    LangMappedFlowingFluid.class,
                    providerName,
                    langMapping,
                    props
            );
            return sourceFluid(providerName, () -> flowingFluid);
        }
        public Builder flowingFluid(final String providerName,
                                    final ForgeFlowingFluid.Properties props,
                                    final ITag.INamedTag<Fluid> tag) {
            final LangMappedFlowingFluid flowingFluid = new LangMappedFlowingFluid(props);
            return sourceFluid(providerName, () -> flowingFluid, tag);
        }
        public Builder flowingFluid(final String providerName,
                                    final Map<LangKey, String> langMapping,
                                    final ForgeFlowingFluid.Properties props,
                                    final ITag.INamedTag<Fluid> tag) {
            final LangMappedFlowingFluid flowingFluid = createLangMappedFluid(
                    LangMappedFlowingFluid.class,
                    providerName,
                    langMapping,
                    props
            );
            return sourceFluid(providerName, () -> flowingFluid, tag);
        }
        public Builder flowingFluid(final String providerName,
                                    final ForgeFlowingFluid.Properties props,
                                    final ResourceLocation tagResource) {
            final LangMappedFlowingFluid flowingFluid = new LangMappedFlowingFluid(props);
            return sourceFluid(providerName, () -> flowingFluid, tagResource);
        }
        public Builder flowingFluid(final String providerName,
                                    final Map<LangKey, String> langMapping,
                                    final ForgeFlowingFluid.Properties props,
                                    final ResourceLocation tagResource) {
            final LangMappedFlowingFluid flowingFluid = createLangMappedFluid(
                    LangMappedFlowingFluid.class,
                    providerName,
                    langMapping,
                    props
            );
            return sourceFluid(providerName, () -> flowingFluid, tagResource);
        }

        private static final String DYNAMIC_CLASS_IDENTIFIER_FORMAT = "%s.%s%s";
        private <T extends LangMappedBlock> T createLangMappedBlock(final Class<T> baseClass,
                                                                    final String providerName,
                                                                    final Map<LangKey, String> langMapping,
                                                                    final AbstractBlock.Properties props) {
            this.checkPackageNameInitialised();
            return new ElementDynamicClassGenerator<>(baseClass)
                    .retrieveDeclaredConstructor(AbstractBlock.Properties.class)
                    .withAnnotation(ElementAnnotationConstructor.createLangMetadata(langMapping))
                    .withAnnotation(ElementAnnotationConstructor.createBlockProvider(providerName))
                    .createUnloadedClass(String.format(
                            DYNAMIC_CLASS_IDENTIFIER_FORMAT,
                            PACKAGE_NAME,
                            providerName,
                            DYNAMIC_BLOCK_CLASS_SUFFIX
                    ))
                    .loadClass()
                    .getInstance(props);
        }

        private LangMappedItem createLangMappedItem(final String providerName,
                                                    final Map<LangKey, String> langMapping,
                                                    final Item.Properties props) {
            this.checkPackageNameInitialised();
            return new ElementDynamicClassGenerator<>(LangMappedItem.class)
                    .retrieveDeclaredConstructor(Item.Properties.class)
                    .withAnnotation(ElementAnnotationConstructor.createLangMetadata(langMapping))
                    .withAnnotation(ElementAnnotationConstructor.createItemProvider(providerName))
                    .createUnloadedClass(String.format(
                            DYNAMIC_CLASS_IDENTIFIER_FORMAT,
                            PACKAGE_NAME,
                            providerName,
                            DYNAMIC_ITEM_CLASS_SUFFIX
                    ))
                    .loadClass()
                    .getInstance(props);
        }

        private <T> T createLangMappedFluid(final Class<T> baseClass,
                                            final String providerName,
                                            final Map<LangKey, String> langMapping,
                                            final ForgeFlowingFluid.Properties props) {
            this.checkPackageNameInitialised();
            return new ElementDynamicClassGenerator<>(baseClass)
                    .retrieveDeclaredConstructor(ForgeFlowingFluid.Properties.class)
                    .withAnnotation(ElementAnnotationConstructor.createLangMetadata(langMapping))
                    .withAnnotation(ElementAnnotationConstructor.createFluidProvider(providerName))
                    .createUnloadedClass(String.format(
                            DYNAMIC_CLASS_IDENTIFIER_FORMAT,
                            PACKAGE_NAME,
                            providerName,
                            ForgeFlowingFluid.Source.class.isAssignableFrom(baseClass) ? DYNAMIC_SOURCE_FLUID_CLASS_SUFFIX : DYNAMIC_FLOWING_FLUID_CLASS_SUFFIX
                    ))
                    .loadClass()
                    .getInstance(props);
        }

        private void checkPackageNameInitialised() {
            if (PACKAGE_NAME.equals(UNINITIALISED_PACKAGE_NAME)) {
                throw new UninitialisedRuntimeClassPackageNameException("Package name for class generation was not instantiated via static injection. Did the context change?");
            }
        }

        public AnonymousElement build() {
            return this.anonymousElement;
        }
    }
}
