package com.engineersbox.expandedfusion.core.registration.provider.grouping;

import java.util.List;

public interface ImplGrouping {

    List<Class<?>> getAllClasses();

    <E> E getCommonIdentifier();

}
