package com.engineersbox.expandedfusion.core.classifier;

import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplGrouping;

public interface ImplementationClassifier<T extends ImplGrouping> {

    void testGrouping(final T grouping);

}
