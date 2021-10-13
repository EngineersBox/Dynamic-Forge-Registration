package com.engineersbox.expandedfusion.core.registration.handler.data.recipe;

import com.engineersbox.expandedfusion.core.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.event.annotation.modloadingcontext.DataEventHandler;
import com.engineersbox.expandedfusion.core.event.annotation.InternalEventHandler;
import com.engineersbox.expandedfusion.core.event.annotation.Subscriber;
import com.engineersbox.expandedfusion.core.registration.annotation.data.recipe.crafting.CraftingRecipe;
import com.engineersbox.expandedfusion.core.registration.annotation.data.recipe.crafting.PatternKey;
import com.engineersbox.expandedfusion.core.registration.annotation.data.recipe.crafting.PatternLine;
import com.engineersbox.expandedfusion.core.registration.annotation.data.recipe.crafting.UnlockCriterion;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.recipe.CriterionCondition;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryObjectContext;
import com.engineersbox.expandedfusion.core.registration.exception.contexts.RegistryObjectRetrievalException;
import com.engineersbox.expandedfusion.core.registration.exception.handler.data.recipe.InvalidCraftingPatternException;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.crafting.CraftingRecipeImplGrouping;
import com.engineersbox.expandedfusion.core.registration.registryObject.element.BlockRegistryObject;
import com.engineersbox.expandedfusion.core.registration.registryObject.element.ItemRegistryObject;
import com.engineersbox.expandedfusion.core.util.math.MathUtils;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.advancements.criterion.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
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
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
@InternalEventHandler
@DataEventHandler
public class CraftingClientEventHandler implements EventSubscriptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(CraftingClientEventHandler.class);
    private final String modId;

    @Inject
    public CraftingClientEventHandler(@Named("modId") final String modId) {
        this.modId = modId;
    }

    @Subscriber
    public void registerRecipes(final GatherDataEvent gatherEvent) {
        gatherEvent.getGenerator().addProvider(new RecipeProvider(gatherEvent.getGenerator()) {

            @Override
            protected void registerRecipes(final Consumer<IFinishedRecipe> consumer) {
                final Map<String, CraftingRecipeImplGrouping> annotatedRecipesToRegister = RegistryObjectContext.getCraftingRecipesToBeRegistered();
                annotatedRecipesToRegister.forEach((final String name, final CraftingRecipeImplGrouping group) -> {
                    final CraftingRecipe[] recipes = group.getCraftingRecipeAnnotations();
                    Arrays.stream(recipes)
                            .map((final CraftingRecipe recipe) -> registerRecipe(name, group, recipe))
                            .forEach((final ShapedRecipeBuilder builder) -> builder.build(consumer));
                });
                final List<Consumer<Consumer<IFinishedRecipe>>> anonymousRecipesToRegister = RegistryObjectContext.getAnonymousRecipesToBeRegistered();
                anonymousRecipesToRegister.forEach((final Consumer<Consumer<IFinishedRecipe>> recipe) -> recipe.accept(consumer));
            }

            @Override
            public String getName() {
                return String.format("[Mod ID: %s] Recipes", modId);
            }
        });
    }

    private ShapedRecipeBuilder registerRecipe(final String name,
                                               final CraftingRecipeImplGrouping group,
                                               final CraftingRecipe recipe) {
        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shapedRecipe(retrieveRecipeResult(name, group));
        final PatternLine[] lines = recipe.pattern();
        if (!MathUtils.inRangeInclusive(lines.length, 1, 3)) {
            throw new InvalidCraftingPatternException(String.format(
                    "Supplied crafting pattern must only be defined by 1-3 pattern lines. %d were supplied.",
                    lines.length
            ));
        }
        final PatternKey[] keys = recipe.keys();
        validatePatternsAndKeys(lines, keys);
        for (final PatternLine line : lines) {
            builder = builder.patternLine(line.value());
        }
        for (final PatternKey key : keys) {
            builder = addKeyRecipeElement(builder, key);
        }
        for (final UnlockCriterion criterion : recipe.criteria()) {
            builder = addAdvancementCriterion(builder, criterion);
        }
        return builder;
    }

    private ShapedRecipeBuilder addAdvancementCriterion(final ShapedRecipeBuilder builder,
                                                        final UnlockCriterion criterion) {
        switch (criterion.requirement()) {
            case HAS_TAG:
                final Optional<ITag.INamedTag<Item>> tagMatch = getKeyAsINamedTag(criterion.ingredient());
                if (tagMatch.isPresent()) {
                    return builder.addCriterion(criterion.key(), CriterionCondition.hasItem(tagMatch.get()));
                }
                throw new InvalidCraftingPatternException("No ingredient could be found for tag " + criterion.ingredient());
            case HAS_ITEM:
                final Optional<IItemProvider> itemProvider = getItemProvider(criterion.ingredient());
                if (itemProvider.isPresent()) {
                    return builder.addCriterion(criterion.key(), CriterionCondition.hasItem(itemProvider.get()));
                }
                throw new InvalidCraftingPatternException("No ingredient could be found for item " + criterion.ingredient());
            case ENTERED_BLOCK:
                final Optional<Block> block = getBlock(criterion.ingredient());
                if (block.isPresent()) {
                    return builder.addCriterion(criterion.key(), CriterionCondition.enteredBlock(block.get()));
                }
                throw new InvalidCraftingPatternException("No block could be found for name " + criterion.ingredient());
        }
        throw new InvalidCraftingPatternException("Unknown requirement type: " + criterion.requirement());
    }

    private Optional<Block> getBlock(final String key) {
        try {
            return Optional.of(RegistryObjectContext.getBlockRegistryObject(key).asBlock());
        } catch (final RegistryObjectRetrievalException ignored) {}
        final Stream<Field> mergedStream = createdMergedFieldStream(key, Blocks.class);
        return filterFields(mergedStream);
    }

    private IItemProvider retrieveRecipeResult(final String name,
                                               final CraftingRecipeImplGrouping group) {
        if (Block.class.isAssignableFrom(group.getRegistryEntry())) {
            return RegistryObjectContext.getBlockRegistryObject(name).asBlock();
        }
        return RegistryObjectContext.getItemRegistryObject(name).asItem();
    }

    private void validatePatternsAndKeys(final PatternLine[] lines,
                                         final PatternKey[] keys) {
        final String mergedPattern = Arrays.stream(lines)
                .map(PatternLine::value)
                .collect(Collectors.joining("\n"));
        for (final Character symbol : Arrays.stream(keys).map(PatternKey::symbol).collect(Collectors.toList())) {
            if (mergedPattern.indexOf(symbol) == -1) {
                throw new InvalidCraftingPatternException(String.format(
                        "Unbound key %s in PatternKey definition has no usage in pattern:%n%s",
                        symbol,
                        mergedPattern
                ));
            }
        }
    }

    private ShapedRecipeBuilder addKeyRecipeElement(final ShapedRecipeBuilder builder,
                                                    final PatternKey key) {
        if (key.ingredient().length < 1) {
            throw new InvalidCraftingPatternException("At least 1 ingredient must be present in @PatternKey declaration");
        }
        if (key.ingredient().length > 1) {
            return addKeyRecipeAsDestructuredIngredient(builder, key);
        }
        final String singleKey = key.ingredient()[0];
        switch (key.type()) {
            case TAG:
                final Optional<ITag.INamedTag<Item>> potentialTagMatch = getKeyAsINamedTag(singleKey);
                if (potentialTagMatch.isPresent()) {
                    return builder.key(key.symbol(), potentialTagMatch.get());
                }
                break;
            case ITEM:
                final Optional<IItemProvider> itemProvider = getItemProvider(singleKey);
                if (itemProvider.isPresent()) {
                    return builder.key(key.symbol(), itemProvider.get());
                }
                break;
        }
        return builder;
    }

    private ShapedRecipeBuilder addKeyRecipeAsDestructuredIngredient(ShapedRecipeBuilder builder,
                                                                     final PatternKey patternKey) {
        for (final String key : patternKey.ingredient()) {
            final Optional<ITag.INamedTag<Item>> tagMatch = getKeyAsINamedTag(key);
            if (tagMatch.isPresent()) {
                builder = builder.key(patternKey.symbol(), Ingredient.fromTag(tagMatch.get()));
                continue;
            }
            final Optional<IItemProvider> itemProvider = getItemProvider(key);
            if (itemProvider.isPresent()) {
                builder = builder.key(patternKey.symbol(), Ingredient.fromItems(itemProvider.get()));
                continue;
            }
            throw new InvalidCraftingPatternException("No corresponding tag or item found for " + key);
        }
        return builder;
    }

    private Optional<ITag.INamedTag<Item>> getKeyAsINamedTag(final String key) {
        final Pair<String, String> resLocPair = convertStringToResLocPair(key);
        final Optional<ITag.INamedTag<Item>> tag = ItemTags.getAllTags().stream()
                .filter((final ITag.INamedTag<Item> namedTag) -> {
                    final ResourceLocation resLoc = namedTag.getName();
                    if (resLocPair.getLeft() != null && !resLoc.getNamespace().equals(resLocPair.getLeft())) {
                        return false;
                    }
                    return resLoc.getPath().equals(resLocPair.getRight());
                })
                .map((final ITag.INamedTag<Item> namedTag) -> namedTag)
                .findFirst();
        if (tag.isPresent()) {
            return tag;
        }
        return getTagViaReflection(key);
    }

    private Pair<String, String> convertStringToResLocPair(final String key) {
        if (!key.contains(":")) {
            return ImmutablePair.of(null, key);
        }
        final String[] splitKey = key.split(":");
        return ImmutablePair.of(splitKey[0], splitKey[1]);
    }

    private Optional<ITag.INamedTag<Item>> getTagViaReflection(final String key) {
        final Stream<Field> mergedStream = createdMergedFieldStream(key, ItemTags.class, Tags.Items.class);
        return filterFields(mergedStream);
    }

    private Optional<IItemProvider> getItemProvider(final String key) {
        final Stream<Field> mergedStream = createdMergedFieldStream(key, Blocks.class, Items.class);
        final Optional<IItemProvider> potentialProviderFromPure = filterFields(mergedStream);
        if (potentialProviderFromPure.isPresent()) {
            return potentialProviderFromPure;
        }
        LOGGER.trace("Pure provider from {} and {} could not be found, attempting retrieval from deferred registries.", Blocks.class.getName(), Items.class.getName());
        try {
            final ItemRegistryObject<? extends Item> itemRegistryObject = RegistryObjectContext.getItemRegistryObject(key);
            return Optional.of(itemRegistryObject.asItem());
        } catch (final RegistryObjectRetrievalException e) {
            LOGGER.trace(e);
        }
        try {
            final BlockRegistryObject<? extends Block> blockRegistryObject = RegistryObjectContext.getBlockRegistryObject(key);
            return Optional.of(blockRegistryObject.asItem());
        } catch (final RegistryObjectRetrievalException e) {
            LOGGER.trace(e);
        }
        return Optional.empty();
    }

    @SuppressWarnings({"unchecked", "RedundantOperationOnEmptyContainer"})
    private Stream<Field> createdMergedFieldStream(final String key, final Class<?> ...classes) {
        Stream<Field> collector = new HashSet<Field>().stream();
        for (final Class<?> clazz : classes) {
            final Stream<Field> newStream = ReflectionUtils.getFields(
                    clazz,
                    (final Field field) -> field.getName().equalsIgnoreCase(key)
            ).stream();
            collector = Stream.concat(collector, newStream);
        }
        return collector;
    }

    @SuppressWarnings("unchecked")
    private <T> Optional<T> filterFields(final Stream<Field> fields) {
        final List<T> availableTags = fields
                .map((final Field field) -> {
                    try {
                        return Optional.of(field.get(null));
                    } catch (IllegalAccessException e) {
                        return Optional.empty();
                    }
                }).filter(Optional::isPresent)
                .map(Optional::get)
                .map((final Object fieldObj) -> (T) fieldObj)
                .distinct()
                .collect(Collectors.toList());
        return availableTags.isEmpty() ? Optional.empty() : Optional.of(availableTags.get(0));
    }
}
