package com.engineersbox.expandedfusion.core.classifier;

import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplGrouping;

public interface ImplementationClassifier<T extends ImplGrouping> {

    String testGrouping(final T grouping);

}
