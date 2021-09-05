package com.engineersbox.expandedfusion.core.functional;

import java.util.Iterator;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class DualStreamComparator<T, E> {

    private final Iterator<T> iter1;
    private final Iterator<E> iter2;

    public DualStreamComparator(final Stream<T> stream1,
                                final Stream<E> stream2) {
        this.iter1 = stream1.iterator();
        this.iter2 = stream2.iterator();
    }

    public DualStreamComparator(final Stream<T> collection1,
                                final E[] collection2) {
        this(collection1, Stream.of(collection2));
    }

    public DualStreamComparator(final T[] collection1,
                                final Stream<E> collection2) {
        this(Stream.of(collection1), collection2);
    }

    public DualStreamComparator(final T[] collection1,
                                final E[] collection2) {
        this(Stream.of(collection1), Stream.of(collection2));
    }

    public boolean matches(final BiPredicate<T, E> matcher) {
        while(iter1.hasNext() && iter2.hasNext()) {
            if (!matcher.test(iter1.next(), iter2.next())) {
                return false;
            }
        }
        return !iter1.hasNext() && !iter2.hasNext();
    }
}
