package com.engineersbox.expandedfusion.core.functional;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class DualStreamIterator<T, E> {

    private final Iterator<T> iter1;
    private final Iterator<E> iter2;

    public DualStreamIterator(final Stream<T> stream1,
                              final Stream<E> stream2) {
        this.iter1 = stream1.iterator();
        this.iter2 = stream2.iterator();
    }

    public DualStreamIterator(final Stream<T> collection1,
                              final E[] collection2) {
        this(collection1, Stream.of(collection2));
    }

    public DualStreamIterator(final T[] collection1,
                              final Stream<E> collection2) {
        this(Stream.of(collection1), collection2);
    }

    public DualStreamIterator(final T[] collection1,
                              final E[] collection2) {
        this(Stream.of(collection1), Stream.of(collection2));
    }

    public boolean apply(final BiConsumer<T, E> consumer) {
        while(iter1.hasNext() && iter2.hasNext()) {
            consumer.accept(iter1.next(), iter2.next());
        }
        return !iter1.hasNext() && !iter2.hasNext();
    }

}
