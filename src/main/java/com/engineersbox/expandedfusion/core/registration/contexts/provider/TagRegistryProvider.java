package com.engineersbox.expandedfusion.core.registration.contexts.provider;

import com.google.inject.Singleton;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public final class TagRegistryProvider {
    public final Map<ResourceLocation, ITag.INamedTag<Block>> blockTags = new HashMap<>();
    public final Map<ResourceLocation, ITag.INamedTag<Item>> itemTags = new HashMap<>();
    public final Map<ResourceLocation, ITag.INamedTag<Fluid>> fluidTags = new HashMap<>();
    public final Map<ITag.INamedTag<Block>, Pair<Set<String>, Boolean>> blockTagsToBeRegistered = new HashMap<>();
    public final Map<ITag.INamedTag<Block>, ITag.INamedTag<Item>> blockTagsToBeRegisteredAsItemTags = new HashMap<>();
    public final Map<ITag.INamedTag<Item>, Pair<Set<String>, Boolean>> itemTagsToBeRegistered = new HashMap<>();
    public final Map<ITag.INamedTag<Fluid>, Pair<Set<String>, Boolean>> sourceFluidTagsToBeRegistered = new HashMap<>();
    public final Map<ITag.INamedTag<Fluid>, Pair<Set<String>, Boolean>> flowingFluidTagsToBeRegistered = new HashMap<>();
}
