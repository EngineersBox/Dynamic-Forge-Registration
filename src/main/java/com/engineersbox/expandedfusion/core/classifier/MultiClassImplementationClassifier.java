package com.engineersbox.expandedfusion.core.classifier;

import com.engineersbox.expandedfusion.core.classifier.exception.GroupingClassificationException;
import com.engineersbox.expandedfusion.core.classifier.requirement.ClassifierRequirement;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplGrouping;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class MultiClassImplementationClassifier<T extends ImplGrouping> implements ImplementationClassifier<T> {

    private static final String REQUIREMENTS_DESCRIPTION_FORMAT = "== Requirement %d ==%n" +
            "> [%s] Class inheritance [Provided: %s] [Required: ? extends %s]%n" +
            "> [%s] Annotation [Provided: @%s] [Required: @%s]%n" +
            "> [%s] Common identifier value [Provided: %s] [Required: %s]%n" +
            "> Requirement: [%s]%n%n";
    private static final String PARTIAL_MATCH_HEADER_FORMAT = "== %s Multi-Class Implementation ==%n";
    private static final String PARTIAL_MATCH_DESCRIPTION_FORMAT_LINE = "> [%s] Class %s:%n" +
            "  - Type matcher: [? extends %s] Implementation: [%s]%n" +
            "  - Annotation: [@%s]%n" +
            "  - Requirement: [%s]%n%n";

    private final Map<String, List<ClassifierRequirement<?, ?>>> requirements;

    public MultiClassImplementationClassifier(final Map<String, List<ClassifierRequirement<?, ?>>> requirements) {
        this.requirements = requirements;
    }

    @Override
    public String testGrouping(final T grouping) {
        final List<Class<?>> classes = grouping.getAllClasses();
        String matching = null;
        final List<Pair<String, Set<Class<?>>>> subsets = new ArrayList<>();
        for (final Map.Entry<String, List<ClassifierRequirement<?, ?>>> entry : this.requirements.entrySet()) {
            final List<Class<?>> classifierClasses = entry.getValue().stream()
                    .filter(ClassifierRequirement::isRequired)
                    .map((final ClassifierRequirement<?, ?> classifierRequirement) -> classifierRequirement.inheritsFrom)
                    .collect(Collectors.toList());
            final List<Class<?>> reducibleClasses = new ArrayList<>(classes);
            Set<Class<?>> intersection = classifierClasses.stream()
                    .filter((final Class<?> clazz) -> {
                        final Optional<Class<?>> match = reducibleClasses.stream().filter(clazz::isAssignableFrom).findFirst();
                        match.ifPresent(reducibleClasses::remove);
                        return match.isPresent();
                    }).collect(Collectors.toSet());
            if (intersection.size() == classifierClasses.size() && reducibleClasses.isEmpty()) {
                matching = entry.getKey();
                break;
            } else if (!intersection.isEmpty() && intersection.size() < classifierClasses.size()) {
                subsets.add(ImmutablePair.of(
                        entry.getKey(),
                        intersection
                ));
            }
        }
        if (matching != null) {
            handleMatching(grouping, classes, matching);
        } else if (!subsets.isEmpty()) {
            handleSubsets(subsets, classes);
        }
        return matching;
    }

    private String asPresentableAlt(final boolean alt) {
        return alt ? "X" : " ";
    }

    private <F> void appendErrorMessagesForClass(final StringBuilder errorString,
                                                 final ClassifierRequirement<?, ?> classReq,
                                                 final Optional<Class<?>> matchingClass,
                                                 final T grouping,
                                                 final int index) {
        final Pair<Boolean, Pair<Class<?>, Class<?>>> inheritancePair = classReq.testMatchesType(matchingClass.orElse(null));
        final Pair<Boolean, Pair<Class<?>, Class<?>>> annotationPair = classReq.testMarkedBy(matchingClass.orElse(null));
        final Pair<Boolean, Pair<F, F>> commonIdentifierPair = classReq.testCommonIdentifier(matchingClass.orElse(null), grouping.getCommonIdentifier());
        if ((Boolean.TRUE.equals(inheritancePair.getLeft())
                && Boolean.TRUE.equals(annotationPair.getLeft())
                && Boolean.TRUE.equals(commonIdentifierPair.getLeft())) || !matchingClass.isPresent()) {
            return;
        }
        errorString.append(String.format(
                REQUIREMENTS_DESCRIPTION_FORMAT,
                index,
                asPresentableAlt(inheritancePair.getLeft()),
                inheritancePair.getRight().getLeft().getCanonicalName(),
                inheritancePair.getRight().getLeft().getCanonicalName(),
                asPresentableAlt(annotationPair.getLeft()),
                annotationPair.getRight().getLeft().getCanonicalName(),
                annotationPair.getRight().getLeft().getCanonicalName(),
                asPresentableAlt(commonIdentifierPair.getLeft()),
                commonIdentifierPair.getRight().getLeft(),
                commonIdentifierPair.getRight().getRight(),
                classReq.condition
        ));
    }

    private void handleMatching(final T grouping,
                                final List<Class<?>> classes,
                                final String matching) {
        final List<ClassifierRequirement<?, ?>> reqs = this.requirements.get(matching);
        final StringBuilder errorString = new StringBuilder();
        for (int i = 0; i < reqs.size(); i++) {
            final ClassifierRequirement<?, ?> classReq = reqs.get(i);
            final Optional<Class<?>> matchingClass = classes.stream()
                    .filter((final Class<?> clazz) -> classReq.testMatchesType(clazz).getLeft())
                    .findFirst();
            appendErrorMessagesForClass(errorString, classReq, matchingClass, grouping, i);
        }
        if (errorString.length() > 1) {
            throw new GroupingClassificationException(String.format(
                    "Matching multi-class implementation %s was found, but contained implementation mismatches",
                    matching
            ), errorString.toString().trim());
        }
    }

    private void handleSubsets(final List<Pair<String, Set<Class<?>>>> subsets,
                               final List<Class<?>> classes) {
        final StringBuilder errorString = new StringBuilder();
        subsets.forEach((final Pair<String, Set<Class<?>>> subset) -> {
            errorString.append(String.format(
                    PARTIAL_MATCH_HEADER_FORMAT,
                    subset.getLeft()
            ));
            final List<ClassifierRequirement<?, ?>> multiClassImplClassifiers = this.requirements.get(subset.getLeft());
            multiClassImplClassifiers.forEach((final ClassifierRequirement<?, ?> classReq) -> {
                final Optional<Class<?>> implementation = classes.stream().filter((final Class<?> clazz) -> classReq.testMatchesType(clazz).getLeft()).findFirst();
                errorString.append(String.format(
                        PARTIAL_MATCH_DESCRIPTION_FORMAT_LINE,
                        asPresentableAlt(implementation.isPresent()),
                        classReq.inheritsFrom.getCanonicalName(),
                        classReq.inheritsFrom.getCanonicalName(),
                        implementation.map(Class::getCanonicalName).orElse("?"),
                        classReq.uniquelyMarkedBy.getCanonicalName(),
                        classReq.condition.name()
                ));
            });
        });
        throw new GroupingClassificationException(String.format(
                "%d partially matching multi-class implementations found:%n",
                subsets.size()
        ), errorString.toString().trim());
    }

    public static class Builder<T extends ImplGrouping> {

        final Map<String, List<ClassifierRequirement<?, ?>>> requirements;

        public Builder() {
            this.requirements = new HashMap<>();
        }

        public Builder<T> withRequirements(final String name,
                                           final ClassifierRequirement<?, ?> ...requirements) {
            this.requirements.put(name, Arrays.asList(requirements));
            return this;
        }

        public MultiClassImplementationClassifier<T> build() {
            return new MultiClassImplementationClassifier<>(this.requirements);
        }
    }
}
