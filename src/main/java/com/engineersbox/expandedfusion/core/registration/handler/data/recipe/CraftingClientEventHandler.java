package com.engineersbox.expandedfusion.core.registration.handler.data.recipe;

import com.engineersbox.expandedfusion.core.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.event.annotation.Subscriber;
import com.engineersbox.expandedfusion.core.registration.annotation.recipe.crafting.CraftingRecipe;
import com.engineersbox.expandedfusion.core.registration.annotation.recipe.crafting.PatternKey;
import com.engineersbox.expandedfusion.core.registration.annotation.recipe.crafting.PatternLine;
import com.engineersbox.expandedfusion.core.registration.annotation.recipe.crafting.UnlockCriterion;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryObjectContext;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.crafting.CraftingRecipeImplGrouping;
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
        for (final UnlockCriterion criterion : recipe.criteria()) {
            builder = addAdvancementCriterion(builder, criterion);
        }
        return builder;
    }

    private ShapedRecipeBuilder addAdvancementCriterion(final ShapedRecipeBuilder builder,
                                                        final UnlockCriterion criterion) {
        switch (criterion.requirement()) {
            case HAS_ITEM_TAG:
                final Optional<ITag.INamedTag<Item>> tagMatch = getKeyAsINamedTag(criterion.ingredient());
                if (tagMatch.isPresent()) {
                    return builder.addCriterion(criterion.key(), hasItem(tagMatch.get()));
                }
                throw new RuntimeException("No ingredient could be found for tag " + criterion.ingredient()); // TODO: Implement an exception for this
            case HAS_ITEM_ITEM:
                final Optional<IItemProvider> itemProvider = getItemProvider(criterion.ingredient());
                if (itemProvider.isPresent()) {
                    return builder.addCriterion(criterion.key(), hasItem(itemProvider.get()));
                }
                throw new RuntimeException("No ingredient could be found for item " + criterion.ingredient()); // TODO: Implement an exception for this
            case ENTERED_BLOCK:
                final Optional<Block> block = getBlock(criterion.ingredient());
                if (block.isPresent()) {
                    return builder.addCriterion(criterion.key(), enteredBlock(block.get()));
                }
                throw new RuntimeException("No block could be found for name " + criterion.ingredient()); // TODO: Implement an exception for this
        }
        throw new RuntimeException("Unknown requirement type: " + criterion.requirement()); // TODO: Implement an exception for this
    }

    private Optional<Block> getBlock(final String key) {
        final Stream<Field> mergedStream = createdMergedFieldStream(key, Blocks.class);
        // TODO: Add stream for blocks created in this project to mergedStream
        return filterFields(mergedStream);
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
        if (key.ingredient().length < 1) {
            throw new RuntimeException(); // TODO: Implement this
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
            throw new RuntimeException("No corresponding tag or item found for " + key); // TODO: Implement an exception for this
        }
        return builder;
    }

    private Optional<ITag.INamedTag<Item>> getKeyAsINamedTag(final String key) {
        final Pair<String, String> resLocPair = convertStringToResLocPair(key);
        final Optional<ITag.INamedTag<Item>> tag = ItemTags.getAllTags().stream()
                .filter(t -> {
                    final ResourceLocation resLoc = t.getName();
                    if (resLocPair.getLeft() != null && !resLoc.getNamespace().equals(resLocPair.getLeft())) {
                        return false;
                    }
                    return resLoc.getPath().equals(resLocPair.getRight());
                })
                .map(t -> (ITag.INamedTag<Item>) t)
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
        // TODO: Add stream for tags created in this project to mergedStream
        return filterFields(mergedStream);
    }

    private Optional<IItemProvider> getItemProvider(final String key) {
        final Stream<Field> mergedStream = createdMergedFieldStream(key, Blocks.class, Items.class);
        // TODO: Add stream for block and items created in this project to mergedStream
        return filterFields(mergedStream);
    }

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

    /**
     * Creates a new {@link EnterBlockTrigger} for use with recipe unlock criteria.
     */
    protected static EnterBlockTrigger.Instance enteredBlock(Block block) {
        return new EnterBlockTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, block, StatePropertiesPredicate.EMPTY);
    }

    /**
     * Creates a new {@link InventoryChangeTrigger} that checks for a player having a certain item.
     */
    protected static InventoryChangeTrigger.Instance hasItem(IItemProvider item) {
        return hasItem(ItemPredicate.Builder.create().item(item).build());
    }

    /**
     * Creates a new {@link InventoryChangeTrigger} that checks for a player having an item within the given tag.
     */
    protected static InventoryChangeTrigger.Instance hasItem(ITag<Item> tag) {
        return hasItem(ItemPredicate.Builder.create().tag(tag).build());
    }

    /**
     * Creates a new {@link InventoryChangeTrigger} that checks for a player having a certain item.
     */
    protected static InventoryChangeTrigger.Instance hasItem(ItemPredicate... predicate) {
        return new InventoryChangeTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, predicate);
    }
}
