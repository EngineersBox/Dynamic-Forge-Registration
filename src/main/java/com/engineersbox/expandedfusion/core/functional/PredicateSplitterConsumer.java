package com.engineersbox.expandedfusion.core.functional;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class PredicateSplitterConsumer<T> implements Consumer<T> {
    private final Predicate<T> predicate;
    private final Consumer<T>  positiveConsumer;
    private final Consumer<T>  negativeConsumer;

    public PredicateSplitterConsumer(final Predicate<T> predicate,
                                     final Consumer<T> positive,
                                     final Consumer<T> negative) {
        this.predicate = predicate;
        this.positiveConsumer = positive;
        this.negativeConsumer = negative;
    }

    @Override
    public void accept(T t) {
        if (predicate.test(t)){
            positiveConsumer.accept(t);
        } else {
            negativeConsumer.accept(t);
        }
    }
}
