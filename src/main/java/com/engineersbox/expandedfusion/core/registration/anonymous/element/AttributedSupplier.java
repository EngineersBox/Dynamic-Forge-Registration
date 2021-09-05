package com.engineersbox.expandedfusion.core.registration.anonymous.element;

import java.util.function.Supplier;

public class AttributedSupplier<T> {

    public final Supplier<T> supplier;
    public final String tabGroup;

    public AttributedSupplier(final Supplier<T> supplier, final String tabGroup) {
        this.supplier = supplier;
        this.tabGroup = tabGroup;
    }
}
