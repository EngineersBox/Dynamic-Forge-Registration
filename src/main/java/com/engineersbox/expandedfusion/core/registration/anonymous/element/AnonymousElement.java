package com.engineersbox.expandedfusion.core.registration.anonymous.element;

import com.engineersbox.expandedfusion.core.reflection.AnnotationFieldUpdater;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LocaleEntry;
import com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.lang.LangKey;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.annotated.block.LangMappedBlock;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.annotated.block.NoDefaultLootTableBlock;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.annotated.fluid.LangMappedFlowingFluid;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.annotated.fluid.LangMappedSourceFluid;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.annotated.item.LangMappedItem;
import com.engineersbox.expandedfusion.core.registration.contexts.Registration;
import com.engineersbox.expandedfusion.elements.block.machine.fusionControlComputer.FusionControlComputer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            return block(providerName, group, () -> new LangMappedBlock(props));
        }
        public Builder block(final String providerName,
                             final Map<LangKey, String> langMapping,
                             final String group,
                             final AbstractBlock.Properties props) {
            final LangMappedBlock langMappedBlock = new LangMappedBlock(props);
            updateInstanceLangMapping(langMappedBlock, langMapping);
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
            final NoDefaultLootTableBlock langMappedBlock = new NoDefaultLootTableBlock(props);
            updateInstanceLangMapping(langMappedBlock, langMapping);
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
            final LangMappedBlock langMappedBlock = new LangMappedBlock(props);
            updateInstanceLangMapping(langMappedBlock, langMapping);
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
            final NoDefaultLootTableBlock langMappedBlock = new NoDefaultLootTableBlock(props);
            updateInstanceLangMapping(langMappedBlock, langMapping);
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
            final LangMappedBlock langMappedBlock = new LangMappedBlock(props);
            updateInstanceLangMapping(langMappedBlock, langMapping);
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
            final NoDefaultLootTableBlock langMappedBlock = new NoDefaultLootTableBlock(props);
            updateInstanceLangMapping(langMappedBlock, langMapping);
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
            final LangMappedBlock langMappedBlock = new LangMappedBlock(props);
            updateInstanceLangMapping(langMappedBlock, langMapping);
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
            final NoDefaultLootTableBlock langMappedBlock = new NoDefaultLootTableBlock(props);
            updateInstanceLangMapping(langMappedBlock, langMapping);
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
            final LangMappedBlock langMappedBlock = new LangMappedBlock(props);
            updateInstanceLangMapping(langMappedBlock, langMapping);
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
            final NoDefaultLootTableBlock langMappedBlock = new NoDefaultLootTableBlock(props);
            updateInstanceLangMapping(langMappedBlock, langMapping);
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
            final LangMappedBlock langMappedBlock = new LangMappedBlock(props);
            updateInstanceLangMapping(langMappedBlock, langMapping);
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
            final NoDefaultLootTableBlock langMappedBlock = new NoDefaultLootTableBlock(props);
            updateInstanceLangMapping(langMappedBlock, langMapping);
            return block(providerName, group, () -> langMappedBlock, tagResource, itemTagToMirrorBlockTagTo);
        }
        public Builder block(final String providerName,
                             final String group,
                             final AbstractBlock.Properties props,
                             final ResourceLocation tagResource,
                             final ResourceLocation mirroredTagResource) {
            return block(providerName, group, () -> new LangMappedBlock(props), tagResource, mirroredTagResource);
        }
        public Builder block(final String providerName,
                             final Map<LangKey, String> langMapping,
                             final String group,
                             final AbstractBlock.Properties props,
                             final ResourceLocation tagResource,
                             final ResourceLocation mirroredTagResource) {
            final LangMappedBlock langMappedBlock = new LangMappedBlock(props);
            updateInstanceLangMapping(langMappedBlock, langMapping);
            return block(providerName, group, () -> langMappedBlock, tagResource, mirroredTagResource);
        }
        public Builder noDefaultLootTableBlock(final String providerName,
                                               final String group,
                                               final AbstractBlock.Properties props,
                                               final ResourceLocation tagResource,
                                               final ResourceLocation mirroredTagResource) {
            return block(providerName, group, () -> new NoDefaultLootTableBlock(props), tagResource, mirroredTagResource);
        }
        public Builder noDefaultLootTableBlock(final String providerName,
                                               final Map<LangKey, String> langMapping,
                                               final String group,
                                               final AbstractBlock.Properties props,
                                               final ResourceLocation tagResource,
                                               final ResourceLocation mirroredTagResource) {
            final NoDefaultLootTableBlock langMappedBlock = new NoDefaultLootTableBlock(props);
            updateInstanceLangMapping(langMappedBlock, langMapping);
            return block(providerName, group, () -> langMappedBlock, tagResource, mirroredTagResource);
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
            return item(providerName, () -> new LangMappedItem(props));
        }
        public Builder item(final String providerName,
                            final Map<LangKey, String> langMapping,
                            final Item.Properties props) {
            final LangMappedItem langMappedItem = new LangMappedItem(props);
            updateInstanceLangMapping(langMappedItem, langMapping);
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
            final LangMappedItem langMappedItem = new LangMappedItem(props);
            updateInstanceLangMapping(langMappedItem, langMapping);
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
            final LangMappedItem langMappedItem = new LangMappedItem(props);
            updateInstanceLangMapping(langMappedItem, langMapping);
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
            final LangMappedItem langMappedItem = new LangMappedItem(new Item.Properties().group(Registration.getTabGroup(group)));
            updateInstanceLangMapping(langMappedItem, langMapping);
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
            final LangMappedItem langMappedItem = new LangMappedItem(new Item.Properties().group(Registration.getTabGroup(group)));
            updateInstanceLangMapping(langMappedItem, langMapping);
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
            final LangMappedItem langMappedItem = new LangMappedItem(new Item.Properties().group(Registration.getTabGroup(group)));
            updateInstanceLangMapping(langMappedItem, langMapping);
            return item(providerName, () -> langMappedItem, tagResource);
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

        public Builder sourceFluid(final String providerName,
                                   final ForgeFlowingFluid.Properties props) {
            final LangMappedSourceFluid sourceFluid = new LangMappedSourceFluid(props);
            return sourceFluid(providerName, () -> sourceFluid);
        }
        public Builder sourceFluid(final String providerName,
                                   final Map<LangKey, String> langMapping,
                                   final ForgeFlowingFluid.Properties props) {
            final LangMappedSourceFluid sourceFluid = new LangMappedSourceFluid(props);
            updateInstanceLangMapping(sourceFluid, langMapping);
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
            final LangMappedSourceFluid sourceFluid = new LangMappedSourceFluid(props);
            updateInstanceLangMapping(sourceFluid, langMapping);
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
            final LangMappedSourceFluid sourceFluid = new LangMappedSourceFluid(props);
            updateInstanceLangMapping(sourceFluid, langMapping);
            return sourceFluid(providerName, () -> sourceFluid, tagResource);
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

        public Builder flowingFluid(final String providerName,
                                    final ForgeFlowingFluid.Properties props) {
            final LangMappedFlowingFluid flowingFluid = new LangMappedFlowingFluid(props);
            return sourceFluid(providerName, () -> flowingFluid);
        }
        public Builder flowingFluid(final String providerName,
                                    final Map<LangKey, String> langMapping,
                                    final ForgeFlowingFluid.Properties props) {
            final LangMappedFlowingFluid flowingFluid = new LangMappedFlowingFluid(props);
            updateInstanceLangMapping(flowingFluid, langMapping);
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
            final LangMappedFlowingFluid flowingFluid = new LangMappedFlowingFluid(props);
            updateInstanceLangMapping(flowingFluid, langMapping);
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
            final LangMappedFlowingFluid flowingFluid = new LangMappedFlowingFluid(props);
            updateInstanceLangMapping(flowingFluid, langMapping);
            return sourceFluid(providerName, () -> flowingFluid, tagResource);
        }

        private <T> void updateInstanceLangMapping(final T instance,
                                                   final Map<LangKey, String> langMapping) {
            final LocaleEntry[] locales = new LocaleEntry[langMapping.size()];
            final List<Map.Entry<LangKey, String>> indexableLangEntries = new ArrayList<>(langMapping.entrySet());
            for (int i = 0; i < langMapping.size(); i++) {
                final Map.Entry<LangKey, String> entryAtIdx = indexableLangEntries.get(i);
                final LocaleEntry localeEntry = new LocaleEntry(){
                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return LocaleEntry.class;
                    }

                    @Override
                    public LangKey key() {
                        return entryAtIdx.getKey();
                    }

                    @Override
                    public String mapping() {
                        return entryAtIdx.getValue();
                    }
                };
                locales[i] = localeEntry;
            }
            final LangMetadata langMetadata = new LangMetadata(){
                @Override
                public Class<? extends Annotation> annotationType() {
                    return LangMetadata.class;
                }

                @Override
                public LocaleEntry[] locales() {
                    return locales;
                }
            };
            new AnnotationFieldUpdater<>(instance)
                    .withNewValue(LangMetadata.class, langMetadata)
                    .performUpdates();
        }

        public AnonymousElement build() {
            return this.anonymousElement;
        }
    }
}
