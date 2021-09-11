package com.engineersbox.expandedfusion.core.registration.provider.shim.data.tags;

import com.engineersbox.expandedfusion.core.registration.anonymous.element.TagBinding;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryProvider;
import com.engineersbox.expandedfusion.core.registration.provider.shim.RegistryShim;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TagDeferredRegistryShim extends RegistryShim<ITag.INamedTag<?>> {

    private final RegistryProvider registryProvider;

    @Inject
    public TagDeferredRegistryShim(final RegistryProvider registryProvider) {
        this.registryProvider = registryProvider;
    }

    public void bindBlockTag(final String providerName,
                             final TagBinding<Block> tagBinding) {
        ITag.INamedTag<Block> blockTag = tagBinding.getTag();
        if (blockTag == null && tagBinding.getTagResource() != null) {
            blockTag = BlockTags.createOptional(tagBinding.getTagResource());
        }
        if (blockTag != null) {
            markTagForDeferredRegistration(providerName, blockTag, this.registryProvider.blockTagsToBeRegistered);
            this.registryProvider.blockTags.put(blockTag.getName(), blockTag);
            ITag.INamedTag<Item> mirrorTag = tagBinding.getMirroredTag();
            if (mirrorTag == null && tagBinding.getMirroredTagResource() != null) {
                mirrorTag = ItemTags.createOptional(tagBinding.getMirroredTagResource());
            }
            if (mirrorTag != null) {
                this.registryProvider.blockTagsToBeRegisteredAsItemTags.put(blockTag, mirrorTag);
            }
        }
    }

    public void bindItemTag(final String providerName,
                            final TagBinding<Item> tagBinding) {
        ITag.INamedTag<Item> itemTag = tagBinding.getTag();
        if (itemTag == null && tagBinding.getTagResource() != null) {
            itemTag = ItemTags.createOptional(tagBinding.getTagResource());
        }
        if (itemTag != null) {
            this.registryProvider.itemTags.put(itemTag.getName(), itemTag);
            markTagForDeferredRegistration(providerName, itemTag, this.registryProvider.itemTagsToBeRegistered);
        }
    }

    public void bindSourceFluidTag(final String providerName, final TagBinding<Fluid> tagBinding) {
        ITag.INamedTag<Fluid> fluidTag = tagBinding.getTag();
        if (fluidTag == null && tagBinding.getTagResource() != null) {
            fluidTag = FluidTags.createOptional(tagBinding.getTagResource());
        }
        if (fluidTag != null) {
            this.registryProvider.fluidTags.put(fluidTag.getName(), fluidTag);
            markTagForDeferredRegistration(providerName, fluidTag, this.registryProvider.sourceFluidTagsToBeRegistered);
        }
    }

    public void bindFlowingFluidTag(final String providerName, final TagBinding<Fluid> tagBinding) {
        ITag.INamedTag<Fluid> fluidTag = tagBinding.getTag();
        if (fluidTag == null && tagBinding.getTagResource() != null) {
            fluidTag = FluidTags.createOptional(tagBinding.getTagResource());
        }
        if (fluidTag != null) {
            this.registryProvider.fluidTags.put(fluidTag.getName(), fluidTag);
            markTagForDeferredRegistration(providerName, fluidTag, this.registryProvider.flowingFluidTagsToBeRegistered);
        }
    }

    private <T> void markTagForDeferredRegistration(final String providerName,
                                                    final ITag.INamedTag<T> tag,
                                                    final Map<ITag.INamedTag<T>, Set<String>> toBeRegistered) {
        Set<String> tagsToBeRegistered = toBeRegistered.get(tag);
        if (tagsToBeRegistered == null) {
            tagsToBeRegistered = new HashSet<>();
        }
        tagsToBeRegistered.add(providerName);
        toBeRegistered.put(tag, tagsToBeRegistered);
    }

}
