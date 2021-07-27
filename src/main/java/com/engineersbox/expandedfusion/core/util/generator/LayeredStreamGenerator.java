package com.engineersbox.expandedfusion.core.util.generator;

import com.google.common.collect.Range;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.stream.IntStream;

public class LayeredStreamGenerator<T> {

    private static final int MAX_STREAM_ELEMENTS = 10;

    @FunctionalInterface
    public interface NIntOptionalFunction<T> {
        Optional<T> accept(final int ...i);
    }

    private final List<Range<Integer>> bounds;
    private final List<NIntOptionalFunction<T>> consumers;

    public LayeredStreamGenerator() {
        this.bounds = new ArrayList<>();
        this.consumers = new ArrayList<>();
    }

    @SafeVarargs
    public final LayeredStreamGenerator<T> setBounds(final Range<Integer>... b) {
        if (b.length > MAX_STREAM_ELEMENTS) {
            throw new IllegalArgumentException("Varargs size exceeds maximum: " + MAX_STREAM_ELEMENTS);
        }
        this.bounds.addAll(Arrays.asList(b));
        return this;
    }

    @SafeVarargs
    public final LayeredStreamGenerator<T> setConsumers(final NIntOptionalFunction<T>... c) {
        if (c.length > MAX_STREAM_ELEMENTS) {
            throw new IllegalArgumentException("Varargs size exceeds maximum: " + MAX_STREAM_ELEMENTS);
        }
        this.consumers.addAll(Arrays.asList(c));
        return this;
    }

    private void apply(final List<T> collection, final int idx, final int ...values) {
        if (idx < 0 || idx >= this.bounds.size()) {
            return;
        }
        final Range<Integer> b = this.bounds.get(idx);
        IntStream.range(b.lowerEndpoint(), b.upperEndpoint()).forEach((i) -> {
            final int[] newValues = ArrayUtils.insert(idx, values, i);
            final Optional<T> res = this.consumers.get(idx).accept(newValues);
            res.ifPresent(collection::add);
            apply(collection, idx + 1, newValues);
        });
    }

    public Optional<Collection<T>> apply() {
        final List<T> collection = new ArrayList<>(this.bounds.size());
        apply(collection, 0);
        if (collection.size() > 0) {
            return Optional.of(collection);
        }
        return Optional.empty();
    }

}
