package com.engineersbox.expandedfusion.core.registration.handler.data.recipe;

import com.engineersbox.expandedfusion.core.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.event.annotation.Subscriber;
import com.engineersbox.expandedfusion.core.registration.annotation.recipe.crafting.CraftingRecipe;
import com.engineersbox.expandedfusion.core.registration.annotation.recipe.crafting.PatternKey;
import com.engineersbox.expandedfusion.core.registration.annotation.recipe.crafting.PatternLine;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryObjectContext;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.crafting.CraftingRecipeImplGrouping;
import net.minecraft.block.Block;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CraftingClientEventHandler implements EventSubscriptionHandler {

    public static final Logger LOGGER = LogManager.getLogger(CraftingClientEventHandler.class);

    @Subscriber
    public void registerRecipes(final GatherDataEvent gatherEvent) {
        gatherEvent.getGenerator().addProvider(new RecipeProvider(gatherEvent.getGenerator()){
            @Override
            protected void registerRecipes(final Consumer<IFinishedRecipe> consumer) {
                RegistryObjectContext.getCraftingRecipesToBeRegistered().forEach((final String name, final CraftingRecipeImplGrouping group) -> {
                    Arrays.stream(group.getCraftingRecipeAnnotations())
                            .map((final CraftingRecipe recipe) -> registerRecipe(name, group, recipe))
                            .forEach((final ShapedRecipeBuilder builder) -> builder.build(consumer));
                });
            }
        });
    }

    private ShapedRecipeBuilder registerRecipe(final String name,
                                               final CraftingRecipeImplGrouping group,
                                               final CraftingRecipe recipe) {
        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shapedRecipe(retrieveRecipeResult(name, group));
        final PatternLine[] lines = recipe.pattern();
        if (lines.length > 3) {
            throw new RuntimeException(); // TODO: Implement an exception for this
        }
        final PatternKey[] keys = recipe.keys();
        if (!validatePatternsAndKeys(lines, keys)) {
            throw new RuntimeException(); // TODO: Implement an exception for this
        }
        for (final PatternLine line : lines) {
            builder = builder.patternLine(line.value());
        }
        for (final PatternKey key : keys) {
            builder = addKeyRecipeElement(builder, key);
        }
        return builder;
    }

    private IItemProvider retrieveRecipeResult(final String name,
                                               final CraftingRecipeImplGrouping group) {
        if (Block.class.isAssignableFrom(group.getRegistryEntry())) {
            return RegistryObjectContext.getBlockRegistryObject(name).asBlock();
        }
        return RegistryObjectContext.getItemRegistryObject(name).asItem();
    }

    private boolean validatePatternsAndKeys(final PatternLine[] lines,
                                            final PatternKey[] keys) {
        final String mergedPattern = Arrays.stream(lines)
                .map(PatternLine::value)
                .collect(Collectors.joining());
        return Arrays.stream(keys)
                .map(PatternKey::symbol)
                .allMatch((final Character key) -> mergedPattern.indexOf(key) != -1);
    }

    private ShapedRecipeBuilder addKeyRecipeElement(final ShapedRecipeBuilder builder,
                                                    final PatternKey key) {
        switch (key.type()) {
            case TAG:
                final Pair<String, String> resLocPair = convertStringToResLocPair(key.ingredient());
                final Optional<? extends ITag.INamedTag<Item>> tag = ItemTags.getAllTags().stream()
                        .filter(t -> {
                            final ResourceLocation resLoc = t.getName();
                            if (resLocPair.getLeft() != null && !resLoc.getNamespace().equals(resLocPair.getLeft())) {
                                return false;
                            }
                            return resLoc.getPath().equals(resLocPair.getRight());
                        })
                        .findFirst();
                if (tag.isPresent()) {
                    return builder.key(key.symbol(), tag.get());
                }
                final Optional<ITag.INamedTag<Item>> potentialTagMatch = getTagViaReflection(key.ingredient());
                if (potentialTagMatch.isPresent()) {
                    return builder.key(key.symbol(), potentialTagMatch.get());
                }
                break;
            case ITEM:
                // TODO: Implement this
                break;
            case INGREDIENT:
                // TODO: Implement this
                break;
        }
        return builder;
    }

    private Pair<String, String> convertStringToResLocPair(final String key) {
        if (!key.contains(":")) {
            return ImmutablePair.of(null, key);
        }
        final String[] splitKey = key.split(":");
        return ImmutablePair.of(splitKey[0], splitKey[1]);
    }

    @SuppressWarnings("unchecked")
    private Optional<ITag.INamedTag<Item>> getTagViaReflection(final String key) {
        final Set<Field> vanillaTagsFields = ReflectionUtils.getFields(
                ItemTags.class,
                (final Field field) -> field.getName().equalsIgnoreCase(key)
        );
        final Set<Field> forgeTagsFields = ReflectionUtils.getFields(
                Tags.Items.class,
                (final Field field) -> field.getName().equalsIgnoreCase(key)
        );
        final List<ITag.INamedTag<Item>> availableTags = Stream.concat(vanillaTagsFields.stream(),forgeTagsFields.stream())
                .map((final Field field) -> {
                    try {
                        return Optional.of(field.get(null));
                    } catch (IllegalAccessException e) {
                        return Optional.empty();
                    }
                }).filter(Optional::isPresent)
                .map(Optional::get)
                .map((final Object fieldObj) -> (ITag.INamedTag<Item>) fieldObj)
                .distinct()
                .collect(Collectors.toList());
        return availableTags.isEmpty() ? Optional.empty() : Optional.of(availableTags.get(0));
    }
}
